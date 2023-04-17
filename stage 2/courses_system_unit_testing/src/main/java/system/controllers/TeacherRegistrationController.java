package system.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import system.daos.contracts.ITeacherDAO;
import system.daos.implementations.DAOException;
import system.daos.implementations.TeacherDAO;
import system.objects.AlertMessage;
import system.objects.Status;
import system.objects.TeacherDTO;

public class TeacherRegistrationController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private ComboBox<String> studyGradeCmbox;

    private final ITeacherDAO TEACHER_DAO = new TeacherDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studyGradeCmbox.setItems(FXCollections.observableArrayList("Maestría", "Doctorado"));
        studyGradeCmbox.getSelectionModel().select("Doctorado");
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (isConfirmed()) {
            App.setRoot("/system/views/VLogin");
        }
    }

    @FXML
    private void signUp(ActionEvent event) throws IOException {
        try {
            invokeTeacherRegistration(getDataFromForm());
        } catch (IllegalArgumentException ex) {
            handleValidationException(ex);
        } catch (DAOException ex) {
            handleDAOException(ex);
        }
    }

    private void invokeTeacherRegistration(TeacherDTO teacher) throws IOException, DAOException {
        int idTeacher = TEACHER_DAO.addTeacher(teacher);
        if (idTeacher > 0) {
            TeacherDTO.getSession().setId(idTeacher);
            App.changeView("/system/views/VCoursesList", 600, 400);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "No fue posible registrar al académico",
                    Status.WARNING));
        }
    }

    private void handleDAOException(DAOException ex) throws IOException {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                ex.getStatus()));
        switch (ex.getStatus()) {
            case ERROR:
            case FATAL:
                App.setRoot("/system/views/VLogin");
                break;
        }
    }

    private void handleValidationException(IllegalArgumentException ex) throws IOException {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                Status.WARNING));
    }

    private boolean isConfirmed() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Deseas salir del registro?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private TeacherDTO getDataFromForm() {
        TeacherDTO.getSession().setName(nameField.getText());
        TeacherDTO.getSession().setEmail(emailField.getText());
        TeacherDTO.getSession().setPassword(passwordField.getText());
        TeacherDTO.getSession().setStudyGrade(studyGradeCmbox.getSelectionModel().getSelectedItem());
        return TeacherDTO.getSession();
    }

}
