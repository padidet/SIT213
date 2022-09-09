package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/** 
 * Un transmetteur ideal.
 * On s'attend a  un taux d'erreur binaire nul venant de lui.
 */
public class TransmetteurParfait extends Transmetteur<Boolean, Boolean> {

	/**
	 * Permet de recevoir une information.
	 * 
	 * @param information - Information a  recevoir
	 * @throws InformationNonConformeException 
	 */
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
    	this.informationRecue = information;
    	this.emettre();
    }

    /**
     * Permet d'emettre une information.
     * Elle est envoyee a  toutes les destinations connectees.
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
