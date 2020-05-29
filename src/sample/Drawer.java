package sample;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import packet.Person;

import java.util.HashMap;

public class Drawer {
    private final double canvasX = 0;
    private final double canvasY = 0;
    private final double canvasHeight = 700;
    private final double canvasWidth = 1000;

    private double headX;
    private double headY;
    private double width;
    private double height;
    private double eyeX1;
    private double eyeX2;
    private double eyeY;
    //ну правда костыль, зато прикольно
    private boolean kostyl;

    HashMap<String,Paint> map = new HashMap<>();;

    private Canvas canvas;

    public Drawer(Canvas canvas){
        this.canvas=canvas;
    }

    public void draw(ObservableList<Person> list){
        canvas.setHeight(700);
        canvas.setWidth(1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.SILVER);
        gc.fillRect(canvasX,canvasY, canvasWidth, canvasHeight);

        getColorMap(list);

        for (int i=0; i< list.size(); ++i){
            double animation = Math.sin(System.currentTimeMillis());
            headX = canvasX + list.get(i).getCoordinates().getX() + list.get(i).getLocation().getX();
            headY = canvasY + list.get(i).getCoordinates().getY() + list.get(i).getLocation().getY() + animation;

            headX = fixCoordinates(headX, canvasWidth);
            headY = fixCoordinates(headY, canvasHeight);

            width = list.get(i).getHeight()*0.33;
            height = list.get(i).getHeight();
            eyeX1 = headX + (width*0.33);
            eyeX2 = eyeX1 + (width*0.33);
            eyeY = headY + (width*0.33);


            //gc.clearRect(headX, headY,width,height);
            gc.setFill(Color.LIGHTYELLOW);
            gc.fillRect(headX, headY, width, width);

            gc.setFill(map.get(list.get(i).getCreator()));
            gc.fillRect(headX, headY+width, width, height*0.667);

            gc.setFill(getColor(list.get(i).getEyeColor()));
            gc.fillRect(eyeX1, eyeY, width*0.2, width*0.2);
            gc.fillRect(eyeX2, eyeY, width*0.2, width*0.2);

            gc.setFill(getColor(list.get(i).getHairColor()));
            gc.fillRect(headX, headY, width, width*0.2);

            if(kostyl){
                gc.setFill(Color.BLACK);
                gc.fillRect(headX, headY+width, width*0.25, height*0.2);
                gc.fillRect(headX+(width*0.75), headY+width, width*0.25, height*0.2);
            }
        }
    }

    private Paint getColor(packet.Color col){
        Paint res = Color.BLACK;
        switch (col){
            case BLACK: res = Color.BLACK; break;
            case BROWN: res = Color.BROWN; break;
            case GREEN: res = Color.GREEN; break;
            case WHITE: res = Color.WHITE; break;
            case ORANGE: res = Color.ORANGE; break;
        }
        return res;
    }

    private void getColorMap(ObservableList<Person> list){
        //HashMap<String,Paint> map = new HashMap<>();
        for (int i=0; i<list.size();i++){
            if(!(map.containsKey(list.get(i).getCreator()))){
                Integer r = (int) (Math.random()*255);
                Integer g = (int) (Math.random()*255);
                Integer b = (int) (Math.random()*255);
                map.put(list.get(i).getCreator(), Color.rgb(r,g,b));
            }
        }
    }

    private double fixCoordinates(double num, double max){
        kostyl = false;
        if(num >= max) {
            num = num % max;
            kostyl=true;
        }
        return num;
    }

}