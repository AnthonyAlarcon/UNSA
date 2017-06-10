package club.iothings.structures;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StructImportDataCSV {
	
	private Connection dbMySQL = null;
	
	private int nb_colonnes = 0;
	private int ind_nom_usuel = 0;
	private int ind_prenom = 0;
	private int ind_adresse_mail = 0;
	private int ind_academie = 0;
	private int ind_uai_occupation = 0;
	private int ind_type_uai = 0;
	private int ind_grade = 0;
	private int ind_ccp = 0;

	public StructImportDataCSV(Connection connMySQL){
		
		dbMySQL = connMySQL;
	
		//--- Récupération de la valeur des indices de colonne ---
		setValues();
	}
	
	private String setValues(){
		
		String resultat = "";
		
		try {
			
			String item = "";
			String valeur = "";
			
			//--- SQL ---
			String query_valeur = "SELECT item, valeur FROM T_REGLE WHERE categorie = 'IMPORT_CSV' ORDER BY valeur";			
			
			Statement stmt_valeur = dbMySQL.createStatement();
			ResultSet rset_valeur = stmt_valeur.executeQuery(query_valeur);

			
			while (rset_valeur.next()){
				
				// --- Affectation des valeurs ---
				item = rset_valeur.getString(1);
				valeur = rset_valeur.getString(2);
				
				switch (item){
				
				case "nom_usuel":
					setInd_NomUsuel(Integer.valueOf(valeur));
					break;
				case "prenom":
					setInd_Prenom(Integer.valueOf(valeur));
					break;
				case "adresse_mail":
					setInd_AdresseMail(Integer.valueOf(valeur));
					break;
				case "academie":
					setInd_Academie(Integer.valueOf(valeur));
					break;
				case "uai_occupation":
					setInd_UaiOccupation(Integer.valueOf(valeur));
					break;
				case "type_uai":
					setInd_TypeUai(Integer.valueOf(valeur));
					break;
				case "grade":
					setInd_Grade(Integer.valueOf(valeur));
					break;
				case "ccp":
					setInd_Ccp(Integer.valueOf(valeur));
					break;
				case "nb_colonnes":
					setNbColonnes(Integer.valueOf(valeur));
					break;
				}
			}
			
			resultat = "OK";
			
		} catch (Exception ex){
			System.out.println("### StructImportDataCSV ### setValues # " + ex.toString());
			resultat = "ERREUR";
		}
		
		return resultat;
	}
	
	private void setInd_NomUsuel(int intNomUsuel){
		ind_nom_usuel = intNomUsuel;
	}
	
	private void setInd_Prenom(int intPrenom){
		ind_prenom = intPrenom;
	}
	
	private void setInd_AdresseMail(int intAdresseMail){
		ind_adresse_mail = intAdresseMail;
	}
	
	private void setInd_Academie(int intAcademie){
		ind_academie = intAcademie;
	}
	
	private void setInd_UaiOccupation(int intUaiOccupation){
		ind_uai_occupation = intUaiOccupation;
	}
	
	private void setInd_TypeUai(int intTypeUai){
		ind_type_uai = intTypeUai;
	}
	
	private void setInd_Grade(int intGrade){
		ind_grade = intGrade;
	}
	
	private void setInd_Ccp(int intCcp){
		ind_ccp = intCcp;
	}
	
	private void setNbColonnes(int intNbColonnes){
		nb_colonnes = intNbColonnes;
	}
	
	public int getInd_NomUsuel(){
		return ind_nom_usuel;
	}
	
	public int getInd_Prenom(){
		return ind_prenom;
	}
	
	public int getInd_AdresseMail(){
		return ind_adresse_mail;
	}
	
	public int getInd_Academie(){
		return ind_academie;
	}
	
	public int getInd_UaiOccupation(){
		return ind_uai_occupation;
	}
	
	public int getInd_TypeUai(){
		return ind_type_uai;
	}
	
	public int getInd_Grade(){
		return ind_grade;
	}
	
	public int getInd_Ccp(){
		return ind_ccp;
	}
	
	public int getNbColonnes(){
		return nb_colonnes;
	}
	
}
