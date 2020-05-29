package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import packet.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Manager {
    private Person p;
    public ObservableList<Person> getList(BDconnector bc){
        Connection connection = bc.getCon();
        ObservableList<Person> list = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from collection");
            while(resultSet.next()){
                p = new Person();
                p.setID(resultSet.getInt("id"));
                p.setCreator(resultSet.getString("creator"));
                String name = resultSet.getString("name");
                Coordinates coo = new Coordinates(resultSet.getFloat("x"), resultSet.getDouble("y"));
                double height = resultSet.getDouble("height");
                Location loc = new Location(resultSet.getFloat("locationx"), resultSet.getDouble("locationy"), resultSet.getString("locationname"));
                Country nationality = Country.valueOf(resultSet.getString("country").toUpperCase());
                Color eyeColor = Color.valueOf(resultSet.getString("eyecolor").toUpperCase());
                Color hairColor = Color.valueOf(resultSet.getString("haircolor").toUpperCase());
                p.setEverything(name, coo, height, eyeColor, hairColor, nationality, loc);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            System.out.println("fff");
        }
        return list;
    }
}