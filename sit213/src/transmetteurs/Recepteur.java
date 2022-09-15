package transmetteurs;

import java.util.Objects;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class Recepteur extends Transmetteur<Float, Boolean>{
	private Information<Boolean> informationGeneree;
	
	private String forme;
	
	private int nbEch;
	
	private float Amin;
	
	private float Amax;
	
	public Recepteur(String forme, int nbEch, float Amin, float Amax) {
		
		this.forme = forme;
		this.nbEch = nbEch;
		this.Amin = Amin;
		this.Amax = Amax;
		
	}
	
	public void convert() {
		informationGeneree = new Information<>();
		if(Objects.equals(forme,"RZ")) {
			convertToRZ();
		}
		if(Objects.equals(forme, "NRZ")) {
			convertToNRZ();
		}
		if(Objects.equals(forme,"NRZT")) {
			convertNRZT();
		}
	}
	
	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		// TODO Auto-generated method stub
		informationRecue = information;
		convert();
		emettre();
		
	}

	@Override
	public void emettre() throws InformationNonConformeException {

		for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationEmise);
		}
	}
	
	/**
	 * @author : Groupe 3
	 * @date : 15/09/2022
	 * @param : aucun
	 * Fonction permettant de convertir du RZ en numérique.
	 */
	protected void convertToRZ() {
		
		for(int j = 0; j < informationRecue.nbElements(); j += nbEch) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // Le seuil de tolérence a définir pour plus tard;
			for(int i = 0; i < nbEch; i++) {
				
				// On cherche parmi les valeurs au milieu du symbole
				// si la moyenne est égale à Amax (sans seuil de tolérence):
				if(nbEch/3 < i &&  i < 2 * nbEch/3) {
					somme += informationRecue.iemeElement(j + i);	// (j + i) correspond à l'indice total.
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
	 * @author : Groupe 3
	 * @date : 15/09/2022
	 * @param : aucun
	 * Fonction permettant de convertir du NRZ en numérique.
	 */
	protected void convertToNRZ() {
		for(int j = 0; j < informationRecue.nbElements(); j += nbEch) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // Le seuil de tolérence a définir pour plus tard;
			for(int i = 0; i < nbEch; i++) {
				somme += informationRecue.iemeElement(j + i);
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
	 * @author : Groupe 3
	 * @date : 15/09/2022
	 * @param : aucun
	 * Fonction permettant de convertir du NRZT en numérique.
	 */
	protected void convertNRZT() {
		for(int j = 0; j < informationRecue.nbElements(); j += nbEch) {
			float somme = 0;
			float moyenne = 0;
			// float seuilTolerence = 0; // Le seuil de tolérence a définir pour plus tard;
			for(int i = 0; i < nbEch; i++) {
				
				// On cherche parmi les valeurs au milieu du symbole
				// si la moyenne est égale à Amax (sans seuil de tolérence):
				if(nbEch/3 < i &&  i < 2 * nbEch/3) {
					somme += informationRecue.iemeElement(j + i);
				}
			}
			moyenne = somme/(nbEch/3);
			if(moyenne >= Amax) {		// Remplacer Amax par Amax - seuilTolerence 
				informationGeneree.add(true);
			}
			else {
				informationGeneree.add(false);
			}
		}
	}
}
