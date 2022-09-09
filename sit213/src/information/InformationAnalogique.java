package information;

import information.Information;

/**
 * Une information analogique est utilis�e pour transmettre la repr�sentation analogique d'un signal.
 * En r�alit�, il y aura eu �chantillonnage � la source.
 */
public class InformationAnalogique extends Information<Float> {

	private int nbEchantillonsParSymbole;

	/**
	 * pour construire une information analogique � partir
	 * 
	 * @param content - le tableau d'�l�ments (cf. classe Information parente)
	 * @param nbEchantillonsParSymbole - le nombre d'�chantillons par symbole (a priori, il divise la longueur de content)
	 */
	public InformationAnalogique(Float [] content, int nbEchantillonsParSymbole) {
		super(content);
		this.nbEchantillonsParSymbole = nbEchantillonsParSymbole;
	}

}
