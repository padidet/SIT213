package transmetteursTest;

import information.*;
import transmetteurs.*;
import destinations.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransmetteurParfaitTest {

	@Test
    public void testRecevoir() throws Exception {
        Boolean bits[] = {true, false, true};
        Information<Boolean> information = new Information<>(bits);
        TransmetteurParfait instance = new TransmetteurParfait();
        DestinationFinale dest = new DestinationFinale();
        instance.connecter(dest);
        instance.recevoir(information);

        assertEquals(instance.getInformationRecue(), information); 
        assertEquals(instance.getInformationEmise(), information);
        assertEquals(dest.getInformationRecue(), information);
    }
	
	@Test
    public void testEmettre() throws Exception {
        TransmetteurParfait instance = new TransmetteurParfait();
        instance.emettre();
        assertEquals(instance.getInformationEmise(), null);
    }

}
