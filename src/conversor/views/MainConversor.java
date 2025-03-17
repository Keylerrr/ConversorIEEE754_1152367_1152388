package conversor.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainConversor extends Application {
	
	public static void main(String[] args) {
        Application.launch(MainConversor.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("IEEEConvertView.fxml"));
    	
        stage.setTitle("IEEEZY CONVERT - Conversor IEEE 754");
        stage.setResizable(false);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

}
