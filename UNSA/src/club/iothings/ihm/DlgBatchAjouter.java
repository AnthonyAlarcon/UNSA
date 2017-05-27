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

public class DlgBatchAjouter extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private FrmBatch parent = null;
	private JButton btnValider = null;
	
	private JLabel labNomFichier = null;
	private JTextField tfNomFichier = null;
	
	private JScrollPane scrollDep = null;
	private JTable tabDep = null;
	private DefaultTableModel tabModel_Dep;
	
	private JScrollPane scrollGrade = null;
	private JTable tabGrade = null;
	private DefaultTableModel tabModel_Grade;
	
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
		
		// --- Colonnes Département ---
		String[] columnNames_Grade = {
				"Choix",
				"Identifiant",
				"Designation"
				};
		
		tabModel_Grade = (DefaultTableModel) tabGrade.getModel();
		tabModel_Grade.setColumnIdentifiers(columnNames_Grade);
		
		// --- Remplissage des tableaux de sélection ---
		RemplirTableau_Dep();
		RemplirTableau_Grade();
		RemplirTableau_Type();
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
		}
		return jContentPane;
	}

	private JButton getBtnValider() {
		if (btnValider == null) {			
			btnValider = new JButton("Valider");
			btnValider.setFont(new Font("Arial", Font.PLAIN, 14));
			btnValider.setBounds(new Rectangle(744, 35, 153, 57));
			btnValider.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {					
					
					String nom_fichier = tfNomFichier.getText();
					
					if (nom_fichier.compareTo("")!=0) {
						
						String choix = "";
						
						// ---
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
						
						parent.AjouterLigne(nom_fichier, cumul_dep, cumul_grade, cumul_typeUAI, cumul_CCP, cumul_groupe);
						
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
			scrollDep.setBounds(new Rectangle(24, 150, 396, 180));
			scrollDep.setViewportView(getTabDep());
		}
		return scrollDep;
	}
	
	private JScrollPane getScrollGrade() {
		if (scrollGrade == null) {
			scrollGrade = new JScrollPane();
			scrollGrade.setBounds(new Rectangle(430, 150, 396, 180));
			scrollGrade.setViewportView(getTabGrade());
		}
		return scrollGrade;
	}
	
	private JTextField getTfNomFichier() {
		if (tfNomFichier == null) {
			tfNomFichier = new JTextField();
			tfNomFichier.setFont(new Font("Arial", Font.PLAIN, 14));
			tfNomFichier.setBounds(new Rectangle(134, 89, 286, 30));
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
			tabDep.setRowHeight(40);
			tabDep.setFont(new Font("Arial", Font.PLAIN, 14));
			
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
			tabGrade.setRowHeight(40);
			tabGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			
		}
		return tabGrade;
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
	
	private void RemplirTableau_Type(){
		
	}
	
	private void RemplirTableau_CCP(){
		
	}
	
	private void RemplirTableau_Groupe(){
		
	}
}
