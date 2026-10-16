package sm.studentmanagement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "majors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnore
    private Department department;

    @Column(name = "major_code", nullable = false, unique = true, length = 20)
    private String majorCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<ClassEntity> classes = new ArrayList<>();
}
