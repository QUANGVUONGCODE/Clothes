package com.vuongdev.Storeclothes.entity;

import com.vuongdev.Storeclothes.dto.response.DepartmentResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "department_id"})
        }
)
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    Departments department;
}
