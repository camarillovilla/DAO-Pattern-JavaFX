/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author josue
 */
public class CoursesListController {

    @FXML
    private void updateProfile(ActionEvent event) throws IOException {
        App.changeView("/system/views/VTeacherEdition", 304, 420);
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        App.changeView("/system/views/VLogin", 304, 409);
    }

}
