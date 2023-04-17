/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.controllers;

import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import system.objects.AlertMessage;

/**
 *
 * @author josue
 */
public class DialogGenerator {

    public static final ButtonType BUTTON_YES = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
    public static final ButtonType BUTTON_NO = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static Optional<ButtonType> getDialog(AlertMessage message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (null != message.getAlertType()) {
            switch (message.getAlertType()) {
                case SUCCESS:
                    break;
                case WARNING:
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Advertencia");
                    break;
                case ERROR:
                case FATAL:
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    break;
                default:
                    throw new IllegalArgumentException("El tipo de mensaje no es admitido en el objeto Alert");
            }
        }
        alert.setTitle("Cuadro de diálogo");
        alert.setContentText(message.getContent());
        return alert.showAndWait();
    }

    public static Optional<ButtonType> getConfirmationDialog(String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                BUTTON_YES,
                BUTTON_NO);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Selecciona la opción de tu preferencia");
        return alert.showAndWait();
    }

    public static Optional<ButtonType> getCustomDialog(String message, ObservableList<ButtonType> options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Selecciona la opción de tu preferencia");
        alert.setContentText(message);
        options.add(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.getButtonTypes().setAll(options);
        return alert.showAndWait();
    }
}
