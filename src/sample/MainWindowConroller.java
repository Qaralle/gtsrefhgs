package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import packet.CommandA;
import packet.Country;
import packet.Person;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindowConroller extends AbstractWorker {

    Object obj = new Object();
    private ObservableList<Person> list;
    private Drawer drawer;
    private TimerTask task;
    private Timer timer = new Timer();
    private Timer animeTimer = new Timer();
    private TimerTask draw;
    private static boolean flag = true;

    @FXML
    private Button translate;
    @FXML
    private ComboBox<String> language;
    @FXML
    private TextField name;
    @FXML
    private TableColumn<Person, Long> colId;
    @FXML
    private TableColumn<Person, String > colName;
    @FXML
    private TableColumn<Person, Float> colX;
    @FXML
    private TableColumn<Person, Double> colY;
    @FXML
    private TableColumn<Person, Double> colHeight;
    @FXML
    private TableColumn<Person, String> colEyeColor;
    @FXML
    private TableColumn<Person, String> colHairColor;
    @FXML
    private TableColumn<Person, String> colLocName;
    @FXML
    private TableColumn<Person, Float> colLocX;
    @FXML
    private TableColumn<Person, Double> colLocY;
    @FXML
    private TableColumn<Person, String> colNationality;
    @FXML
    private TableColumn<Person, String> colCreator;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private Canvas canvas;


    @FXML
    private Text loginLabel;

    @FXML
    private Button infoButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button addIfMinButton;

    @FXML
    private Button removeHeadButton;

    @FXML
    private TextField idLabel;

    @FXML
    private Button removeByIdButton;

    @FXML
    private Button countlessthenButton;

    @FXML
    private TextField loclabel;

    @FXML
    private Button removeByNatButton;

    @FXML
    private Button filterstartsButton;

    @FXML
    private TextField namelable;

    @FXML
    private Button executeScriptButton;

    @FXML
    private TextField fileNameloc;

    @FXML
    private ComboBox<Country> natBox;


    @FXML
    private void filter(ActionEvent event){
        ObservableList<Person> filteredList = Main.manager.getList(Main.bc);
        if(name.getText().length()>0){
            filteredList.clear();
            list.stream().filter(p -> p.getName().equals(name.getText())).forEach(filteredList::add);
            task.cancel();
        }else startUpdating();
        personTable.setItems(filteredList);
    }

    private void update(){
        list = Main.manager.getList(Main.bc);
        personTable.setItems(list);
        //drawer.draw(list);
    }

    private void translate(String lang){
        try {

            flag = false;
            switch (lang) {
                case "Русский":
                    Main.resources = ResourceBundle.getBundle("sample/resources/locale_ru");
                    break;
                case "Nederlands":
                    Main.resources = ResourceBundle.getBundle("sample/resources/locale_nd");
                    break;
                case "Svensk":
                    Main.resources = ResourceBundle.getBundle("sample/resources/locale_sw");
                    break;
                case "Español":
                    Main.resources = ResourceBundle.getBundle("sample/resources/locale_sp");
                    break;
            }
            Scene scene = translate.getScene();
            try {
                scene.setRoot(FXMLLoader.load(getClass().getResource("main.fxml"), Main.resources));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }catch (NullPointerException ignored){}
    }
    private void startUpdating() {
        task = new TimerTask() {
            @Override
            public void run() {
                update();
                System.out.println("hui");
            }
        };
        timer.schedule(task, 0L, 1000L);
    }

    public void initialize() {

        colId.setCellValueFactory(cellData -> cellData.getValue().getPropID().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().getPropName());
        colX.setCellValueFactory(cellData -> cellData.getValue().getCoordinates().getPropX().asObject());
        colY.setCellValueFactory(cellData -> cellData.getValue().getCoordinates().getPropY().asObject());
        colHeight.setCellValueFactory(cellData -> cellData.getValue().getPropHeight().asObject());
        colEyeColor.setCellValueFactory(cellData -> cellData.getValue().getPropEyeColor());
        colHairColor.setCellValueFactory(cellData -> cellData.getValue().getPropHairColor());
        colLocName.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropName());
        colLocX.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropX().asObject());
        colLocY.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropY().asObject());
        colNationality.setCellValueFactory(cellData -> cellData.getValue().getPropNationality());
        colCreator.setCellValueFactory(cellData -> cellData.getValue().getPropCreator());

        try {
            language.getItems().addAll("Русский", "Nederlands", "Svensk", "Español");
            translate.setOnAction(event -> translate(language.getValue()));
        }catch (NullPointerException ignored){}

        list = Main.manager.getList(Main.bc);
        drawer = new Drawer(canvas);
        personTable.setOnSort(event -> task.cancel());

        startUpdating();

        draw = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(list);
            }
        };
        animeTimer.schedule(draw, 0L, 100L);


        natBox.setItems(FXCollections.observableArrayList(Country.values()));
        infoButton.setOnAction(event -> {
            Thread task = new Thread(() -> {
                key = "info";
                CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });
        helpButton.setOnAction(event -> {
            Thread task = new Thread(() -> {
                key = "help";
                CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });

        clearButton.setOnAction(event -> {
            Thread task = new Thread(() -> {
                key = "clear";
                CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
                sendCommand.setLogin(login);
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });
        historyButton.setOnAction(event -> {
            Thread task = new Thread(() -> {
                key = "history";
                CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
                try {
                    sendSmth(sendCommand);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });

        removeHeadButton.setOnAction(event -> {
            Thread task = new Thread(() -> {
                key = "remove_head";
                CommandA sendCommand = new CommandA(key, Validator3000.ACCESS);
                sendCommand.setLogin(login);
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });

        addButton.setOnAction(event -> {
                key = "add";
                CommandA com = new CommandA(key, Validator3000.ACCESS);
                com.setLogin(login);
                openValid(com,key,"1");
        });
        addIfMinButton.setOnAction(event -> {
            key = "add_if_min";
            CommandA com = new CommandA(key, Validator3000.ACCESS);
            com.setLogin(login);
            openValid(com,key,"1");
        });
        updateButton.setOnAction(event ->{
           long checkType1 = Long.parseLong(idLabel.getText());
            key = "update";
            CommandA sendCommand = new CommandA(key, checkType1, Validator3000.ACCESS);
            Thread task = new Thread(()->{
                try {
                    synchronized (obj) {
                        sendSmth(sendCommand);
                        obj.wait(2000);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            task.start();
            try {
                task.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String s = new String(Validator3000.finalReceiveData);
            System.out.println(s);
            if (s.contains("Объект с таким id найден")) {
                sendCommand.setLogin(login);
                openValid(sendCommand,key,"1");
            }

        });
        removeByIdButton.setOnAction(event -> {
            long checkType = Long.parseLong(idLabel.getText());

            key = "remove_by_id";
            CommandA sendCommand = new CommandA(key, checkType, Validator3000.ACCESS);
            sendCommand.setLogin(login);
            Thread task = new Thread(()->{
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });
        countlessthenButton.setOnAction(event -> {
            key = "count_less_than_location";
            CommandA sendCommand = new CommandA(key, loclabel.getText(), 1, Validator3000.ACCESS);
            Thread task = new Thread(()-> {
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();

        });
        removeByNatButton.setOnAction(event -> {
            try {
                if (natBox.getValue() == null) {
                    throw new NullPointerException();
                }else {
                    Country checkType2 = natBox.getValue();
                    key = "remove_any_by_nationality";
                    CommandA sendCommand = new CommandA(key, checkType2, Validator3000.ACCESS);
                    sendCommand.setLogin(login);
                    Thread task = new Thread(()->{
                        try {
                            sendSmth(sendCommand);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    task.start();
                }
            }catch (NullPointerException  ex){
                showAlert("Вы не ввели национальность");
            }

        });
        executeScriptButton.setOnAction(event -> {
            File file = new File(fileNameloc.getText());
            if (!file.exists()) {
                try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    showAlert("Файл отсуствует");
                }
            }
            key = "execute_script";
            CommandA sendCommand = new CommandA(key, fileNameloc.getText(), Validator3000.ACCESS);
            sendCommand.setLogin(login);
            Thread task = new Thread(()-> {
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });

        filterstartsButton.setOnAction(event->{
            key = "filter_starts_with_name";
            CommandA sendCommand = new CommandA(key, namelable.getText(), Validator3000.ACCESS);
            Thread task = new Thread(()-> {
                try {
                    sendSmth(sendCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            task.start();
        });
    }

    public void initData(Customer customer, Object obj_){
        this.obj=obj_;
        this.login=customer.getLogin();
        loginLabel.setText(customer.getLogin());
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


    private final javafx.event.EventHandler<WindowEvent> closeEventHandler = new javafx.event.EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            System.exit(0);

        }
    };

    public javafx.event.EventHandler<WindowEvent> getCloseEventHandler(){
        return closeEventHandler;
    }


    private void openValid(CommandA com_,String title_, String key_){
        FXMLLoader loader = new FXMLLoader();
        String loc = "/sample/valid"+key_+".fxml";

        loader.setLocation(getClass().getResource(loc));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title_);
        ValidController controller = loader.getController();
        controller.intData(com_);
        stage.setOnCloseRequest(controller.getCloseEventHandler());
        stage.showAndWait();

    }
}
