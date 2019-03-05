package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrapOfModel {
    @XmlElement(name = "points1")
    private ArrayList<Point> firstLagrPoints;
    @XmlElement(name = "points2")
    private ArrayList<Point> secondLagrPoints;
    @XmlElement(name = "startInterval")
    private Double start;
    @XmlElement(name = "endInterval")
    private Double end;
    @XmlElement(name = "eps")
    private Double eps;

    public WrapOfModel(ArrayList<Point> firstLagrPoints, ArrayList<Point> secondLagrPoints, Double start, Double end, Double eps) {
        this.firstLagrPoints = firstLagrPoints;
        this.secondLagrPoints = secondLagrPoints;
        this.start = start;
        this.end = end;
        this.eps = eps;
    }

    public ArrayList<Point> getFirstLagrPoints() {
        return firstLagrPoints;
    }

    public void setFirstLagrPoints(ArrayList<Point> firstLagrPoints) {
        this.firstLagrPoints = firstLagrPoints;
    }

    public ArrayList<Point> getSecondLagrPoints() {
        return secondLagrPoints;
    }

    public void setSecondLagrPoints(ArrayList<Point> secondLagrPoints) {
        this.secondLagrPoints = secondLagrPoints;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    public WrapOfModel() {
    }
}
