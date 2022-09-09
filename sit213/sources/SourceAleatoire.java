package sources;
import java.util.Random;

import information.Information;

/**
 * Une source qui envoie un message aleatoire
 */
public class SourceAleatoire extends Source<Boolean> {

	/**
	 * Constructeur d'une source aleatoire.
	 * Elle envoie un message de longueur 7 en booleens.
	 */
    public SourceAleatoire () {
        informationGeneree = new Information<Boolean>();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
        	informationGeneree.add(random.nextBoolean());
        }
    }

    /**
     * Constructeur d'une source aleatoire personnalisable.
     * Elle envoie un message de longueur personnalisee en booleens.
     * 0 et 1 sont equiprobables.
     * 
     * @param l - Longueur du message binaire a  envoyer
     */
    public SourceAleatoire (int l) {
        informationGeneree = new Information<Boolean>();
        Random random = new Random();
        for (int i = 0; i < l; i++) {
        	informationGeneree.add(random.nextBoolean());
        }
    }

    /**
     * Constructeur d'une source aleatoire personnalisable supportant une graine de generation.
     * Elle envoie un message de longueur personnalisee a  partir d'une graine personnalisee en booleens.
     * C'est la graine qui definit les probabilites des symboles.
     * 
     * @param l - Longueur du message binaine a  envoyer
     * @param seed - Graine pour l'aleatoire
     */
    public SourceAleatoire (int l, int seed) {
        informationGeneree = new Information<Boolean>();
        Random random = new Random(seed);
        for (int i = 0; i < l; i++) {
        	informationGeneree.add(random.nextBoolean());
        }
    }

}
