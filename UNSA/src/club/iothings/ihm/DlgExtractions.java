package club.iothings.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import club.iothings.fonctions.FcnExportEtablissement;
import club.iothings.fonctions.FcnExportFiltres;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class DlgExtractions extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextField tfEmplacement_Etab = null;
	private JButton btnEtablissement = null;
	
	private JScrollPane scrollPartieA = null;
	private JScrollPane scrollPartieB = null;
	private JScrollPane scrollPartieC = null;
	private JScrollPane scrollPartieD = null;
	private JTextArea taPartieA = null;
	private JTextArea taPartieB = null;
	private JTextArea taPartieC = null;
	private JTextArea taPartieD = null;
	private JLabel labPartieA = new JLabel();
	private JLabel labPartieB = new JLabel();
	private JLabel labPartieC = new JLabel();
	private JLabel labPartieD = new JLabel();
	
	private JLabel labTypeUAI = new JLabel();
	private JLabel labGroupe = new JLabel();
	private JLabel labCCP = new JLabel();
		
	private JButton btnFiltres = null;
	private JTextField tfDepartement = null;
	private JTextField tfGrade = null;
	private JTextField tfTypeUAI = null;
	private JTextField tfGroupe = null;
	private JTextField tfCCP = null;
	
	private JButton btnFermer = null;
	
	private JLabel labTitre = null;
	private JLabel labEtablissement = null;
	private JLabel labGrade = null;
	private JLabel labDepartement = null;
	
	private JProgressBar progressEtablissement = null;
	
	private Connection dbMySQL = null;
	
	private boolean process_running = false;	
	
	
	public DlgExtractions(Connection connMySQL) {
		super();
		initialize();
		
		//----- Affectation de la connection à la base de données -----
		dbMySQL = connMySQL;
		
		//----- Répertoire de destination des fichiers compilés -----
		tfEmplacement_Etab.setText("C:/Fichiers_Rectorat/Etablissements/");
				
	}
	
	private void initialize() {
		this.setSize(970, 610);
		this.setContentPane(getJContentPane());
		this.setTitle("Extractions - UNSA");
		this.setResizable(false);
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTfEmplacement_Etab(), null);
			jContentPane.add(getBtnEtablissement(), null);
			jContentPane.add(getProgressEtablissement(), null);
			
			jContentPane.add(getTfDepartement(), null);
			jContentPane.add(getTfGrade(), null);
			jContentPane.add(getTfTypeUAI(), null);
			jContentPane.add(getTfCCP(), null);
			jContentPane.add(getTfGroupe(), null);
			jContentPane.add(getBtnFiltres(), null);
			jContentPane.add(getBtnFermer(), null);
						
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("Extraction des données");
			jContentPane.add(labTitre, null);
			
			labEtablissement = new JLabel();
			labEtablissement.setBounds(new Rectangle(20, 100, 100, 30));
			labEtablissement.setFont(new Font("Arial", Font.PLAIN, 14));
			labEtablissement.setText("Etablissements");
			jContentPane.add(labEtablissement, null);
			
			labDepartement = new JLabel();
			labDepartement.setBounds(new Rectangle(20, 141, 100, 30));
			labDepartement.setFont(new Font("Arial", Font.PLAIN, 14));
			labDepartement.setText("Departement");
			jContentPane.add(labDepartement, null);
			
			labGrade = new JLabel();
			labGrade.setBounds(new Rectangle(20, 291, 100, 30));
			labGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			labGrade.setText("Grade");
			jContentPane.add(labGrade, null);
			
			labTypeUAI = new JLabel();
			labTypeUAI.setBounds(new Rectangle(340, 141, 70, 30));
			labTypeUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			labTypeUAI.setText("Type UAI");
			jContentPane.add(labTypeUAI, null);
			
			labGroupe = new JLabel();
			labGroupe.setBounds(new Rectangle(642, 141, 70, 30));
			labGroupe.setFont(new Font("Arial", Font.PLAIN, 14));
			labGroupe.setText("Groupe");
			jContentPane.add(labGroupe, null);
			
			labCCP = new JLabel();
			labCCP.setBounds(new Rectangle(403, 291, 70, 30));
			labCCP.setFont(new Font("Arial", Font.PLAIN, 14));
			labCCP.setText("CCP");
			jContentPane.add(labCCP, null);
			
			scrollPartieA = new JScrollPane();
			scrollPartieA.setBounds(130, 367, 290, 70);
			scrollPartieA.setViewportView(getTaPartieA());
			jContentPane.add(scrollPartieA);
			
			scrollPartieB = new JScrollPane();
			scrollPartieB.setBounds(130, 448, 290, 70);
			scrollPartieB.setViewportView(getTaPartieB());
			jContentPane.add(scrollPartieB);
			
			scrollPartieC = new JScrollPane();
			scrollPartieC.setBounds(600, 367, 290, 70);
			scrollPartieC.setViewportView(getTaPartieC());
			jContentPane.add(scrollPartieC);
			
			scrollPartieD = new JScrollPane();
			scrollPartieD.setBounds(600, 448, 290, 70);
			scrollPartieD.setViewportView(getTaPartieD());
			jContentPane.add(scrollPartieD);
			
			labPartieA = new JLabel();
			labPartieA.setText("Partie A");
			labPartieA.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieA.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieA.setBounds(20, 367, 100, 30);
			jContentPane.add(labPartieA);
			
			labPartieB = new JLabel();
			labPartieB.setText("Partie B");
			labPartieB.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieB.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieB.setBounds(20, 445, 100, 30);
			jContentPane.add(labPartieB);
			
			labPartieC = new JLabel();
			labPartieC.setText("Partie C");
			labPartieC.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieC.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieC.setBounds(483, 367, 100, 30);
			jContentPane.add(labPartieC);
			
			labPartieD = new JLabel();
			labPartieD.setText("Partie D");
			labPartieD.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieD.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieD.setBounds(483, 448, 100, 30);
			jContentPane.add(labPartieD);
			
			// --- Déclaration du modèle ---
