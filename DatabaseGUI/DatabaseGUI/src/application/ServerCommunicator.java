/****************************************************
 * Yishai Seela 305373706 89-281-03
 * Ori Hirshfeld 201085776 89-281-02
 * Ido Klein 203392964 89-281-03
 * <p>
 ***************************************************/
package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;

/*
 * ServerCommunicator - communicates with server 
 */
public class ServerCommunicator {
	private Connection  connection;
	private Statement statement;
	private String hostStr;
	private String usernameStr;
	private String passwordStr;

	/*
     * constructor
     */
	public ServerCommunicator() {
		this.connection = null;
		this.statement = null;
		this.hostStr = null;
		this.usernameStr = null;
		this.passwordStr = null;
	}

	/*
     * createConnection - create connection with database
     */
	public void createConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (hostStr == null || usernameStr == null || passwordStr == null) {
			this.readConfFile("./conf.txt");
			this.hostStr += "?autoReconnect=true&useSSL=false";
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(hostStr, usernameStr, passwordStr);
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("FAILED CONNECTING TO SQL SERVER!");
			alert.setContentText("(we tried to connect to " + hostStr + " with the username " +
					usernameStr + " and the password " + passwordStr + ").");
			alert.showAndWait();
			System.out.println(" FAILED CONNECTING TO SQL SERVER!");
			System.out.println(" (we tried to connect to " + hostStr + " with the username " +
					usernameStr + " and the password " + passwordStr + "). ");
			//e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.statement = this.connection.createStatement();
	}

	/*
     * readConfFile - read host,  username and password from config file
     */
	private void readConfFile(String confFileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(confFileName));
			this.hostStr = br.readLine();
			this.usernameStr = br.readLine();
			this.passwordStr = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("The file " + confFileName + " wasn't found.");
			alert.showAndWait();
			System.out.println("The file " + confFileName + " wasn't found.");
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("We couldn't read " + confFileName + " because of IOException.");
			alert.showAndWait();
			System.out.println("We couldn't read " + confFileName + " because of IOException.");
		}
	}
 
	/*
     * executeSqlCommand - execute the SQL command
     */
	public void executeSqlCommand(String sqlCommand) {
		if (statement == null) {
			try {
				this.createConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} try {
			this.statement.execute(sqlCommand);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/*
     * executeSqlQuery - execute the SQL query
     * return result of query as ResultSet
     */
	public ResultSet executeSqlQuery(String sqlQuery) throws Exception {

		if (statement == null) {
			try {
				this.createConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} //try {
		return this.statement.executeQuery(sqlQuery);
		//} catch (Exception e) {
		//      System.out.println(e.getLocalizedMessage());
		//  return null;
		//}
	}
}