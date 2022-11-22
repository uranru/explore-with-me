package ru.practicum.explore.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HitRepository extends JpaRepository<Hit,Long>, QuerydslPredicateExecutor<Hit> {
}
