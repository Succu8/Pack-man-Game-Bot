package Packman_Game_Bot;
import javafx.scene.*;
import javafx.application.*;
import javafx.scene.input.KeyCode;
import javafx.stage.*;
import javafx.scene.layout.*;
import java.io.*;
import java.util.*;

public class MyBotPLayer extends Application implements BotPlayer {
    private Map map;
    private BallPane ballPane = new BallPane();
    private Player player;
    private Food food;
    private BorderPane pane = new BorderPane();
    private int[][] path;

    public void start(Stage stage) {
        Scanner input = new Scanner(System.in);
        String s = input.next();
        File file = new File(s);
        if (!file.exists()) {
            System.out.println("File " + s + " does not exist");
            System.exit(1);
        } else {
            System.out.println("Map size: 8");
            map = new Map(file);
            pane.getChildren().add(map);
            player = new MyPlayer(map);
            food = new Food(map, player);
            feed(food);
            pane.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.E) {
                    Thread thread = new Thread(() -> eat());
                    thread.start();
                } else if (e.getCode() == KeyCode.F) {
                    Thread thread = new Thread(() -> find());
                    thread.start();
                }
            });

            Scene scene = new Scene(pane, 400, 400);
            stage.setScene(scene);
            stage.setTitle("EATER");
            stage.show();
            pane.requestFocus();
        }
    }

    @Override
    public void feed(Food f) {
        this.food = f;
    }

    @Override
    public void eat() {
        boolean continuePlay = true;
        int points = 0;
        while (continuePlay) {
            if(food.getPoints() > points){
                ballPane.setArcColor();
                points = food.getPoints();
            }

            int foodX = food.getPosition().getX();
            int foodY = food.getPosition().getY();
            int botX = player.getPosition().getX();
            int botY = player.getPosition().getY();
            int hor = Math.abs(botX - foodX);
            int ver = Math.abs(botY - foodY);
            try {
                for (int i = 0; i < hor; i++) {
                    if (botX > foodX) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                player.moveLeft();
                            }
                        });
                        Thread.sleep(250L);
                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                player.moveRight();
                            }
                        });
                        Thread.sleep(250L);
                    }
                }
            } catch (InterruptedException exc) {
            }
            try {
                for (int i = 0; i < ver; i++) {
                    if (botY > foodY) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                player.moveUp();
                            }
                        });
                        Thread.sleep(250L);
                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                player.moveDown();
                            }
                        });
                        Thread.sleep(250L);
                    }
                }
            } catch (InterruptedException exc) {
            }
            if (food.getPosition() == null) {
                continuePlay = false;
            }
        }
    }

    @Override
    public void find() {
        while (true) {
            ArrayList<String> positions = shortestPath();
            for(String p : positions) {
                if(p.equals("right")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            moveRight();;
                        }
                    });
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e) {
                    }
                }
                if(p.equals("left")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            moveLeft();
                        }
                    });
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e) {
                    }
                }
                if(p.equals("up")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            moveUp();
                        }
                    });
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e) {
                    }
                }
                if(p.equals("down")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            moveDown();
                        }
                    });
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    @Override
    public void moveRight() {
        player.moveRight();
    }

    @Override
    public void moveLeft() {
        player.moveLeft();
    }

    @Override
    public void moveUp() {
        player.moveUp();
    }

    @Override
    public void moveDown() {
        player.moveDown();
    }

    @Override
    public Position getPosition() {
        return player.getPosition();
    }


    public ArrayList<String> shortestPath() {
        path = new int[map.getSize()][map.getSize()];
        int botX = player.getPosition().getX();
        int botY = player.getPosition().getY();
        int foodX = food.getPosition().getX();
        int foodY = food.getPosition().getY();
        path[foodX][foodY] = 1;
        int sourcePos = 0;
        boolean positionCaught = false;
        while(path[botX][botY] != 1){
            for (int i = 0; i < path.length; i++) {
                for (int j = 0; j < path.length; j++) {
                    if (path[i][j] != 0 || map.getValue(j, i) == 1) {
                        continue;
                    }
                    markCell(i, j);
                    if (i == botX && j == botY && path[i][j] != 0) {
                        sourcePos = path[i][j];
                        positionCaught = true;
                        break;
                    }
                }
                if(positionCaught)
                    break;
            }
            if(positionCaught)
                break;
        }
        return getPath(sourcePos);
    }
    public void markCell(int x, int y) {
        int n = 1000;
        if(x>0 && map.getValue(y, x-1) != 1 && path[x-1][y]!=0)
            n = Math.min(n, path[x-1][y]);//check left
        if(x<map.getSize()-1 && map.getValue(y, x+1) != 1 && path[x+1][y]!=0)
            n = Math.min(n, path[x+1][y]);//check right
        if(y>0 && map.getValue(y-1, x) != 1 && path[x][y-1]!=0)
            n = Math.min(n, path[x][y-1]);//check above
        if(y<map.getSize()-1 && map.getValue(y+1, x) != 1 && path[x][y+1]!=0)
            n = Math.min(n, path[x][y+1]);//check below

        if(n != 1000)
            path[x][y] = n+1;

    }
    public ArrayList<String> getPath(int dest){
        int botX = player.getPosition().getX();
        int botY = player.getPosition().getY();
        ArrayList<String> pos = new ArrayList<>();
        String s = "";
        for (int k = dest; k>0; k--) {
            if(botX>0 && path[botX-1][botY] == k){
                botX--;
                s = "left";
            }
            else if(botX<map.getSize()-1 && path[botX+1][botY] == k){
                botX++;
                s = "right";
            }
            else if(botY>0 && path[botX][botY-1] == k){
                botY--;
                s = "up";
            }
            else if(botY<map.getSize()-1 && path[botX][botY+1] == k) {
                botY++;
                s = "down";
            }
            pos.add(s);
        }
        return pos;
    }
}

