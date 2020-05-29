package sample;

import javafx.concurrent.Task;

public class TestThread extends Task<Void> {
    @Override
    protected Void call() throws Exception {

          System.out.println("Thread#1 is alive");
          TestTread2 th2 = new TestTread2();
          Thread task = new Thread(th2);
          task.start();
          task.join();

        return null;
    }

}
