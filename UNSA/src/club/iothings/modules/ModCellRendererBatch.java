package club.iothings.modules;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ModCellRendererBatch extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
						
		Component cell = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);	
		
		int num_row = row;
		int num_col = column;
		
		// --- Paramétrage ---
		int indice_col_statut = 7;
		
		// --- On récupère le statut de la compilation ---
		String statut_compilation = String.valueOf(table.getValueAt(num_row, indice_col_statut));	
		
		if (num_col == indice_col_statut){
			if (statut_compilation.compareTo("OK")==0){
				cell.setBackground(Color.green);
				cell.setForeground(Color.white);
			} else {
				if (statut_compilation.compareTo("ERREUR")==0){
					cell.setBackground(Color.red);
					cell.setForeground(Color.white);
				} else {
					cell.setBackground(Color.white);
					cell.setForeground(Color.black);
				}
			}
		} else {
			cell.setBackground(Color.white);
			cell.setForeground(Color.black);
		}	
	
		return cell;
	}	
}