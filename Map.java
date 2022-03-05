package Packman_Game_Bot;

import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import java.io.*;
import java.util.*;
import javafx.scene.paint.*;

public class Map extends Pane {
    private int unit = 50;
    private int size;
    private int[][] map;
    private Position start;
    public Map(File file){
        try {
            try (Scanner input = new Scanner(file)) {
                while (input.hasNextInt()) {
                    size = input.nextInt();
                    map = new int[size][size];
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            map[i][j] = input.nextInt();
                        }
                    }
                }
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        Glow glow = new Glow();
                        glow.setLevel(0.8);
                        Rectangle rectangle = new Rectangle(unit,unit);
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setX(j * unit);
                        rectangle.setY(i * unit);
                        rectangle.setEffect(glow);
                        if (map[i][j] == 0 || map[i][j] == 2) {
                            rectangle.setStroke(Color.DARKBLUE);
                            rectangle.setFill(Color.BLACK);
                        }
                        else if(map[i][j] == 1){
                            rectangle.setStroke(Color.DARKBLUE);
                            rectangle.setFill(Color.DARKRED);
                        }
                        if(map[i][j] == 2){
                            start = new Position(i, j);
                        }
                        getChildren().add(rectangle);
                        getChildren().add(new Circle());
                    }
                }
            }
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }
    public int getUnit(){
        return unit;
    }
    public int getSize(){
        return size;
    }
    public Position getStartPosition(){
        return start;
    }
    public int getValue(int row, int column ) {
        return map[row][column];
    }

    public int[][] getMap(){
        return map;
    }
}
