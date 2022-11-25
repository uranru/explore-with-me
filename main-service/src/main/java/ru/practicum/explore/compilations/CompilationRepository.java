package ru.practicum.explore.compilations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompilationRepository extends JpaRepository<Compilation,Long> {


    Page<Compilation> findCompilationsByPinned(Boolean pined, Pageable pageable);
}
