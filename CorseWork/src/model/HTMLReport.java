package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.exeptions.XMLWorkException;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;


public class HTMLReport {
    public HTMLReport(double start, double end, double eps, ArrayList<Point> points1, ArrayList<Point> points2, Stage stage,
                      LineChart chart, Double min) throws XMLWorkException, IOException {
        FileChooser fchooser = new FileChooser();
        fchooser.setTitle("Сохранить отчет");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".html файл",
                "*.html");
        fchooser.getExtensionFilters().add(extFilter);
        File file = fchooser.showSaveDialog(stage);
        if (file == null) throw new XMLWorkException("Вы не указали путь к файлу", "нет имени файла");

        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            out.printf("<html>%n");

            out.printf("<head>%n");
            out.printf("<meta http-equiv='Content-Type' content='text/html; " +
                    "charset=UTF-8'>%n");
            out.printf("</head>%n");
            out.printf("<body>%n");
            out.printf("<h2>Отчет</h2>%n");
            out.printf("<p>В результате решения функции " +
                    "с такими выводными данными</p>%n");
            out.printf("<h4>Данные для первого интерполяционного уравнения Лагранжа: <span style='font-family:Times, Serif;'>" +
                    "<em>g(x)</em></span></h4>%n");
            out.printf("<table border = '1' cellpadding=4 cellspacing=0>%n");
            out.printf("<tr>%n");
            out.printf("<th>Индекс</th>%n");
            out.printf("<th>x</th>%n");
            out.printf("<th>y</th>%n");
            out.printf("</tr>%n");
            out.printf("<td>%n");
            for (int i = 0; i < points1.size(); i++) {
                out.printf("<tr>%n");
                out.printf("<td>%d</td>", i);
                out.printf("<td>%8.3f</td>%n", points1.get(i).getX());
                out.printf("<td>%8.3f</td>%n", points1.get(i).getY());
                out.printf("</tr>%n");
            }
            out.printf("</table>%n");
            out.printf("<h4>Данные для второго интерполяционного уравнения Лагранжа:<span style='font-family:Times, Serif;'>" +
                    "<em>g(t)</em></span></h4>%n");
            out.printf("<table border = '1' cellpadding=4 cellspacing=0>%n");
            out.printf("<tr>%n");
            out.printf("<th>Индекс</th>%n");
            out.printf("<th>x</th>%n");
            out.printf("<th>y</th>%n");
            out.printf("</tr>%n");
            out.printf("<td>%n");
            for (int i = 0; i < points2.size(); i++) {
                out.printf("<tr>%n");
                out.printf("<td>%d</td>", i);
                out.printf("<td>%8.3f</td>%n", points2.get(i).getX());
                out.printf("<td>%8.3f</td>%n", points2.get(i).getY());
                out.printf("</tr>%n");
            }
            out.printf("</table>%n");

            if (min != null) {
                out.printf("<p>Минимум: " + min + "</p>");
            } else {
                out.printf("<p>Mинимум не найден. </p>");
            }

            out.printf("<p>График функции:</p>");
            String imgpath = file.getPath();
            imgpath = imgpath.replaceAll("html", "png");
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(imgpath));
            } catch (IOException io) {
                System.out.println("Не удалось сохранить изображение");
            }
            if (imgpath != null) {

                File img = new File(imgpath);
                out.printf("<img src = \"" + img.getName() + "\"/>");
            }
            out.printf("</body>%n");
            out.printf("</html>%n");


            WebView webview = new WebView();
            webview.getEngine().load(file.toURI().toString());
            Stage newstage = new Stage();
            Scene scene = new Scene(webview, 500, 500, Color.web("#666970"));
            newstage.setScene(scene);
            newstage.show();


        } catch (IOException e) {
            throw e;
        }

    }
}