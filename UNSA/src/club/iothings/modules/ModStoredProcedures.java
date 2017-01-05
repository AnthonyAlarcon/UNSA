package club.iothings.modules;

import java.sql.CallableStatement;
import java.sql.Connection;

public class ModStoredProcedures {
	
	private static Connection dbMySQL = null;
	
	public ModStoredProcedures(Connection connSQL){
		dbMySQL = connSQL;
	}
	
	public String sp_Data_Ajouter(String strNomUsuel, String strPrenom, String strAdresseMail, String strAcademie, String strUaiOccupation, String strTypeUai, String strGrade, String strCcp){	

		String resultat = "";
		
		try {
			CallableStatement stmt = null;
			stmt = dbMySQL.prepareCall("{call pm_data_ajouter(?,?,?,?,?,?,?,?)}");					
			stmt.setString("inom_usuel", strNomUsuel);
			stmt.setString("iprenom", strPrenom);
			stmt.setString("iadresse_mail", strAdresseMail);
			stmt.setString("iacademie", strAcademie);
			stmt.setString("iuai_occupation", strUaiOccupation);
			stmt.setString("itype_uai", strTypeUai);
			stmt.setString("igrade", strGrade);
			stmt.setString("iccp", strCcp);			
			stmt.execute();
			
			resultat = "OK";
			
		} catch(Exception ex) {
			resultat = "ERREUR";
			System.out.println("### sp_Data_Ajouter ### " + ex.toString());
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
			System.out.println("### sp_Data_Supprimer_Academie ###" + ex.toString());
		}
		return resultat;
	}

}
