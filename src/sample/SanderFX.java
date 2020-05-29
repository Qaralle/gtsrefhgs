package sample;

import packet.CommandA;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class SanderFX implements Runnable {
    protected String userCommand;
    protected String[] userCommand_;
    protected Scanner scan;

    public String login;
    byte[] sendData;
    private OnExitThread thread;
    //protected receiver res;
    {

        userCommand = "";
        scan=new Scanner(System.in);
    }

    CommandA sentence;
    String key = new String();


    public SanderFX(OnExitThread thread){
        this.thread=thread;
    }
    @Override
    public void run() {

    }

    private byte[] serialaze(CommandA ob){

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ob);
            oos.close();
            byte[] obj = baos.toByteArray();
            baos.close();
            return obj;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    private void sendSmth(CommandA data) throws IOException {
        InetAddress IPAddress = InetAddress.getByName(null);
        sentence = data;
        sendData = serialaze(data);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1229);
        Validator3000.clientSocket.send(sendPacket);

    }
}
