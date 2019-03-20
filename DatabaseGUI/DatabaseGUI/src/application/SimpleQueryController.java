/****************************************************
* Yishai Seela 305373706 89-281-03
* Ori Hirshfeld 201085776 89-281-02
* Ido Klein 203392964 89-281-03
*
***************************************************/

package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/*
* SimpleQueryController - controller of simple query window
*/
public class SimpleQueryController {
	private ServerCommunicator serverCommunicator;

	// @FXML
	// private ObservableList observableArrayList;
	@FXML
	private Label instructions;
	@FXML
	private Button tablesBtn;
	@FXML
	private Button columnsBtn;
	@FXML
	private TextField tableNumberTextField;
	@FXML
	private TextField chosenColsTxtField;
	@FXML
	private Button sendQuaryButton;
	@FXML
	private Button mainMenuButton;
	// @FXML
	// private ComboBox chooseTable;
	@FXML
	private TextArea output;
	@FXML
	private TextArea columnsOutput;
	@FXML
	private TextArea queryAnswerOutput;
	@FXML
	private VBox vBox;
	@FXML
	private TextField where;

	private ArrayList<CheckBox> boxes;
	private String table;
	private ArrayList<String> tables = new ArrayList<String>();
	private ArrayList<String> columns = new ArrayList<String>();
	private String chosenTableName = "";

	/*
	 * constructor
	 */
	public SimpleQueryController() {
		this.serverCommunicator = new ServerCommunicator();
		// this.getTables();
	}

