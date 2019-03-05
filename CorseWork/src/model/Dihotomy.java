package model;

import model.exeptions.BoundException;
import model.exeptions.EpsException;

import java.util.LinkedList;
import java.util.function.Function;

public class Dihotomy {
    public static Double calculate(double start, double end, double eps, Function<Double, Double> f1, Function<Double, Double> f2) throws BoundException, EpsException {

        if (start >= end) throw new BoundException("Начало интервала больше или равно концу интервала.");
        if (eps >= 1 || eps <= 0) throw new EpsException("Точность введена неверно.");
        Double result = null;
        double a, b, x1, x2;
        a = start;
        b = end;
        int n = 0;
        double f11, f22 = 0;
        // f1 = (Lagr) f1;
        //f2 = (Lagr) f2;

        while (true) {
            n++;
            double x = (a + b) / 2;
            x1 = x - eps / 3;
            x2 = x + eps / 3;
            double y = f1.apply(x);
            f11 = f1.apply(x1) - f2.apply(x1);
            f22 = f1.apply(x2) - f2.apply(x2);
            if (f11 >= f22)
                a = x1;
            else
                b = x2;
            System.out.println("Итерация - " + n + ": " + "x = " + x + " left = " + a + " right = " + b + " F1(x1)= " + f11 + " F2(x2) = " + f22);
            if (Math.abs(b - a) < eps)
                break;
        }
        result = ((a + b) / 2);
        return result;
    }
}
