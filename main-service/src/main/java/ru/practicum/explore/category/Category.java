package ru.practicum.explore.category;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@ToString
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "сategory_id")
    Long id;
    @NotEmpty
    @Column(name = "сategory_name")
    String name;

    public Category() {
    }

}
