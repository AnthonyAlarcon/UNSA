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
	
	public FcnExportEtablissement(Connection connMySQL, String strEmplacement, String strPrefixe, DlgExtractions frmParent){
		
		//----- Affectation des paramètres -----
		dbMySQL = connMySQL;
		emplacement = strEmplacement;
		prefixe = strPrefixe;
		parent = frmParent;
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
			
			resultat = "OK";
			
		} catch (Exception ex){
			resultat = "ERREUR";
			System.out.println("### Erreur FcnExportEtablissement ### " + ex.toString());
		}
		
		return resultat;
	}
}
