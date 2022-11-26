package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.CategoryMapper;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.client.HitClient;
import ru.practicum.explore.client.HitDto;
import ru.practicum.explore.client.ViewClient;
import ru.practicum.explore.event.*;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.enums.EventState;
import ru.practicum.explore.event.enums.EventStateResolution;
import ru.practicum.explore.event.mapper.CommentEventMapper;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.CommentEventRepository;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.Request;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserMapper;
import ru.practicum.explore.user.service.UserService;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.exeption.*;

import com.querydsl.core.types.dsl.BooleanExpression;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CommentEventRepository commentEventRepository;
    private final RequestRepository requestRepository;

    private final CategoryService categoryService;
    private final UserService userService;

    private final HitClient hitClient;
    private final ViewClient viewClient;

    @Override
    @Transactional
    public EventFullDto addUserEvent(NewEventDto newEventDto, Long initiatorId) {
        log.debug("Выполнен метод:: addUserEvent({}, {})", newEventDto, initiatorId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(category);
        User user = userService.getUserById(initiatorId);
        UserShortDto userShortDto = UserMapper.toUserShortDto(user);

        Event event;
        try {
            event = EventMapper.toEvent(newEventDto,category,user);
        } catch (ConstraintViolationException exception) {
            throw new ApiConstraintViolationException(exception.getMessage());
        }

        log.debug("Выполнен маппинг:: toEvent(...)");

        try {
            event = eventRepository.save(event);
        } catch (ConstraintViolationException exception) {
            throw new ApiConstraintViolationException(exception.getMessage());
        }

        return EventMapper.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    @Transactional
    public EventFullDto approvalAdminEvent(Long eventId) {
        log.debug("Выполнен метод:: approvalAdminEvent{{}}", eventId);
        Event event;
        EventState eventState = EventState.PUBLISHED;

        try {
            event = eventRepository.findById(eventId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        event.setState(eventState);
        event.setPublishedOn(LocalDateTime.now());
        Event updateEvent = eventRepository.save(event);

        Category category = updateEvent.getCategory();
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(category);
        User user = updateEvent.getInitiator();
        UserShortDto userShortDto = UserMapper.toUserShortDto(user);

        return EventMapper.toEventFullDto(updateEvent,categoryDto,userShortDto);
    }

    @Override
    @Transactional
    public EventFullDto rejectAdminEvent(Long eventId, CommentEventDto commentEventDto) {
        log.debug("Выполнен метод:: rejectAdminEvent{{}, {}}", eventId, commentEventDto);
        Event event;
        EventState eventState = EventState.CANCELED;

        try {
            event = eventRepository.findById(eventId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event state is not correct");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        event.setState(eventState);
        Event updateEvent = eventRepository.save(event);
        if (commentEventDto != null) {
            commentEventDto = CommentEventMapper
                    .toCommentEventDto(commentEventRepository.save(CommentEventMapper.toCommentEvent(commentEventDto, updateEvent)));
        }

        Category category = updateEvent.getCategory();
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(category);
        User user = updateEvent.getInitiator();
        UserShortDto userShortDto = UserMapper.toUserShortDto(user);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(updateEvent,categoryDto,userShortDto);
        eventFullDto.setComment(commentEventDto);

        return eventFullDto;
    }

    @Override
    public Event getEventById(Long eventId) {
        log.debug("Выполнен метод:: getUserEventById({})",eventId);

        Event event;
        try {
            event = eventRepository.findById(eventId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        return event;
    }

    @Override
    public EventFullDto getEventFullDtoById(Long eventId, HttpServletRequest request) {
        log.debug("Выполнен метод:: getEventFullDtoById({})",eventId);
        Event event = getEventById(eventId);
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(event.getCategory());
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

        hitClient.addHit(HitDto.builder()
                        .app("main-service")
                        .uri(request.getRequestURI())
                        .ip(request.getRemoteAddr())
                        .timestamp(LocalDateTime.now().format(EventMapper.formatter))
                .build());

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event,categoryDto,userShortDto);
        eventFullDto = updateEventFullDtoAddComment(eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getUserEventFullDtoById(Long userId,Long eventId) {
        log.debug("Выполнен метод:: getUserEventFullDtoById({},{})",userId,eventId);
        Event event = getEventById(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            CategoryDto categoryDto = CategoryMapper.toСategoryDto(event.getCategory());
            UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

            EventFullDto eventFullDto = EventMapper.toEventFullDto(event,categoryDto,userShortDto);
            eventFullDto = updateEventFullDtoAddComment(eventFullDto);
            return eventFullDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }
    }

    @Override
    public List<EventShortDto> getUserEventShortDtoById(Long initiatorId, Pageable pageable) {
        log.debug("Выполнен метод:: getUserEventShortDtoById({})",initiatorId);
        List<Event> eventList = new ArrayList<>();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        User user;

        try {
            user = userService.getUserById(initiatorId);
            eventList = eventRepository.findAllByInitiatorOrderById(user, pageable).getContent();
        }  catch (NoSuchElementException exception) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        eventList.stream().forEach(
                event -> eventShortDtoList.add(
                        EventMapper.toEventShortDto(event,
                                CategoryMapper.toСategoryDto(event.getCategory()),
                                UserMapper.toUserShortDto(user)
                        )
                )
        );

        eventShortDtoList.forEach(this::updateEventShortDtoAddComment);

        return eventShortDtoList;
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest) {
        log.debug("Выполнен метод:: updateUserEvent({}, {})",userId, updateEventRequest);
        Event event;

        try {
            event = eventRepository.findById(updateEventRequest.getEventId()).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        event.setState(EventState.PENDING);
        event = eventRepository.save(updateEvent(event,updateEventRequest));
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(event.getCategory());
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

        return EventMapper.toEventFullDto(event,categoryDto,userShortDto);
    }

    @Override
    @Transactional
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        log.debug("Выполнен метод:: cancelUserEvent({}, {})",userId, eventId);
        Event event;

        try {
            event = eventRepository.findById(eventId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        if (!userId.equals(event.getInitiator().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        event.setState(EventState.CANCELED);
        event = eventRepository.save(event);
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(event.getCategory());
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

        return EventMapper.toEventFullDto(event,categoryDto,userShortDto);
    }

    @Override
    @Transactional
    public EventFullDto updateAdminEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        log.debug("Выполнен метод:: updateAdminEvent({}, {})",eventId, adminUpdateEventRequest);
        Event event;

        try {
            event = eventRepository.findById(eventId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        event = eventRepository.save(updateEvent(event,adminUpdateEventRequest));
        CategoryDto categoryDto = CategoryMapper.toСategoryDto(event.getCategory());
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

        return EventMapper.toEventFullDto(event,categoryDto,userShortDto);
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Pageable pageable) {
        log.debug("Выполнен метод:: getAdminEvents(...)");

        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        QEvent qEvent = QEvent.event;
        BooleanExpression customerExpression = qEvent.isNotNull();


        if (users != null && !users.isEmpty()) {
            customerExpression = customerExpression
                    .and(qEvent.initiator.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            customerExpression = customerExpression
                    .and(qEvent.state.in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            customerExpression = customerExpression
                    .and(qEvent.category.id.in(categories));
        }


        if (rangeStart != null) {
            customerExpression = customerExpression
                    .and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart,EventMapper.formatter)));
        }
        if (rangeEnd != null) {
            customerExpression = customerExpression
                    .and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd,EventMapper.formatter)));
        }


        log.debug("Query DSL customerExpression:: {}", customerExpression);

        List<Event> eventList = eventRepository.findAll(customerExpression,pageable).getContent();

        eventList.stream().forEach(
                event -> eventFullDtoList.add(
                        EventMapper.toEventFullDto(event,
                                CategoryMapper.toСategoryDto(event.getCategory()),
                                UserMapper.toUserShortDto(event.getInitiator())
                        )
                )
        );

        eventFullDtoList.forEach(this::updateEventFullDtoAddComment);

        return eventFullDtoList;
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, Pageable pageable) {
        log.debug("Выполнен метод:: getPublicEvents");

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        Map<Long,Integer> eventsGroupByRequests = groupCountRequests();

        QEvent qEvent = QEvent.event;
        BooleanExpression customerExpression = qEvent.isNotNull();

        if (text != null && !text.isEmpty()) {
            customerExpression = customerExpression
                    .and((qEvent.annotation.containsIgnoreCase(text))
                            .or(qEvent.description.containsIgnoreCase(text))
                    );
        }
        if (categories != null && !categories.isEmpty()) {
            customerExpression = customerExpression
                    .and(qEvent.category.id.in(categories));
        }
        if (paid != null) {
            customerExpression = customerExpression
                    .and(qEvent.paid.eq(paid));
        }
        if (rangeStart != null) {
            customerExpression = customerExpression
                    .and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart,EventMapper.formatter)));
        }
        if (rangeEnd != null) {
            customerExpression = customerExpression
                    .and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd,EventMapper.formatter)));
        }
        if (onlyAvailable != null) {
            if (onlyAvailable) {

            customerExpression = customerExpression
                        .and(qEvent.confirmedRequests.gt(Integer.valueOf(eventsGroupByRequests.get(qEvent.id))));
            }
        }

        log.debug("Query DSL customerExpression:: {}", customerExpression);

        List<Event> eventList = eventRepository.findAll(customerExpression,pageable).getContent();

        eventList.stream().forEach(
                event -> eventShortDtoList.add(
                        (EventMapper.toEventShortDto(event,
                                CategoryMapper.toСategoryDto(event.getCategory()),
                                UserMapper.toUserShortDto(event.getInitiator())
                        )))
                );

        eventShortDtoList.forEach(this::updateEventShortDtoAddComment);

        eventShortDtoList.forEach(eventShortDto -> eventShortDto
                .setViews(viewClient.getHitsByEventId("main-service", eventShortDto.getId())));

        return eventShortDtoList;

    }

    @Override
    public List<Event> toEventList(List<Long> eventIdList) {
        log.debug("Выполнен метод:: toEventList({})", eventIdList);
        List<Event> eventList = new ArrayList<>();

        eventIdList.stream().forEach(
                eventId -> eventList.add(
                       getEventById(eventId)
                )
        );

        return eventList;
    }

    @Override
    public List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        log.debug("Выполнен метод:: toEventShortDtoList(...)");
        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        eventList.stream().forEach(
                event -> eventShortDtoList.add(
                        EventMapper.toEventShortDto(event,
                                CategoryMapper.toСategoryDto(event.getCategory()),
                                UserMapper.toUserShortDto(event.getInitiator())
                        )
                )
        );

        return eventShortDtoList;
    }

    private Event updateEvent(Event event, UpdateEventRequest updateEventRequest) {
        log.debug("Выполнен метод:: updateEvent({}, {})",event, updateEventRequest);

        event.setAnnotation(updateEventRequest.getAnnotation());
        event.setCategory(categoryService.getCategoryById(updateEventRequest.getCategory()));
        event.setDescription(updateEventRequest.getDescription());
        event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(),EventMapper.formatter));
        event.setPaid(updateEventRequest.getPaid());
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());

        return event;
    }

    private Event updateEvent(Event event, AdminUpdateEventRequest adminUpdateEventRequest) {
        log.debug("Выполнен метод:: updateEvent({}, {})",event, adminUpdateEventRequest);

        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        event.setCategory(categoryService.getCategoryById(adminUpdateEventRequest.getCategory()));
        event.setDescription(adminUpdateEventRequest.getDescription());
        event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),EventMapper.formatter));
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocationLat(adminUpdateEventRequest.getLocation().get("lat"));
            event.setLocationLon(adminUpdateEventRequest.getLocation().get("lon"));
        }
        event.setPaid(adminUpdateEventRequest.getPaid());
        event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        event.setTitle(adminUpdateEventRequest.getTitle());

        return event;
    }

    public Map<Long, Integer> groupCountRequests() {
        log.debug("Выполнен метод:: groupCountRequests");
        Map<Long, Integer> eventsCount = new HashMap<>();
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            Long eventId = request.getEvent().getId();
            if (eventsCount.containsKey(eventId)) {
                eventsCount.put(eventId, eventsCount.get(eventId) + 1);
            } else {
                eventsCount.put(eventId, 1);
            }
        }
        return eventsCount;
    }

    private EventFullDto updateEventFullDtoAddComment(EventFullDto eventFullDto) {
        log.debug("Выполнен метод:: updateEventFullDtoAddComment");
        if (eventFullDto == null) {
            return null;
        }

        CommentEventDto commentEventDto =  CommentEventMapper
                .toCommentEventDto(commentEventRepository.findCommentEventsByEvent_Id(eventFullDto.getId()));

        if (commentEventDto != null) {
            eventFullDto.setComment(commentEventDto);
        }
        return eventFullDto;
    }

    private EventShortDto updateEventShortDtoAddComment(EventShortDto eventShortDto) {
        log.debug("Выполнен метод:: updateEventShortDtoAddComment");
        if (eventShortDto == null) {
            return null;
        }

        CommentEventDto commentEventDto =  CommentEventMapper
                .toCommentEventDto(commentEventRepository.findCommentEventsByEvent_Id(eventShortDto.getId()));

        if (commentEventDto != null) {
            eventShortDto.setComment(commentEventDto.getText());
        }
        return eventShortDto;
    }

}
