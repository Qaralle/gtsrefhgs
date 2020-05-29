package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class TestTread2 extends Task<Void> {
    @Override
    protected Void call() throws Exception {
        showAlert();
        while (true){
            System.out.println("Test thread#2 is alive");
        }
    }
    public void showAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");
            alert.showAndWait();
        });
    }
}
