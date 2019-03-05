import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    Stage stage;
    AnchorPane root;

    public Stage getStage() {
        return stage;
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("sample.fxml"));
            root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            Controller cntrllr = loader.getController();
            cntrllr.setMain(this);
            stage.show();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("Минимум методом Дихотомии");
        initRootLayout();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
