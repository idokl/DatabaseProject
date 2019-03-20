/****************************************************
 * Yishai Seela 305373706 89-281-03
 * Ori Hirshfeld 201085776 89-281-02
 * Ido Klein 203392964 89-281-03
 * 
 ***************************************************/

package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * MenuController - controller of menu
 */
public class MenuController

{
	@FXML
	private Button DMLbutton;
	@FXML
	private Button DDLbutton;
	@FXML
	private Button simpleQueryButton2;

	/*
	 * constructor
	 */
	public MenuController() {
		
	}

	@FXML
	/*
	 * initialize - initialize menu
	 */
	private void initialize() {

	}
/*
	@FXML
	private void printOutput()
	{
		outputText.setText(inputText.getText());
	}
*/
	/*
	 * onActionOfDMLbutton - activate DML button
	 */
	public void onActionOfDMLbutton() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("DML.fxml"));
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = Main.getPrimeStage();
			stage.setScene(scene);
			stage.show();
			// primaryStage.setScene(scene);
			// primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
 	* onActionOfDDLbutton - activate DML button
 	*/
	public void onActionOfDDLbutton() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("DDL.fxml"));
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = Main.getPrimeStage();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	public void passToSimpleQuery2() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("SimpleQuery.fxml"));
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = Main.getPrimeStage();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

}
