import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.*;
import model.exeptions.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class Controller {
    @FXML
    private TextField p1x;
    @FXML
    private TextField p1y;
    @FXML
    private TextField p2x;
    @FXML
    private TextField p2y;
    @FXML
    private TextField st;
    @FXML
    private TextField end;
    @FXML
    private TextField eps;
    @FXML
    private TextField min;
    @FXML
    private AnchorPane parentchart;
    @FXML
    private LineChart chart;
    @FXML
    private TableView<Point> tblview;
    @FXML
    private TableView<Point> tblview2;
    @FXML
    private TableColumn<Point, Double> x1;
    @FXML
    private TableColumn<Point, Double> y1;
    @FXML
    private TableColumn<Point, Double> x2;
    @FXML
    private TableColumn<Point, Double> y2;
    @FXML
    private Button close;
    @FXML
    private MenuItem saveReport;

    private Double minimum;
    private Main main;
    private ArrayList<Point> p1list = new ArrayList();
    private ArrayList<Point> p2list = new ArrayList();

    public void setMain(Main main) {
        this.main = main;
    }

    public void initialize() {
        x1.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        x1.setCellValueFactory(cellBack -> cellBack.getValue().xProperty().asObject());

        y1.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        y1.setCellValueFactory(cellBack -> cellBack.getValue().yProperty().asObject());

        x2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        x2.setCellValueFactory(cellBack -> cellBack.getValue().xProperty().asObject());

        y2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        y2.setCellValueFactory(cellBack -> cellBack.getValue().yProperty().asObject());

        saveReport.setDisable(true);

    }

    @FXML
    private void addPoint() {
        try {
            Point p;
            if (p1x.getText().length() > 0 && p1y.getText().length() > 0) {

                p = new Point(
                        Double.parseDouble(p1x.getText()),
                        Double.parseDouble(p1y.getText())
                );
                tblview.getItems().add(p);
                p1list.add(p);

                p1x.clear();
                p1y.clear();

            }
            if (p2x.getText().length() > 0 && p2y.getText().length() > 0) {
                p = new Point(
                        Double.parseDouble(p2x.getText()),
                        Double.parseDouble(p2y.getText())
                );
                tblview2.getItems().add(p);
                p2list.add(p);

                p2x.clear();
                p2y.clear();
            }
        } catch (NumberFormatException ne) {
            saveReport.setDisable(true);
            //System.out.println(ne.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Недопустимые символы");
            al.setContentText("Введите числовое значение");
            al.showAndWait();
        }
    }

    @FXML
    private void countButton() throws SameValueExeption, BoundException, EpsException, NumberFormatException, OnePointException {
        try {
            chart.getData().clear();
            Function<Double, Double> lag1 = new Lagr(p1list);
            Function<Double, Double> lag2 = new Lagr(p2list);
            Function<Double, Double> f = (Double x) -> {
                return lag1.apply(x) - lag2.apply(x);
            };

            minimum = Dihotomy.calculate(
                    Double.parseDouble(st.getText()),
                    Double.parseDouble(end.getText()),
                    Double.parseDouble(eps.getText()),
                    lag1, lag2
            );
            if (minimum != null) {
                min.setText(f.apply(minimum).toString());//Min X
            } else {
                min.setText("Нет минимума");
            }
            double step = (Double.parseDouble(end.getText()) - Double.parseDouble(st.getText())) / 1000;
            XYChart.Series series = new XYChart.Series<>();
            XYChart.Series fSeries = new XYChart.Series<>();
            XYChart.Series gSeries = new XYChart.Series<>();
            XYChart.Series minSeries = new XYChart.Series<>();
            minSeries.setName("min");
            XYChart.Data minData = new XYChart.Data<Double, Double>(minimum, f.apply(minimum));
            minSeries.getData().add(minData);

            fSeries.setName("f(x)");
            gSeries.setName("g(x)");
            series.setName("f(x) - g(x)");
            chart.getData().add(series);
            chart.getData().add(fSeries);
            chart.getData().add(gSeries);
            chart.getData().add(minSeries);

            for (double i = Double.parseDouble(st.getText()); i < Double.parseDouble(end.getText()); i += step) {
                XYChart.Data data = new XYChart.Data<Double, Double>(i, f.apply(i));
                XYChart.Data fData = new XYChart.Data<Double, Double>(i, lag1.apply(i));
                XYChart.Data gData = new XYChart.Data<Double, Double>(i, lag2.apply(i));
                series.getData().add(data);
                fSeries.getData().add(fData);
                gSeries.getData().add(gData);

                data.getNode().setVisible(false);
                fData.getNode().setVisible(false);
                gData.getNode().setVisible(false);

            }
            saveReport.setDisable(false);
        } catch (SameValueExeption sve) {
            saveReport.setDisable(true);
            //System.out.println(sve.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Одинаковые х");
            al.setContentText("Введите разные х");
            al.showAndWait();
        } catch (NumberFormatException nfe) {
            saveReport.setDisable(true);
            //System.out.println(nfe.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Введены недопустимые символы");
            al.setContentText("Введите числовые значения");
            al.showAndWait();
        } catch (BoundException be) {
            saveReport.setDisable(true);
            // System.out.println(be.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Неправельный итервал");
            al.setContentText("Начало интервала не должно быть больше или равно концу интервала");
            al.showAndWait();
        } catch (EpsException ee) {
            saveReport.setDisable(true);
            //System.out.println(ee.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Введена неправильная точность");
            al.setContentText("Точность должна быть меньше единицы и больше нуля");
            al.showAndWait();
        } catch (OnePointException ope) {
            saveReport.setDisable(true);
            //System.out.println(ee.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Введена только одна точка");
            al.setContentText("Точек должно быть две");
            al.showAndWait();
        }
    }


    @FXML
    private void deleteButton() {
        Point d1 = tblview.getSelectionModel().getSelectedItem();
        Point d2 = tblview2.getSelectionModel().getSelectedItem();

        if (d1 != null) {
            tblview.getItems().remove(d1);
            p1list.remove(d1);
            tblview.getSelectionModel().clearSelection();
        } else if (d2 != null) {
            tblview2.getItems().remove(d2);
            p2list.remove(d2);
            tblview2.getSelectionModel().clearSelection();
        }
        saveReport.setDisable(true);
    }

    @FXML
    private void openButton() throws XMLWorkException, JAXBException {
        try {
            WrapOfModel wroom = XML.openFile(main.getStage());
            st.setText(wroom.getStart().toString());
            end.setText(wroom.getEnd().toString());
            eps.setText(wroom.getEps().toString());

            tblview.getItems().clear();
            tblview.getItems().addAll(wroom.getFirstLagrPoints());
            p1list.clear();
            p1list.addAll(wroom.getFirstLagrPoints());


            tblview2.getItems().clear();
            tblview2.getItems().addAll(wroom.getSecondLagrPoints());
            p2list.clear();
            p2list.addAll(wroom.getSecondLagrPoints());

        } catch (XMLWorkException xml) {
            saveReport.setDisable(true);
            //System.out.println(ee.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Невозможно открыть файл");
            al.setContentText("Возникла непредвиденная ошибка при открытии файла");
            al.showAndWait();
        } catch (JAXBException j) {
            saveReport.setDisable(true);
            //System.out.println(ee.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Невозможно открыть файл");
            al.setContentText("Возникла непредвиденная ошибка при открытии файла");
            al.showAndWait();
        }
        saveReport.setDisable(true);
    }

    @FXML
    private void saveButton() {
        try {
            XML.saveToXML(p1list, p2list, Double.parseDouble(st.getText()),
                    Double.parseDouble(end.getText()),
                    Double.parseDouble(eps.getText()), main.getStage());
        } catch (JAXBException e) {
            saveReport.setDisable(true);
            //System.out.println(ee.getMessage());
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Ошибка");
            al.setHeaderText("Невозможно сохранить файл");
            al.setContentText("Возникла ошибка при сохранении файла");
            al.showAndWait();
        }
    }

    @FXML
    private void htmlButton() throws IOException, XMLWorkException {
        HTMLReport report = new HTMLReport(Double.parseDouble(st.getText()), Double.parseDouble(end.getText()),
                Double.parseDouble(eps.getText()), p1list, p2list, main.getStage(), chart, minimum);

    }

    @FXML
    private void about() {
        Alert ab = new Alert(Alert.AlertType.NONE);
        ab.setTitle("О программе");
        ab.setContentText("Вычисление минимума методом Дихотомии" + "\n" +
                "Автор программы: Матяш Алексей " + "\n");
        ab.getDialogPane().getButtonTypes().add(ButtonType.OK);
        ab.showAndWait();

    }

    @FXML
    private void helpButton() {
        WebView browser = new WebView();
        String path = main.getClass().getClassLoader().getResource("model/Help/help.html").toExternalForm();
        browser.getEngine().load(path);
        Stage stage = new Stage();
        Scene scene = new Scene(browser, 900, 500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }

}













