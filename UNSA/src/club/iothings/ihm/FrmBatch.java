package club.iothings.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dialog.ModalityType;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import club.iothings.fonctions.FcnExportFiltres;
import club.iothings.modules.ModCellRendererBatch;

public class FrmBatch extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnLancer = null;
	private JButton btnAjouter = null;
	private JButton btnSupprimer = null;
	private JButton btnReset = null;
	
	private JLabel labTitre = null;
	
	private JLabel labEmplacement = null;
	private JTextField tfEmplacement = null;
	
	private JLabel labPrefixe = null;
	private JTextField tfPrefixe = null;
	
	private JPanel jContentPane_haut = null;
	private JPanel jContentPane_bas = null;
	
	private JScrollPane scrollBatch = null;
	private JTable tabBatch = null;
	private DefaultTableModel tabModel;
	
	private Connection dbMySQL = null;
	
	private String partie_A = "";
	private String partie_B = "";
	//private String partie_C = "";
	private String partie_D = "";
	
	private JLabel labPartieA = null;
	private JTextField tfPartieA = null;
	
	private JLabel labPartieB = null;
	private JTextField tfPartieB = null;
	
	private JLabel labPartieD = null;
	private JScrollPane scrollPartieD = null;
	private JTextArea taPartieD = null;
	
	public FrmBatch(Connection connMySQL) {
		super();
		initialize();
		
		// --- Partie A, B, C et D ---
		tfPartieA.setText("UNSA Montpellier");
		tfPartieB.setText("UNSA-EDUCATION Montpellier");
		//partie_C = "";
		taPartieD.setText("unsa-education.syndicat@ac-montpellier.fr,lrmp@unsa-education.org,frederic.vaysse@unsa.org,francoise.parrini@se-unsa.org,gilles,tena@gmail.com");
		
		// --- Affectation de la connection à la base de données ---
		dbMySQL = connMySQL;
		
		// --- Répertoire de destination des fichiers compilés ---
		tfEmplacement.setText("C:/Fichiers_Rectorat/Batch/");
		
		// --- Préfixe utilisé dans le nom des fichiers ---
		tfPrefixe.setText("liste." + tfPartieB.getText() + ".");
		
		
		String[] columnNames = {
				"Nom du fichier",
				"Département",
				"Grade",
				"Type UAI",
				"CCP",
				"Groupe",
				"Ville",
				"Compilation"
				};
		
		
		// --- TabModel du JTable ---
		tabModel = (DefaultTableModel) tabBatch.getModel();
		tabModel.setColumnIdentifiers(columnNames);
		
		// --- Paramétrage du tri ---
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tabBatch.getModel());
		tabBatch.setRowSorter(sorter);
		
		// --- Paramétrage de la largeur des colonnes ---
		DimensionColonnes(tabBatch);
		
		
	}
	
	private void initialize() {
		this.setSize(1200, 600);
		this.setMinimumSize(new Dimension(1200, 600));
		this.setTitle("Batch - UNSA");
		
		getContentPane().add(getScrollBatch(), BorderLayout.CENTER);
		getContentPane().add(getJContentPane_Haut(), BorderLayout.NORTH);
		getContentPane().add(getJContentPane_Bas(), BorderLayout.SOUTH);
	}
	
	private JPanel getJContentPane_Haut() {
		if (jContentPane_haut == null){
			jContentPane_haut = new JPanel();
			jContentPane_haut.setLayout(null);
			jContentPane_haut.setPreferredSize(new Dimension(150, 300));
			
			jContentPane_haut.add(getBtnLancer(), null);
			jContentPane_haut.add(getBtnAjouter(), null);
			jContentPane_haut.add(getBtnSupprimer(), null);
			jContentPane_haut.add(getBtnReset(), null);
			
			
			jContentPane_haut.add(getTfEmplacement(), null);
			
			labEmplacement = new JLabel();
			labEmplacement.setBounds(new Rectangle(476, 71, 100, 30));
			labEmplacement.setFont(new Font("Arial", Font.PLAIN, 14));
			labEmplacement.setText("Emplacement");
			jContentPane_haut.add(labEmplacement, null);
			
			jContentPane_haut.add(getTfPrefixe(), null);
			
			labPrefixe = new JLabel();
			labPrefixe.setBounds(new Rectangle(24, 71, 100, 30));
			labPrefixe.setFont(new Font("Arial", Font.PLAIN, 14));
			labPrefixe.setText("Préfixe");
			jContentPane_haut.add(labPrefixe, null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("Créer un batch de fichiers");
			jContentPane_haut.add(labTitre, null);
			
			labPartieA = new JLabel();
			labPartieA.setBounds(new Rectangle(24, 112, 100, 30));
			labPartieA.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieA.setText("Partie A");
			jContentPane_haut.add(labPartieA, null);
			
			jContentPane_haut.add(getTfPartieA(), null);
			
			labPartieB = new JLabel();
			labPartieB.setBounds(new Rectangle(476, 111, 100, 30));
			labPartieB.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieB.setText("Partie B");
			jContentPane_haut.add(labPartieB, null);
			
			jContentPane_haut.add(getTfPartieB(), null);
			
			labPartieD = new JLabel();
			labPartieD.setBounds(new Rectangle(24, 153, 100, 30));
			labPartieD.setFont(new Font("Arial", Font.PLAIN, 14));
			labPartieD.setText("Partie D");
			jContentPane_haut.add(labPartieD, null);
			
			jContentPane_haut.add(getScrollPartieD(), null);
			
		}
		return jContentPane_haut;
	}
	
	private JPanel getJContentPane_Bas() {
		if (jContentPane_bas == null){
			jContentPane_bas = new JPanel();
			jContentPane_bas.setLayout(null);
			jContentPane_bas.setPreferredSize(new Dimension(150, 40));
		}
		return jContentPane_bas;
	}
	
	private JTextField getTfEmplacement() {
		if (tfEmplacement == null) {
			tfEmplacement = new JTextField();
			tfEmplacement.setFont(new Font("Arial", Font.PLAIN, 14));
			tfEmplacement.setBounds(new Rectangle(586, 71, 286, 30));
		}
		return tfEmplacement;
	}
	
	private JTextField getTfPrefixe() {
		if (tfPrefixe == null) {
			tfPrefixe = new JTextField();
			tfPrefixe.setFont(new Font("Arial", Font.PLAIN, 14));
			tfPrefixe.setBounds(new Rectangle(134, 71, 286, 30));
		}
		return tfPrefixe;
	}
	
	private JTextField getTfPartieA() {
		if (tfPartieA == null) {
			tfPartieA = new JTextField();
			tfPartieA.setFont(new Font("Arial", Font.PLAIN, 14));
			tfPartieA.setBounds(new Rectangle(134, 113, 286, 30));
		}
		return tfPartieA;
	}
	
	private JTextField getTfPartieB() {
		if (tfPartieB == null) {
			tfPartieB = new JTextField();
			tfPartieB.setFont(new Font("Arial", Font.PLAIN, 14));
			tfPartieB.setBounds(new Rectangle(586, 112, 286, 30));
		}
		return tfPartieB;
	}
		
	private JButton getBtnLancer() {
		if (btnLancer == null) {			
			btnLancer = new JButton("Lancer");
			btnLancer.setFont(new Font("Arial", Font.PLAIN, 14));
			btnLancer.setBounds(new Rectangle(974, 259, 200, 30));
			btnLancer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					String emplacement = tfEmplacement.getText();
					String prefixe = tfPrefixe.getText();
					
					FcnExportFiltres filt = new FcnExportFiltres(dbMySQL, emplacement);
					
					// --- Déclaration des variables intermédiaires ---
					String nom_fichier = "";
					String departement = "";
					String grade = "";
					String type_uai = "";
					String ccp = "";
					String groupe = "";
					String ville = "";
					
					String resultat = "";
					
					// --- Affectation des valeurs ---
					partie_A = tfPartieA.getText();
					partie_B = tfPartieB.getText();
					partie_D = taPartieD.getText();
					
					// --- Vérification des saisies obligatoires ---
					if ((emplacement.compareTo("")!=0) && (prefixe.compareTo("")!=0) && (partie_A.compareTo("")!=0) && (partie_B.compareTo("")!=0) && (partie_D.compareTo("")!=0)){
						// --- Vérification du contenu du tableau ---
						if (tabModel.getRowCount() > 0){
							
							// --- Balayage des lignes du tableau ---
							for (int i=0; i<tabModel.getRowCount(); i++){
								
								// --- Affectation des valeurs contenues dans le tableau ---
								nom_fichier = tabModel.getValueAt(i, 0).toString();
								departement = tabModel.getValueAt(i, 1).toString();
								grade = tabModel.getValueAt(i, 2).toString();
								type_uai = tabModel.getValueAt(i, 3).toString();
								ccp = tabModel.getValueAt(i, 4).toString();
								groupe = tabModel.getValueAt(i, 5).toString();
								ville = tabModel.getValueAt(i, 6).toString();
								
								// --- Résultat du traitement ---
								resultat = filt.start(prefixe + nom_fichier, departement, grade, type_uai, ccp, groupe, ville);
								
								// --- Renvoi du résultat du traitement dans le tableau ---
								tabModel.setValueAt(resultat, i, 7);
							}
							
							try {
								
								// --- Création de la liste récapitulative ---
								BufferedWriter out_liste = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement + "liste.csv"), "UTF-8"));
								
								for (int i=0; i < tabModel.getRowCount(); i++){
									
									// --- Affectation des valeurs contenues dans le tableau ---
									nom_fichier = tabModel.getValueAt(i, 0).toString();
									
									out_liste.write(partie_A + ";" + partie_B + ";" + nom_fichier + ";" + partie_D);
									out_liste.newLine();
								}
								out_liste.close();
								
							} catch (Exception ex){
								System.out.println("### FrmBatch ### getBtnLancer # Liste de la liste " + ex.toString());
							}
							
						} else {
							JOptionPane.showMessageDialog(FrmBatch.this, "Le tableau est vide", "Batch - UNSA", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(FrmBatch.this, "Les champs de saisie ne sont pas remplis correctement", "Batch - UNSA", JOptionPane.WARNING_MESSAGE);
					}
					
				}
			});
		}
		return btnLancer;
	}
	
	private JButton getBtnAjouter() {
		if (btnAjouter == null) {			
			btnAjouter = new JButton("Ajouter un fichier au batch");
			btnAjouter.setFont(new Font("Arial", Font.PLAIN, 14));
			btnAjouter.setBounds(new Rectangle(10, 259, 200, 30));
			btnAjouter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					// --- Ouverture ---
					DlgBatchAjouter ajouter = new DlgBatchAjouter(dbMySQL, FrmBatch.this);
					ajouter.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					ajouter.setModalityType(ModalityType.APPLICATION_MODAL);
					ajouter.setLocationRelativeTo(null);
					ajouter.setVisible(true);
				}
			});
		}
		return btnAjouter;
	}
	
	private JButton getBtnSupprimer() {
		if (btnSupprimer == null) {			
			btnSupprimer = new JButton("Supprimer la sélection");
			btnSupprimer.setFont(new Font("Arial", Font.PLAIN, 14));
			btnSupprimer.setBounds(new Rectangle(220, 259, 200, 30));
			btnSupprimer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					int selected_row = tabBatch.getSelectedRow();
					
					if (selected_row != -1){
						
						String nom_fichier = tabBatch.getValueAt(selected_row, 0).toString();
						
						Object[] options = {"Oui", "Non"};
						int reponse = JOptionPane.showOptionDialog(FrmBatch.this, "Supprimer du batch le fichier sélectionné [" + nom_fichier + "] ?", "Batch", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						
						// --- Confirmation de l'utilisateur ---
						if (reponse==0){
							tabModel.removeRow(selected_row);
						}
					} else {
						JOptionPane.showMessageDialog(FrmBatch.this, "Aucune ligne n'est sélectionnée", "Batch", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}
		return btnSupprimer;
	}
	
	private JButton getBtnReset() {
		if (btnReset == null) {			
			btnReset = new JButton("Reset");
			btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
			btnReset.setBounds(new Rectangle(430, 259, 200, 30));
			btnReset.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					// --- Suppression de toutes les lignes ---
					tabModel.setRowCount(0);
				}
			});
		}
		return btnReset;
	}
	
	private JScrollPane getScrollBatch() {
		if (scrollBatch == null) {
			scrollBatch = new JScrollPane();
			scrollBatch.setViewportView(getTabBatch());
		}
		return scrollBatch;
	}
	
	private JTextArea getTaPartieD(){
		if (taPartieD == null){
			taPartieD = new JTextArea();
			taPartieD.setFont(new Font("Arial", Font.PLAIN, 14));
			taPartieD.setLineWrap(true);
			taPartieD.setWrapStyleWord(true);
		}
		return taPartieD;
	}
	
	private JScrollPane getScrollPartieD() {
		if (scrollPartieD == null) {
			scrollPartieD = new JScrollPane();
			scrollPartieD.setBounds(new Rectangle(134, 154, 738, 70));
			scrollPartieD.setViewportView(getTaPartieD());
		}
		return scrollPartieD;
	}
	
	private JTable getTabBatch() {
		if (tabBatch == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules du tableau ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					return false;
				}
			};
			
			tabBatch = new JTable(tableModel);
			tabBatch.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabBatch.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tabBatch.setColumnSelectionAllowed(false);
			tabBatch.setRowSelectionAllowed(true);
			tabBatch.setRowHeight(40);
			tabBatch.setFont(new Font("Arial", Font.PLAIN, 14));
			
			// --- Personnalisation de l'affichage --- 
			tabBatch.setDefaultRenderer(Object.class, new ModCellRendererBatch());
			
			// Action Clic
