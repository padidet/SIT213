package transmetteurs;

import java.util.*;

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
	/*
	protected void convertToNRZT() {
		for (int i = 0; i < informationRecue.nbElements(); i++) {
			for (int j = 1; j <= nbEch; j++) {
				if (informationRecue.iemeElement(i)) {
					if (j <= nbEch / 3) {
						informationGeneree.add(Amin + ((Amax - Amin) / (nbEch / 3)) * (j - 1));
					}
					else if ((j > nbEch / 3) && (j <= nbEch / 3 * 2)) {
						informationGeneree.add(Amax);
					}
					else {
						informationGeneree.add(Amin + ((Amax - Amin) / (nbEch / 3)) * (nbEch - j));
					}
				}
				else {
					informationGeneree.add(Amin);	
				}
			}
		}
	}
	//*/
	
	/**
	 * Il y a 8 modeles. Ce choix d'implementation est plus lourd mais plus simple.
	 * 
	 * Pour la première valeur:
	 * H pour Haut;
	 * B pour Bas;
	 * 
	 * Pour la seconde valeur:
	 * S pour Stable en debut;
	 * P pour Pente  en debut;
	 * 
	 * Pour la seconde valeur:
	 * S pour Stable en fin;
	 * P pour Pente  en fin;
	 * 
	 * Modele 1: HSS
	 * Modele 2: HSP
	 * Modele 3: HPS
	 * Modele 4: HPP
	 * Modele 5: BSS
	 * Modele 6: BSP
	 * Modele 7: BPS
	 * Modele 8: BPP
	 * 
	 */
	private float[] modeleNRZT(String modele, int nbEch, float Amax, float offSet) {
		float[] valeursRetour = new float[nbEch];
		// Modele 1:
		if     (modele == "HSS") {
			for(int i=0; i < nbEch; i++) {
				valeursRetour[i] = Amax + offSet;
			}
		}
		// Modele 2:
		else if(modele == "HSP") {
			for(int i=0; i < nbEch; i++) {
				if(i < 2*nbEch/3) {
					valeursRetour[i] = Amax + offSet;
				}
				else {
					valeursRetour[i] = (Amax * (nbEch - i)/nbEch * 3) + offSet;
				}
			}
		}
		// Modele 3:
		else if(modele == "HPS") {
			for(int i=0; i < nbEch; i++) {
				if(i < nbEch/3) {
					valeursRetour[i] = (Amax * i/nbEch * 3) + offSet;
				}
				else {
					valeursRetour[i] = Amax + offSet;
				}
			}
		}
		// Modele 4:
		else if(modele == "HPP") {
			for(int i=0; i < nbEch; i++) {
				if(i < nbEch/3) {
					valeursRetour[i] = (Amax * i/nbEch * 3) + offSet;
				}
				else if(i >= nbEch/3 && i < 2*nbEch/3){
					valeursRetour[i] = Amax + offSet;
				}
				else {
					valeursRetour[i] = (Amax * (nbEch - i)/nbEch * 3) + offSet;
				}
			}
		}
		// Modele 5:
		else if(modele=="BSS") {
			for(int i=0; i < nbEch; i++) {
				valeursRetour[i] = -Amax + offSet;
			}
		}
		// Modele 6:
		else if(modele=="BSP") {
			for(int i=0; i < nbEch; i++) {
				if(i < 2 * nbEch/3) {
					valeursRetour[i] = -Amax + offSet;
				}
				else {
					valeursRetour[i] = -(Amax * (nbEch - i)/nbEch * 3) + offSet;
				}
			}
		}
		// Modele 7:
		else if(modele=="BPS") {
			for(int i=0; i < nbEch; i++) {
				if(i < nbEch/3) {
					valeursRetour[i] = -(Amax * i/nbEch * 3) + offSet;
				}
				else {
					valeursRetour[i] = -Amax + offSet;
				}
			}
		}
		// Modele 8:
		else if(modele=="BPP") {
			for(int i=0; i < nbEch; i++) {
				if(i < nbEch/3) {
					valeursRetour[i] = -(Amax * i/nbEch * 3) + offSet;
				}
				else if(i >= nbEch/3 && i < 2*nbEch/3){
					valeursRetour[i] = -Amax + offSet;
				}
				else {
					valeursRetour[i] = -(Amax * (nbEch - i)/nbEch * 3) + offSet;
				}
			}
		}
		return valeursRetour;
	}
	
	/**
	 * Convertit l'information booleenne recue en analogique NRZT.
	 * @implNote Dans le cas ou c'est le premier bit, on ajoute une pente montante ou descendante. Au total, il y aura 8 modeles.
	 * message a utiliser pour tester NRZT : 0001011100 et 100010111001
	 */
	protected void convertToNRZT() {
		
		float offset = 0.0f; // Valeur que l'on peut modifier en tant que parametre.
		for (int i = 0; i < informationRecue.nbElements(); i++) {
			float[] listeValeurs = new float[nbEch];
			
			// Le cas ou c'est le premier element de la liste:
			if(i == 0) {
				if(informationRecue.iemeElement(i) == true) {
					if(informationRecue.iemeElement(i+1) == null || informationRecue.iemeElement(i+1) == false) {
						listeValeurs = modeleNRZT("HPP", nbEch, Amax, offset);
					}
					else {
						listeValeurs = modeleNRZT("HPS", nbEch, Amax, offset);
					}
				}
				else {
					if(informationRecue.iemeElement(i+1) == null || informationRecue.iemeElement(i+1) == true) {
						listeValeurs = modeleNRZT("BPP", nbEch, Amax, offset);
					}
					else {
						listeValeurs = modeleNRZT("BPS", nbEch, Amax, offset);
					}
				}
			}
			
			// Vérifie si c'est le dernier element de la liste:
			else if (i == informationRecue.nbElements() - 1) {
				if(informationRecue.iemeElement(i) == true) {
					if(informationRecue.iemeElement(i-1) == null || informationRecue.iemeElement(i-1) == false) {
						listeValeurs = modeleNRZT("HPP", nbEch, Amax, offset);
					}
					else {
						listeValeurs = modeleNRZT("HSP", nbEch, Amax, offset);
					}
				}
				else {
					if(informationRecue.iemeElement(i-1) == null || informationRecue.iemeElement(i-1) == true) {
						listeValeurs = modeleNRZT("BPP", nbEch, Amax, offset);
					}
					else {
						listeValeurs = modeleNRZT("BSP", nbEch, Amax, offset);
					}
				}
			}
			
			// L'element binaire est precede et succede par des elements binaires:
			else {
				if(informationRecue.iemeElement(i) == true) {
					if(informationRecue.iemeElement(i-1) == true) {
						if(informationRecue.iemeElement(i+1) == true) {
							listeValeurs = modeleNRZT("HSS", nbEch, Amax, offset);
						}
						else {
							listeValeurs = modeleNRZT("HSP", nbEch, Amax, offset);
						}
					}
					else {
						if(informationRecue.iemeElement(i+1) == true) {
							listeValeurs = modeleNRZT("HPS", nbEch, Amax, offset);
						}
						else {
							listeValeurs = modeleNRZT("HPP", nbEch, Amax, offset);
						}
					}
				}
				else {
					if(informationRecue.iemeElement(i-1) == true) {
						if(informationRecue.iemeElement(i+1) == true) {
							listeValeurs = modeleNRZT("BPP", nbEch, Amax, offset);
						}
						else {
							listeValeurs = modeleNRZT("BPS", nbEch, Amax, offset);
						}
					}
					else {
						if(informationRecue.iemeElement(i+1) == true) {
							listeValeurs = modeleNRZT("BSP", nbEch, Amax, offset);
						}
						else {
							listeValeurs = modeleNRZT("BSS", nbEch, Amax, offset);
						}
					}
				}
			}
			
			
			for(int j=0; j < nbEch; j++) {
				informationGeneree.add(listeValeurs[j]);
			}
		}
	}
	
	
	//* a faire pour le calcul du SNR
	public Information<Float> getSignalAnalogiqueEntree(){
		return this.informationGeneree;
	}
	//*/
}
