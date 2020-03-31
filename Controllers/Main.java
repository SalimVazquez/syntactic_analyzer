package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/sample.fxml"));
        primaryStage.getIcons().add(new Image("/assets/logoUP.png"));
        primaryStage.setTitle("Analizador sintatico");
        primaryStage.setScene(new Scene(root, 750, 565));
        primaryStage.show();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Informacion de ejecucion");
        alert.setResizable(true);
        alert.setContentText("Ejemplo : if a<b and true: print("+"a"+"); else: print("+"b"+")");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}