package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * La classe Emetteur specialise la classe abstraite Transmetteur pour faire de l'emission.
 * Un emetteur prend l'information generee par une source et la transmet a un autre transmetteur.
 * 
 * @author groupeA3
 * @date 2022/09/15
 */
public class Emetteur extends Transmetteur<Boolean, Float> {

	/** Information contenant l'information recue par l'emetteur de la source, une fois formatee/convertie au format voulu */
	private Information<Float> informationGeneree;

	/** Precise le format de generation de l'information voulu */ 
	private String forme;

	/** Nombre d'echantillons avec lequel proceder a l'echantillonnage du signal */
	private int nbEch;

	/** Valeur analogique minimale accessible par l'information (amplitude) */
	private float Amin;

	/** Valeur analogique maximale accessible par l'information (amplitude) */
	private float Amax;

	/**
	 * Le constructeur definit tous les attributs pour creer une instance de la classe.
	 * 
	 * @param forme
	 * @param nbEch
	 * @param Amin
	 * @param Amax
	 */
	public Emetteur(String forme, int nbEch, float Amin, float Amax){
		this.forme = forme;
		this.nbEch = nbEch;
		this.Amin = Amin;
		this.Amax = Amax;
	}

	/**
	 * Convertit l'information (booleenne) recue en information (analogique) generee selon le format voulu.
	 * Le format est defini par l'attribut forme.
	 * 
	 * Valeurs reconnues :
	 * - "RZ"
	 * - "NRZ"
	 * - "NRZT"
	 */
	public void convert() {
		informationGeneree = new Information<>();

		if (forme.equals("RZ")) {
			convertToRZ();
		} else if (forme.equals("NRZ")) {
			convertToNRZ();
		} else if (forme.equals("NRZT")) {
			convertToNRZT();
		}
	}

	/**
	 * Methode principale de l'emetteur.
	 * Elle sert a recevoir l'information d'une source, a la convertir au format voulu, puis a l'emettre vers les transmetteurs connect√©s.
	 */
	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		informationRecue = information;
		convert();
		emettre();
	}

	/**
	 * Methode d'emission de l'information generee vers les transmetteurs connectes.
	 * Elle intervient typiquement apres formatage.
	 */
	@Override
	public void emettre() throws InformationNonConformeException {
		informationEmise = informationGeneree;
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

	/**
	 * Convertit l'information booleenne recue en analogique RZ.
	 */
	protected void convertToRZ() {
		for (Boolean value: informationRecue) {
			if (value) {
				for (int i = 0; i < nbEch; i++) {
					if (i < nbEch/3) {
						informationGeneree.add(Amin);
					}
					else if (i> 2*nbEch/3) {
						informationGeneree.add(Amin);
					}
					else {
						informationGeneree.add(Amax);
					}
				}
			}
			else {
				for(int i = 0; i< nbEch; i++) {
					informationGeneree.add(Amin);
				}
			}
		}
	}

	/**
	 * Convertit l'information booleenne recue en analogique NRZ.
	 */
	protected void convertToNRZ() {
		for (Boolean value: informationRecue) {
			for (int i = 0; i < nbEch; i++) {
				if (value) {
					informationGeneree.add(Amax);
				}
				else {
					informationGeneree.add(Amin);
				}
			}
		}
	}

	/**
	 * Convertit l'information booleenne recue en analogique NRZT.
	 */
	protected void convertToNRZT() {
		int echExtra = nbEch % 3;
		int periodeCourte = nbEch / 3; // nombre d'echantillons du permier et du troisieme tiers de chaque symbole
		int periodeLongue = periodeCourte + echExtra; // nombre d'echantillons du deuxieme tiers de chaque symbole
		// periodeLongue est plus long que periodeCourte de 1 ou de 2 en cas de non-divisibilitÈ
		float difference = Amax - Amin;
		float moyenne = (Amax + Amin) / 2;
		float coef = difference / (2*periodeCourte);
		boolean prec = informationRecue.iemeElement(0);

		if (prec) {
			generationAffine(coef, periodeCourte, moyenne);
			generationAffine(0, periodeLongue, Amax);
		} else {
			generationAffine(-coef, periodeCourte, moyenne);
			generationAffine(0, periodeLongue, Amin);
		}

		for (int i = 0; i < informationRecue.nbElements(); i++) {
			if (i + 1 >= informationRecue.nbElements()) { // si prec est le dernier symbole
				if (prec)
					generationAffine(0, periodeCourte, Amax);
				else
					generationAffine(0, periodeCourte, Amin);
				break;
			}

			if (prec && informationRecue.iemeElement(i)) {
				generationAffine(0, nbEch, Amax);
			} else if (!prec && !informationRecue.iemeElement(i)) {
				generationAffine(0, nbEch, Amin);
			} else if (!prec && informationRecue.iemeElement(i)) {
				generationAffine(coef, 2*periodeCourte, Amin);
				generationAffine(0, periodeLongue, Amax);
			} else {
				generationAffine(-coef, 2*periodeCourte, Amax);
				generationAffine(0, periodeLongue, Amin);
			}

			prec = informationRecue.iemeElement(i + 1);
		}
	}

	/**
	 * Ajoute une portion de droite affine a l'information generee
	 * 
	 * @param a - Le coefficient directeur
	 * @param t - La taille de l'intervalle
	 * @param b - L'ordonnee a l'origine
	 */
	private void generationAffine(float a, int t, float b) {
		for (int x = 0; x < t; x++) {
			informationGeneree.add(a*x + b);
		}
	}
}
