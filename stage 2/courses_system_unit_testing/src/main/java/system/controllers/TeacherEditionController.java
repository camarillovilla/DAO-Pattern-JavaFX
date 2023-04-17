/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author josue
 */
public class TeacherEditionController implements Initializable {

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
        setDataInForm(TeacherDTO.getSession());
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (isConfirmed()) {
            App.changeView("/system/views/VCoursesList", 600, 400);
        }
    }

    @FXML
    private void update(ActionEvent event) throws IOException {
        try {
            invokeTeacherUpdate(
                    TeacherDTO.getSession().getId(),
                    getDataFromForm(TeacherDTO.getNewInstance()));
        } catch (IllegalArgumentException ex) {
            handleValidationException(ex);
        } catch (DAOException ex) {
            handleDAOException(ex);
        }
    }

    private TeacherDTO getDataFromForm(TeacherDTO teacher) {
        teacher.setId(TeacherDTO.getSession().getId());
        teacher.setName(nameField.getText());
        teacher.setEmail(emailField.getText());
        teacher.setPassword(passwordField.getText());
        teacher.setStudyGrade(studyGradeCmbox.getSelectionModel().getSelectedItem());
        return teacher;
    }

    private void setDataInForm(TeacherDTO teacher) {
        emailField.setText(teacher.getEmail());
        passwordField.setText(teacher.getPassword());
        nameField.setText(teacher.getName());
        studyGradeCmbox.getSelectionModel().select(teacher.getStudyGrade());
    }

    private boolean isConfirmed() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Deseas cancelar la actualización?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void invokeTeacherUpdate(int teacherId, TeacherDTO teacher) throws IOException, DAOException {
        int response = TEACHER_DAO.updateTeacher(teacherId, teacher);
        if (response > 0) {
            getDataFromForm(TeacherDTO.getSession());
            App.changeView("/system/views/VCoursesList", 600, 400);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "No fue posible actualizar al académico",
                    Status.WARNING));
        }
    }

    private void handleDAOException(DAOException ex) throws IOException {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                ex.getStatus()));
        switch (ex.getStatus()) {
            case ERROR:
                App.changeView("/system/views/VCoursesList", 600, 400);
                break;
            case FATAL:
                App.setRoot("/system/views/VLogin");
                break;
        }
    }

    private void handleValidationException(IllegalArgumentException ex) {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                Status.WARNING));
    }

}
