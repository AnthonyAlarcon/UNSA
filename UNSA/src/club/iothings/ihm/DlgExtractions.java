package club.iothings.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import club.iothings.fonctions.FcnExportEtablissement;
import club.iothings.fonctions.FcnExportFiltres;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;


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
	private JLabel labCCP = new JLabel();
	
	private JScrollPane scrollGrade = null;
	private JList<String> listGrade = null;
	private DefaultListModel<String> modelGrade = null;
	
	private JTextField tfEmplacement_Cat = null;
	
	
	private JButton btnFiltres = null;
	private JTextField tfDepartement = null;
	private JTextField tfGrade = null;
	private JTextField tfTypeUAI = null;
	private JTextField tfCCP = null;
	
	private JButton btnCategorie = null;
	
	private JButton btnFermer = null;
	
	private JLabel labTitre = null;
	private JLabel labEtablissement = null;
	private JLabel labCategorie = null;
	private JLabel labGrade = null;
	private JLabel labDepartement = null;
	
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
		
		
		remplirListeGrade();
		
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
			
			jContentPane.add(getTfDepartement(), null);
			jContentPane.add(getTfGrade(), null);
			jContentPane.add(getTfTypeUAI(), null);
			jContentPane.add(getTfCCP(), null);
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
			
			labCategorie = new JLabel();
			labCategorie.setBounds(new Rectangle(20, 140, 100, 30));
			labCategorie.setFont(new Font("Arial", Font.PLAIN, 14));
			labCategorie.setText("Catégorie");
			jContentPane.add(labCategorie, null);
			
			labDepartement = new JLabel();
			labDepartement.setBounds(new Rectangle(20, 250, 100, 30));
			labDepartement.setFont(new Font("Arial", Font.PLAIN, 14));
			labDepartement.setText("Departement");
			jContentPane.add(labDepartement, null);
			
			labGrade = new JLabel();
			labGrade.setBounds(new Rectangle(20, 291, 100, 30));
			labGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			labGrade.setText("Grade");
			jContentPane.add(labGrade, null);
			
			labTypeUAI = new JLabel();
			labTypeUAI.setBounds(new Rectangle(403, 250, 70, 30));
			labTypeUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			labTypeUAI.setText("Type UAI");
			jContentPane.add(labTypeUAI, null);
			
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
														
							FcnExportFiltres filtres = new FcnExportFiltres(dbMySQL, "C:/Fichiers_Rectorat/Filtres/", tfDepartement.getText(), tfGrade.getText(), tfTypeUAI.getText(), tfCCP.getText(), DlgExtractions.this);
							filtres.start();
							
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
	
	private JTextField getTfDepartement() {
		if (tfDepartement == null) {
			tfDepartement = new JTextField();
			tfDepartement.setFont(new Font("Arial", Font.PLAIN, 14));
			tfDepartement.setBounds(new Rectangle(130, 251, 200, 30));
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
			tfTypeUAI.setBounds(new Rectangle(483, 251, 200, 30));
		}
		return tfTypeUAI;
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
	
//	private JList<String> getListGrade() {
//		if (listGrade == null) {
//			listGrade = new JList<String>(modelGrade);
//			listGrade.setFont(new Font("Arial", Font.PLAIN, 14));
//			listGrade.setCellRenderer(new CheckboxListRenderer());
//			listGrade.setSelectionMode(ListSelectionModel.);
//		}
//		return listGrade;
//	}
	
	private void remplirListeGrade(){
		try {
			
			//--- SQL ---
			String query_grade = "SELECT id from T_GRADE ORDER BY id";			
			
			Statement stmt_grade = dbMySQL.createStatement();
			ResultSet rset_grade = stmt_grade.executeQuery(query_grade);
			
			while (rset_grade.next()){
				modelGrade.addElement(rset_grade.getString(1));
			}
			
		} catch (Exception ex){
			System.out.println("### " + ex.toString());
		}
	}
}
