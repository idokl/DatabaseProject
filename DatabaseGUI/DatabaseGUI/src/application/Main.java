/****************************************************
 * Yishai Seela 305373706 89-281-03
 * Ori Hirshfeld 201085776 89-281-02
 * Ido Klein 203392964 89-281-03
 * 
 ***************************************************/

package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//import javafx.scene.layout.BorderPane;

/*
 * Main - main function
 */
public class Main extends Application {
	private static Stage primeStage;
	
	@Override
	/*
	 * start - open start window
	 */
	public void start(Stage primaryStage) {
		setPrimeStage(primaryStage);
		try {
			Parent root;
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//credit to: https://stackoverflow.com/questions/15805881/how-can-i-obtain-the-primary-stage-in-a-javafx-application

	/*
 	* setPrimeStage - set prime stage
 	*/
	public void setPrimeStage(Stage pStage) {
		Main.primeStage = pStage;
	}

	/*
 	* getPrimeStage - get prime stage
 	* return prime stage
 	*/
	public static Stage getPrimeStage() {
		return primeStage;
	}

	/*
	 * main - launch main window
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
