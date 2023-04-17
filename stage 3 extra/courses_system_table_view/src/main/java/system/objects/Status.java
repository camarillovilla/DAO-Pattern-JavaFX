/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.objects;

/**
 *
 * @author josue
 */
public enum Status {

    SUCCESS("100"),
    WARNING("200"),
    ERROR("300"),
    FATAL("400");

    private final String code;

    private Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
