package melke.bogdo.kth.lab2.labb2mungodb.View.Test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.View.LoginView;

public class TestLoginView extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login Test");

        // Create the LoginView
        Scene scene = new Scene(LoginView.create(stage), 400, 200);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