	/*
	 * getTables - get names of tables in database
	 */
	public void getTables() {
		output.clear();
		columnsOutput.clear();
		queryAnswerOutput.clear();
		chosenColsTxtField.clear();
		where.clear();
		columns = new ArrayList<String>();
		
		String query = "SHOW tables";
		try {
			ResultSet rs = serverCommunicator.executeSqlQuery(query);
			int cols = rs.getMetaData().getColumnCount();
			/* ArrayList<String> */ tables = new ArrayList<String>();
			while (rs.next()) {
				// result.next();
				for (int i = 1; i <= cols; ++i) {
					tables.add(rs.getString(i));
				}
			}
			if (0 == tables.size()) {
				output.clear();
				output.setText("No tables");
				return;
			}
			output.clear();
			output.setText("The tables are: \n0. " + tables.get(0));
			for (int j = 1; j < tables.size(); j++) {
				output.appendText("\n" + j + ". " + tables.get(j));
			}
			instructions.setText("Please write the number of the chosen table in the"
					+ " \"table number\" text field\n and click on \"show columns of table\"");

			/*
			 * chooseTable = new ComboBox();
			 * chooseTable.setItems(FXCollections.observableArrayList(tables));
			 * chooseTable.valueProperty().addListener(new
			 * ChangeListener<String>() {
			 * 
			 * @Override public void changed(ObservableValue observable, String
			 * oldVal, String tableName) { addAttribute(tableName); } });
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getColumns() {
		columnsOutput.clear();
		queryAnswerOutput.clear();
		chosenColsTxtField.clear();
		where.clear();

		columns = new ArrayList<String>();
		String chosenTableNumString = tableNumberTextField.getText();
		Integer chosenTableNum = null;
		try {
			chosenTableNum = Integer.parseInt(chosenTableNumString);
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("You have to write a valid number of table in the \"table number\" text field");
			alert.showAndWait();
			return;
		}
		if ((chosenTableNum == null) || chosenTableNum >= this.tables.size()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("You didn't write a valid number of table in the \"table number\" text field");
			if (tables.size() == 0) {
				alert.setContentText("There are not available tables");
			} else {
				alert.setContentText("There are " + tables.size() + " tables. Please choose number from 0 to "
						+ (tables.size() - 1));
			}
			alert.showAndWait();
			return;
		}
		chosenTableName = tables.get(chosenTableNum);
		String query = "SELECT * FROM " + chosenTableName;
		try {
			ResultSet rs = serverCommunicator.executeSqlQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i <= columnsNumber; i++) {
				columns.add(rsmd.getColumnName(i));
			}
			columnsOutput.setText("the columns of\n\"" + chosenTableName + "\" are:\n");
			for (int i = 0; i < columns.size(); i++) {
				columnsOutput.appendText(i + ". " + columns.get(i) + "\n");
			}
			instructions.setText("Please choose some columns of the table and write their indexes\n"
					+ "splitted by commas in the \"columns numbers\" text field.\n"
					+ "(For example: if you want to get the first column and the third column, write 0,2)");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * addAttribute - add possible attributes to show in query
	 */
/*	
	private void addAttribute(String tableName) {

		try {
			table = tableName;
			boxes = new ArrayList<>();
			vBox.getChildren().clear();
			ResultSet rs = serverCommunicator.executeSqlQuery("DESCRIBE " + table);
			// String[] attributes = rs.toString().split("\n");
			int cols = rs.getMetaData().getColumnCount();
			// ArrayList<String> attributes = new ArrayList<String>();
			while (rs.next()) {
				// result.next();
				for (int i = 1; i <= cols; ++i) {
					// attributes.add(rs.getString(i));
					CheckBox cb = new CheckBox(rs.getString(i));
					boxes.add(cb);
					vBox.getChildren().add(cb);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	
	/*
	 * send query to server
	 */
	public void sendQuary() {
		queryAnswerOutput.clear();
		
		if (chosenTableName == "") {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("You have to choose table first");
			alert.setContentText("Please follow the instructions");
			alert.showAndWait();
			return;
		}
		
		//check "columns numbers" text field
		String chosenColumnsNumbersString = chosenColsTxtField.getText();
		String[] colsNumbersStrings = chosenColumnsNumbersString.split(","); 
		ArrayList<Integer> colsNumbers = new ArrayList<Integer>();
		Integer chosenCol = null;
		for (int i = 0; i < colsNumbersStrings.length; i++) {
			try {
				chosenCol = Integer.parseInt(colsNumbersStrings[i]);
				colsNumbers.add(chosenCol);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("You have to write a valid numbers splitted by commas in \"columns numbers\" text field");
				alert.setContentText(colsNumbersStrings[i] +  " is not a valid number.\n"
								+ "try to enter valid input in this text field. for example: 0,1");
				alert.showAndWait();
				return;
			}
		}
		for (Integer i: colsNumbers) {
			if (i >= this.columns.size()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("You didn't write a valid number of column in the \"columns numbers\" text field");
				if (columns.size() == 0) {
					alert.setContentText("There are not available columns");
				} else {
					alert.setContentText("There are " + columns.size() + " columns. Please choose numbers from 0 to "
							+ (columns.size() - 1));
				}
				alert.showAndWait();
				return;
			}
		}
		String columnsStringForQuery = this.columns.get(colsNumbers.get(0));
		for(int i = 1; i < colsNumbers.size(); i++) {
			columnsStringForQuery += ", " + this.columns.get(colsNumbers.get(i));
		}
		//System.out.println("debug: " + columnsStringForQuery);
		String query = "SELECT " + columnsStringForQuery + " FROM " + chosenTableName;
		String indication = "Your query is:\n" + query;
		String whereStatement = where.getText();
		if (whereStatement.trim().length() > 0) {
			query += " WHERE " + whereStatement;
			indication += "\nWHERE " + whereStatement;
		}
		query += ";";
		indication += ";";
		//System.out.println("debug: the query is " + query);
		instructions.setText(indication);
		try {
			ResultSet rs = serverCommunicator.executeSqlQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						queryAnswerOutput.appendText(" || ");
					String columnValue = rs.getString(i);
					queryAnswerOutput.appendText(rsmd.getColumnName(i) + ": " + columnValue);
				}
				queryAnswerOutput.appendText("\n");
			}
		} catch (Exception e) {
			queryAnswerOutput.setText("LOGICAL ERROR\n");
			queryAnswerOutput.appendText(e.getLocalizedMessage());
		}

		
		/*		
		try {		
			output.clear();
			String attributes = "";
			for (CheckBox cb : boxes) {
				if (cb.isSelected()) {
					attributes += cb.getText();
					attributes += ", ";
				}
			}
			String query = "SELECT ";
			query += attributes;
			query += "FROM " + table + " WHERE " + where.getText() + ";";

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

		} catch (Exception e) {
			System.out.println("Exception");
		}
		*/
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
