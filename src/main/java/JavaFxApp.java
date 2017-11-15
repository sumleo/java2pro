
import Utils.SQLhandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashSet;

public class JavaFxApp extends Application {

    public static void main(String[] args) {
        SQLhandler sqLhandler = new SQLhandler();
        sqLhandler.doQuery();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HashSet<String> hashSet = new HashSet<String>();
    }
}
