/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import system.daos.contracts.ICourseDAO;
import system.daos.implementations.CourseDAO;
import system.daos.implementations.DAOException;
import system.objects.AlertMessage;
import system.objects.CourseDTO;
import system.objects.Status;
import system.objects.TeacherDTO;

/**
 *
 * @author josue
 */
public class CoursesListController implements Initializable {

    @FXML
    private TableView<CourseDTO> coursesTable;
    @FXML
    private TableColumn<CourseDTO, String> nrcColumn;
    @FXML
    private TableColumn<CourseDTO, String> nameColumn;
    @FXML
    private TableColumn<CourseDTO, String> courseTypeColumn;
    @FXML
    private TableColumn<CourseDTO, String> updatedAtColumn;

    private final ICourseDAO COURSE_DAO = new CourseDAO();
    private CourseDTO itemSelected = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeTableView();
            coursesTable.setItems(COURSE_DAO.getCoursesForTeacher(TeacherDTO.getSession().getId()));
        } catch (DAOException ex) {
            handleDAOException(ex);
        }
    }

    @FXML
    private void updateProfile(ActionEvent event) throws IOException {
        App.changeView("/system/views/VTeacherEdition", 304, 420);
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        App.changeView("/system/views/VLogin", 304, 409);
    }

    @FXML
    private void selectCourse(MouseEvent event) {
        CourseDTO course = coursesTable.getSelectionModel().getSelectedItem();
        if (course != null) {
            itemSelected = course;
        }
    }

    @FXML
    private void addCourse(ActionEvent event) throws IOException {
        App.changeView("/system/views/VCourseRegistration", 293, 395);
    }

    @FXML
    private void editCourse(ActionEvent event) throws IOException {
        if (this.itemSelected != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/system/views/VCourseEdition.fxml"));
            App.changeView(fxmlLoader, 293, 395);
            CourseEditionController controller = fxmlLoader.getController();
            controller.setCourse(this.itemSelected);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Por favor, selecciona un curso de la tabla para habilitar está opción",
                    Status.WARNING));
        }
    }

    @FXML
    private void deleteCourse(ActionEvent event) {
        if (this.itemSelected != null) {
            if (isConfirmed()) {
                invokeCourseDelete();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Por favor, selecciona un curso de la tabla para habilitar está opción",
                    Status.WARNING));
        }
    }

    private boolean isConfirmed() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Deseas eliminar el curso?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void invokeCourseDelete() {
        try {
            COURSE_DAO.deleteCourse(this.itemSelected.getId());
            coursesTable.getItems().remove(this.itemSelected);
            this.itemSelected = null;
        } catch (DAOException ex) {
            handleDAOException(ex);
        };
    }

    private void handleDAOException(DAOException ex) {
        try {
            DialogGenerator.getDialog(new AlertMessage(
                    ex.getMessage(),
                    ex.getStatus()));
            switch (ex.getStatus()) {
                case ERROR:
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

    private void initializeTableView() {
        nrcColumn.setCellValueFactory(new PropertyValueFactory<CourseDTO, String>("nrc"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<CourseDTO, String>("name"));
        courseTypeColumn.setCellValueFactory(new PropertyValueFactory<CourseDTO, String>("courseType"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<CourseDTO, String>("updatedAt"));
    }

}
