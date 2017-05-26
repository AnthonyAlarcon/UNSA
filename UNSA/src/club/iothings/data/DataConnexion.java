package club.iothings.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnexion {
	
	private String url = "";
	private String database = "";
	private String login = "";
	private String password = "";
	private String version = "";
	
	private Connection dbMySQL = null;
	
	public DataConnexion(){
		
		//----- Affectation des valeurs -----
		setValues();
	}
	
	private void setValues(){
		
		// --- Serveur MySQL web ---
//		url = "jdbc:mysql://www.iothings.club:3306/";
//		database = "db_unsa";
//		login = "alarcon";
//		password = "gunnar30";
		
		// --- Serveur MySQL local ---
		url = "jdbc:mysql://127.0.0.1/";
		database = "db_unsa";
		login = "user_unsa";
		password = "unsa";
		
		// --- Version du logiciel client ---
		version = "1.04";
	}
	
	public String start(){
		
		String resultat = "";
		
		try {
			
			// --- Reset de la connection ---
			dbMySQL = null;
			
			// --- Tentative de connexion à la base de données ---
			dbMySQL = DriverManager.getConnection( url + database, login, password);
			resultat = "OK";
			
		} catch (Exception ex) {
			
			// --- Reset de la connection ---
			dbMySQL = null;
			
			resultat = "ERREUR";
			System.out.println("### DataConnexion ### start ### " + ex.toString());
		}
		
		return resultat;
	}
	
	public Connection getDbMySQL(){
		return dbMySQL;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getDbName(){
		return database;
	}
}
