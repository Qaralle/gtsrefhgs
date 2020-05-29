package sample;

import java.util.function.Consumer;

public class Customer implements Consumer<String> {
    private String login;
    @Override
    public void accept(String s) {
        login=s;
    }


    public String getLogin(){
        return login;
    }

}
