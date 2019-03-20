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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/*
 * DMLController - controller of DDL command
 */
public class DMLController {
	private ServerCommunicator serverCommunicator;

	@FXML
	private Button sendButton;
	@FXML
	private Button simpleQueryButton;
	@FXML
	private TextArea sqlCommandsTextArea;
	@FXML
	private TextArea output;
	@FXML
	private TextField fromFile;
	@FXML
	private Button backButton;

	private DMLValidator validator;

	/*
	 * constructor
	 */
	public DMLController() {
		this.serverCommunicator = new ServerCommunicator();
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

	/*
	 * sendCommands - check if commands are valid and send them to database
	 */
	public void sendCommands() {
		output.clear();
		String query = sqlCommandsTextArea.getText();

		this.validator = new DMLValidator(query);
		if (!validator.ValidateStructure().equals("VALID")) {
			output.appendText("WRONG QUERY STRUCTURE");
			return;
		}

		try {
			String[] queries = query.split(";");
			for (int j = 0; j < queries.length; j++) {
				if (queries[j].trim().length() > 0) {
					this.validator = new DMLValidator(queries[j]);
					if (!validator.ValidateStructure().equals("VALID")) {
						output.appendText("WRONG QUERY STRUCTURE");
						return;
					}

					// serverCommunicator.executeSqlCommand(queries[j]);
					ResultSet rs = serverCommunicator.executeSqlQuery(queries[j]);

					// ResultSet rs = serverCommunicator.executeSqlQuery(query); 
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1)
								output.appendText(" || ");
							String columnValue = rs.getString(i);
							// System.out.print(rsmd.getColumnName(i) + ": " +
							// columnValue);
							output.appendText(rsmd.getColumnName(i) + ": " + columnValue);
						}
						output.appendText("\n");
					}
				}
			}
		} catch (Exception e) {
			output.appendText("LOGICAL ERROR\n");
			output.appendText(e.getLocalizedMessage());
		}
		sqlCommandsTextArea.clear();
	}

	/*
	 * passToSimpleQuery - go to simple query window
	 */
	public void passToSimpleQuery() {
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

	/*
	 * sendCommandsFromFile - send SQL commands if they are valid
	 */
	public void sendCommandsFromFile() {
		try {
			output.clear();
			String queries = fromFile.getText();
			String query;
			BufferedReader br = new BufferedReader(new FileReader(queries));
			while ((query = br.readLine()) != null) {
				this.validator = new DMLValidator(query);
				if (!validator.ValidateStructure().equals("VALID")) {
					output.appendText("WRONG QUERY STRUCTURE");
					return;
				}
				try {
					ResultSet rs = serverCommunicator.executeSqlQuery(query);
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1)
								output.appendText(" || ");
							// System.out.print(", ");
							String columnValue = rs.getString(i);
							// System.out.print(rsmd.getColumnName(i) + ": " +
							// columnValue);
							output.appendText(rsmd.getColumnName(i) + ": " + columnValue);
						}
						output.appendText("\n");
					}
				} catch (Exception e) {
					output.appendText("LOGICAL ERROR\n");
					output.appendText(e.getLocalizedMessage());
				}

			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file " + fromFile + " wasn't found.");
		} catch (IOException e) {
			System.out.println("We couldn't read " + fromFile + " because of IOException.");
		}
		fromFile.clear();
	}

}
