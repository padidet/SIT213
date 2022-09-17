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
	 * Elle sert a recevoir l'information d'une source, a la convertir au format voulu, puis a l'emettre vers les transmetteurs connectés.
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
			if (value == true) {
				for(int i = 0; i < nbEch; i++) {
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
		for (Boolean value:informationRecue) {
			for(int i = 0; i < nbEch; i++) {
				if(value == true) {
					informationGeneree.add(Amax/* - bruit */);
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
		if (informationRecue.nbElements() < 2) {
			
			float moyenne = (Amax + Amin) / 2;
			float coefMax = Amax - moyenne;
			float coefMin = moyenne - Amin;
			float prec = moyenne;
			
			if (informationRecue.iemeElement(0)) { 
				for (int i = 0; i < nbEch; i++) {
					
					if (i < nbEch * 2/3) {
						prec = Amax;
						informationGeneree.add(Amax);
					}
					if (i > nbEch * 2/3) {
						prec -= (3*coefMax/nbEch);
						informationGeneree.add(prec);
					}
				}
			}
			else 
			{
				for (int i = 0; i < nbEch; i++) {
					
					if (i < nbEch * 2/3) {
						prec = Amin;
						informationGeneree.add(Amin);
					}
					if (i > nbEch * 2/3) {
						prec += (3 * Math.abs(coefMin) / nbEch);
						informationGeneree.add(prec);
					}
				}
			}
		}
	}
	/* a faire pour le calcul du SNR
	public Information<Float> getSignalAnalogiqueEntree(){
		return this.informationGeneree;
	}
	//*/
}
