package ru.practicum.explore.compilations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.request.Request;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {


    Page<Compilation> findCompilationsByPinned(Boolean pined, Pageable pageable);
}
