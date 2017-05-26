package club.iothings.fonctions;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import club.iothings.ihm.DlgExtractions;
import club.iothings.modules.ModFonctions;

public class FcnExportFiltres {

	private Connection dbMySQL = null;
	private String emplacement = "";
	private String nom_fichier = "";
	private DlgExtractions parent = null;
	
	private ModFonctions fct = null;
	
	private String departement = "";
	private String grade = "";
	private String type_uai = "";
	private String ccp = "";
	
	
	public FcnExportFiltres(Connection connMySQL, String strEmplacement, String strNomFichier, String strDepartement, String strGrade, String strTypeUAI, String strCCP, DlgExtractions frmParent){
		
		//----- Affectation des paramètres -----
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		nom_fichier = strNomFichier;
		parent = frmParent;
		fct = new ModFonctions();
		
		departement = strDepartement.trim();
		grade = strGrade.trim();
		type_uai = strTypeUAI.trim();
		ccp = strCCP.trim();
	}
	
	public String start(){
		String resultat = "";
		
		try {
			
			String filtre_dep = "";
			String filtre_grade = "";
			String filtre_typeUAI = "";
			String filtre_CCP = "";
			
			
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
			
			
			// --- Query ---
			String query_mail = "SELECT D.adresse_mail FROM T_DATA D, T_UAI U WHERE D.uai_occupation = U.id " + filtre_grade + filtre_dep + filtre_typeUAI + filtre_CCP + " ORDER BY adresse_mail";
			
			System.out.println("<FcnExportFiltres><start> Query " + query_mail);
			
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
				
				System.out.println("<FcnExportFiltres><start> Compilation OK");
				
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
