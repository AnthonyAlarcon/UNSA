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


public class DlgExtractions extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextField tfEmplacement_Etab = null;
	private JButton btnEtablissement = null;
	private JButton btnFermer = null;
	
	private JLabel labTitre = null;
	private JLabel labEtablissement = null;
	
	private JProgressBar progressEtablissement = null;
	
	private Connection dbMySQL = null;
	
	private boolean process_running = false;	
	
	
	public DlgExtractions(Connection connMySQL) {
		super();
		initialize();
		
		//----- Affectation de la connection à la base de données -----
		dbMySQL = connMySQL;
		
		//----- Répertoire de destination des fichiers compilés -----
		tfEmplacement_Etab.setText("C:/UNSA/Etablissements/");
	}
	
	private void initialize() {
		this.setSize(800, 480);
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
							
							FcnExportEtablissement etab = new FcnExportEtablissement(dbMySQL, tfEmplacement_Etab.getText(), prefixe, DlgExtractions.this);
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
	
	private JButton getBtnFermer() {
		if (btnFermer == null) {			
			btnFermer = new JButton("Fermer");
			btnFermer.setFont(new Font("Arial", Font.PLAIN, 14));
			btnFermer.setBounds(new Rectangle(631, 411, 153, 30));
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
}
