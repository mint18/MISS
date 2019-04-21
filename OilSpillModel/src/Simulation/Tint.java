package Simulation;

import Simulation.Type;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tint extends StackPane {

    private static final int AREA_SIZE = 300;
    private static final int TILE_SIZE = 2;
    public int x;
    public int y;
    public Rectangle square;

    private Color colour1 = Color.web("000000"); //170b2a
    private Color colour2 = Color.web("170b2a"); // 241178
    private Color colour3 = Color.web("1f104f"); //25103a
    private Color colour4 = Color.web("241178");
    private Color colour5 = Color.web("25103a");
    private Color source = Color.web("000000");
    private Color water = Color.web("87cefa");


    public Tint(double oilLevel, Type type, int x, int y) {
        this.x = x;
        this.y = y;
        this.square = new Rectangle(TILE_SIZE, TILE_SIZE);
        this.setFill(oilLevel, type);
        getChildren().add(square);
    }

    public void setFill(double oilLevel, Type type) {
        if (x == 0 || y == 0 || y == AREA_SIZE - 1 || x == AREA_SIZE - 1) {
            square.setFill(Color.BLACK);
        } else {

            switch (type) {
                case OIL:
                    square.setFill(this.colour1);
                    if (oilLevel < 1.0E-14) {
                        square.setFill(this.water);
                    } else if (oilLevel < 1.0E-11) {
                        square.setFill(this.colour5);
                    } else if (oilLevel < 1.0E-8) {
                        square.setFill(this.colour4);
                    } else if (oilLevel < 1.0E-5) {
                        square.setFill(this.colour3);
                    } else if (oilLevel < 1.0E-2) {
                        square.setFill(this.colour2);
                    }
                    break;
                case SOURCE:
                    square.setFill(source);
                    break;
                case WATER:
                    square.setFill(this.water);
                    break;
            }
        }
    }
}
