package BattleShip;

import javafx.scene.Parent;

import java.util.ArrayList;

public class Ship extends Parent {
    public int type;
    public boolean vertical = true;
    public int ShipArray[] = {0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4};
    private int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = ShipArray[type];


    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}

