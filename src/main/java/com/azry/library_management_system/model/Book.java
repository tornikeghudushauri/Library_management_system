package com.azry.library_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
@SequenceGenerator(name = "ID_GEN", sequenceName = "ID_SEQ", initialValue = 1000, allocationSize = 1)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GEN")
    private Long id;

    private String title;

    private String author;

    @Column(unique = true)
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
}