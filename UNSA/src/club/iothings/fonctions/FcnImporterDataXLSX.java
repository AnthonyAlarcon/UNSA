package club.iothings.fonctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import club.iothings.modules.ModStoredProcedures;
import club.iothings.structures.StructImportDataCSV;

public class FcnImporterDataXLSX {
	
	private Connection dbMySQL = null;
		
	private StructImportDataCSV imp_csv = null;
	private ModStoredProcedures proc = null;
	
	private long interval;
	
	public FcnImporterDataXLSX(Connection connMySQL){
		
		// --- Affectation des valeurs ---
		dbMySQL = connMySQL;
		
		
		imp_csv = new StructImportDataCSV(dbMySQL);
		proc = new ModStoredProcedures(dbMySQL);
	}
	
	public String start(String strEmplacement){
		
		String resultat = "";
		
		try {
			
			// --- Ouverture du fichier sélectionné ---
			BufferedReader reader = new BufferedReader(new FileReader(strEmplacement));
			int count_lignes = 0;
			
			Vector<String> vecLignes = new Vector<String>();
			String ligne = reader.readLine();
			
			// --- Capture de l'heure de départ ---
			Date date_debut = new Date();
			
			//----- Chargement du fichier en mémoire -----
			while(ligne != null){
				
				// --- Cas #1 : "ANT;CTA" ---
				// ---> Utilisation du caractère de séparation dans les données du fichier. Pré-traitement nécessaire 
				ligne = ligne.replace("ANT;CTA", "ANT-CTA");
														
				vecLignes.add(ligne);
				count_lignes = count_lignes + 1;
				
				ligne = reader.readLine();
			}
			reader.close();
			
			//--- Déclaration des variables temporaires ---
			String nom_usage = "";
			String prenom = "";
			String adresse_mail = "";
			String academie = "";
			String uai_occupation = "";
			String departement = "";
			String type_uai = "";
			String grade = "";
			String ccp = "";
			
			//----- Reset des données antérieures -----
			resultat = proc.sp_Data_SupprimerTout();

			// --- Boucle d'importation ---
			for (int i=0; i < vecLignes.size(); i++){
				
				String splitarray[] = vecLignes.get(i).split(";");
				
				nom_usage = splitarray[imp_csv.getInd_NomUsuel()];	
				prenom = splitarray[imp_csv.getInd_Prenom()];
				adresse_mail = splitarray[imp_csv.getInd_AdresseMail()];
				academie = splitarray[imp_csv.getInd_Academie()];
				uai_occupation = splitarray[imp_csv.getInd_UaiOccupation()];
				departement = IsolerDepartement(uai_occupation);
				type_uai = splitarray[imp_csv.getInd_TypeUai()];
				
				// --- Traitement final du cas #1 : "ANT;CTA" ---
				grade = splitarray[imp_csv.getInd_Grade()];
				grade = grade.replaceAll("\"", "");

				ccp = splitarray[imp_csv.getInd_Ccp()];
				

				//----- ByPass de la ligne d'entête du fichier CSV -----
				if (i > 0){
					
					// --- T_DATA ---
					resultat = proc.sp_Data_Ajouter(i, nom_usage, prenom, adresse_mail, academie, uai_occupation, grade, ccp);
					
					// --- T_GRADE ---
					proc.sp_Grade_Ajouter(i, grade, "VIDE", "VIDE");
					
					// --- T_UAI ---
					proc.sp_UAI_ajouter(i, uai_occupation, "VIDE", departement, academie, type_uai, "VIDE", "VIDE");
					
					// --- T_DEPARTEMENT ---
					proc.sp_Departement_ajouter(i, departement, "VIDE");
					
				}

			}
			
			// --- Capture de l'heure de fin ---
			Date date_fin = new Date();
			
			// --- Calcul de la durée du traitement en minutes ---
			interval = date_fin.getTime() - date_debut.getTime();
			interval = interval / 1000 / 60;
						
			resultat = "OK";			
			
		} catch (Exception ex) {
			resultat = "ERREUR";
			System.out.println("### FcnImporterData ### start ### " + ex.toString());
		}
				
		return resultat;
	}
	
	private String IsolerDepartement (String strInput){
		
		String resultat = "";
		
		try {
			
			if (strInput.length()>=3){
				resultat = strInput.substring(0,3);
			} else {
				resultat = "ERREUR";
			}
			
		} catch (Exception ex) {
			System.out.println("### FcnImporterData ### IsolerDepartement ### " + ex.toString());
		}
		
		return resultat;
	}
	
	public long getInterval() {
		return interval;
	}

}
