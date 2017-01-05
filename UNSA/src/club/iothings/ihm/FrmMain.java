package club.iothings.ihm;

import club.iothings.structures.StructImportCSV;
import club.iothings.data.DataConnexion;
import club.iothings.modules.ModStoredProcedures;
import club.iothings.modules.MonFiltre;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public class FrmMain extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnConnexion = null;
	private JButton btnImporter = null;
	private JButton btnExtractions = null;
	
	private JLabel labTitre = null;
	private JLabel labProgression = null;
	
	private JScrollPane scrollMessages = null;
	private JList<String> listMessages = null;
	private DefaultListModel<String> modele_messages = null;
	
	private Connection dbMySQL = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FrmMain thisClass = new FrmMain();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setLocationRelativeTo(null);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmMain() {
		super();
		initialize();
		
	}
	
	private void initialize() {
		this.setSize(900, 600);
		this.setContentPane(getJContentPane());
		this.setTitle("UNSA");
		this.setResizable(false);
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnConnexion(), null);
			jContentPane.add(getBtnImporter(), null);
			jContentPane.add(getBtnExtraction(), null);
			jContentPane.add(getScrollMessages(), null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("UNSA");
			jContentPane.add(labTitre, null);
			
			labProgression= new JLabel();
			labProgression.setBounds(new Rectangle(640, 110, 140, 40));
			labProgression.setFont(new Font("Arial", Font.PLAIN, 14));
			labProgression.setText("");
			jContentPane.add(labProgression, null);
		}
		return jContentPane;
	}
	
	private JScrollPane getScrollMessages() {
		if (scrollMessages == null) {
			scrollMessages = new JScrollPane();
			scrollMessages.setBounds(new Rectangle(10, 161, 874, 339));
			scrollMessages.setViewportView(getListMessages());
		}
		return scrollMessages;
	}
	
	private JList<String> getListMessages() {
		if (listMessages == null) {
			listMessages = new JList<String>();
			listMessages.setBackground(Color.white);
			listMessages.setForeground(Color.black);
			
			listMessages.setFont(new Font("Arial", 0, 14));
			
			modele_messages = new DefaultListModel<String>();
			listMessages.setModel(modele_messages);
		}
		return listMessages;
	}
	
	private JButton getBtnConnexion() {
		if (btnConnexion == null) {			
			btnConnexion = new JButton("Connexion");
			btnConnexion.setFont(new Font("Arial", Font.PLAIN, 14));
			btnConnexion.setBounds(new Rectangle(10, 511, 200, 50));
			btnConnexion.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					DataConnexion data = new DataConnexion();
					String test_connexion = data.start();
					
					if (test_connexion.compareTo("OK")==0){
						dbMySQL = data.getDbMySQL();
						
						//----- Verrouillage & déverrouillage des boutons -----
						btnConnexion.setEnabled(false);
						btnImporter.setEnabled(true);
						btnExtractions.setEnabled(true);
						
						addLogView("Connexion à la base de données " + data.getDbName() + " : OK");
						
						//JOptionPane.showMessageDialog(FrmMain.this, "Connexion établie avec la base de données", "UNSA", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(FrmMain.this, "Echec de la connexion à la base de données a échoué", "UNSA - Erreur", JOptionPane.ERROR_MESSAGE);
						addLogView("Connexion à la base de données " + data.getDbName() + " : ECHEC");
					}
				}
			});
		}
		return btnConnexion;
	}
	
	private JButton getBtnExtraction() {
		if (btnExtractions == null) {			
			btnExtractions = new JButton("Extractions");
			btnExtractions.setFont(new Font("Arial", Font.PLAIN, 14));
			btnExtractions.setBounds(new Rectangle(684, 511, 200, 50));
			btnExtractions.setEnabled(false);
			btnExtractions.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {			
					
					DlgExtractions extracts = new DlgExtractions(dbMySQL);
					extracts.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					extracts.setModalityType(ModalityType.APPLICATION_MODAL);
					extracts.setLocationRelativeTo(null);
					extracts.setVisible(true);
				}
			});
		}
		return btnExtractions;
	}
	
	private JButton getBtnImporter() {
		if (btnImporter == null) {			
			btnImporter = new JButton("Importer");
			btnImporter.setFont(new Font("Arial", Font.PLAIN, 14));
			btnImporter.setBounds(new Rectangle(220, 511, 200, 50));
			btnImporter.setEnabled(false);
			btnImporter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {								
					
					Thread thread_importer = new Thread() {
						public void run() {
							
							try {
								btnImporter.setEnabled(false);
								
								StructImportCSV imp_csv = new StructImportCSV(dbMySQL);
								ModStoredProcedures proc = new ModStoredProcedures(dbMySQL);
								
								
								String critere_academie = "11";
								
								
								//----- Ouverture de la fenêtre de sélection du fichier -----
								JFileChooser fc = new JFileChooser();
								fc.setAcceptAllFileFilterUsed(false);					
								fc.setPreferredSize(new Dimension(600,400));
								
								MonFiltre filtreTXT = new MonFiltre(new String[]{"csv","CSV"}, "Fichier CSV");
								fc.addChoosableFileFilter(filtreTXT);
								
								int returnVal = fc.showOpenDialog(FrmMain.this);
								
								//----- L'utilisateur approuve le fichier sélectionné -----
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									
									String emplacement = fc.getSelectedFile().getPath();
									
									BufferedReader reader = new BufferedReader(new FileReader(emplacement));
									int count_lignes = 0;
									
									Vector<String> vecLignes = new Vector<String>();
									String ligne = reader.readLine();
									
									Date date_debut = new Date();
									System.out.println("Date Début = " + date_debut);
									
									//----- Chargement du fichier en mémoire -----
									while(ligne != null){
										
										vecLignes.add(ligne);
										count_lignes = count_lignes + 1;
										
										ligne = reader.readLine();
									}
									reader.close();
									
									addLogView("Lecture du fichier " + emplacement + " : OK");
									addLogView("Nombre de lignes trouvées : " + vecLignes.size());
									
									//--- Déclaration des variables temporaires ---
									String nom_usage = "";
									String prenom = "";
									String adresse_mail = "";
									String academie = "";
									String uai_occupation = "";
									String type_uai = "";
									String grade = "";
									String ccp = "";
									
									String resultat = "";
									
									//----- Reset des données antérieures -----
									resultat = proc.sp_Data_Supprimer_Academie(critere_academie);
									addLogView("Reset des données antérieures (académie = " + critere_academie + ") : " + resultat);
									
									addLogView("Traitement des données...");
													
									for (int i=0; i < vecLignes.size(); i++){
										
										String splitarray[] = vecLignes.get(i).split(";");
										
										nom_usage = splitarray[imp_csv.getInd_NomUsuel()];
										//System.out.println(">> " + nom_usage);
										
										prenom = splitarray[imp_csv.getInd_Prenom()];
										//System.out.println(">> " + prenom);
										
										adresse_mail = splitarray[imp_csv.getInd_AdresseMail()];
										//System.out.println(">> " + adresse_mail);
										
										academie = splitarray[imp_csv.getInd_Academie()];
										//System.out.println(">> " + academie);
										
										uai_occupation = splitarray[imp_csv.getInd_UaiOccupation()];
										//System.out.println(">> " + uai_occupation);
										
										type_uai = splitarray[imp_csv.getInd_TypeUai()];
										//System.out.println(">> " + type_uai);
										
										grade = splitarray[imp_csv.getInd_Grade()];
										//System.out.println(">> " + grade);
										
										ccp = splitarray[imp_csv.getInd_Ccp()];
										//System.out.println(">> " + ccp);
						
										//----- ByPass de la ligne d'entête du fichier CSV -----
										if (i > 0){
											resultat = proc.sp_Data_Ajouter(nom_usage, prenom, adresse_mail, academie, uai_occupation, type_uai, grade, ccp);
											
											if (resultat.compareTo("OK")!=0){
												System.out.println("Erreur I= " + i);
											}
										}
										
										//----- Mise à jour du témoin de progression -----
										updateProgress(i + 1, vecLignes.size());
									}
									
									
									Date date_fin = new Date();
									System.out.println("Date Fin = " + date_fin);
									
									long interval = date_fin.getTime() - date_debut.getTime();
									
									interval = interval / 1000 / 60;
									
									System.out.println("[" + count_lignes + "] Durée = " + String.valueOf(interval) + " minutes");
									
									addLogView("Importation terminée en " + String.valueOf(interval) + " minutes");
								}
								
								btnImporter.setEnabled(true);
								
							} catch (Exception ex){
								System.out.println("### Erreur ### " + ex.toString());
							}						
						}
					};
					
					//Lancement du Thread
					thread_importer.setDaemon(true);
					thread_importer.start();
				}
			});
		}
		return btnImporter;
	}
	
	public void addLogView(String strTexte){

		final String texte_final = strTexte;
		
		SwingUtilities.invokeLater(
			    new Runnable(){
			        public void run(){
			        	modele_messages.addElement(texte_final);
			        }
			    }
			);
	}
	
	public void updateProgress(int indice, int valeurMax){
		SwingUtilities.invokeLater(
			    new Runnable(){
			        public void run(){
			        	labProgression.setText(indice + " / " + valeurMax);
			        }
			    }
			);
	}
}