//			modelGrade = new DefaultListModel<String>();
//			
//			scrollGrade = new JScrollPane();
//			scrollGrade.setBounds(130, 247, 290, 70);
//			scrollGrade.setViewportView(getListGrade());
//			jContentPane.add(scrollGrade);

		}
		return jContentPane;
	}
	
	private JButton getBtnEtablissement() {
		if (btnEtablissement == null) {			
			btnEtablissement = new JButton("Compiler");
			btnEtablissement.setFont(new Font("Arial", Font.PLAIN, 14));
			btnEtablissement.setBounds(new Rectangle(430, 100, 153, 30));
			btnEtablissement.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					Thread thread_export_etablissement = new Thread() {
						public void run() {
							
							process_running = true;
							btnEtablissement.setEnabled(false);
							tfEmplacement_Etab.setEnabled(false);
							
							String partieA = taPartieA.getText();
							String partieB = taPartieB.getText();
							String partieC = taPartieC.getText();
							String partieD = taPartieD.getText();
							
							// --- Filtres ---
							String filtre_departement = tfDepartement.getText();
							String filtre_type_uai = tfTypeUAI.getText();
							String filtre_groupe = tfGroupe.getText();
							
							FcnExportEtablissement etab = new FcnExportEtablissement(dbMySQL, tfEmplacement_Etab.getText(), partieA, partieB, partieC, partieD, DlgExtractions.this);
							etab.start(filtre_departement, filtre_type_uai, filtre_groupe);
							
							btnEtablissement.setEnabled(true);
							tfEmplacement_Etab.setEnabled(true);
							process_running = false;
						}
					};
					
					//----- Lancement du thread -----
					thread_export_etablissement.setDaemon(true);
					thread_export_etablissement.start();
				}
			});
		}
		return btnEtablissement;
	}
	
	private JButton getBtnFiltres() {
		if (btnFiltres == null) {			
			btnFiltres = new JButton("Filtres");
			btnFiltres.setFont(new Font("Arial", Font.PLAIN, 14));
			btnFiltres.setBounds(new Rectangle(737, 250, 153, 72));
			btnFiltres.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					Thread thread_export_categorie = new Thread() {
						public void run() {
							
							process_running = true;
							btnFiltres.setEnabled(false);
							tfDepartement.setEnabled(false);
							tfGrade.setEnabled(false);
							tfTypeUAI.setEnabled(false);
							tfCCP.setEnabled(false);
														
							FcnExportFiltres filtres = new FcnExportFiltres(dbMySQL, "C:/Fichiers_Rectorat/Filtres/");
							filtres.start("ExportFiltres", tfDepartement.getText(), tfGrade.getText(), tfTypeUAI.getText(), tfCCP.getText(), "", "");
							
							btnFiltres.setEnabled(true);
							tfDepartement.setEnabled(true);
							tfGrade.setEnabled(true);
							tfTypeUAI.setEnabled(true);
							tfCCP.setEnabled(true);
							process_running = false;
						}
					};
					
					//----- Lancement du thread -----
					thread_export_categorie.setDaemon(true);
					thread_export_categorie.start();
				}
			});
		}
		return btnFiltres;
	}
	
	private JButton getBtnFermer() {
		if (btnFermer == null) {			
			btnFermer = new JButton("Fermer");
			btnFermer.setFont(new Font("Arial", Font.PLAIN, 14));
			btnFermer.setBounds(new Rectangle(801, 541, 153, 30));
			btnFermer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					if (process_running == true){
						Object[] options = {"Oui", "Non"};
						
						int reponse = JOptionPane.showOptionDialog(DlgExtractions.this, "Un processus est actuellement actif. La fermeture de cette fenêtre entrera celle de l'application. Confirmer ?", "Extraction - UNSA", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						
						//----- Confirmation de la fermeture ------
						if (reponse==0){														
							System.exit(0);
						}
					} else {
						DlgExtractions.this.dispose();
					}
				}
			});
		}
		return btnFermer;
	}

	private JTextField getTfEmplacement_Etab() {
		if (tfEmplacement_Etab == null) {
			tfEmplacement_Etab = new JTextField();
			tfEmplacement_Etab.setFont(new Font("Arial", Font.PLAIN, 14));
			tfEmplacement_Etab.setBounds(new Rectangle(130, 100, 290, 30));
		}
		return tfEmplacement_Etab;
	}
	
	private JTextField getTfDepartement() {
		if (tfDepartement == null) {
			tfDepartement = new JTextField();
			tfDepartement.setFont(new Font("Arial", Font.PLAIN, 14));
			tfDepartement.setBounds(new Rectangle(130, 142, 200, 30));
			tfDepartement.setOpaque(true);
			tfDepartement.setBackground(Color.YELLOW);
		}
		return tfDepartement;
	}
	
	private JTextField getTfGrade() {
		if (tfGrade == null) {
			tfGrade = new JTextField();
			tfGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			tfGrade.setBounds(new Rectangle(130, 292, 200, 30));
		}
		return tfGrade;
	}
	
	private JTextField getTfTypeUAI() {
		if (tfTypeUAI == null) {
			tfTypeUAI = new JTextField();
			tfTypeUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			tfTypeUAI.setBounds(new Rectangle(420, 142, 200, 30));
			tfTypeUAI.setOpaque(true);
			tfTypeUAI.setBackground(Color.YELLOW);
		}
		return tfTypeUAI;
	}
	
	private JTextField getTfGroupe() {
		if (tfGroupe == null) {
			tfGroupe = new JTextField();
			tfGroupe.setFont(new Font("Arial", Font.PLAIN, 14));
			tfGroupe.setBounds(new Rectangle(722, 141, 200, 30));
			tfGroupe.setOpaque(true);
			tfGroupe.setBackground(Color.YELLOW);
		}
		return tfGroupe;
	}
	
	private JTextField getTfCCP() {
		if (tfCCP == null) {
			tfCCP = new JTextField();
			tfCCP.setFont(new Font("Arial", Font.PLAIN, 14));
			tfCCP.setBounds(new Rectangle(483, 292, 200, 30));
		}
		return tfCCP;
	}
	
	
	private JProgressBar getProgressEtablissement() {
		if (progressEtablissement == null) {
			progressEtablissement = new JProgressBar();
			progressEtablissement.setFont(new Font("Arial", Font.PLAIN, 14));
			progressEtablissement.setBounds(600, 100, 150, 30);
			progressEtablissement.setStringPainted(true);
			progressEtablissement.setString("%");
			progressEtablissement.setForeground(Color.getHSBColor(0.5833f, 0.80f, 1.00f));
		}
		return progressEtablissement;
	}
	
	public void updatePbEtablissementMax(int valeurMax){
		SwingUtilities.invokeLater(
			    new Runnable(){
			        public void run(){
			        	progressEtablissement.setMaximum(valeurMax);
			        }
			    }
			);
	}
	
	public void updatePbEtablissementValue(int valeur){
		SwingUtilities.invokeLater(
			    new Runnable(){
			        public void run(){
			        	progressEtablissement.setValue(valeur);
			        }
			    }
			);
	}
	
	public void updatePbEtablissementText(String valeur){
		SwingUtilities.invokeLater(
			    new Runnable(){
			        public void run(){
			        	progressEtablissement.setString(valeur);
			        }
			    }
			);
	}
	
	private JTextArea getTaPartieA() {
		if (taPartieA == null) {
			taPartieA = new JTextArea();
			taPartieA.setText("UNSA Montpellier");
		}
		return taPartieA;
	}
	
	private JTextArea getTaPartieB() {
		if (taPartieB == null) {
			taPartieB = new JTextArea();
			taPartieB.setText("UNSA-EDUCATION Montpellier");
		}
		return taPartieB;
	}
	
	private JTextArea getTaPartieC() {
		if (taPartieC == null) {
			taPartieC = new JTextArea();
			taPartieC.setText("");
		}
		return taPartieC;
	}
	
	private JTextArea getTaPartieD() {
		if (taPartieD == null) {
			taPartieD = new JTextArea();
			taPartieD.setText("unsa-education.syndicat@ac-montpellier.fr,lrmp@unsa-education.org,frederic.vaysse@unsa.org,francoise.parrini@se-unsa.org,gilles,tena@gmail.com");
		}
		return taPartieD;
	}
	
}
