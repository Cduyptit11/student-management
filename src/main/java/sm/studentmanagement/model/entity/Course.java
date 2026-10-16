package sm.studentmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 20)
    private String courseCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private Integer credits = 3;

    // Cấu hình tỷ trọng điểm linh hoạt theo từng môn học
    @Builder.Default
    @Column(name = "attendance_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal attendanceWeight = new BigDecimal("0.10"); // 10%

    @Builder.Default
    @Column(name = "assignment_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal assignmentWeight = new BigDecimal("0.20"); // 20%

    @Builder.Default
    @Column(name = "midterm_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal midtermWeight = new BigDecimal("0.20");    // 20%

    @Builder.Default
    @Column(name = "final_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal finalWeight = new BigDecimal("0.50");      // 50%

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
