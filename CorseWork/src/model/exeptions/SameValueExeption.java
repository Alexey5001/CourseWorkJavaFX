package model.exeptions;
import model.Point;

public class SameValueExeption extends Exception {
    public SameValueExeption(String exline) {
        super(exline);
    }
    public SameValueExeption(String exline, Point x1, Point x2){
        super(exline + " (" + x1.getX() + ";" + x1.getY() + ") (" + x2.getX() + ";" + x2.getY() + ")");
    }
}
