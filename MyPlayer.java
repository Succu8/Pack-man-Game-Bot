package Packman_Game_Bot;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.util.*;

public class MyPlayer implements Player{
    private BallPane ball;
    private Map map;
    private int x;
    private int y;

    public MyPlayer(Map map){
        this.map = map;
        x = map.getStartPosition().getX() * map.getUnit() + map.getUnit()/2;
        y = map.getStartPosition().getY() * map.getUnit() + map.getUnit()/2;
        ball = new BallPane();
        ball.radius((double)map.getUnit()/2);
        ball.centerX(x);
        ball.centerY(y);
        map.getChildren().add(ball);

    }
    @Override
    public void moveRight() {
        x = position(ball.getX());
        y = position(ball.getY());
        if(x < map.getSize()-1) {
            if(x + 1 < map.getSize() && map.getValue(y, x+1) != 1) {
                ball.centerX(ball.getX() + map.getUnit());
                ball.rotate(0);
            }
            if( map.getValue(y, x+1) == 1 ) {
                System.out.println("Invalid position!");
            }
        }
    }
    @Override
    public void moveLeft() {
        x = position(ball.getX());
        y = position(ball.getY());
        if(x > 0) {
            if( map.getValue(y, x-1) != 1) {
                ball.centerX(ball.getX() - map.getUnit());
                ball.rotate(180);
            }
            if((map.getValue(y, x-1) == 1)) {
                System.out.println("Invalid position!");
            }
        }
    }
    @Override
    public void moveUp() {
        x = position(ball.getX());
        y = position(ball.getY());
        if(y > 0) {
            if(map.getValue(y-1, x) != 1) {
                ball.centerY(ball.getY() - map.getUnit());
                ball.rotate(270);
            }
            if((map.getValue(y-1, x) == 1)) {
                System.out.println("Invalid position!");
            }
        }
    }
    @Override
    public void moveDown() {
        x = position(ball.getX());
        y = position(ball.getY());
        if(y < map.getSize()-1) {
            if(map.getValue(y+1, x) != 1) {
                ball.centerY(ball.getY() + map.getUnit());
                ball.rotate(90);
            }
            if((map.getValue(y+1, x) == 1)) {
                System.out.println("Invalid position!");
            }
        }
    }
    @Override
    public Position getPosition() {
        x = position(ball.getX());
        y = position(ball.getY());
        return new Position(x , y);
    }
    public int position(double n){
        return (int)((n-map.getUnit()/2)/map.getUnit());
    }
}

class BallPane extends Pane{
    private Arc arc;

    BallPane(){
        arc = new Arc(300, 250, 64,48,45,270);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        getChildren().add(arc);

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(500), startAngle(), lengthAngle()));
        animation.setAutoReverse(true);
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    public KeyValue startAngle(){ // Responsible for the start angle of the mouth of the player
        return new KeyValue(arc.startAngleProperty(), 0);
    }
    public KeyValue lengthAngle(){      //Responsible for the length of the player
        return new KeyValue(arc.lengthProperty(), 360);
    }
    public void centerX(double x){
        arc.setCenterX(x);
    }
    public void centerY(double y){
        arc.setCenterY(y);
    }
    public double getX(){
        return arc.getCenterX();
    }
    public double getY(){
        return arc.getCenterY();
    }
    public void radius(double r){
        arc.setRadiusX(r);
        arc.setRadiusY(r);
    }
    public void rotate(double a){
        arc.setRotate(a);
    }
    public void setArcColor(){
    }
}
