package sm.studentmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "lecturer_name", length = 100)
    private String lecturerName;

    @Column(nullable = false, length = 50)
    private String room;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 2: Monday -> 8: Sunday

    @Column(name = "start_period", nullable = false)
    private Integer startPeriod; // 1-10

    @Column(name = "period_count", nullable = false)
    private Integer periodCount; // Số tiết (vd: 3 tiết)

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
