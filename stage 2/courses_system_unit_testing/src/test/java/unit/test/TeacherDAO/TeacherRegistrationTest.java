package unit.test.TeacherDAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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
public class TeacherRegistrationTest {

    private static TeacherDAO TEACHER_DAO;
    private static TeacherDTO teacherForTesting;
    private static TeacherDTO auxTeacherForTesting;

    private static final String DEFAULT_EMAIL = "anónimo@gmail.com";
    private static final String DEFAULT_PASSWORD = "anónimo1234";
    private static final String DEFAULT_NAME = "anónimo";
    private static final String DEFAULT_STUDY_GRADE = "Doctorado";

    private static final String DEFAULT_AUX_EMAIL = "auxiliar@gmail.com";
    private static final String DEFAULT_AUX_PASSWORD = "auxilia1234";
    private static final String DEFAULT_AUX_NAME = "auxiliar";
    private static final String DEFAULT_AUX_STUDY_GRADE = "Maestría";

    @BeforeAll
    static void setup() {
        TEACHER_DAO = new TeacherDAO();
        teacherForTesting = TeacherDTO.getNewInstance();
        auxTeacherForTesting = TeacherDTO.getNewInstance();
        auxTeacherForTesting.setEmail(DEFAULT_AUX_EMAIL);
        auxTeacherForTesting.setPassword(DEFAULT_AUX_PASSWORD);
        auxTeacherForTesting.setName(DEFAULT_AUX_NAME);
        auxTeacherForTesting.setStudyGrade(DEFAULT_AUX_STUDY_GRADE);
        try {
            int id = TEACHER_DAO.addTeacher(auxTeacherForTesting);
            auxTeacherForTesting.setId(id);
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @BeforeEach
    void init() {
        teacherForTesting.setEmail(DEFAULT_EMAIL);
        teacherForTesting.setPassword(DEFAULT_PASSWORD);
        teacherForTesting.setName(DEFAULT_NAME);
        teacherForTesting.setStudyGrade(DEFAULT_STUDY_GRADE);
    }

    @AfterEach
    void tearDown() {
        try {
            TEACHER_DAO.deleteTeacher(teacherForTesting.getId());
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterAll
    static void done() {
        try {
            TEACHER_DAO.deleteTeacher(auxTeacherForTesting.getId());
            DBConnection.close();
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testSuccessfulTeacherAddition() {
        int idTeacher = -1;
        try {
            idTeacher = TEACHER_DAO.addTeacher(teacherForTesting);
            teacherForTesting.setId(idTeacher);
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertTrue(idTeacher > 0);
    }

    @Test
    void testFailedTeacherAdditionWithDuplicatedEmail() {
        int idTeacher = -1;
        try {
            teacherForTesting.setEmail(DEFAULT_AUX_EMAIL);
            idTeacher = TEACHER_DAO.addTeacher(teacherForTesting);
        } catch (DAOException ex) {
            Logger.getLogger(TeacherRegistrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertFalse(idTeacher > 0);
    }

}
