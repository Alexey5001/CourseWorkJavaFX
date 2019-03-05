package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.xml.bind.annotation.XmlAttribute;

public class Point {
    private DoubleProperty x, y;

    @XmlAttribute(name = "x")
    public double getX() {
        return x.getValue();
    }

    @XmlAttribute(name = "y")
    public double getY() {
        return y.getValue();
    }

    public Point(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }

    public void setX(double x) {
        this.x.setValue(x);
    }

    public void setY(double y) {
        this.y.setValue(y);
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x.getValue() +
                ", y=" + y.getValue() +
                '}';
    }

    public Point() {
        this(0, 0);
    }
}