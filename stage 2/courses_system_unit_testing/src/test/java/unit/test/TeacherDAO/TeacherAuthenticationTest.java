/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.TeacherDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import system.daos.implementations.DAOException;
import system.daos.implementations.DBConnection;
import system.daos.implementations.TeacherDAO;
import system.objects.TeacherDTO;

/**
 *
 * @author josue
 */
public class TeacherAuthenticationTest {

    private static TeacherDAO TEACHER_DAO;
    private static TeacherDTO teacherForTesting;

    private static final String DEFAULT_EMAIL = "anónimo@gmail.com";
    private static final String DEFAULT_PASSWORD = "anónimo1234";
    private static final String DEFAULT_NAME = "anónimo";
    private static final String DEFAULT_STUDY_GRADE = "Doctorado";

    @BeforeAll
    static void setup() {
        TEACHER_DAO = new TeacherDAO();
        teacherForTesting = TeacherDTO.getNewInstance();
        teacherForTesting.setEmail(DEFAULT_EMAIL);
        teacherForTesting.setPassword(DEFAULT_PASSWORD);
        teacherForTesting.setName(DEFAULT_NAME);
        teacherForTesting.setStudyGrade(DEFAULT_STUDY_GRADE);
        try {
            int idTeacher = TEACHER_DAO.addTeacher(teacherForTesting);
            teacherForTesting.setId(idTeacher);
        } catch (DAOException ex) {
            Logger.getLogger(TeacherUpdateTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterAll
    static void done() {
        try {
            TEACHER_DAO.deleteTeacher(teacherForTesting.getId());
            DBConnection.close();
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testSuccessfulTeacherAuthentication() {
        int idTeacher = -1;
        try {
            TeacherDTO teacher = TEACHER_DAO.authenticateTeacher(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            idTeacher = teacher.getId();
        } catch (DAOException ex) {
            Logger.getLogger(TeacherAuthenticationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertTrue(idTeacher > 0);
    }

    @Test
    void testFailedTeacherWithWrongEmailAuthentication() {
        int idTeacher = -1;
        try {
            TeacherDTO teacher = TEACHER_DAO.authenticateTeacher("Non-register-email@gmail.com", DEFAULT_PASSWORD);
            idTeacher = teacher.getId();
        } catch (DAOException ex) {
            Logger.getLogger(TeacherAuthenticationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertTrue(idTeacher <= 0);
    }

    @Test
    void testFailedTeacherWithWrongPasswordAuthentication() {
        int idTeacher = -1;
        try {
            TeacherDTO teacher = TEACHER_DAO.authenticateTeacher(DEFAULT_EMAIL, "non-registered-password");
            idTeacher = teacher.getId();
        } catch (DAOException ex) {
            Logger.getLogger(TeacherAuthenticationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertTrue(idTeacher <= 0);
    }
}
