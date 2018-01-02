package club.iothings.ihm;

import java.awt.Dimension;
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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import club.iothings.modules.ModCellRendererModele;

public class DlgModele extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnAnnuler = null;
	private JButton btnValider = null;
	//private JButton btnRechercher = null;
	
	private FrmBatch parent = null;
	private Connection dbMySQL = null;
	
	private JLabel labTitre = null;
	
	// ---
	private DefaultTableModel tabModel;
		
	private JScrollPane scrollModele= null;
	private JTable tabModele = null;
	
	public DlgModele(Connection connMySQL, FrmBatch frmParent) {
		super();
		initialize();
		
		dbMySQL = connMySQL;
		parent = frmParent;
		
		// Paramétrage du Header
		String[] columnNames = {"Choix",
				"Numéro",
				"Désignation",
				};
		
		//TabModel du JTable
		tabModel = (DefaultTableModel) tabModele.getModel();
		tabModel.setColumnIdentifiers(columnNames);
		
		// Paramétrage de la largeur des colonnes
		DimensionColonnes(tabModele);
		
		AfficherTableau();
	}

	private void initialize() {
		this.setSize(700, 500);
		this.setMinimumSize(new Dimension(200, 200));
		this.setResizable(false);
		this.setTitle("Modele - UNSA");
		this.setContentPane(getJContentPane());
		
	}
	
	private JButton getBtnAnnuler() {
		if (btnAnnuler == null) {
			btnAnnuler = new JButton("Annuler");
			btnAnnuler.setBounds(new Rectangle(10, 419, 674, 46));
			btnAnnuler.setFont(new Font("Arial", Font.PLAIN, 14));
			btnAnnuler.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DlgModele.this.dispose();
				}
			});
		}
		return btnAnnuler;
	}
	
	private JButton getBtnValider() {
		if (btnValider == null) {
			btnValider = new JButton("Valider");
			btnValider.setBounds(new Rectangle(10, 362, 674, 46));
			btnValider.setFont(new Font("Arial", Font.PLAIN, 14));
			btnValider.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if (tabModel.getRowCount() > 0){
						for (int i=0; i < tabModel.getRowCount(); i++){
							
														
							String choix = String.valueOf(tabModel.getValueAt(i, 0)); 
							String numero = String.valueOf(tabModel.getValueAt(i, 1)); 
							
							if (choix.compareTo("true")==0){
								parent.AjouterModele(numero);
							}
						}
						
						DlgModele.this.dispose();
						
					} else {
						JOptionPane.showMessageDialog(DlgModele.this, "Le tableau est vide", "Modèle - UNSA", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}
		return btnValider;
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnValider(), null);
			jContentPane.add(getBtnAnnuler(), null);
			jContentPane.add(getScrollModele(), null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("Choisir un modèle de fichier à ajouter");
			jContentPane.add(labTitre, null);
			
		}
		return jContentPane;
	}
	
	private JScrollPane getScrollModele() {
		if (scrollModele == null) {
			scrollModele = new JScrollPane();
			scrollModele.setBounds(new Rectangle(10, 75, 674, 276));
			scrollModele.setViewportView(getTabModele());
		}
		return scrollModele;
	}
	
	private JTable getTabModele() {
		if (tabModele == null) {
			
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
			
			tabModele = new JTable();
			tabModele.setModel(tableModel);
			tabModele.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabModele.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabModele.setColumnSelectionAllowed(false);
			tabModele.setRowSelectionAllowed(true);
			tabModele.getTableHeader().setResizingAllowed(false);
			tabModele.getTableHeader().setReorderingAllowed(false);
			tabModele.setRowHeight(40);
			tabModele.setFont(new Font("Arial", Font.PLAIN, 14));
			
			// --- Personnalisation de l'affichage --- 
			tabModele.setDefaultRenderer(Object.class, new ModCellRendererModele());
			
		}
		return tabModele;
	}
	
	private void DimensionColonnes(JTable tableau){
		tableau.getTableHeader().setReorderingAllowed(false);
		//tableau.getTableHeader().setResizingAllowed(false);
		
		tableau.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableau.getColumnModel().getColumn(1).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(2).setPreferredWidth(400);
	}
	
	private String AfficherTableau(){
		String resultat = "";
		
		try {
			
			String query = "SELECT nom, designation FROM T_MODELE";
			
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			int colNo = 3;
			tabModel.setRowCount(0);
			
			
			while(rset.next()) {
				Object[] ligne = new Object[colNo];
				
				ligne[0] = Boolean.FALSE;
				ligne[1] = rset.getString(1);
				ligne[2] = rset.getString(2);
				
				tabModel.addRow(ligne);
			}
			
		} catch (Exception ex){
			System.out.println("### DlgModele ### AfficherTableau ### " + ex.toString());
		}
		
		return resultat;
	}
	
}
