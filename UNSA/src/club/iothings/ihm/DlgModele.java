package club.iothings.ihm;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DlgModele extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnAnnuler = null;
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
		String[] columnNames = {"Nom",
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
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			//jContentPane.add(getBtnOK(), null);
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
			scrollModele.setBounds(new Rectangle(10, 105, 674, 303));
			scrollModele.setViewportView(getTabModele());
		}
		return scrollModele;
	}
	
	private JTable getTabModele() {
		if (tabModele == null) {
			
			//Paramétrage pour les check box
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// Verrouillage des cellules
				@Override
				public boolean isCellEditable(int row, int column) {					
					return false;
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
			
			
			
			// Action Clic
			tabModele.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					// Double-clic
					if (e.getClickCount() == 2){								
						
						if ((e.getButton() == MouseEvent.BUTTON1)) {
														
							String selection = "";
							selection = String.valueOf(tabModele.getValueAt(tabModele.getSelectedRow(), 0));
							
							parent.AjouterModele(selection);							
							DlgModele.this.dispose();
						}
					}

				}
			});
		}
		return tabModele;
	}
	
	private void DimensionColonnes(JTable tableau){
		tableau.getTableHeader().setReorderingAllowed(false);
		//tableau.getTableHeader().setResizingAllowed(false);
		
		tableau.getColumnModel().getColumn(0).setPreferredWidth(500);
	}
	
	private String AfficherTableau(){
		String resultat = "";
		
		try {
			
			String query = "SELECT nom, departement, type_uai, grade, ccp, groupe, ville FROM T_MODELE";
			
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			int colNo = 1;
			tabModel.setRowCount(0);
			
			String cellValue ="";

			
			
			while(rset.next()) {
				String[] ligne = new String[colNo];
				
				for(int i = 0; i < colNo; i++){
					cellValue = rset.getString(i+1);
					
					if (cellValue==null){
						cellValue = "";
					}
					
					ligne[i] = cellValue;
				}
				
				tabModel.addRow(ligne);
			}
			
		} catch (Exception ex){
			
		}
		
		return resultat;
	}
	
}
