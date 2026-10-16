package sm.studentmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.model.entity.*;
import sm.studentmanagement.model.enums.ConductClassification;
import sm.studentmanagement.model.enums.ConductStatus;
import sm.studentmanagement.model.enums.RoleName;
import sm.studentmanagement.model.enums.StudentStatus;
import sm.studentmanagement.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;
    private final GradeRepository gradeRepository;
    private final ConductScoreRepository conductScoreRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Dữ liệu khởi tạo đã tồn tại, bỏ qua bước seed data.");
            return;
        }

        log.info("========== BẮT ĐẦU KHỞI TẠO DỮ LIỆU MẪU (MOCK DATA) ==========");

        // 1. Roles
        Role adminRole = roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).description("Quản trị viên toàn hệ thống").build());
        Role teacherRole = roleRepository.save(Role.builder().name(RoleName.ROLE_TEACHER).description("Giáo viên chủ nhiệm").build());

        // 2. Users & Teachers
        User adminUser = userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@university.edu.vn")
                .roles(Set.of(adminRole))
                .build());

        User userTeacher1 = userRepository.save(User.builder()
                .username("teacher1")
                .password(passwordEncoder.encode("teacher123"))
                .email("teacher1@university.edu.vn")
                .roles(Set.of(teacherRole))
                .build());

        Teacher teacher1 = teacherRepository.save(Teacher.builder()
                .user(userTeacher1)
                .teacherCode("GV001")
                .fullName("ThS. Nguyễn Văn Thầy")
                .degree("Thạc sĩ")
                .build());

        User userTeacher2 = userRepository.save(User.builder()
                .username("teacher2")
                .password(passwordEncoder.encode("teacher123"))
                .email("teacher2@university.edu.vn")
                .roles(Set.of(teacherRole))
                .build());

        Teacher teacher2 = teacherRepository.save(Teacher.builder()
                .user(userTeacher2)
                .teacherCode("GV002")
                .fullName("TS. Trần Thị Cô")
                .degree("Tiến sĩ")
                .build());

        // 3. Departments & Majors
        Department itDept = departmentRepository.save(Department.builder()
                .departmentCode("CNTT")
                .name("Khoa Công Nghệ Thông Tin")
                .build());

        Major seMajor = majorRepository.save(Major.builder()
                .department(itDept)
                .majorCode("KTPM")
                .name("Kỹ Thuật Phần Mềm")
                .build());

        // 4. Classes (Lớp hành chính gắn liền GVCN)
        ClassEntity classA = classRepository.save(ClassEntity.builder()
                .classCode("CNTT-K21A")
                .name("Lớp Kỹ thuật phần mềm K21A")
                .major(seMajor)
                .teacher(teacher1) // Phân công cho teacher1
                .cohort(2021)
                .build());

        ClassEntity classB = classRepository.save(ClassEntity.builder()
                .classCode("CNTT-K21B")
                .name("Lớp Kỹ thuật phần mềm K21B")
                .major(seMajor)
                .teacher(teacher2) // Phân công cho teacher2
                .cohort(2021)
                .build());

        // 5. Students
        Student s1 = studentRepository.save(Student.builder()
                .studentCode("SV21001")
                .fullName("Nguyễn Văn An")
                .classEntity(classA)
                .dob(LocalDate.of(2003, 5, 12))
                .gender("MALE")
                .email("an.nv21001@st.university.edu.vn")
                .phone("0981112233")
                .address("Hà Nội")
                .status(StudentStatus.STUDYING)
                .build());

        Student s2 = studentRepository.save(Student.builder()
                .studentCode("SV21002")
                .fullName("Lê Thị Bình")
                .classEntity(classA)
                .dob(LocalDate.of(2003, 8, 20))
                .gender("FEMALE")
                .email("binh.lt21002@st.university.edu.vn")
                .phone("0982223344")
                .address("Hải Phòng")
                .status(StudentStatus.STUDYING)
                .build());

        Student s3 = studentRepository.save(Student.builder()
                .studentCode("SV21003")
                .fullName("Trần Văn Cường")
                .classEntity(classA)
                .dob(LocalDate.of(2003, 11, 5))
                .gender("MALE")
                .email("cuong.tv21003@st.university.edu.vn")
                .phone("0983334455")
                .address("Đà Nẵng")
                .status(StudentStatus.STUDYING)
                .build());

        Student s4 = studentRepository.save(Student.builder()
                .studentCode("SV21004")
                .fullName("Phạm Thị Dung")
                .classEntity(classB)
                .dob(LocalDate.of(2003, 2, 14))
                .gender("FEMALE")
                .email("dung.pt21004@st.university.edu.vn")
                .phone("0984445566")
                .address("Quảng Ninh")
                .status(StudentStatus.STUDYING)
                .build());

        // 6. Semesters
        Semester sem1 = semesterRepository.save(Semester.builder()
                .semesterCode("2024_2025_HK1")
                .name("Học kỳ 1 năm học 2024-2025")
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2025, 1, 15))
                .isActive(true)
                .build());

        // 7. Courses with flexible grade weights
        Course courseJava = courseRepository.save(Course.builder()
                .courseCode("CS101")
                .name("Lập trình Java Nâng Cao")
                .credits(3)
                .attendanceWeight(new BigDecimal("0.10"))
                .assignmentWeight(new BigDecimal("0.20"))
                .midtermWeight(new BigDecimal("0.20"))
                .finalWeight(new BigDecimal("0.50"))
                .build());

        Course courseDb = courseRepository.save(Course.builder()
                .courseCode("CS102")
                .name("Cơ sở dữ liệu nâng cao")
                .credits(3)
                .attendanceWeight(new BigDecimal("0.10"))
                .assignmentWeight(new BigDecimal("0.20"))
                .midtermWeight(new BigDecimal("0.30"))
                .finalWeight(new BigDecimal("0.40"))
                .build());

        // 8. Schedules
        scheduleRepository.save(Schedule.builder()
                .classEntity(classA)
                .course(courseJava)
                .semester(sem1)
                .lecturerName("ThS. Đỗ Minh Quân")
                .room("A1-302")
                .dayOfWeek(2) // Thứ Hai
                .startPeriod(1)
                .periodCount(3)
                .startDate(LocalDate.of(2024, 9, 2))
                .endDate(LocalDate.of(2024, 12, 30))
                .build());

        scheduleRepository.save(Schedule.builder()
                .classEntity(classA)
                .course(courseDb)
                .semester(sem1)
                .lecturerName("TS. Bùi Hữu Nghĩa")
                .room("B2-105")
                .dayOfWeek(4) // Thứ Tư
                .startPeriod(4)
                .periodCount(3)
                .startDate(LocalDate.of(2024, 9, 4))
                .endDate(LocalDate.of(2024, 12, 30))
                .build());

        // 9. Grades for Student SV21001
        BigDecimal att = new BigDecimal("9.0");
        BigDecimal ass = new BigDecimal("8.5");
        BigDecimal mid = new BigDecimal("8.0");
        BigDecimal fin = new BigDecimal("9.0");
        BigDecimal total10 = att.multiply(courseJava.getAttendanceWeight())
                .add(ass.multiply(courseJava.getAssignmentWeight()))
                .add(mid.multiply(courseJava.getMidtermWeight()))
                .add(fin.multiply(courseJava.getFinalWeight()))
                .setScale(2, RoundingMode.HALF_UP);

        gradeRepository.save(Grade.builder()
                .student(s1)
                .course(courseJava)
                .semester(sem1)
                .attendanceScore(att)
                .assignmentScore(ass)
                .midtermScore(mid)
                .finalScore(fin)
                .totalScore10(total10)
                .totalScore4(new BigDecimal("4.0"))
                .letterGrade("A")
                .isLocked(false)
                .build());

        // 10. Conduct Scores
        conductScoreRepository.save(ConductScore.builder()
                .student(s1)
                .semester(sem1)
                .score(88)
                .classification(ConductClassification.TOT)
                .teacherNotes("Ý thức học tập tốt, chấp hành nghiêm túc nội quy.")
                .status(ConductStatus.APPROVED)
                .build());

        conductScoreRepository.save(ConductScore.builder()
                .student(s2)
                .semester(sem1)
                .score(92)
                .classification(ConductClassification.XUAT_SAC)
                .teacherNotes("Ban cán sự gương mẫu, hoạt động tích cực.")
                .status(ConductStatus.APPROVED)
                .build());

        log.info("========== HOÀN TẤT KHỞI TẠO MOCK DATA ==========");
        log.info("Tài khoản ADMIN: username = admin, password = admin123");
        log.info("Tài khoản GVCN 1: username = teacher1, password = teacher123 (Chủ nhiệm CNTT-K21A)");
        log.info("Tài khoản GVCN 2: username = teacher2, password = teacher123 (Chủ nhiệm CNTT-K21B)");
    }
}
