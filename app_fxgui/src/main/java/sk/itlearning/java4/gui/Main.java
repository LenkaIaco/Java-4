package sk.itlearning.java4.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override //prepisujeme metodu start, to je start nasej standalone appky reprezentovanej foldrom app_fxgui
    public void start(Stage stage) {
    	
    	FXMLLoader loader = new FXMLLoader();
    	
    	loader.setLocation(getClass().getResource("main.fxml"));//v jave by tu bol JPanel, ale tu je miesto toho ref. na exter.subor co definuje graficky layout(moderne appky uz robia externe grafiku nie priamo cez Javu
    	
        Parent root;
        
		try {
			root = loader.load();
	        stage.setTitle("Delete Tool");
	        stage.setScene(new Scene(root, 420, 300));
	        
	        MainController c = loader.getController();
			c.init();
			
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}   
    }

    public static void main(String[] args) {
        launch();
    }

}