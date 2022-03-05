
package Packman_Game_Bot;

public interface Player {
    void moveRight();
    void moveLeft();
    void moveUp();
    void moveDown();
    Position getPosition();
}
