package sm.studentmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ClassDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String classCode;
        private String name;
        private String majorName;
        private Long teacherId;
        private String teacherName;
        private String teacherCode;
        private Integer cohort;
        private int totalStudents;
    }
}
