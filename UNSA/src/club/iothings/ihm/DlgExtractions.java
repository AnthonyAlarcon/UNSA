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
		
	private JTextField tfEmplacement_Cat = null;
	private JButton btnCategorie = null;
	
	private JButton btnFermer = null;
	
	private JLabel labTitre = null;
	private JLabel labEtablissement = null;
	private JLabel labCategorie = null;
	
	private JProgressBar progressEtablissement = null;
	private JProgressBar progressCategorie = null;
	
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
		this.setSize(938, 610);
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
			
			jContentPane.add(getTfEmplacement_Cat(), null);
			jContentPane.add(getBtnCategorie(), null);
			jContentPane.add(getProgressCategorie(), null);
			
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
			
			labCategorie = new JLabel();
			labCategorie.setBounds(new Rectangle(20, 140, 100, 30));
			labCategorie.setFont(new Font("Arial", Font.PLAIN, 14));
			labCategorie.setText("Catégorie");
			jContentPane.add(labCategorie, null);
			
			scrollPartieA = new JScrollPane();
			scrollPartieA.setBounds(130, 240, 290, 70);
			scrollPartieA.setViewportView(getTaPartieA());
			jContentPane.add(scrollPartieA);
			
			scrollPartieB = new JScrollPane();
			scrollPartieB.setBounds(130, 321, 290, 70);
			scrollPartieB.setViewportView(getTaPartieB());
			jContentPane.add(scrollPartieB);
			
			scrollPartieC = new JScrollPane();
			scrollPartieC.setBounds(600, 240, 290, 70);
			scrollPartieC.setViewportView(getTaPartieC());
			jContentPane.add(scrollPartieC);
			
			scrollPartieD = new JScrollPane();
			scrollPartieD.setBounds(600, 321, 290, 70);
			scrollPartieD.setViewportView(getTaPartieD());
			jContentPane.add(scrollPartieD);
			
			labPartieA = new JLabel();
			labPartieA.setText("Partie A");
			labPartieA.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieA.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieA.setBounds(20, 240, 100, 30);
			jContentPane.add(labPartieA);
			
			labPartieB = new JLabel();
			labPartieB.setText("Partie B");
			labPartieB.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieB.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieB.setBounds(20, 318, 100, 30);
			jContentPane.add(labPartieB);
			
			labPartieC = new JLabel();
			labPartieC.setText("Partie C");
			labPartieC.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieC.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieC.setBounds(483, 240, 100, 30);
			jContentPane.add(labPartieC);
			
			labPartieD = new JLabel();
			labPartieD.setText("Partie D");
			labPartieD.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieD.setBounds(new Rectangle(20, 140, 100, 30));
			labPartieD.setBounds(483, 321, 100, 30);
			jContentPane.add(labPartieD);
			
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
									
							String prefixe = "liste.UNSA EDUCATION Montpellier.";
							
							process_running = true;
							btnEtablissement.setEnabled(false);
							tfEmplacement_Etab.setEnabled(false);
							
							String partieA = taPartieA.getText();
							String partieB = taPartieB.getText();
							String partieC = taPartieC.getText();
							String partieD = taPartieD.getText();
							
							FcnExportEtablissement etab = new FcnExportEtablissement(dbMySQL, tfEmplacement_Etab.getText(), partieA, partieB, partieC, partieD, DlgExtractions.this);
							etab.start();
							
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
	
	private JButton getBtnCategorie() {
		if (btnCategorie == null) {			
			btnCategorie = new JButton("Compiler");
			btnCategorie.setFont(new Font("Arial", Font.PLAIN, 14));
			btnCategorie.setBounds(new Rectangle(430, 140, 153, 30));
			btnCategorie.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					Thread thread_export_categorie = new Thread() {
						public void run() {
									
//							String prefixe = "liste.UNSA EDUCATION Montpellier.";
//							
//							process_running = true;
//							btnEtablissement.setEnabled(false);
//							tfEmplacement_Etab.setEnabled(false);
//							
//							FcnExportEtablissement etab = new FcnExportEtablissement(dbMySQL, tfEmplacement_Cat.getText(), prefixe, DlgExtractions.this);
//							etab.start();
//							
//							btnEtablissement.setEnabled(true);
//							tfEmplacement_Etab.setEnabled(true);
//							process_running = false;
						}
					};
					
					//----- Lancement du thread -----
					thread_export_categorie.setDaemon(true);
					thread_export_categorie.start();
				}
			});
		}
		return btnCategorie;
	}
	
	private JButton getBtnFermer() {
		if (btnFermer == null) {			
			btnFermer = new JButton("Fermer");
			btnFermer.setFont(new Font("Arial", Font.PLAIN, 14));
			btnFermer.setBounds(new Rectangle(769, 541, 153, 30));
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
	
	private JTextField getTfEmplacement_Cat() {
		if (tfEmplacement_Cat == null) {
			tfEmplacement_Cat = new JTextField();
			tfEmplacement_Cat.setFont(new Font("Arial", Font.PLAIN, 14));
			tfEmplacement_Cat.setBounds(new Rectangle(130, 141, 290, 30));
		}
		return tfEmplacement_Cat;
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
	
	private JProgressBar getProgressCategorie() {
		if (progressCategorie == null) {
			progressCategorie = new JProgressBar();
			progressCategorie.setFont(new Font("Arial", Font.PLAIN, 14));
			progressCategorie.setBounds(600, 140, 150, 30);
			progressCategorie.setStringPainted(true);
			progressCategorie.setString("%");
			progressCategorie.setForeground(Color.getHSBColor(0.5833f, 0.80f, 1.00f));
		}
		return progressCategorie;
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
