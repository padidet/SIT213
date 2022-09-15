package transmetteurs;

import java.util.Objects;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class Emetteur extends Transmetteur<Boolean, Float>{
	private Information<Float> informationGeneree;
	
	private String forme;
	
	private int nbEch;
	
	private float Amin;
	
	private float Amax;

	public Emetteur(String forme, int nbEch, float Amin, float Amax){
		
		
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
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		// TODO Auto-generated method stub
		informationRecue = information;
		
		
	}
	
	@Override
	public void emettre() throws InformationNonConformeException {
		// TODO Auto-generated method stub
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}
	
	protected void convertToRZ() {
		for(Boolean value:informationRecue) {
			if(value == true) {
				for(int i = 0; i < nbEch; i++) {
					if(i < nbEch/3 || i >= nbEch * (2/3)) {
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
	
	protected void convertToNRZ() {
		for(Boolean value:informationRecue) {
			if(value == true) {
				for(int i = 0; i < nbEch; i++) {
					informationGeneree.add(Amax);
				}
			}
			else {
				for(int i = 0; i < nbEch; i++) {
					informationGeneree.add(Amin);
				}
			}
		}
	}	
	
	protected void convertNRZT() {
		for(Boolean value:informationRecue) {
			
			if(value == true) {
				for(int i = 0; i < nbEch; i++) {
					if(i < nbEch / 3) {
						informationGeneree.add(i * (Amax - ((Amax + Amin) / 2))/ (nbEch / 3) + (Amax + Amin) / 2);
					}
					else if(i < 2 * nbEch/3 ) {
						informationGeneree.add(Amax);
					}
					else {
						informationGeneree.add(i * (Amin-Amax) / (2 * nbEch/3 ) + Amax + (Amax - Amin));
					}
				}
			}
			else {
				for(int i = 0; i < nbEch; i++) {
					if(i < nbEch /3) {
						informationGeneree.add(i * (Amin - ((Amax + Amin) / 2))/ (nbEch / 3) + (Amax - ((Amax + Amin))));
					}
					else if(i < 2 * nbEch/3 ) {
						informationGeneree.add(Amin);
					}
					else {
						informationGeneree.add(i * (Amax - Amin) / (2 * nbEch/3 ) + Amin - (Amax - Amin));
					}
				}
			}
		}
		
	}

}

