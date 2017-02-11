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
	
	public FcnExportEtablissement(Connection connMySQL, String strEmplacement, String strPrefixe, DlgExtractions frmParent){
		
		//----- Affectation des paramètres -----
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		prefixe = strPrefixe;
		parent = frmParent;
		
		// --- Préfixes et suffixes utilisés dans la liste récapitulative ---
		partie_a = "UNSA-EDUCATION Montpellier;UNSA-EDUCATION Montpellier;";
		partie_b = ";unsa-education.syndicat@ac-montpellier.fr,lrmp@unsa-education.org,frederic.vaysse@unsa.org,francoise.parrini@se-unsa.org,gilles,tena@gmail.com";
	}
	
	public String start(){
		String resultat = "";
		
		try {
			
			Vector<String> vecRNE = new Vector<String>();
			String rne = "";
			int indice = 0;
			
			//--- SQL ---
			String query_rne = "SELECT uai_occupation, COUNT(*) as nb FROM T_DATA GROUP BY uai_occupation ORDER BY uai_occupation";			
			
			Statement stmt_rne = dbMySQL.createStatement();
			ResultSet rset_rne = stmt_rne.executeQuery(query_rne);
			
			while (rset_rne.next()){
				vecRNE.add(rset_rne.getString(1));
			}
			
			//System.out.println("RNE trouvés : " + vecRNE.size());
			
			parent.updatePbEtablissementValue(0);
			parent.updatePbEtablissementMax(vecRNE.size());
			
			for (int i=0; i < vecRNE.size(); i++){
				rne = vecRNE.get(i);
				System.out.println(i + " / RNE = " + rne);
				
				String query_mail = "SELECT adresse_mail FROM T_DATA WHERE uai_occupation ='" + rne + "' ORDER BY adresse_mail";
				
				Statement stmt_mail = dbMySQL.createStatement();
				ResultSet rset_mail = stmt_mail.executeQuery(query_mail);
				
				
				try {	
					
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + prefixe + rne + ".csv"), "UTF-8"));
					
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
				out_liste.write(partie_a + vecRNE.get(i) + partie_b);
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
