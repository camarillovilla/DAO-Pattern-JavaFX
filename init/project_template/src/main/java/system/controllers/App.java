package system.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/system/views/primary"), 304, 409);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void changeView(String url, int width, int height) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        configureStage(currentStage, width, height);
        App.setRoot(url);
    }

    public static void changeView(FXMLLoader loader, int width, int height) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        scene.setRoot(loader.load());
        configureStage(currentStage, width, height);
    }

    private static void configureStage(Stage stage, int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }

}
