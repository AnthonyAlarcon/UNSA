package club.iothings.ihm;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import club.iothings.modules.ModStoredProcedures;
import club.iothings.modules.MonFiltre;

public class FrmParametres extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnMajUAI = null;
	private JLabel labTitre = null;
	private JTextArea taMajUAI = null;
	private Connection dbMySQL = null;
	
	public FrmParametres(Connection connMySQL) {
		super();
		initialize();
		
		dbMySQL = connMySQL;
	}

	private void initialize() {
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setTitle("Paramètres - UNSA");
		this.setContentPane(getJContentPane());
		this.setResizable(false);
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnMajUAI(), null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("Paramètres");
			jContentPane.add(labTitre, null);
			
			taMajUAI = new JTextArea();
			taMajUAI.setBounds(new Rectangle(330, 80, 454, 70));
			taMajUAI.setText("La mise à jour des données UAI s'effectue via l'importation d'un fichier Excel (XLSX) comportant 4 colonnes au format texte : "
					+ "Numéro UAI, Nom UAI, Groupe, Ville.");
			taMajUAI.setWrapStyleWord(true);
			taMajUAI.setLineWrap(true);
			taMajUAI.setOpaque(false);
			taMajUAI.setEditable(false);
			taMajUAI.setFocusable(false);
			taMajUAI.setBackground(UIManager.getColor("Label.background"));
			taMajUAI.setFont(new Font("Arial", Font.PLAIN, 14));
		    taMajUAI.setBorder(UIManager.getBorder("Label.border"));;
			jContentPane.add(taMajUAI, null);
			
		}
		return jContentPane;
	}
	
	private JButton getBtnMajUAI() {
		if (btnMajUAI == null) {			
			btnMajUAI = new JButton("Mise à jour des données UAI");
			btnMajUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			btnMajUAI.setBounds(new Rectangle(20, 80, 300, 50));
			btnMajUAI.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {			
					
					ModStoredProcedures proc = new ModStoredProcedures(dbMySQL);

					// Ouverture de la fenêtre de sélection du fichier
					JFileChooser fc = new JFileChooser();
					fc.setAcceptAllFileFilterUsed(false);					
					fc.setPreferredSize(new Dimension(600,400));
					MonFiltre filtreTXT = new MonFiltre(new String[]{"xlsx","XLSX"}, "Fichier Excel");
					fc.addChoosableFileFilter(filtreTXT);
					
					int returnVal = fc.showOpenDialog(FrmParametres.this);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						
						try {
							
							System.out.println("*** Démarrage Maj UAI ***");
							
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
							
							System.out.println("Nombre de lignes détectées : " + maxRows);
							
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
									System.out.println("### ERREUR ### UAI Ligne " + i);
								}
							}
							
							// --- Fermeture du fichier ---
							wb.close();
							
							System.out.println("*** Fin Maj UAI ***");
							
						} catch (Exception ex){
							System.out.println("### FrmMain ### getBtnMajUAI ### " + ex.toString());
						}
					}
				}
			});
		}
		return btnMajUAI;
	}
}
