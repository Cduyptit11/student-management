package sm.studentmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_code", nullable = false, unique = true, length = 20)
    private String departmentCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Major> majors = new ArrayList<>();
}
