package club.iothings.modules;

import java.sql.CallableStatement;
import java.sql.Connection;

public class ModStoredProcedures {
	
	private static Connection dbMySQL = null;
	
	public ModStoredProcedures(Connection connSQL){
		dbMySQL = connSQL;
	}
	
	public String sp_Data_Ajouter(String strNomUsuel, String strPrenom, String strAdresseMail, String strAcademie, String strUaiOccupation, String strGrade, String strCcp){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_data_ajouter(?,?,?,?,?,?,?)}");					
			stmt.setString("inom_usuel", strNomUsuel);
			stmt.setString("iprenom", strPrenom);
			stmt.setString("iadresse_mail", strAdresseMail);
			stmt.setString("iacademie", strAcademie);
			stmt.setString("iuai_occupation", strUaiOccupation);
			stmt.setString("igrade", strGrade);
			stmt.setString("iccp", strCcp);			
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Data_Ajouter ### " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Data_Supprimer_Academie(String strAcademie){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_data_supprimer_academie(?)}");
			stmt.setString("iacademie", strAcademie);
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Data_Supprimer_Academie ###" + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Grade_Ajouter(String strId, String strDesignation, String strCategorie){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_grade_ajouter(?,?,?)}");					
			stmt.setString("iid", strId);
			stmt.setString("idesignation", strDesignation);
			stmt.setString("icategorie", strCategorie);	
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Grade_Ajouter ### " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_UAI_ajouter(String strId, String strNom, String strDepartement, String strAcademie, String strTypeUAI, String strGroupe, String strVille){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_uai_ajouter(?,?,?,?,?,?,?)}");					
			stmt.setString("iid", strId);
			stmt.setString("inom", strNom);
			stmt.setString("idepartement", strDepartement);
			stmt.setString("iacademie", strAcademie);
			stmt.setString("itype_uai", strTypeUAI);
			stmt.setString("igroupe", strGroupe);
			stmt.setString("iville", strVille);
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedure ### sp_UAI_Ajouter ### " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Departement_ajouter(String strNumero, String strNom){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_departement_ajouter(?,?)}");					
			stmt.setString("inumero", strNumero);
			stmt.setString("inom", strNom);
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedure ### sp_Departement_ajouter ### " + ex.toString());
		}
		return resultat;
	}

}
