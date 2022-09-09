package information;

import information.Information;

/**
 * Une information analogique est utilisée pour transmettre la représentation analogique d'un signal.
 * En réalité, il y aura eu échantillonnage à la source.
 */
public class InformationAnalogique extends Information<Float> {

	private int nbEchantillonsParSymbole;

	/**
	 * pour construire une information analogique à partir
	 * 
	 * @param content - le tableau d'éléments (cf. classe Information parente)
	 * @param nbEchantillonsParSymbole - le nombre d'échantillons par symbole (a priori, il divise la longueur de content)
	 */
	public InformationAnalogique(Float [] content, int nbEchantillonsParSymbole) {
		super(content);
		this.nbEchantillonsParSymbole = nbEchantillonsParSymbole;
	}

}
