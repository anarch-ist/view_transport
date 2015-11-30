package ru.logist.sbat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class App extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // create GUI
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        stage.setOnCloseRequest(event -> {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(root, 500, 300);
        stage.setTitle("Генератор данных");
        stage.setScene(scene);
        stage.show();
    }
}
