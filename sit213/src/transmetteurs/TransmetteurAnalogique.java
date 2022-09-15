package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurAnalogique extends Transmetteur<Float, Float>{
	
	@Override
	public void emettre() throws InformationNonConformeException {
		this.informationEmise = this.informationRecue;
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

	@Override
		public void recevoir(Information<Float> information) throws InformationNonConformeException {
		this.informationRecue = information;
    	this.emettre();
		}
	
	
}