//			tabBatch.addMouseListener(new java.awt.event.MouseAdapter() {
//				public void mouseClicked(java.awt.event.MouseEvent e) {
//					
//					Point p = e.getPoint();
//					int ligne = tabRetouches.rowAtPoint(p);
//					
//					// --- Reset des données ---
//					String appareil = "";
//					String zone = "";
//					String sous_zone = "";
//					int page = 0;
//					String type_retouche = "";
//					String rubrique = "";
//					
//					appareil = String.valueOf(tabRetouches.getValueAt(ligne, 0));			
//					zone = String.valueOf(tabRetouches.getValueAt(ligne, 1));
//					sous_zone = String.valueOf(tabRetouches.getValueAt(ligne, 2));
//					page = Integer.valueOf(tabRetouches.getValueAt(ligne, 3).toString());
//					type_retouche = String.valueOf(tabRetouches.getValueAt(ligne, 4));
//					rubrique = String.valueOf(tabRetouches.getValueAt(ligne, 5));
//					
//					// Clic Droit pour Détails
//					if ((e.getButton() == MouseEvent.BUTTON3)) {
//												
//						int row = tabRetouches.rowAtPoint(p);
//						int col = tabRetouches.columnAtPoint(p);
//						
//						// Change la ligne sélectionnée si clic droit
//						tabRetouches.changeSelection(row, col, false, false);
//
//						tabMenu.show(e.getComponent(),e.getX(), e.getY());
//					}
//				}
//			});
		}
		return tabBatch;
	}
	
	private void DimensionColonnes(JTable tableau){
		tableau.getTableHeader().setReorderingAllowed(false);
		tableau.getTableHeader().setResizingAllowed(true);
		
		tableau.getColumnModel().getColumn(0).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(1).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(2).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(3).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(4).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(5).setPreferredWidth(100);
		tableau.getColumnModel().getColumn(6).setPreferredWidth(100);
		tableau.getColumnModel().getColumn(7).setPreferredWidth(100);
	}
	
	public void AjouterLigne(String strNomFichier, String strDepartement, String strGrade, String strTypeUAI, String strCCP, String strGroupe, String strVille) {
		
		try {
			String[] ligne = new String[8];
			
			ligne[0] = strNomFichier;
			ligne[1] = strDepartement;
			ligne[2] = strGrade;
			ligne[3] = strTypeUAI;
			ligne[4] = strCCP;
			ligne[5] = strGroupe;
			ligne[6] = strVille;
			ligne[7] = "Non lancée";
			
			tabModel.addRow(ligne);
		} catch (Exception ex){
			System.out.println("### FrmBatch ### AjouterLigne " + ex.toString());
		}
	}
	
}
