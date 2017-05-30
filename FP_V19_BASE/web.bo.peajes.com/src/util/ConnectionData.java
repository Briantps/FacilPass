package util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class to manage data base connection for reports 
 * 
 * @author gt-irincon.
 * 
 */
public class ConnectionData {

	private Connection connection = null;

	/**
	 * Constructor 
	 */
	public ConnectionData() {

	}
	
	/**
	 * method returns data base connection 
	 * @return data base connection
	 */
	public Connection getConnection() {
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("jdbc/bo");
			connection = dataSource.getConnection();
			return connection;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method that close data base connection 
	 * @throws SQLException
	 */
	public void closeConection() throws SQLException {
		connection.close();
	}
}