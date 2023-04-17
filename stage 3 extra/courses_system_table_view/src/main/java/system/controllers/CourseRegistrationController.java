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
import system.daos.contracts.ICourseDAO;
import system.daos.implementations.CourseDAO;
import system.daos.implementations.DAOException;
import system.objects.AlertMessage;
import system.objects.CourseDTO;
import system.objects.Status;
import static system.objects.Status.ERROR;
import static system.objects.Status.FATAL;
import static system.objects.Status.WARNING;
import system.objects.TeacherDTO;

/**
 *
 * @author josue
 */
public class CourseRegistrationController implements Initializable {

    @FXML
    private TextField nrcField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> courseTypeCmbox;

    private final ICourseDAO COURSE_DAO = new CourseDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseTypeCmbox.setItems(FXCollections.observableArrayList("Presencial", "Autoacceso", "Multimodal", "Intersemestral"));
        courseTypeCmbox.getSelectionModel().select("Presencial");
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (isConfirmed()) {
            App.changeView("/system/views/VCoursesList", 600, 400);
        }
    }

    @FXML
    private void addCourse(ActionEvent event) throws IOException {
        try {
            invokeCourseRegistration(getDataFromForm());
        } catch (IllegalArgumentException ex) {
            handleValidationException(ex);
        } catch (DAOException ex) {
            handleDAOException(ex);
        }
    }

    private void invokeCourseRegistration(CourseDTO course) throws DAOException, IOException {
        int idCourse = COURSE_DAO.addCourse(course);
        if (idCourse > 0) {
            App.changeView("/system/views/VCoursesList", 600, 400);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "El curso no fue agregado al sistema",
                    Status.WARNING));
        }
    }

    private boolean isConfirmed() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Deseas cancelar el registro?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private CourseDTO getDataFromForm() {
        return new CourseDTO(
                nrcField.getText(),
                nameField.getText(),
                courseTypeCmbox.getSelectionModel().getSelectedItem(),
                TeacherDTO.getSession().getId());

    }

    private void handleDAOException(DAOException ex) {
        try {
            DialogGenerator.getDialog(new AlertMessage(
                    ex.getMessage(),
                    ex.getStatus()));
            switch (ex.getStatus()) {
                case ERROR:
                    App.changeView("/system/views/VCoursesList", 600, 400);
                    break;
                case FATAL:
                    App.changeView("/system/views/VLogin", 304, 409);
                    break;
                case WARNING:
                    break;
                default:
                    throw new IllegalArgumentException("No se reconoce el estado de la operación");
            }
        } catch (IOException ex1) {
            throw new IllegalArgumentException("No se encontró la vista referenciada");
        }
    }

    private void handleValidationException(IllegalArgumentException ex) {
        DialogGenerator.getDialog(new AlertMessage(
                ex.getMessage(),
                Status.WARNING));
    }

}
