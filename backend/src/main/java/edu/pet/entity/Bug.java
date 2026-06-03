package edu.pet.entity;

import edu.pet.enums.Priority;
import edu.pet.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@Data
@NoArgsConstructor // пустой конструктор нужен для работы с бд
@AllArgsConstructor
@Entity
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id по порядку, так как записи не удаляются, коллизий не будет
    @Column(name="bug_id")
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String info;
    @NotNull
    private Priority priority;
    private State state;
}
