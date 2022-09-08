package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/** 
 * Un transmetteur idéal.
 * On s'attend à un taux d'erreur binaire nul venant de lui.
 */
public class TransmetteurParfait extends Transmetteur<Boolean, Boolean> {

	/**
	 * Permet de recevoir une information.
	 * 
	 * @param information - Information à recevoir
	 * @throws InformationNonConformeException 
	 */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
    	this.informationRecue = information;
    	this.emettre();
    }

    /**
     * Permet d'émettre une information.
     * Elle est envoyée à toutes les destinations connectées.
     * 
     * @throws InformationNonConformeException
     */
    @Override
    public void emettre() throws InformationNonConformeException {
    	this.informationEmise = this.informationRecue;
    	for (DestinationInterface<Boolean> destination : this.destinationsConnectees) {
    		destination.recevoir(this.informationEmise);
    	}
    }
}
