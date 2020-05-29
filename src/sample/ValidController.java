package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import packet.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ValidController extends AbstractWorker {
    private CommandA com;
    @FXML
    private TextField nameLable;

    @FXML
    private TextField hieghtLabel;

    @FXML
    private TextField yLabel;

    @FXML
    private ComboBox<Color> hairColorBox;

    @FXML
    private ComboBox<Color> eyrColorBox;

    @FXML
    private ComboBox<Country> nationalityBox;

    @FXML
    private TextField xLabel;

    @FXML
    private TextField locationLabel;

    @FXML
    private TextField x1Label;

    @FXML
    private TextField y1Label;

    @FXML
    private Button addButton;


    public void initialize() {
        hairColorBox.setItems( FXCollections.observableArrayList(Color.values()));
        eyrColorBox.setItems(FXCollections.observableArrayList(Color.values()));
        nationalityBox.setItems(FXCollections.observableArrayList(Country.values()));

        addButton.setOnAction(event -> {
            try {



                if (hairColorBox.getValue()==null){
                    throw new NullPointerException();
                } else  com.setHairColor(hairColorBox.getValue());

                if (eyrColorBox.getValue()==null){
                    throw new NullPointerException();
                } else  com.setEyeColor(eyrColorBox.getValue());

                if (nationalityBox.getValue()==null){

                    throw new NullPointerException();
                } else     com.setNationality(nationalityBox.getValue());

                com.setName(nameLable.getText());
                com.setHeight(Double.valueOf(hieghtLabel.getText()));
                com.setX(Float.valueOf(xLabel.getText()));
                com.setY(Double.valueOf(yLabel.getText()));
                com.setLocation(locationLabel.getText());
                com.setX1(Float.valueOf(x1Label.getText()));
                com.setY1(Double.valueOf(y1Label.getText()));
                com.setStringTOsend();
                System.out.println(com.getLogin());
                Thread ht = new Thread(()->{
                    try {
                        sendSmth(com);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                ht.start();
            }catch (NumberFormatException ex){
                showAlert("Ошибка ввода числа");
            } catch (NullPointerException ex){
                showAlert("Вы оставили выбираемый тип пустым");
            }
        });


    }

    public void intData(CommandA com_){
        com=com_;
    }


        private final javafx.event.EventHandler<WindowEvent> closeEventHandler = new javafx.event.EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
          //  System.exit(0);

        }
    };

    public javafx.event.EventHandler<WindowEvent> getCloseEventHandler(){
        return closeEventHandler;
    }
    public void showAlert(String str) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText(null);
            alert.setContentText(str);
            alert.showAndWait();
        });
    }


}
