package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Optional;

/**
 * Класс, реализующий поток получения ответов от сервера
 */
public class Receiver extends Task<Boolean> {
    private final Controller controller;
    private final Object obj;
    private final Object obj2;

    private String string_to_print;
    byte[] receiveData = new byte[4*1024];

    public Receiver(Controller cntrl,Object obj_,Object obj2_) {
        this.controller=cntrl;
        this.obj=obj_;
        this.obj2=obj2_;
    }

    @Override
    protected Boolean call() {
        while (true) {
            try {

                getSmth();
                showAlert(string_to_print);
            } catch (IOException sex) {

            }
        }
    }

    /**
     * Получить что-либо от сервера
     */
    private void getSmth() throws IOException {
        string_to_print = "stroka kotoryy ti nikogda ne bydesh ispolzovat";
        Validator3000.clientSocket.setSoTimeout(5000);
        DatagramPacket pack = new DatagramPacket(receiveData, receiveData.length);
        Validator3000.clientSocket.receive(pack);
        int size = pack.getLength();
        Validator3000.finalReceiveData = new byte[size];
        synchronized (obj2) {
            for (int i = 0; i < size; ++i) {
                Validator3000.finalReceiveData[i] = receiveData[i];
            }
            obj2.notify();
            string_to_print = new String(Validator3000.finalReceiveData);
        }
        if (string_to_print.contains("Неверный")){
            synchronized (obj) {
                controller.setFlag("ERROR");
                obj.notify();
            }
        }

        if (string_to_print.contains("&")){
            synchronized (obj) {
            controller.setFlag("LOGIN");
            Validator3000.ACCESS= Optional.ofNullable(string_to_print.split("&",2)[1])
                    .filter(str -> str.length() != 0)
                    .map(str -> str.substring(0, str.length() - 1))
                    .orElse(string_to_print.split("&",2)[1]);
                obj.notify();
            }
        }

        //return new String(finalReceiveData);

    }
    public void showAlert(String str) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText(str);
            alert.showAndWait();
        });
    }


}
