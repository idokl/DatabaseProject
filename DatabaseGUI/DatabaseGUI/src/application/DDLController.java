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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * DDLController - controller of DDL command
 */
public class DDLController {
	private ServerCommunicator serverCommunicator;
	
	@FXML
	private Button sendButton;
	@FXML
	private Button backButton;
	@FXML
	private TextArea sqlCommandsTextArea;
	
	@FXML
	private TextField fromFile;

	/*
	 * constructor
	 */
	public DDLController() {
		this.serverCommunicator = new ServerCommunicator();
	}

	/*
	 * sendCommands - get commands from text area
	 */
	public void sendCommands() {
		//String command = sqlCommandsTextArea.getText();
		String[] commands = sqlCommandsTextArea.getText().split(";");
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].trim().length() > 0) {
				serverCommunicator.executeSqlCommand(commands[i]);
			}
		}
		sqlCommandsTextArea.clear();
	}

	/*
	 * sendCommandsFromFile - execute SQL commands
	 */
	public void sendCommandsFromFile() {
		try {
			String commands= fromFile.getText();
			String command;
			BufferedReader br = new BufferedReader(new FileReader(commands));
			while((command= br.readLine())!=null)
			{
				serverCommunicator.executeSqlCommand(command);
			}
		    br.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file " + fromFile + " wasn't found.");
		} catch (IOException e) {
			System.out.println("We couldn't read " + fromFile + " because of IOException.");
		}
		fromFile.clear();
	}

	/*
	 * backToMainMenu - back to main menu button
	 */
	public void backToMainMenu() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = Main.getPrimeStage();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
