package club.iothings.fonctions;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import club.iothings.modules.ModFonctions;

public class FcnExportFiltres {

	private Connection dbMySQL = null;
	private String emplacement = "";
	
	private ModFonctions fct = null;
	
	
	public FcnExportFiltres(Connection connMySQL, String strEmplacement){
		
		// --- Affectation des valeurs ---
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		
		// --- Initialisation du module de fonctions ---
		fct = new ModFonctions();
	}
	
	public String start(String strNomFichier, String strDepartement, String strGrade, String strTypeUAI, String strCCP, String strGroupe){
		
		String resultat = "";
		
		//----- Affectation des paramètres -----
		String nom_fichier = strNomFichier.trim();
		String departement = strDepartement.trim();
		String grade = strGrade.trim();
		String type_uai = strTypeUAI.trim();
		String ccp = strCCP.trim();
		String groupe = strGroupe.trim();
		
		// --- Variables utilisées pour les filtres ---
		String filtre_dep = "";
		String filtre_grade = "";
		String filtre_typeUAI = "";
		String filtre_CCP = "";
		String filtre_groupe = "";
		
		try {
			
			// --- Filtre Département ---
			if (departement.compareTo("")!=0){
				filtre_dep = " AND U.departement IN (" + fct.MEF_Query_ValuesIn(departement, ";") + ")";
			}
			
			//--- Filtre Grade ---
			if (grade.compareTo("")!=0){
				filtre_grade = " AND D.grade IN (" + fct.MEF_Query_ValuesIn(grade, ";") + ")";
			}
			
			//--- Filtre TypeUAI ---
			if (type_uai.compareTo("")!=0){
				filtre_typeUAI = " AND D.type_uai IN (" + fct.MEF_Query_ValuesIn(type_uai, ";") + ")";
			}
			
			//--- Filtre CCP ---
			if (ccp.compareTo("")!=0){
				filtre_CCP = " AND D.ccp IN (" + fct.MEF_Query_ValuesIn(ccp, ";") + ")";
			}
			
			//--- Filtre CCP ---
			if (groupe.compareTo("")!=0){
				filtre_CCP = " AND U.groupe IN (" + fct.MEF_Query_ValuesIn(groupe, ";") + ")";
			}
			
			
			// --- Query ---
			String query_mail = "SELECT D.adresse_mail FROM T_DATA D, T_UAI U WHERE D.uai_occupation = U.id " + filtre_grade + filtre_dep + filtre_typeUAI + filtre_CCP + filtre_groupe + " ORDER BY adresse_mail";
			
			System.out.println("<FcnExportFiltres><start> Query = " + query_mail);
			
			Statement stmt_mail = dbMySQL.createStatement();
			ResultSet rset_mail = stmt_mail.executeQuery(query_mail);
			
			// --- Compilation du fichier ---
			try {	
				
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + nom_fichier + ".csv"), "UTF-8"));
				
				while (rset_mail.next()){						
					out.write(rset_mail.getString(1));
					out.newLine();
				}
				out.close();
				
				// --- Resultat OK ---
				resultat = "OK";
				
				System.out.println("<FcnExportFiltres><start> Compilation " + nom_fichier + " : OK");
				
			} catch (IOException ioex) {
				System.out.println("### FcnExportFiltres ### start ### Compilation " + ioex.toString());
			}
			
		} catch (Exception ex){
			resultat = "ERREUR";
			System.out.println("### FcnExportFiltres ### start ### " + ex.toString());
		}
		
		return resultat;
	}

}
