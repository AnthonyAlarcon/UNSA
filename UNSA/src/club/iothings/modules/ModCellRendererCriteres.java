package club.iothings.modules;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ModCellRendererCriteres extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
						
		Component cell = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);	
		
		int num_row = row;
		
		// --- On récupère le statut de la compilation ---
		String choix = String.valueOf(table.getValueAt(num_row, 0));	
		
		if (choix.compareTo("true")==0){
			cell.setBackground(Color.yellow);
			cell.setForeground(Color.black);
		} else {
			cell.setBackground(Color.white);
			cell.setForeground(Color.black);
		}
		
		return cell;
	}	

}
