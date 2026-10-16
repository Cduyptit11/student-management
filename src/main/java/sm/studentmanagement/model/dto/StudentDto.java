package sm.studentmanagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sm.studentmanagement.model.enums.StudentStatus;

import java.time.LocalDate;

public class StudentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrUpdateRequest {
        @NotBlank(message = "Mã sinh viên không được để trống")
        private String studentCode;

        @NotBlank(message = "Họ và tên không được để trống")
        private String fullName;

        @NotNull(message = "Lớp học không được để trống")
        private Long classId;

        @NotNull(message = "Ngày sinh không được để trống")
        private LocalDate dob;

        private String gender;
        private String email;
        private String phone;
        private String address;
        private StudentStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String studentCode;
        private String fullName;
        private Long classId;
        private String classCode;
        private String className;
        private LocalDate dob;
        private String gender;
        private String email;
        private String phone;
        private String address;
        private StudentStatus status;
    }
}
