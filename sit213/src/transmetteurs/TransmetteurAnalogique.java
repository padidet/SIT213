package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurAnalogique extends Transmetteur<Float, Float>{
	
	@Override
	public void emettre() throws InformationNonConformeException {
		// TODO Auto-generated method stub
		this.informationEmise = this.informationRecue;
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

	@Override
		public void recevoir(Information<Float> information) throws InformationNonConformeException {
			// TODO Auto-generated method stub
		this.informationRecue = information;
    	this.emettre();
		}
	
	
}
