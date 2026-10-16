package sm.studentmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import sm.studentmanagement.model.enums.RoleName;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false, unique = true)
    private RoleName name;

    @Column(length = 100)
    private String description;
}
