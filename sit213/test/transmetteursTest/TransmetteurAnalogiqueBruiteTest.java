package transmetteursTest;

import information.*;
import transmetteurs.*;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransmetteurAnalogiqueBruiteTest {

	 @Test
	    public void testRecevoir() throws Exception {
	        Float bits[] = {-1.0f, 2.0f, 0.0f};
	        Information<Float> information = new Information<>(bits);
	        TransmetteurAnalogiqueBruite instance = new TransmetteurAnalogiqueBruite(-1,3000);
	        Recepteur recepteurAnalogique = new Recepteur("RZ", 3, -2f, 2f);
	        instance.connecter(recepteurAnalogique);
	        instance.recevoir(information);

	        assertEquals(instance.getInformationRecue(), information);
	        assertEquals(instance.getInformationEmise(), information);
	        assertEquals(information, recepteurAnalogique.getInformationRecue());
	    }

}
