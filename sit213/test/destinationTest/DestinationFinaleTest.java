package destinationTest;

import information.*;
import destinations.*;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DestinationFinaleTest {

	@Test
    public void testRecevoir() {
        System.out.println("Test recevoir");
        Boolean bits[] = {true, false, true};
        Information<Boolean> information = new Information<>(bits);
        DestinationFinale instance = new DestinationFinale();
        try {
            instance.recevoir(information);
        } catch (InformationNonConformeException e) {
            fail("The test fail because recevoir didn't accept bool array.");
        }
        Information<Boolean> informationRecue = instance.getInformationRecue();

        assertEquals(informationRecue, information);
    }

}
