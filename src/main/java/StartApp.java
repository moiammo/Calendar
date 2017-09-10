import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApp extends Application {
    //5810404936 Yarnadhis Poolsawat
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("calendarPage.fxml"));
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(new Scene(root, 533, 417));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
