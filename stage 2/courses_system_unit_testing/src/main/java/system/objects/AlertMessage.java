/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.objects;

/**
 *
 * @author josue
 */
public class AlertMessage {

    private String content = "La operación se ha realizado exitosamente";
    private Status alertType = Status.SUCCESS;

    public AlertMessage(String content, Status alertType) {
        if (content.isEmpty() || alertType == null) {
            throw new IllegalArgumentException("Debes ingresar un mensaje y un estádo en el mensaje de alerta");
        }
        this.content = content;
        this.alertType = alertType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getAlertType() {
        return alertType;
    }

    public void setAlertType(Status alertType) {
        this.alertType = alertType;
    }

}
