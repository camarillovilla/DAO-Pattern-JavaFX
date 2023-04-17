/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author josue
 */
public class CourseDTO {

    private int id;
    private String nrc;
    private String name;
    private String courseType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int idTeacher;

    private final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private final String NRC_REGEX = "^\\d{5}$";
    private final String NAME_REGEX = "^(?!.*[\\!\\#\\$%\\&'\\(\\)\\*\\+\\-\\.,\\/\\:\\;<\\=\\>\\?\\@\\[\\\\\\]\\^_`\\{\\|\\}\\~])(?!.*  )(?!^ $)(?!.*\\d)^.{3,300}$";
    private final String COURSE_TYPE_REGEX = "^(Presencial|Autoacceso|Intersemestral|Multimodal)$";

    public CourseDTO() {
    }

    public CourseDTO(String nrc, String name, String courseType, int idTeacher) {
        checkNrc(nrc);
        checkName(name);
        checkCourseType(courseType);
        this.nrc = nrc;
        this.name = name;
        this.courseType = courseType;
        this.idTeacher = idTeacher;
    }

    public CourseDTO(int id, String nrc, String name, String courseType, LocalDateTime createdAt, LocalDateTime updatedAt, int idTeacher) {
        checkNrc(nrc);
        checkName(name);
        checkCourseType(courseType);
        this.id = id;
        this.nrc = nrc;
        this.name = name;
        this.courseType = courseType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.idTeacher = idTeacher;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCreatedAt() {
        return createdAt.format(FORMATTER);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt.format(FORMATTER);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    private void checkNrc(String nrc) {
        Pattern pattern = Pattern.compile(NRC_REGEX);
        Matcher matcher = pattern.matcher(nrc);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El NRC debe cumplir las siguientes condiciones:\n"
                    + "1.- Debe contener exactamente 5 digitos\n");
        }
    }

    private void checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El nombre del curso debe tener las siguientes características:\n"
                    + "1.- Debe contener de 3 a 300 caractéres como máximo\n"
                    + "2.- No puede contener más de 2 espacios en blanco juntos\n"
                    + "3.- No puede tener solo espacios en blanco\n"
                    + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, ,, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
        }
    }

    private void checkCourseType(String courseType) {
        Pattern pattern = Pattern.compile(COURSE_TYPE_REGEX);
        Matcher matcher = pattern.matcher(courseType);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El tipo de curso solo puede ser:\n"
                    + "1.- Presencial\n"
                    + "2.- Autoacceso\n"
                    + "3.- Intersemestral\n"
                    + "4.- Multimodal\n");
        }
    }

}
