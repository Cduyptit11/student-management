package sm.studentmanagement.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ScheduleDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "Lớp học không được để trống")
        private Long classId;

        @NotNull(message = "Môn học không được để trống")
        private Long courseId;

        @NotNull(message = "Học kỳ không được để trống")
        private Integer semesterId;

        private String lecturerName;

        @NotBlank(message = "Phòng học không được để trống")
        private String room;

        @NotNull(message = "Thứ trong tuần không được để trống")
        @Min(value = 2, message = "Thứ phải từ 2 (Thứ Hai)")
        @Max(value = 8, message = "Thứ phải đến 8 (Chủ Nhật)")
        private Integer dayOfWeek;

        @NotNull(message = "Tiết bắt đầu không được để trống")
        @Min(value = 1, message = "Tiết bắt đầu từ 1")
        @Max(value = 10, message = "Tiết tối đa là 10")
        private Integer startPeriod;

        @NotNull(message = "Số tiết không được để trống")
        @Min(value = 1, message = "Tối thiểu 1 tiết")
        private Integer periodCount;

        @NotNull(message = "Ngày bắt đầu không được để trống")
        private LocalDate startDate;

        @NotNull(message = "Ngày kết thúc không được để trống")
        private LocalDate endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long classId;
        private String classCode;
        private String className;
        private Long courseId;
        private String courseCode;
        private String courseName;
        private Integer semesterId;
        private String semesterName;
        private String lecturerName;
        private String room;
        private Integer dayOfWeek;
        private Integer startPeriod;
        private Integer periodCount;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
