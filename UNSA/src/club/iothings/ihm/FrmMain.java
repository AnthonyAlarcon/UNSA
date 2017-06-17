package club.iothings.ihm;

import club.iothings.data.DataConnexion;
import club.iothings.fonctions.FcnImporterDataXLSX;
import club.iothings.modules.ModStoredProcedures;
import club.iothings.modules.MonFiltre;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.io.FileInputStream;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FrmMain extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnConnexion = null;
	private JButton btnImporter = null;
	private JButton btnExtractions = null;
	private JButton btnBatch = null;
	private JButton btnMajUAI = null;
	
	private JLabel labTitre = null;
	private JLabel labVersion = null;
	
	private JScrollPane scrollMessages = null;
	private JList<String> listMessages = null;
	private DefaultListModel<String> modele_messages = null;
	
	private JProgressBar progressImport = null;
	
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
		
		DataConnexion dbMySQL = new DataConnexion();
		
		labVersion.setText(dbMySQL.getVersion());
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
			jContentPane.add(getBtnBatch(), null);
			jContentPane.add(getBtnMajUAI(), null);
			jContentPane.add(getScrollMessages(), null);
			jContentPane.add(getProgressImport(), null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 80, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("UNSA");
			jContentPane.add(labTitre, null);
			
			labVersion = new JLabel();
			labVersion.setBounds(new Rectangle(110, 24, 50, 40));
			labVersion.setFont(new Font("Arial", Font.PLAIN, 14));
			labVersion.setText("");
			jContentPane.add(labVersion, null);
		}
		return jContentPane;
	}
	
	private JScrollPane getScrollMessages() {
		if (scrollMessages == null) {
			scrollMessages = new JScrollPane();
			scrollMessages.setBounds(new Rectangle(10, 106, 874, 333));
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
			btnConnexion.setBounds(new Rectangle(10, 450, 200, 50));
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
						btnBatch.setEnabled(true);
						btnMajUAI.setEnabled(true);
						
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
	
	private JButton getBtnBatch() {
		if (btnBatch == null) {			
			btnBatch = new JButton("Batch");
			btnBatch.setFont(new Font("Arial", Font.PLAIN, 14));
			btnBatch.setBounds(new Rectangle(474, 511, 200, 50));
			btnBatch.setEnabled(false);
			btnBatch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {			
					
					FrmBatch batch = new FrmBatch(dbMySQL);
					batch.setLocationRelativeTo(null);
					batch.setVisible(true);
				}
			});
		}
		return btnBatch;
	}
	
	private JButton getBtnMajUAI() {
		if (btnMajUAI == null) {			
			btnMajUAI = new JButton("Maj UAI");
			btnMajUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			btnMajUAI.setBounds(new Rectangle(10, 511, 200, 50));
			btnMajUAI.setEnabled(false);
			btnMajUAI.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {			
					
					ModStoredProcedures proc = new ModStoredProcedures(dbMySQL);

					// Ouverture de la fenêtre de sélection du fichier
					JFileChooser fc = new JFileChooser();
					fc.setAcceptAllFileFilterUsed(false);					
					fc.setPreferredSize(new Dimension(600,400));
					MonFiltre filtreTXT = new MonFiltre(new String[]{"xlsx","XLSX"}, "Fichier Excel");
					fc.addChoosableFileFilter(filtreTXT);
					
					int returnVal = fc.showOpenDialog(FrmMain.this);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						
						try {
							
							addLogView("*** Démarrage Maj UAI ***");
							
							String emplacement = fc.getSelectedFile().getPath();
							FileInputStream fileExcel = new FileInputStream(emplacement);
							
							XSSFWorkbook wb = new XSSFWorkbook(fileExcel);
							XSSFSheet sheet = wb.getSheetAt(0);
							
							int maxRows = sheet.getLastRowNum();
							
							String resultat = "";
							
							// ---
							String uai = "";
							String nom = "";
							String groupe = "";
							String ville = "";
							
							addLogView("Nombre de lignes détectées : " + maxRows);
							
							// --- Boucle d'importation initialisée à 1 (entête) ---
							for (int i=1; i<maxRows; i++){
								
								//System.out.println("I = " + i);
								
								// --- Affectation des valeurs ---
								uai = sheet.getRow(i).getCell(0).getStringCellValue();
								nom = sheet.getRow(i).getCell(1).getStringCellValue();
								groupe = sheet.getRow(i).getCell(2).getStringCellValue();
								ville = sheet.getRow(i).getCell(3).getStringCellValue();
								
								resultat = proc.sp_UAI_Maj_Infos(i, uai, nom, groupe, ville);
								
								// ---
								if (resultat.compareTo("ERREUR")==0){
									addLogView("### ERREUR ### UAI Ligne " + i);
								}
							}
							
							// --- Fermeture du fichier ---
							wb.close();
							
							addLogView("*** Fin Maj UAI ***");
							
						} catch (Exception ex){
							System.out.println("### FrmMain ### getBtnMajUAI ### " + ex.toString());
						}
					}
				}
			});
		}
		return btnMajUAI;
	}
	
	private JProgressBar getProgressImport() {
		if (progressImport == null) {
			progressImport = new JProgressBar();
			progressImport.setFont(new Font("Arial", Font.PLAIN, 14));
			progressImport.setBounds(430, 450, 454, 50);
			progressImport.setStringPainted(true);
			progressImport.setString("%");
			progressImport.setForeground(Color.getHSBColor(0.5833f, 0.80f, 1.00f));
		}
		return progressImport;
	}
	
	private JButton getBtnImporter() {
		if (btnImporter == null) {			
			btnImporter = new JButton("Importer les données");
			btnImporter.setFont(new Font("Arial", Font.PLAIN, 14));
			btnImporter.setBounds(new Rectangle(220, 450, 200, 50));
			btnImporter.setEnabled(false);
			btnImporter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {								
					
					Thread thread_importer = new Thread() {
						public void run() {
							
							try {
								btnImporter.setEnabled(false);	
								
								String resultat = "";
								String emplacement = "";
								
								//----- Ouverture de la fenêtre de sélection du fichier -----
								JFileChooser fc = new JFileChooser();
								fc.setAcceptAllFileFilterUsed(false);					
								fc.setPreferredSize(new Dimension(600,400));
								
								MonFiltre filtreTXT = new MonFiltre(new String[]{"csv","CSV"}, "Fichier CSV");
								fc.addChoosableFileFilter(filtreTXT);
								
								int returnVal = fc.showOpenDialog(FrmMain.this);
								
								//----- L'utilisateur approuve le fichier sélectionné -----
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									
									emplacement = fc.getSelectedFile().getPath();
									
									// --- Initialisation du module d'importation ---
									FcnImporterDataXLSX import_data = new FcnImporterDataXLSX(dbMySQL, FrmMain.this);	
									resultat = import_data.start(emplacement);
									
								}
								
								btnImporter.setEnabled(true);
								
							} catch (Exception ex){
								System.out.println("### FrmMain ### btnImporter ### " + ex.toString());
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
		
		try {
			SwingUtilities.invokeLater(
				    new Runnable(){
				        public void run(){
				        	modele_messages.addElement(texte_final);
				        }
				    }
				);
		} catch (Exception ex){
			System.out.println("### FrmMain ### addLogView ### " + ex.toString());
		}
	}
	
	public void updateProgressMax(int valeurMax){
		
		try {
			SwingUtilities.invokeLater(
				    new Runnable(){
				        public void run(){
				        	progressImport.setMaximum(valeurMax);
				        }
				    }
				);
		} catch (Exception ex){
			System.out.println("### FrmMain ### updateProgressMax ### " + ex.toString());
		}
	}
	
	public void updateProgressValue(int indice){
		
		try {
			SwingUtilities.invokeLater(
				    new Runnable(){
				        public void run(){
				        	progressImport.setValue(indice);
				        }
				    }
				);
		} catch (Exception ex){
			System.out.println("### FrmMain ### updateProgressValue ### " + ex.toString());
		}
	}
	
	public void updateProgressText(String strTexte){
		
		try {
			SwingUtilities.invokeLater(
				    new Runnable(){
				        public void run(){
				        	progressImport.setString(strTexte);
				        }
				    }
				);
		} catch (Exception ex){
			System.out.println("### FrmMain ### updateProgressText ### " + ex.toString());
		}
	}
	
	
}
