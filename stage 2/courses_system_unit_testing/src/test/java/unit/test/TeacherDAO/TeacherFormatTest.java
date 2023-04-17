package unit.test.TeacherDAO;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import system.objects.TeacherDTO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author josue
 */
public class TeacherFormatTest {

    @Test
    void testSuccessfulTeacherEmailValidation() {
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        String validEmail = "anónimo012@uv.mx";
        try {
            teacher.setEmail(validEmail);
        } catch (IllegalArgumentException ex) {
            validEmail = "Invalid";
        }
        Assertions.assertEquals(teacher.getEmail(), validEmail);
    }

    @Test
    void testSuccessfulTeacherEmailValidationWithLimitCharacters() {
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        String validEmail = "emailconmuchoscaracteresemailconmuchoscaracteresemailco"
                + "nmuchoscaracteresemailconmuchoscaracteresemailconmuch"
                + "oscaracteresemailconmuchoscaracteresemailconmuchoscar"
                + "acteresemailconmuchoscaracteresemailconmuchoscaracter"
                + "esemailconmuchoscaracteresemailc@gmail.com";
        try {
            teacher.setEmail(validEmail);
        } catch (IllegalArgumentException ex) {
            validEmail = "Invalid";
        }
        Assertions.assertEquals(teacher.getEmail(), validEmail);
    }

    @Test
    void testTryTeacherEmailValidationWithInvalidDomain() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setEmail("invalidEmail@non_correct.com");
        });
    }

    @Test
    void testTryTeacherEmailValidationWithLongEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setEmail(
                    "emailconmuchoscaracteresemailconmuchoscaracteresemailco"
                    + "nmuchoscaracteresemailconmuchoscaracteresemailconmuch"
                    + "oscaracteresemailconmuchoscaracteresemailconmuchoscar"
                    + "acteresemailconmuchoscaracteresemailconmuchoscaracter"
                    + "esemailconmuchoscaracteresemailco@gmail.com");
        });
    }

    @Test
    void testTryTeacherEmailValidationWithShortEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setEmail("@gmail.com");
        });
    }

    @Test
    void testTryTeacherEmailValidationWithBlankSpaces() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setEmail("email @gmail.com");
        });
    }

    @Test
    void testSuccessfulPasswordValidation() {
        String validatePassword = "_contra_";
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        try {
            teacher.setPassword(validatePassword);
        } catch (IllegalArgumentException ex) {
            validatePassword = "inválida";
        }
        Assertions.assertEquals(validatePassword, teacher.getPassword());
    }

    @Test
    void testFailedLongPasswordValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setPassword(
                    "una_contraseña_extensa_una_contraseña_extensa_una_"
                    + "contraseña_extensa_una_contraseña_extensa_una_contr");
        });
    }

    @Test
    void testFailedShortPasswordValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setPassword("una_con");
        });
    }

    @Test
    void testSuccessfulNameValidation() {
        String validName = "Anónimo apellido apellido";
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        try {
            teacher.setName(validName);
        } catch (IllegalArgumentException ex) {
            validName = "inválida";
        }
        Assertions.assertEquals(validName, teacher.getName());
    }

    @Test
    void testFailedShortNameValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName("us");
        });
    }

    @Test
    void testFailedLongNameValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName(
                    "Nombre ApellidoPaterno ApellidoMaterno Nombre Apellido"
                    + "Paterno ApellidoMaterno Nombre ApellidoPaterno Apelli"
                    + "doMaterno Nombre ApellidoPaterno ApellidoMaterno Nombre "
                    + "ApellidoPaterno ApellidoMaterno Nombre ApellidoPaterno "
                    + "ApellidoMaterno Nombre ApellidoPaterno ApellidoMaterno "
                    + "Nombre ApellidoPaterno Nombr");
        });
    }

    @Test
    void testFailedTwoConsecutiveSpacesInNameValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName(
                    "Nombre  Dos espacios despues         ");
        });
    }

    @Test
    void testFailedNameWithNumbersValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName(
                    "Anónimo Apellido1 Apellido2");
        });
    }

    @Test
    void testFailedNameWithOnlyBlankSpacesValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName(
                    "                   ");
        });
    }

    @Test
    void testFailedNameWithSpecialCharactersValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setName(
                    "Anónim@ [{?=el.");
        });
    }

    @Test
    void testSuccessfulStudyGradeValidation() {
        String validOptionOne = "Doctorado";
        String validOptionTwo = "Maestría";
        TeacherDTO teacher = TeacherDTO.getNewInstance();
        try {
            teacher.setStudyGrade(validOptionOne);
            teacher.setStudyGrade(validOptionTwo);
        } catch (IllegalArgumentException ex) {
            validOptionOne = "inválida";
            validOptionTwo = "inválida";
        }
        Assertions.assertEquals(validOptionTwo, teacher.getStudyGrade());
    }

    @Test
    void testFailedNotFoundStudyGradeValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TeacherDTO.getNewInstance().setStudyGrade("Licenciatura");
        });
    }

}
