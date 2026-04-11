package com.vuongdev.Storeclothes.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "sizes")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    String name;

    @Column(name = "sort_order", nullable = false)
    Integer sortOrder;

    @Column(name = "status", nullable = false, length = 20)
    String status;
}
