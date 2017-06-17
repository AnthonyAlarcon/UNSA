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

public class FcnExportEtablissement {

	private Connection dbMySQL = null;
	private String emplacement = "";
	private String prefixe = "";
	private DlgExtractions parent = null;
	
	private String partie_a = "";
	private String partie_b = "";
	private String partie_c = "";
	private String partie_d = "";
	
	public FcnExportEtablissement(Connection connMySQL, String strEmplacement, String strA, String strB, String strC, String strD, DlgExtractions frmParent){
		
		//----- Affectation des paramètres -----
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		parent = frmParent;
		
		// --- Préfixes et suffixes utilisés dans la liste récapitulative ---
		partie_a = strA;
		partie_b = strB;
		partie_c = strC;
		partie_d = strD;
		
		// --- Initialisation du préfixe utilisé dans nom de chaque fichier ---
		prefixe = "liste." + partie_b + ".";
		
	}
	
	public String start(String strDepartement, String strTypeUAI, String strGroupe){
		String resultat = "";
		
		try {
			
			Vector<String> vecRNE = new Vector<String>();
			String rne = "";
			
			Vector<String> vecVilleEtab = new Vector<String>();
			String ville_etab = "";
			
			int indice = 0;
			
			// --- Filtre Département ---
			String compil_filtres = "";
			
			if (strDepartement.compareTo("")!=0){
				compil_filtres = "WHERE departement = '" + strDepartement + "'";
			}
			
			if (strTypeUAI.compareTo("")!=0){
				if (compil_filtres.compareTo("")!=0){
					compil_filtres = compil_filtres + " AND type_uai = '" + strTypeUAI + "'";
				} else {
					compil_filtres = "WHERE type_uai = '" + strTypeUAI + "'";
				}
			}
			
			if (strGroupe.compareTo("")!=0){
				if (compil_filtres.compareTo("")!=0){
					compil_filtres = compil_filtres + " AND groupe LIKE '" + strGroupe.replace("*", "%") + "'";
				} else {
					compil_filtres = "WHERE groupe = '" + strGroupe + "'";
				}
			}
			
			//--- SQL ---
			String query_rne = "SELECT id, ville, nom FROM T_UAI " + compil_filtres + " GROUP BY id ORDER BY id";	
			
			System.out.print(">> " + query_rne);
			
			Statement stmt_rne = dbMySQL.createStatement();
			ResultSet rset_rne = stmt_rne.executeQuery(query_rne);
			
			while (rset_rne.next()){
				vecRNE.add(rset_rne.getString(1));
				vecVilleEtab.add(rset_rne.getString(2) + "_" + rset_rne.getString(3));
			}
			
			System.out.print(">> " + vecRNE.size());
			
			parent.updatePbEtablissementValue(0);
			parent.updatePbEtablissementMax(vecRNE.size());
			
			for (int i=0; i < vecRNE.size(); i++){
				rne = vecRNE.get(i);
				ville_etab = vecVilleEtab.get(i);
				
				System.out.println(i + " / RNE = " + rne);
				
				String query_mail = "SELECT adresse_mail FROM T_DATA WHERE uai_occupation ='" + rne + "' ORDER BY adresse_mail";
				
				Statement stmt_mail = dbMySQL.createStatement();
				ResultSet rset_mail = stmt_mail.executeQuery(query_mail);
				
				
				try {	
					
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + prefixe + rne + "_" + ville_etab + ".csv"), "UTF-8"));
					
					while (rset_mail.next()){						
						out.write(rset_mail.getString(1));
						out.newLine();
					}
					out.close();
					
				} catch (IOException ioex) {
					System.out.println("### " + ioex.toString());
				}
				
				//----- Mise à jour de la barre de progression -----
				indice = i+ 1;
				parent.updatePbEtablissementValue(indice);
				parent.updatePbEtablissementText(String.valueOf(indice));
			}
			
			
			// --- Création de la liste récapitulative ---
			BufferedWriter out_liste = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + "liste.csv"), "UTF-8"));
			
			for (int i=0; i < vecRNE.size(); i++){
				out_liste.write(partie_a + ";" + partie_b + ";" + vecRNE.get(i) + "_" + vecVilleEtab.get(i) + ";" + partie_d);
				out_liste.newLine();
			}
			out_liste.close();
			
			
			resultat = "OK";
			
		} catch (Exception ex){
			resultat = "ERREUR";
			System.out.println("### Erreur FcnExportEtablissement ### " + ex.toString());
		}
		
		return resultat;
	}
}
