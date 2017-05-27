package club.iothings.ihm;

import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import club.iothings.modules.ModCellRendererBatch;

public class DlgBatchAjouter extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private FrmBatch parent = null;
	private JButton btnValider = null;
	
	private JScrollPane scrollDep = null;
	private JTable tabDep = null;
	private DefaultTableModel tabModel_Dep;
	
	public DlgBatchAjouter(FrmBatch frmParent) {
		super();
		initialize();
		
		parent = frmParent;

		// --- Colonnes Département ---
		String[] columnNames_Dep = {
				"Choix",
				"Numéro",
				"Nom"
				};
		
		// --- TabModel du JTable ---
		tabModel_Dep = (DefaultTableModel) tabDep.getModel();
		tabModel_Dep.setColumnIdentifiers(columnNames_Dep);
		
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

			jContentPane.add(getScrollDep(), null);
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
					
					parent.AjouterLigne("Nom", "Dep", "grade", "type", "ccp", "groupe");
					
				}
			});
		}
		return btnValider;
	}
	
	private JScrollPane getScrollDep() {
		if (scrollDep == null) {
			scrollDep = new JScrollPane();
			scrollDep.setBounds(new Rectangle(25, 43, 295, 180));
			scrollDep.setViewportView(getTabDep());
		}
		return scrollDep;
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
	
	private void RemplirTableau_Dep(){
		
	}
	
	private void RemplirTableau_Grade(){
		
	}
	
	private void RemplirTableau_Type(){
		
	}
	
	private void RemplirTableau_CCP(){
		
	}
	
	private void RemplirTableau_Groupe(){
		
	}
}
