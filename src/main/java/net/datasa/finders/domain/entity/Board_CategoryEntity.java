package net.datasa.finders.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_category")
public class Board_CategoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_num")
    Integer categoryNum;

    @ManyToOne
    @JoinColumn(name = "project_num", referencedColumnName = "project_num")
    BoardEntity boardEntity;

    @Column(name = "category", nullable = false)
    String category;

    @Column(name = "required_num", nullable = false)
    int requiredNum;
}