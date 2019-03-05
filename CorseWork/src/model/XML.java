package model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.exeptions.XMLWorkException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

public class XML extends Exception {
    public static WrapOfModel openFile(Stage stage) throws XMLWorkException, JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        File file = fileChooser.showOpenDialog(stage);
        return openFile(file);
    }


    public static WrapOfModel openFile(File file) throws XMLWorkException, JAXBException {
        WrapOfModel wrapper = null;
        if (file == null) throw new XMLWorkException("Вы не указали файл ", "*нету имени файла*");
        try {
            JAXBContext context = JAXBContext
                    .newInstance(WrapOfModel.class);
            Unmarshaller um = context.createUnmarshaller();

            wrapper = (WrapOfModel) um.unmarshal(file);
        } catch (JAXBException e) { // catches ANY exception
            throw e;
        }

        if (wrapper.getStart() == null) throw new XMLWorkException("Начало интервала не введено.", file.getName());
        if (wrapper.getEnd() == null) throw new XMLWorkException("Конец интервала не введен.", file.getName());
        if (wrapper.getEps() == null) throw new XMLWorkException("Точность не введена.", file.getName());
        if (wrapper.getFirstLagrPoints() == null || wrapper.getSecondLagrPoints() == null)
            throw new XMLWorkException("Нет набора точек.", file.getName());

        return wrapper;
    }

    public static void saveToXML(ArrayList<Point> points1, ArrayList<Point> points2, double a, double b, double eps, Stage stage) throws JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                JAXBContext context = JAXBContext
                        .newInstance(WrapOfModel.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                WrapOfModel wrapper = new WrapOfModel(points1, points2, a, b, eps);

                marshaller.marshal(wrapper, file);
            } catch (JAXBException e) { // catches ANY exception
                throw e;
            }

        }

    }

}

