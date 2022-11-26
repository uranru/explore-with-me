package ru.practicum.explore.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.compilations.Compilation;
import ru.practicum.explore.compilations.CompilationMapper;
import ru.practicum.explore.compilations.CompilationRepository;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.exeption.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto adminAddCompilation(NewCompilationDto newCompilationDto) {
        log.debug("Выполнен метод adminAddCompilation({})", newCompilationDto);
        Compilation compilation;

        if (newCompilationDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        try {
            compilation = compilationRepository
                    .save(CompilationMapper.toCompilation(newCompilationDto,
                            eventService.toEventList(newCompilationDto.getEvents())));
        } catch (ConstraintViolationException exception) {
            throw new ApiConstraintViolationException(exception.getMessage());
        }

        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilation,
                        eventService.toEventShortDtoList(compilation.getEvents()));

        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        log.debug("Выполнен метод getCompilationById({})", compilationId);
        Compilation compilation;

        if (compilationId == null || compilationId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        try {
            compilation = compilationRepository.findById(compilationId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
       }

        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilation,
                        eventService.toEventShortDtoList(compilation.getEvents()));

        log.debug("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDto adminUpdateCompilationAddEvent(Long compilationId, Long eventId) {
        log.debug("Выполнен метод adminUpdateCompilationAddEvent({}, {})", compilationId, eventId);
        if (compilationId == null || eventId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();
        Event event = eventService.getEventById(eventId);
        List<Event> eventList = compilation.getEvents();
        if (!eventList.contains(event)) {
            eventList.add(event);
        }

        compilation = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = eventService.toEventShortDtoList(compilation.getEvents());

        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilation,eventShortDtoList);

        log.debug("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDto adminUpdateCompilationDeleteEvent(Long compilationId, Long eventId) {
        log.debug("Выполнен метод adminUpdateCompilationDeleteEvent({}, {})", compilationId, eventId);
        if (compilationId == null || eventId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();
        Event event = eventService.getEventById(eventId);
        List<Event> eventList = compilation.getEvents();
        if (eventList.contains(event)) {
            eventList.remove(event);
        }

        compilation = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = eventService.toEventShortDtoList(compilation.getEvents());

        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilation,eventShortDtoList);

        log.debug("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDto adminUpdateCompilationPin(Long compilationId, Boolean pinned) {
        log.debug("Выполнен метод adminUpdateCompilationPin({}, {})", compilationId, pinned);
        Compilation compilation;

        if (compilationId == null || compilationId < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        try {
            compilation = compilationRepository.findById(compilationId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        compilation.setPinned(pinned);
        compilation = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = eventService.toEventShortDtoList(compilation.getEvents());

        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilation,eventShortDtoList);

        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        log.debug("Выполнен метод getCompilations({}, {})", pinned, pageable);
        List<Compilation> compilationList;
        List<CompilationDto> compilationDtoList = new ArrayList<>();

        if (pinned == true) {
            try {
                compilationList = compilationRepository.findCompilationsByPinned(true,pageable).getContent();
            } catch (NoSuchElementException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
            }
        } else {
            try {
                compilationList = compilationRepository.findAll(pageable).getContent();
            } catch (NoSuchElementException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
            }
        }

        compilationList.stream().forEach(
                compilation -> compilationDtoList.add(
                        CompilationMapper.toCompilationDto(compilation,
                                eventService.toEventShortDtoList(compilation.getEvents())
                        )
                )
        );

        return compilationDtoList;
    }

    @Override
    @Transactional
    public void adminDeleteCompilation(Long compilationId) {

        log.debug("Выполнен метод adminDeleteCompilation({})", compilationId);
        try {
            compilationRepository.deleteById(compilationId);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }
    }

}
