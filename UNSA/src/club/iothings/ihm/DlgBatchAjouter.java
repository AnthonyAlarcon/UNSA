package club.iothings.ihm;

import java.awt.Font;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import club.iothings.modules.ModCellRendererCriteres;

public class DlgBatchAjouter extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private FrmBatch parent = null;
	private JButton btnValider = null;
	
	private JLabel labDepartement = null;
	private JLabel labGrade = null;
	private JLabel labTypeUAI = null;
	private JLabel labCCP = null;
	
	private JLabel labNomFichier = null;
	private JTextField tfNomFichier = null;
	
	private JScrollPane scrollDep = null;
	private JTable tabDep = null;
	private DefaultTableModel tabModel_Dep;
	
	private JScrollPane scrollGrade = null;
	private JTable tabGrade = null;
	private DefaultTableModel tabModel_Grade;
	
	private JScrollPane scrollTypeUAI = null;
	private JTable tabTypeUAI = null;
	private DefaultTableModel tabModel_TypeUAI;
	
	private JScrollPane scrollCCP = null;
	private JTable tabCCP = null;
	private DefaultTableModel tabModel_CCP;
	
	private Connection dbMySQL = null;
	
	public DlgBatchAjouter(Connection connMySQL, FrmBatch frmParent) {
		super();
		initialize();
		
		dbMySQL = connMySQL;
		parent = frmParent;

		// --- Colonnes Département ---
		String[] columnNames_Dep = {
				"Choix",
				"Numéro",
				"Nom"
				};
		
		tabModel_Dep = (DefaultTableModel) tabDep.getModel();
		tabModel_Dep.setColumnIdentifiers(columnNames_Dep);
		
		// --- Colonnes Grade ---
		String[] columnNames_Grade = {
				"Choix",
				"Identifiant",
				"Designation"
				};
		
		tabModel_Grade = (DefaultTableModel) tabGrade.getModel();
		tabModel_Grade.setColumnIdentifiers(columnNames_Grade);
		
		// --- Colonnes TypeUAI ---
		String[] columnNames_TypeUAI = {
				"Choix",
				"Type"
				};
		
		tabModel_TypeUAI = (DefaultTableModel) tabTypeUAI.getModel();
		tabModel_TypeUAI.setColumnIdentifiers(columnNames_TypeUAI);
		
		// --- Colonnes TypeUAI ---
		String[] columnNames_CCP = {
				"Choix",
				"CCP"
				};
		
		tabModel_CCP = (DefaultTableModel) tabCCP.getModel();
		tabModel_CCP.setColumnIdentifiers(columnNames_CCP);
		
		// --- Remplissage des tableaux de sélection ---
		RemplirTableau_Dep();
		RemplirTableau_Grade();
		RemplirTableau_TypeUAI();
		RemplirTableau_CCP();
		RemplirTableau_Groupe();
	}

	private void initialize() {
		this.setSize(938, 610);
		this.setContentPane(getJContentPane());
		this.setTitle("Ajouter Batch - UNSA");
		this.setResizable(false);
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnValider(), null);
			
			jContentPane.add(getTfNomFichier(), null);
			
			labNomFichier = new JLabel();
			labNomFichier.setBounds(new Rectangle(24, 89, 100, 30));
			labNomFichier.setFont(new Font("Arial", Font.PLAIN, 14));
			labNomFichier.setText("Nom du fichier");
			jContentPane.add(labNomFichier, null);
						
			jContentPane.add(getScrollDep(), null);
			jContentPane.add(getScrollGrade(), null);
			jContentPane.add(getScrollTypeUAI(), null);
			jContentPane.add(getScrollCCP(), null);
			
			labDepartement = new JLabel();
			labDepartement.setBounds(new Rectangle(24, 147, 100, 30));
			labDepartement.setFont(new Font("Arial", Font.PLAIN, 14));
			labDepartement.setText("Département");
			jContentPane.add(labDepartement, null);
			
			labGrade = new JLabel();
			labGrade.setBounds(new Rectangle(477, 147, 100, 30));
			labGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			labGrade.setText("Grade");
			jContentPane.add(labGrade, null);
			
			labTypeUAI = new JLabel();
			labTypeUAI.setBounds(new Rectangle(24, 352, 100, 30));
			labTypeUAI.setFont(new Font("Arial", Font.PLAIN, 14));
			labTypeUAI.setText("Type UAI");
			jContentPane.add(labTypeUAI, null);
			
			labCCP = new JLabel();
			labCCP.setBounds(new Rectangle(477, 352, 100, 30));
			labCCP.setFont(new Font("Arial", Font.PLAIN, 14));
			labCCP.setText("CCP");
			jContentPane.add(labCCP, null);
		}
		return jContentPane;
	}

	private JButton getBtnValider() {
		if (btnValider == null) {			
			btnValider = new JButton("Valider");
			btnValider.setFont(new Font("Arial", Font.PLAIN, 14));
			btnValider.setBounds(new Rectangle(743, 62, 153, 57));
			btnValider.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					String nom_fichier = tfNomFichier.getText();
					
					if (nom_fichier.compareTo("")!=0) {
											
						String choix = "";
						
						// --- Variables de cumul des critères ---
						String cumul_dep = "";
						String cumul_grade = "";
						String cumul_typeUAI = "";
						String cumul_CCP = "";
						String cumul_groupe = "";
						
						// --- Département ---
						if (tabModel_Dep.getRowCount() > 0){
							for (int i=0; i < tabModel_Dep.getRowCount(); i++){
								
								choix = String.valueOf(tabDep.getValueAt(i, 0)); 
								
								if (choix.compareTo("true")==0){
									if (cumul_dep.compareTo("")==0){
										cumul_dep = String.valueOf(tabDep.getValueAt(i, 1));
									} else {
										cumul_dep = cumul_dep + ";" +  String.valueOf(tabDep.getValueAt(i, 1));
									}
								}
							}
						}
						
						// --- Grade ---
						if (tabModel_Grade.getRowCount() > 0){
							for (int i=0; i < tabModel_Grade.getRowCount(); i++){
								
								choix = String.valueOf(tabGrade.getValueAt(i, 0)); 
								
								if (choix.compareTo("true")==0){
									if (cumul_grade.compareTo("")==0){
										cumul_grade = String.valueOf(tabGrade.getValueAt(i, 1));
									} else {
										cumul_grade = cumul_grade + ";" +  String.valueOf(tabGrade.getValueAt(i, 1));
									}
								}
							}
						}
						
						// --- Type UAI ---
						if (tabModel_TypeUAI.getRowCount() > 0){
							for (int i=0; i < tabModel_TypeUAI.getRowCount(); i++){
								
								choix = String.valueOf(tabTypeUAI.getValueAt(i, 0)); 
								
								if (choix.compareTo("true")==0){
									if (cumul_typeUAI.compareTo("")==0){
										cumul_typeUAI = String.valueOf(tabTypeUAI.getValueAt(i, 1));
									} else {
										cumul_typeUAI = cumul_typeUAI + ";" +  String.valueOf(tabTypeUAI.getValueAt(i, 1));
									}
								}
							}
						}
						
						// --- Type CCP ---
						if (tabModel_CCP.getRowCount() > 0){
							for (int i=0; i < tabModel_CCP.getRowCount(); i++){
								
								choix = String.valueOf(tabCCP.getValueAt(i, 0)); 
								
								if (choix.compareTo("true")==0){
									if (cumul_CCP.compareTo("")==0){
										cumul_CCP = String.valueOf(tabCCP.getValueAt(i, 1));
									} else {
										cumul_CCP = cumul_CCP + ";" +  String.valueOf(tabCCP.getValueAt(i, 1));
									}
								}
							}
						}
						
						parent.AjouterLigne(nom_fichier, cumul_dep, cumul_grade, cumul_typeUAI, cumul_CCP, cumul_groupe);
						
						// --- Fermeture de la fenêtre ---
						DlgBatchAjouter.this.dispose();
						
					} else {
						JOptionPane.showMessageDialog(DlgBatchAjouter.this, "Veuillez saisir un nom de fichier", "", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}
		return btnValider;
	}
	
	private JScrollPane getScrollDep() {
		if (scrollDep == null) {
			scrollDep = new JScrollPane();
			scrollDep.setBounds(new Rectangle(24, 188, 420, 142));
			scrollDep.setViewportView(getTabDep());
		}
		return scrollDep;
	}
	
	private JScrollPane getScrollGrade() {
		if (scrollGrade == null) {
			scrollGrade = new JScrollPane();
			scrollGrade.setBounds(new Rectangle(477, 188, 420, 142));
			scrollGrade.setViewportView(getTabGrade());
		}
		return scrollGrade;
	}
	
	private JScrollPane getScrollTypeUAI() {
		if (scrollTypeUAI == null) {
			scrollTypeUAI = new JScrollPane();
			scrollTypeUAI.setBounds(new Rectangle(24, 393, 420, 142));
			scrollTypeUAI.setViewportView(getTabTypeUAI());
		}
		return scrollTypeUAI;
	}
	
	private JScrollPane getScrollCCP() {
		if (scrollCCP == null) {
			scrollCCP = new JScrollPane();
			scrollCCP.setBounds(new Rectangle(477, 393, 420, 142));
			scrollCCP.setViewportView(getTabCCP());
		}
		return scrollCCP;
	}
	
	private JTextField getTfNomFichier() {
		if (tfNomFichier == null) {
			tfNomFichier = new JTextField();
			tfNomFichier.setFont(new Font("Arial", Font.PLAIN, 14));
			tfNomFichier.setBounds(new Rectangle(134, 89, 310, 30));
		}
		return tfNomFichier;
	}
	
	private JTable getTabDep() {
		if (tabDep == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules du tableau sauf la première colonne ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					if (column == 0){
						return true;
					} else {
						return false;
					}
				}
			
				// --- CheckBox sur la première colonne ---
				@Override
				public Class<?> getColumnClass(int columnIndex)
				{
					// Format Boolean sur la première colonne
					if(columnIndex==0)
						return Boolean.class;
					return super.getColumnClass(columnIndex);
				}
			};		
			
			tabDep = new JTable(tableModel);
			tabDep.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabDep.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tabDep.setColumnSelectionAllowed(false);
			tabDep.setRowSelectionAllowed(true);
			tabDep.setRowHeight(20);
			tabDep.setFont(new Font("Arial", Font.PLAIN, 12));
			
			// --- Personnalisation de l'affichage --- 
			tabDep.setDefaultRenderer(Object.class, new ModCellRendererCriteres());
			
			// --- Rafaîchissement de la mise en forme sur clic ---
			tabDep.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					tabModel_Dep.fireTableDataChanged();
				}
			});
			
		}
		return tabDep;
	}
	
	private JTable getTabGrade() {
		if (tabGrade == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules du tableau sauf la première colonne ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					if (column == 0){
						return true;
					} else {
						return false;
					}
				}
			
				// --- CheckBox sur la première colonne ---
				@Override
				public Class<?> getColumnClass(int columnIndex)
				{
					// Format Boolean sur la première colonne
					if(columnIndex==0)
						return Boolean.class;
					return super.getColumnClass(columnIndex);
				}
			};		
			
			tabGrade = new JTable(tableModel);
			tabGrade.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabGrade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tabGrade.setColumnSelectionAllowed(false);
			tabGrade.setRowSelectionAllowed(true);
			tabGrade.setRowHeight(20);
			tabGrade.setFont(new Font("Arial", Font.PLAIN, 12));
			
			// --- Personnalisation de l'affichage --- 
			tabGrade.setDefaultRenderer(Object.class, new ModCellRendererCriteres());
			
			// --- Rafaîchissement de la mise en forme sur clic ---
			tabGrade.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					tabModel_Grade.fireTableDataChanged();
				}
			});
			
		}
		return tabGrade;
	}
	
	private JTable getTabTypeUAI() {
		if (tabTypeUAI == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules du tableau sauf la première colonne ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					if (column == 0){
						return true;
					} else {
						return false;
					}
				}
			
				// --- CheckBox sur la première colonne ---
				@Override
				public Class<?> getColumnClass(int columnIndex)
				{
					// Format Boolean sur la première colonne
					if(columnIndex==0)
						return Boolean.class;
					return super.getColumnClass(columnIndex);
				}
			};		
			
			tabTypeUAI = new JTable(tableModel);
			tabTypeUAI.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabTypeUAI.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tabTypeUAI.setColumnSelectionAllowed(false);
			tabTypeUAI.setRowSelectionAllowed(true);
			tabTypeUAI.setRowHeight(20);
			tabTypeUAI.setFont(new Font("Arial", Font.PLAIN, 12));
			
			// --- Personnalisation de l'affichage --- 
			tabTypeUAI.setDefaultRenderer(Object.class, new ModCellRendererCriteres());
			
			// --- Rafaîchissement de la mise en forme sur clic ---
			tabTypeUAI.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					tabModel_TypeUAI.fireTableDataChanged();
				}
			});
			
		}
		return tabTypeUAI;
	}
	
	private JTable getTabCCP() {
		if (tabCCP == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules du tableau sauf la première colonne ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					if (column == 0){
						return true;
					} else {
						return false;
					}
				}
			
				// --- CheckBox sur la première colonne ---
				@Override
				public Class<?> getColumnClass(int columnIndex)
				{
					// Format Boolean sur la première colonne
					if(columnIndex==0)
						return Boolean.class;
					return super.getColumnClass(columnIndex);
				}
			};		
			
			tabCCP = new JTable(tableModel);
			tabCCP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabCCP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tabCCP.setColumnSelectionAllowed(false);
			tabCCP.setRowSelectionAllowed(true);
			tabCCP.setRowHeight(20);
			tabCCP.setFont(new Font("Arial", Font.PLAIN, 12));
			
			// --- Personnalisation de l'affichage --- 
			tabCCP.setDefaultRenderer(Object.class, new ModCellRendererCriteres());
			
			// --- Rafaîchissement de la mise en forme sur clic ---
			tabCCP.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					tabModel_CCP.fireTableDataChanged();
				}
			});
			
		}
		return tabCCP;
	}
	
	private void RemplirTableau_Dep(){
		try {
			String query = "SELECT numero, nom FROM T_DEPARTEMENT ORDER BY numero ";
			
			// --- Numéro de colonnes affichées par le tableau ---
			int colNo = 2;
			
			// --- Recordset ---
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
						
			String cellValue = "";
			
			// --- Boucle d'insertion ---
			while(rset.next()) {
				Object[] ligne = new Object[colNo + 1];
				
				// ---
				ligne[0] = Boolean.FALSE;
				
				for(int i = 0; i < colNo; i++){
					cellValue = rset.getString(i+1);
					
					if (cellValue==null){
						cellValue = "";
					}
					
					ligne[i+1] = cellValue;
				}
				
				// --- Ajout de la ligne au tableau ---
				tabModel_Dep.addRow(ligne);
			}
		} catch (Exception ex){
			System.out.println("### DlgBatchAjouter ### RemplirTableau_Dep " + ex.toString());
		}
	}
	
	private void RemplirTableau_Grade(){
		try {
			String query = "SELECT id, designation FROM T_GRADE ORDER BY id";
			
			// --- Numéro de colonnes affichées par le tableau ---
			int colNo = 2;
			
			// --- Recordset ---
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
						
			String cellValue = "";
			
			// --- Boucle d'insertion ---
			while(rset.next()) {
				Object[] ligne = new Object[colNo + 1];
				
				ligne[0] = Boolean.FALSE;
				
				for(int i = 0; i < colNo; i++){
					cellValue = rset.getString(i+1);
					
					if (cellValue==null){
						cellValue = "";
					}
										
					ligne[i+1] = cellValue;
				}
				
				// --- Ajout de la ligne au tableau ---
				tabModel_Grade.addRow(ligne);
			}
		} catch (Exception ex){
			System.out.println("### DlgBatchAjouter ### RemplirTableau_Grade " + ex.toString());
		}
	}
	
	private void RemplirTableau_TypeUAI(){
		try {
			String query = "SELECT type_uai, COUNT(*) FROM T_UAI GROUP BY type_uai ORDER BY type_uai";
			
			// --- Numéro de colonnes affichées par le tableau ---
			int colNo = 1;
			
			// --- Recordset ---
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
						
			String cellValue = "";
			
			// --- Boucle d'insertion ---
			while(rset.next()) {
				Object[] ligne = new Object[colNo + 1];
				
				ligne[0] = Boolean.FALSE;
				
				for(int i = 0; i < colNo; i++){
					cellValue = rset.getString(i+1);
					
					if (cellValue==null){
						cellValue = "";
					}
										
					ligne[i+1] = cellValue;
				}
				
				// --- Ajout de la ligne au tableau ---
				tabModel_TypeUAI.addRow(ligne);
			}
		} catch (Exception ex){
			System.out.println("### DlgBatchAjouter ### RemplirTableau_TypeUAI " + ex.toString());
		}
	}
	
	private void RemplirTableau_CCP(){
		try {
			String query = "SELECT ccp, COUNT(*) FROM T_DATA GROUP BY ccp ORDER BY ccp";
			
			// --- Numéro de colonnes affichées par le tableau ---
			int colNo = 1;
			
			// --- Recordset ---
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
						
			String cellValue = "";
			
			// --- Boucle d'insertion ---
			while(rset.next()) {
				Object[] ligne = new Object[colNo + 1];
				
				ligne[0] = Boolean.FALSE;
				
				for(int i = 0; i < colNo; i++){
					cellValue = rset.getString(i+1);
					
					if (cellValue==null){
						cellValue = "";
					}
										
					ligne[i+1] = cellValue;
				}
				
				// --- Ajout de la ligne au tableau ---
				tabModel_CCP.addRow(ligne);
			}
		} catch (Exception ex){
			System.out.println("### DlgBatchAjouter ### RemplirTableau_CCP " + ex.toString());
		}
	}
	
	private void RemplirTableau_Groupe(){
		
	}
}
