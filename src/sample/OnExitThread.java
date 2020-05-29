package sample;

import packet.CommandA;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Класс, реализующий поток отправки серверу информации о завершении работы клиентского приложения
 */
public class OnExitThread extends Thread{
    private String login;

    public void setLogin(String login) {
        this.login = login;
    }

    public void run(){
        try {
            InetAddress IPAddress = InetAddress.getByName(null);
            String key = "exit";
            CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
            sendCommand.setLogin(login);

            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sendCommand);
            oos.close();
            byte[] obj = baos.toByteArray();
            baos.close();

            DatagramPacket sendPacket = new DatagramPacket(obj, obj.length, IPAddress, 1229);
            Validator3000.clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
