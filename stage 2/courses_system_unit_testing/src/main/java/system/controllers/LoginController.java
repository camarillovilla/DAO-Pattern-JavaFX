package system.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import system.daos.contracts.ITeacherDAO;
import system.daos.implementations.DAOException;
import system.daos.implementations.TeacherDAO;
import system.objects.AlertMessage;
import system.objects.Status;
import system.objects.TeacherDTO;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final ITeacherDAO TEACHER_DAO = new TeacherDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TeacherDTO.cleanSession();
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException {
        App.setRoot("/system/views/VTeacherRegistration");
    }

    @FXML
    private void signIn(ActionEvent event) throws IOException {
        try {
            invokeTeacherAuthentication(
                    emailField.getText(),
                    passwordField.getText());
        } catch (DAOException ex) {
            handleDAOException(ex);
        }
    }

    private void invokeTeacherAuthentication(String email, String password) throws IOException, DAOException {
        TeacherDTO teacher = TEACHER_DAO.authenticateTeacher(email, password);
        if (teacher.getId() > 0) {
            App.changeView("/system/views/VCoursesList", 600, 400);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se encotró ningún academico con ese correo y contraseña",
                    Status.WARNING));
        }
    }

    private void handleDAOException(DAOException ex) {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                ex.getStatus()));
    }

}
