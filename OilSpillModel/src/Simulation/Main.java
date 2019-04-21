package Simulation;
import Simulation.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appearance.fxml"));
        Parent root = (Parent) loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Oil Spill Model");
        primaryStage.setScene(new Scene(root, 940, 660));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
