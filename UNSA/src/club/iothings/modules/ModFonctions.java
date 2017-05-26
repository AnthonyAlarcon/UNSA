package club.iothings.modules;

public class ModFonctions {

	public String MEF_Query_ValuesIn(String strValeur, String strSeparateur){
		
		String resultat = "";
		
		String valeur = "";
		String ligne = "";
		
		try {
			if (strValeur.compareTo("")!=0){
				
				String splitarray[] = strValeur.split(strSeparateur);
				
				for (int i=0; i<splitarray.length; i++){
					
					valeur = splitarray[i];
					
					if (ligne.compareTo("")==0){
						ligne = "'" + valeur + "'";
					} else {
						ligne = ligne + ",'" + valeur + "'";
					}
					resultat = ligne;
				}
			}
			
		} catch (Exception ex) {
			resultat = "ERREUR";
			System.out.println("### ModFonctions ### MEF_Query_ValuesIn ### " + ex.toString());
		}
				
		return resultat;
	}
}
