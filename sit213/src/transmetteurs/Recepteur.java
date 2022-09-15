package transmetteurs;

import java.util.Objects;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * La classe Recepteur specialise la classe abstraite Transmetteur pour faire de la reception.
 * Un recepteur prend l'information transmise par un autre transmetteur et la transmet a une destination.
 * 
 * @author: Groupe 3
 * @date: 2022/09/15
 */
public class Recepteur extends Transmetteur<Float, Boolean> {

	/** Information contenant l'information recue par le recepteur d'un transmetteur, une fois formatee/convertie au format voulu */
	private Information<Boolean> informationGeneree;

	/** Precise le format de reception de l'information voulu */ 
	private String forme;

	/** Nombre d'echantillons avec lequel proceder a l'echantillonnage du signal */
	private int nbEch;

	/** Valeur analogique minimale accessible par l'information (amplitude) */
	private float Amin;

	/** Valeur analogique maximale accessible par l'information (amplitude) */
	private float Amax;

	/**
	 * Le constructeur definit tous les attributs pour creer une nouvelle instance de la classe.
	 * 
	 * @param forme
	 * @param nbEch
	 * @param Amin
	 * @param Amax
	 */
	public Recepteur(String forme, int nbEch, float Amin, float Amax) {
		this.forme = forme;
		this.nbEch = nbEch;
		this.Amin = Amin;
		this.Amax = Amax;
	}

	/**
	 * Convertit l'information (analogique) recue en information (booleenne) generee selon le format voulu.
	 * Le format est defini par l'attribut forme.
	 */
	public void convert() {
		informationGeneree = new Information<>();
		if(Objects.equals(forme,"RZ")) {
			convertFromRZ();
		}
		if(Objects.equals(forme, "NRZ")) {
			convertFromNRZ();
		}
		if(Objects.equals(forme,"NRZT")) {
			convertFromNRZT();
		}
	}

	/**
	 * Methode principale du recepteur.
	 * Elle sert a recevoir l'information d'un autre transmetteur, a la convertir au format voulu, puis a l'emettre vers les destinations connectees.
	 */
	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		informationRecue = information;
		convert();
		emettre();
	}

	/**
	 * Methode d'emission de l'information generee vers les destinations connectees.
	 * Elle intervient typiquement apres formatage.
	 */
	public void emettre() throws InformationNonConformeException {
		for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationEmise);
		}
	}
	
	/**
	 * Convertit l'information analogique RZ recue en booleen.
	 */
	protected void convertFromRZ() {
		
		
		for(float value: informationRecue) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // Le seuil de tolérence a définir pour plus tard;
			for(int i = 0; i < nbEch; i++) {
				
				// On cherche parmi les valeurs au milieu du symbole
				// si la moyenne est égale à Amax (sans seuil de tolérence):
				if(nbEch/3 < i &&  i < 2 * nbEch/3) {
					somme += value;
				}
			}
			moyenne = somme/(nbEch/3);
			if(moyenne >= Amax) {		// Remplacer Amax par Amax - seuilTolerence 
				informationGeneree.add(true);
			}
			else if(moyenne <= Amin) {
				informationGeneree.add(false);
			}
			else {
				informationGeneree.add(false);
			}
		}
	}
	
	/**
	 * Convertit l'information analogique NRZ recue en booleen.
	 */
	protected void convertFromNRZ() {
		for(float value:informationRecue) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // Le seuil de tolérence a définir pour plus tard;
			for(int i = 0; i < nbEch; i++) {
				somme += value;
			}
			moyenne = somme/(nbEch);
			if(moyenne >= Amax) {		// Remplacer (Amax) par (Amax - seuilTolerence) 
				informationGeneree.add(true);
			}
			else if(moyenne <= Amin) {		// Remplacer (Amin) par (Amin - seuilTolerence)
				informationGeneree.add(false);
			}
			else {
				informationGeneree.add(false);	// Le cas où le symbole est centré en 0. N'arrive jamais sans bruit normalement.
			}
		}
	}	
	
	/**
	 * Convertit l'information analogique NRZT recue en booleen.
	 */
	protected void convertFromNRZT() {
		for(float value:informationRecue) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // TODO: seuil de tolerence à définir
			for(int i = 0; i < nbEch; i++) {
				
				// On cherche parmi les valeurs au milieu du symbole
				// si la moyenne est égale à Amax (sans seuil de tolerence):
				if(nbEch/3 < i &&  i < 2 * nbEch/3) {
					somme += value;
				}
			}
			moyenne = somme/(nbEch/3);
			if(moyenne >= Amax) { // A terme, remplacer Amax par Amax - seuilTolerence 
				informationGeneree.add(true);
			}
			else {
				informationGeneree.add(false);
			}
		}
	}
}
