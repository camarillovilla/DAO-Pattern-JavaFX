/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.objects;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author josue
 */
public class TeacherDTO {

    private static TeacherDTO teacher;
    private int id;
    private String email;
    private String password;
    private String name;
    private String studyGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final String EMAIL_REGEX = "^(?=.{1,256}$)[^\\s@]+@(?:uv\\.mx|estudiantes\\.uv\\.mx|gmail\\.com|hotmail\\.com|outlook\\.com|edu\\.mx)$";
    private final String PASSWORD_REGEX = "^.{8,100}$";
    private final String NAME_REGEX = "^(?!.*[\\!\\#\\$%\\&'\\(\\)\\*\\+\\-\\.,\\/\\:\\;<\\=\\>\\?\\@\\[\\\\\\]\\^_`\\{\\|\\}\\~])(?!.*  )(?!^ $)(?!.*\\d)^.{3,300}$";
    private final String STUDY_GRADE_REGEX = "^(Doctorado|Maestría)$";

    public static TeacherDTO getSession() {
        if (teacher == null) {
            teacher = new TeacherDTO();
            teacher.setId(-1);
        }
        return teacher;
    }

    public static TeacherDTO cleanSession() {
        getSession();
        teacher.setId(-1);
        teacher.setEmail("anónimo@uv.mx");
        teacher.setPassword("sin_contraseña");
        teacher.setName("anónimo");
        teacher.setStudyGrade("Doctorado");
        return teacher;
    }

    public static TeacherDTO getNewInstance() {
        return new TeacherDTO();
    }

    private TeacherDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        checkEmail(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        checkPassword(password);
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    public String getStudyGrade() {
        return studyGrade;
    }

    public void setStudyGrade(String studyGrade) {
        checkStudyGrade(studyGrade);
        this.studyGrade = studyGrade;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void checkEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El email debe contener las siguientes características:\n"
                    + "1.- No debe contener espacios en blanco\n"
                    + "2.- Solo los siguientes dominios son permitidos: (@uv.mx, @estudiantes.uv.mx, @gmail.com, @hotmail.com, @outlook.com, @edu.mx)\n");
        }
    }

    private void checkPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("La contraseña debe contener las siguientes características:\n"
                    + "1.- Debe contener de 8 a 100 caractéres como máximo");
        }
    }

    private void checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El nombre debe tener las siguientes características:\n"
                    + "1.- Debe contener de 3 a 300 caractéres como máximo\n"
                    + "2.- No puede contener más de 2 espacios en blanco juntos\n"
                    + "3.- No puede tener solo espacios en blanco\n"
                    + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, ,, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
        }
    }

    private void checkStudyGrade(String studyGrade) {
        Pattern pattern = Pattern.compile(STUDY_GRADE_REGEX);
        Matcher matcher = pattern.matcher(studyGrade);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El grado de estudios del académico puede ser solo:\n"
                    + "1.- Doctorado\n"
                    + "2.- Maestría\n");
        }
    }

}
