package club.iothings.fonctions;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import club.iothings.ihm.DlgExtractions;

public class FcnExportFiltres {

	private Connection dbMySQL = null;
	private String emplacement = "";
	private DlgExtractions parent = null;
	
	private String departement = "";
	private String grade = "";
	private String type_uai = "";
	private String ccp = "";
	
	public FcnExportFiltres(Connection connMySQL, String strEmplacement, String strDepartement, String strGrade, String strTypeUAI, String strCCP, DlgExtractions frmParent){
		
		//----- Affectation des paramètres -----
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		parent = frmParent;
		
		departement = strDepartement.trim();
		grade = strGrade.trim();
		type_uai = strTypeUAI.trim();
		ccp = strCCP.trim();
	}
	
	public String start(){
		String resultat = "";
		
		try {
			
			String ligne_dep = "";
			String filtre_dep = "";
			
			String ligne_grade = "";
			String filtre_grade = "";
			
			String ligne_typeUAI = "";
			String filtre_typeUAI = "";
			
			String ligne_CCP = "";
			String filtre_CCP = "";
			
			//--- Filtre Grade ---
			if (grade.compareTo("")!=0){
				String splitarray_grade[] = grade.split(";");
				
				String valeur_grade = "";
				
				for (int i=0; i<splitarray_grade.length; i++){
					
					valeur_grade = splitarray_grade[i];
					System.out.println("> " + splitarray_grade[i]);
					
					if (ligne_grade.compareTo("")==0){
						ligne_grade = "'" + valeur_grade + "'";
					} else {
						ligne_grade = ligne_grade + ",'" + valeur_grade + "'";
					}
				}
		
				System.out.println(">> " + ligne_grade);
				filtre_grade = " AND D.grade IN (" + ligne_grade + ")";
			}
			
			// --- Filtre Département ---
			if (departement.compareTo("")!=0){
				String splitarray_dep[] = departement.split(";");
				
				String valeur_dep = "";
				
				for (int i=0; i<splitarray_dep.length; i++){
					
					valeur_dep = splitarray_dep[i];
					System.out.println("> " + splitarray_dep[i]);
					
					if (ligne_dep.compareTo("")==0){
						ligne_dep = "'" + valeur_dep + "'";
					} else {
						ligne_dep = ligne_dep + ",'" + valeur_dep + "'";
					}
				}
				System.out.println(">> " + ligne_dep);
				filtre_dep = " AND U.departement IN (" + ligne_dep + ")";
			}
			
			//--- Filtre TypeUAI ---
			if (type_uai.compareTo("")!=0){
				String splitarray_typeUAI[] = type_uai.split(";");
				
				String valeur_typeUAI = "";
				
				for (int i=0; i<splitarray_typeUAI.length; i++){
					
					valeur_typeUAI = splitarray_typeUAI[i];
					System.out.println("> " + splitarray_typeUAI[i]);
					
					if (ligne_typeUAI.compareTo("")==0){
						ligne_typeUAI = "'" + valeur_typeUAI + "'";
					} else {
						ligne_typeUAI = ligne_typeUAI + ",'" + valeur_typeUAI + "'";
					}
				}
		
				System.out.println(">> " + ligne_typeUAI);
				filtre_typeUAI = " AND D.type_uai IN (" + ligne_typeUAI + ")";
			}
			
			//--- Filtre CCP ---
			if (ccp.compareTo("")!=0){
				String splitarray_CCP[] = ccp.split(";");
				
				String valeur_ccp = "";
				
				for (int i=0; i<splitarray_CCP.length; i++){
					
					valeur_ccp = splitarray_CCP[i];
					System.out.println("> " + splitarray_CCP[i]);
					
					if (ligne_CCP.compareTo("")==0){
						ligne_CCP = "'" + valeur_ccp + "'";
					} else {
						ligne_CCP = ligne_CCP + ",'" + valeur_ccp + "'";
					}
				}
		
				System.out.println(">> " + ligne_CCP);
				filtre_CCP = " AND D.ccp IN (" + ligne_CCP + ")";
			}
			
			
			// --- Query ---
			String query_mail = "SELECT D.adresse_mail FROM T_DATA D, T_UAI U WHERE D.uai_occupation = U.id " + filtre_grade + filtre_dep + filtre_typeUAI + filtre_CCP + " ORDER BY adresse_mail";
			System.out.println(">>> " + query_mail);
			
			Statement stmt_mail = dbMySQL.createStatement();
			ResultSet rset_mail = stmt_mail.executeQuery(query_mail);
			
			try {	
				
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + "TestFiltres" + ".csv"), "UTF-8"));
				
				while (rset_mail.next()){						
					out.write(rset_mail.getString(1));
					out.newLine();
				}
				out.close();
				
			} catch (IOException ioex) {
				System.out.println("### " + ioex.toString());
			}
			
			
			System.out.println("Compilation terminée !");
			
			
//			for (int i=0; i < vecRNE.size(); i++){
//				rne = vecRNE.get(i);
//				System.out.println(i + " / RNE = " + rne);
//				
				
//				
//				
				
//				
//				//----- Mise à jour de la barre de progression -----
//				indice = i+ 1;
//				parent.updatePbEtablissementValue(indice);
//				parent.updatePbEtablissementText(String.valueOf(indice));
//			}
//			
//			
//			// --- Création de la liste récapitulative ---
//			BufferedWriter out_liste = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + "liste.csv"), "UTF-8"));
//			
//			for (int i=0; i < vecRNE.size(); i++){
//				out_liste.write(partie_a + ";" + partie_b + ";" + vecRNE.get(i) + ";" + partie_d);
//				out_liste.newLine();
//			}
//			out_liste.close();
//			
//			
//			resultat = "OK";
			
		} catch (Exception ex){
			resultat = "ERREUR";
			System.out.println("### Erreur FcnExportEtablissement ### " + ex.toString());
		}
		
		return resultat;
	}

}
