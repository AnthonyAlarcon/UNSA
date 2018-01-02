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
import javax.swing.table.DefaultTableModel;

import club.iothings.modules.ModStoredProcedures;

public class DlgGradeMaj extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnAnnuler = null;
	private JButton btnValider = null;
	
	private Connection dbMySQL = null;
	private JLabel labTitre = null;
	
	private DefaultTableModel tabModel;
	private JScrollPane scrollGrade= null;
	private JTable tabGrade = null;
	
	public DlgGradeMaj(Connection connMySQL) {
		super();
		initialize();
		
		dbMySQL = connMySQL;
		
		// Paramétrage du Header
		String[] columnNames = {"Grade",
				"Ancienne valeur",
				"Nouvelle valeur",
				};
		
		//TabModel du JTable
		tabModel = (DefaultTableModel) tabGrade.getModel();
		tabModel.setColumnIdentifiers(columnNames);
		
		// Paramétrage de la largeur des colonnes
		DimensionColonnes(tabGrade);
		
		AfficherTableau();
		
	}
	
	private void initialize() {
		this.setSize(700, 500);
		this.setMinimumSize(new Dimension(200, 200));
		this.setResizable(false);
		this.setTitle("Modele - UNSA");
		this.setContentPane(getJContentPane());
		
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnValider(), null);
			jContentPane.add(getBtnAnnuler(), null);
			jContentPane.add(getScrollGrade(), null);
			
			labTitre = new JLabel();
			labTitre.setBounds(new Rectangle(20, 20, 600, 40));
			labTitre.setFont(new Font("Arial", Font.BOLD, 24));
			labTitre.setText("Visualisation des modifications");
			jContentPane.add(labTitre, null);
			
		}
		return jContentPane;
	}
	
	private JScrollPane getScrollGrade() {
		if (scrollGrade == null) {
			scrollGrade = new JScrollPane();
			scrollGrade.setBounds(new Rectangle(10, 75, 674, 276));
			scrollGrade.setViewportView(getTabGrade());
		}
		return scrollGrade;
	}
	
	private JTable getTabGrade() {
		if (tabGrade == null) {
			
			// --- Personnalisation du TableModel ---
			DefaultTableModel tableModel = new DefaultTableModel()
			{
				private static final long serialVersionUID = 1L;
				
				// --- Verrouillage des cellules ---
				@Override
				public boolean isCellEditable(int row, int column) {					
					return false;
				}
			};
			
			tabGrade = new JTable();
			tabGrade.setModel(tableModel);
			tabGrade.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tabGrade.setColumnSelectionAllowed(false);
			tabGrade.setRowSelectionAllowed(true);
			tabGrade.getTableHeader().setResizingAllowed(false);
			tabGrade.getTableHeader().setReorderingAllowed(false);
			tabGrade.setRowHeight(40);
			tabGrade.setFont(new Font("Arial", Font.PLAIN, 14));
			
		}
		return tabGrade;
	}
	
	private void DimensionColonnes(JTable tableau){
		tableau.getTableHeader().setReorderingAllowed(false);
		//tableau.getTableHeader().setResizingAllowed(false);
		
		tableau.getColumnModel().getColumn(0).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(1).setPreferredWidth(150);
		tableau.getColumnModel().getColumn(2).setPreferredWidth(150);
	}
	
	private String AfficherTableau(){
		String resultat = "";
		
		try {
			
			String query = "SELECT id, old_value, new_value FROM V_CHECK_MAJ_GRADE";
			
			Statement stmt = dbMySQL.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			int colNo = 3;
			tabModel.setRowCount(0);
			
			while(rset.next()) {
				String[] ligne = new String[colNo];
				
				ligne[0] = rset.getString(1);
				ligne[1] = rset.getString(2);
				ligne[2] = rset.getString(3);
				
				tabModel.addRow(ligne);
			}
			
		} catch (Exception ex){
			System.out.println("### DlgGradeMaj ### AfficherTableau ### " + ex.toString());
		}
		
		return resultat;
	}
	
	private JButton getBtnAnnuler() {
		if (btnAnnuler == null) {
			btnAnnuler = new JButton("Annuler");
			btnAnnuler.setBounds(new Rectangle(10, 419, 674, 46));
			btnAnnuler.setFont(new Font("Arial", Font.PLAIN, 14));
			btnAnnuler.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DlgGradeMaj.this.dispose();
				}
			});
		}
		return btnAnnuler;
	}
	
	private JButton getBtnValider() {
		if (btnValider == null) {
			btnValider = new JButton("Appliquer les modifications");
			btnValider.setBounds(new Rectangle(10, 362, 674, 46));
			btnValider.setFont(new Font("Arial", Font.PLAIN, 14));
			btnValider.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					ModStoredProcedures proc = new ModStoredProcedures(dbMySQL);
					
					String resultat = proc.sp_Grade_Maj_Designation();
					
					if (resultat.compareTo("OK")==0){
						JOptionPane.showMessageDialog(DlgGradeMaj.this, "Les modifications ont été prises en compte", "Grade - UNSA", JOptionPane.INFORMATION_MESSAGE);
						DlgGradeMaj.this.dispose();
						
					} else {
						JOptionPane.showMessageDialog(DlgGradeMaj.this, resultat, "Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return btnValider;
	}

}
