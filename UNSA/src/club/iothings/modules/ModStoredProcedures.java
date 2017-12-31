package club.iothings.modules;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class ModStoredProcedures {
	
	private static Connection dbMySQL = null;
	
	public ModStoredProcedures(Connection connSQL){
		dbMySQL = connSQL;
	}
	
	public String sp_Data_Ajouter(Integer ind, String strNomUsuel, String strPrenom, String strAdresseMail, String strAcademie, String strUaiOccupation, String strGrade, String strCcp){	

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
			System.out.println("### ModStoredProcedures ### sp_Data_Ajouter ### (ligne " + ind + ") " + ex.toString());
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
	
	public String sp_Data_SupprimerTout(){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call px_data_supprimertout}");
			stmt.execute();
			
			resultat = "OK";
			
		} catch (MySQLIntegrityConstraintViolationException ex_primary){
			resultat = "PRIMARY";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Data_SupprimerTout ###" + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Grade_Ajouter(Integer ind, String strId, String strDesignation, String strCategorie){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_grade_ajouter(?,?,?)}");					
			stmt.setString("iid", strId);
			stmt.setString("idesignation", strDesignation);
			stmt.setString("icategorie", strCategorie);	
			stmt.execute();
			
			resultat = "OK";
		
		} catch (MySQLIntegrityConstraintViolationException ex_primary){
			resultat = "PRIMARY";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Grade_Ajouter ### (ligne " + ind + ") " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_UAI_ajouter(Integer ind, String strId, String strNom, String strDepartement, String strAcademie, String strTypeUAI, String strGroupe, String strVille){	

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
			
		} catch (MySQLIntegrityConstraintViolationException ex_primary){
			resultat = "PRIMARY";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedure ### sp_UAI_Ajouter ### (ligne " + ind + ") " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_UAI_Maj_Infos(Integer ind, String strId, String strNom, String strGroupe, String strVille){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_uai_maj_infos(?,?,?,?)}");					
			stmt.setString("iid", strId);
			stmt.setString("inom", strNom);
			stmt.setString("igroupe", strGroupe);
			stmt.setString("iville", strVille);
			stmt.execute();
			
			resultat = "OK";
			
		} catch (MySQLIntegrityConstraintViolationException ex_primary){
			resultat = "PRIMARY";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedure ### sp_UAI_Maj_Infos ### (ligne " + ind + ") " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Departement_ajouter(Integer ind, String strNumero, String strNom){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_departement_ajouter(?,?)}");					
			stmt.setString("inumero", strNumero);
			stmt.setString("inom", strNom);
			stmt.execute();
			
			resultat = "OK";
			
		} catch (MySQLIntegrityConstraintViolationException ex_primary){
			resultat = "PRIMARY";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedure ### sp_Departement_ajouter ### (ligne " + ind + ") " + ex.toString());
		}
		return resultat;
	}
	
	public String sp_Modele_Ajouter(String strNom, String strCritere, String strDepartement, String strTypeUAI, String strGrade, String strCCP, String strGroupe, String strVille){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_modele_ajouter(?,?,?,?,?,?,?,?)}");					
			stmt.setString("inom", strNom);
			stmt.setString("icritere", strCritere);
			stmt.setString("idepartement", strDepartement);
			stmt.setString("itype_uai", strTypeUAI);
			stmt.setString("igrade", strGrade);
			stmt.setString("iccp", strCCP);
			stmt.setString("igroupe", strGroupe);	
			stmt.setString("iville", strVille);	
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModStoredProcedures ### sp_Modele_Ajouter ### " + ex.toString());
		}
		return resultat;
	}

}
