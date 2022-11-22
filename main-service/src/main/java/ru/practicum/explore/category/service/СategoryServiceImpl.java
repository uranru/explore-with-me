package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.category.dto.СategoryDto;
import ru.practicum.explore.category.Сategory;
import ru.practicum.explore.category.СategoryMapper;
import ru.practicum.explore.category.СategoryRepository;
import ru.practicum.explore.exeption.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class СategoryServiceImpl implements СategoryService{
    private final СategoryRepository categoryRepository;

    @Override
    public СategoryDto addСategory(СategoryDto categoryDto) {

        log.debug("Выполнен метод addСategory: {}",categoryDto);
        Сategory category = СategoryMapper.toСategory(categoryDto);

        try {
            return СategoryMapper.toСategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiStorageException(exception.getMessage());
        }
    }

    @Override
    public void deleteСategoryById(Long id) {
        log.debug("Выполнен метод deleteСategoryById: {}", id);
        try {
            categoryRepository.deleteById(id);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }
    }

    @Override
    public Сategory getСategoryById(Long id) {
        log.debug("Выполнен метод getСategoryById: {}", id);
        Сategory category = new Сategory();
        try {
            category = categoryRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }

       return category;
    }

    @Override
    public СategoryDto getСategoryDtoById(Long id) {
        return СategoryMapper.toСategoryDto(getСategoryById(id));
    }

    @Override
    public СategoryDto updateСategory(СategoryDto categoryDto) {
        log.debug("Выполнен метод updateСategory: {}",categoryDto);
        Сategory category;
        try {
            category = categoryRepository.save(СategoryMapper.toСategory(categoryDto));
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        } catch (ConstraintViolationException exception) {
            throw new ApiConstraintViolationException(exception.getMessage());
        } catch (DataIntegrityViolationException exception) {
            throw new ApiStorageException(exception.getMessage());
        }

        return СategoryMapper.toСategoryDto(category);
    }

    @Override
    public List<СategoryDto> findAllСategoryOrderById(int from, int size) {
        log.debug("Выполнен метод findAllСategoryOrderById: from:{}, size:{}", from, size);

        if (from < 0 || size < 0) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(400), "");
        }

        Page<Сategory> categoryList = categoryRepository.findAll(PageRequest.of(from/size, size));

        List<СategoryDto> categoryDtoList = new ArrayList<>();
        for (Сategory category: categoryList) {
            categoryDtoList.add(СategoryMapper.toСategoryDto(category));
        }

        return categoryDtoList;
    }


}
