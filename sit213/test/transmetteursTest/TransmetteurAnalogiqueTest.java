package transmetteursTest;

import information.*;
import transmetteurs.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransmetteurAnalogiqueTest {

	@Test
    public void testRecevoir() throws Exception {
        Float bits[] = {-1.0f, 2.0f, 0.0f};
        Information<Float> information = new Information<>(bits);
        TransmetteurAnalogique instance = new TransmetteurAnalogique();
        Recepteur recepteurAnalogique = new Recepteur("RZ", 3, -2f, 2f);
        instance.connecter(recepteurAnalogique);
        instance.recevoir(information);

        assertEquals(instance.getInformationRecue(), information);
        assertEquals(instance.getInformationEmise(), information);
        assertEquals(recepteurAnalogique.getInformationRecue(), information);
    }
	
	@Test
    public void testReceptionVide() throws Exception {
        TransmetteurAnalogique instance = new TransmetteurAnalogique();
        assertEquals(instance.getInformationRecue(), null);
    }
	
	
	@Test
    public void testEmettre() throws Exception {
        TransmetteurParfait instance = new TransmetteurParfait();
        instance.emettre();
        assertEquals(instance.getInformationEmise(), null);
    }
}
