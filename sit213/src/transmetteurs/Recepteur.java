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
	
	protected void convertToRZ() {
		for(float value:informationRecue) {
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
		if(informationRecue.nbElements()<2) {
			
			float moyenne = (Amax+Amin)/2;
			float coefMax = Amax - moyenne;
			float coefMin = moyenne - Amin;
			float prec = moyenne;
			
			if(informationRecue.iemeElement(0)) { 
				for(int i=0; i<nbEch; i++) {
					
					if(i<nbEch*2/3) {
						prec = Amax;
						informationGeneree.add(Amax);
					}
					if(i> nbEch * 2/3) {
						prec -= (3*coefMax/nbEch);
						informationGeneree.add(prec);
					}
				}
			}
			else 
			{
				for(int i=0; i<nbEch; i++) {
					
					if(i<nbEch*2/3) {
						prec = Amin;
						informationGeneree.add(Amin);
					}
					if(i> nbEch * 2/3) {
						prec += (3*Math.abs(coefMin)/nbEch);
						informationGeneree.add(prec);
					}
				}
			}
		}
		
	}
}
