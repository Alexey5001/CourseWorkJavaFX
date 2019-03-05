package model;

import model.exeptions.OnePointException;
import model.exeptions.SameValueExeption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

public class Lagr implements Function<Double, Double> {

    ArrayList<Point> list = new ArrayList<Point>();

    public Lagr(ArrayList list) throws SameValueExeption, OnePointException {
        this.list = list;
        exSameX();
        onePointExc();
        this.list.sort(Comparator.comparingDouble(o -> o.getX()));
    }

    private void exSameX() throws SameValueExeption {
        for (int i = 0; i < list.size(); i++) {
            Point x = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                if (i != j)
                    if (x.getX() == list.get(j).getX()) throw new SameValueExeption("Same value of x", x, list.get(j));
            }
        }
    }

    private void onePointExc() throws OnePointException {
        if (list.size() == 1) throw new OnePointException("cant build graf with one point");

    }

    @Override
    public Double apply(Double x) {                   // интерполяция
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            double mult = 1;
            for (int j = 0; j < list.size(); j++) {
                if (j != i) {
                    mult *= (x - list.get(j).getX()) / (list.get(i).getX() - list.get(j).getX());
                }
            }
            sum += mult * list.get(i).getY();
        }

        return sum;
    }
}
