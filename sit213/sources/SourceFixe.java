package sources;

import information.Information;
import simulateur.ArgumentsException;

/**
 * Une source qui envoie toujours le même message
 */
public class SourceFixe extends Source<Boolean> {

	/**
	 * Constructeur d'une source fixe prédéterminée.
	 * Elle envoie '101101' en booléens.
	 */
    public SourceFixe () {
        informationGeneree = new Information<Boolean>();
        informationGeneree.add(true);
        informationGeneree.add(false);
        informationGeneree.add(true);
        informationGeneree.add(true);
        informationGeneree.add(false);
        informationGeneree.add(true);
    }

    /**
     * Constructeur d'une source fixe personnalisable.
     * Elle envoie un message personnalisé en booléens.
     * 
     * @param m - Message binaire à envoyer (doit être une chaîne de 0 et de 1)
     * 
     * @throws ArgumentsException - Lorsque la chaîne est non instanciée, vide ou contient autre chose que des 0 et des 1
     */
    public SourceFixe (String m) throws ArgumentsException {
    	if (m == null) {
    		throw new ArgumentsException("Le paramètre n'est pas instancié.");
    	} else if (!m.matches("^[01]+$")) {
    		throw new ArgumentsException("Le paramètre est vide ou contient autre chose que les caractères '0' et '1'.");
    	}
    	informationGeneree = new Information<Boolean>();
    	for (int i = 0; i < m.length(); i++) {
    		if (m.charAt(i) == '0') {
    			informationGeneree.add(false);
    		} else {
    			informationGeneree.add(true);
    		}
    	}
    }

}
