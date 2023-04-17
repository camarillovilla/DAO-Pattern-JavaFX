/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.daos.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import system.controllers.App;
import system.objects.Status;

/**
 *
 * @author josue
 */
public class DBConnection {

    private static Connection connection;

    private static final String URL_PROPERTY_FIELD = "mysql.db.url";
    private static final String USER_PROPERTY_FIELD = "mysql.db.user";
    private static final String PASSWORD_PROPERTY_FIELD = "mysql.db.password";

    private DBConnection() {
    }

    public static Connection getInstance() throws DAOException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
        } catch (SQLException ex) {
            App.getLogger().error(ex.getMessage());
            throw new DAOException(
                    "Lo sentimos, no fue posible conectarse con la base de datos del sistema",
                    Status.FATAL);
        }
        return connection;
    }

    public static boolean close() throws DAOException {
        boolean isClosed = false;
        try {
            if (connection != null) {
                connection.close();
            }
            isClosed = true;
        } catch (SQLException ex) {
            App.getLogger().error(ex.getMessage());
            throw new DAOException(
                    "Lo sentimos, algo va mal con el sistema",
                    Status.FATAL);
        }
        return isClosed;
    }

    public static boolean rollback() throws DAOException {
        boolean isReversed = false;
        try {
            if (connection != null) {
                connection.rollback();
            }
            isReversed = true;
        } catch (SQLException ex) {
            App.getLogger().error(ex.getMessage());
            throw new DAOException(
                    "Lo sentimos, algo va mal con el sistema",
                    Status.FATAL);
        }
        return isReversed;
    }

    private static Connection getConnection() throws SQLException {
        Connection newConnection = null;
        Properties properties = new DBConnection().getPropertiesFile();
        if (properties != null) {
            newConnection = DriverManager.getConnection(
                    properties.getProperty(URL_PROPERTY_FIELD),
                    properties.getProperty(USER_PROPERTY_FIELD),
                    properties.getProperty(PASSWORD_PROPERTY_FIELD));

        } else {
            throw new SQLException("No fue posible encontrar las credenciales de la base de datos");
        }
        return newConnection;
    }

    private Properties getPropertiesFile() {
        Properties properties = null;
        try {
            InputStream file = new FileInputStream("src/main/resources/database.properties");
            if (file != null) {
                properties = new Properties();
                properties.load(file);
            }
            file.close();
        } catch (FileNotFoundException ex) {
            App.getLogger().error(ex.getMessage());
        } catch (IOException ex) {
            App.getLogger().error(ex.getMessage());
        }
        return properties;
    }

}
