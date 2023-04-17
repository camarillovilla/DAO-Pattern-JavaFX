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
import system.objects.CourseDTO;
import system.daos.contracts.ICourseDAO;
import system.daos.implementations.CourseDAO;
import system.daos.implementations.DAOException;
import system.objects.AlertMessage;
import system.objects.Status;

/**
 *
 * @author josue
 */
public class CourseEditionController implements Initializable {

    @FXML
    private TextField nrcField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> courseTypeCmbox;

    private final ICourseDAO COURSE_DAO = new CourseDAO();
    private CourseDTO course;

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
    private void updateCourse(ActionEvent event) throws IOException {
        try {
            invokeCourseUpdate(this.course.getId(), getDataFromForm());
        } catch (IllegalArgumentException ex) {
            handleValidationException(ex);
        } catch (DAOException ex) {
            handleDAOException(ex);
        }

    }

    private void invokeCourseUpdate(int idCourse, CourseDTO course) throws DAOException, IOException {
        int response = COURSE_DAO.updateCourse(idCourse, course);
        if (response > 0) {
            App.changeView("/system/views/VCoursesList", 600, 400);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "No fue posible actualizar el curso",
                    Status.FATAL));
        }
    }

    private boolean isConfirmed() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Deseas cancelar la actualización?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
        setDataInForm(course);
    }

    private void setDataInForm(CourseDTO course) {
        nrcField.setText(course.getNrc());
        nameField.setText(course.getName());
        courseTypeCmbox.getSelectionModel().select(course.getCourseType());
    }

    private CourseDTO getDataFromForm() {
        CourseDTO course = new CourseDTO(
                nrcField.getText(),
                nameField.getText(),
                courseTypeCmbox.getSelectionModel().getSelectedItem(),
                this.course.getIdTeacher());
        return course;
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
