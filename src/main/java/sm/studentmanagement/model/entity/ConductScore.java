package sm.studentmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import sm.studentmanagement.model.enums.ConductClassification;
import sm.studentmanagement.model.enums.ConductStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "conduct_scores", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "semester_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConductScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private Integer score; // 0 - 100

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConductClassification classification;

    @Column(name = "teacher_notes", columnDefinition = "TEXT")
    private String teacherNotes;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConductStatus status = ConductStatus.DRAFT;

    @UpdateTimestamp
    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;
}
