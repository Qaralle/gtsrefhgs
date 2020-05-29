package sample;

import javafx.concurrent.Task;

import java.net.*;
import java.util.Map;


/**
 * Класс, выполняющий функци инвокера
 * @author Maxim Antonov and Andrey Lyubkin
 */
public class Validator3000 extends Task<Void> {

    private final Controller cntrl;
    private final Object obj;
    private final Object obj2;

    public Validator3000(OnExitThread thread, Controller controller, Object obj_, Object obj2_){
        this.thread=thread;
        this.port=12345;
        this.cntrl=controller;
        this.obj=obj_;
        this.obj2=obj2_;
    }
    //private boolean WORK=true;
    static String ACCESS;




    private Integer port;
    static byte[] finalReceiveData;
    static DatagramSocket clientSocket;
    private OnExitThread thread;
    protected Map<String, String> bufferMap;
    protected String[] bufferStringForArgs;

    {
        ACCESS="DEFAULT";
    }

    /**
     * Старт работы
     */

    @Override
    protected Void call() throws Exception {
        clientSocket = new DatagramSocket(port);
        Thread receiver = new Thread(new Receiver(cntrl,obj,obj2));
        //Thread Sanders = new Thread(new Sender(thread));
        synchronized (ACCESS) {
            receiver.start();
            receiver.join();
            //Sanders.start();
            //Sanders.join();
        }
        return null;
    }

    public void setPort(Integer port_){
        this.port=port_;
    }
}
