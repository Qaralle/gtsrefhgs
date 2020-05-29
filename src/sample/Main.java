package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import packet.BDconnector;

import java.util.ResourceBundle;

public class Main extends Application {
    public static ResourceBundle resources;
    public static Manager manager;
    public static BDconnector bc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        manager = new Manager();
        bc = new BDconnector(3758);
        resources = ResourceBundle.getBundle("sample/resources/locale_ru");
        loader.setLocation(getClass().getResource("sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("trash");
        primaryStage.setScene(new Scene(root, 540, 400));
        primaryStage.show();
        Controller controller = loader.getController();

        primaryStage.setOnCloseRequest(controller.getCloseEventHandler());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
