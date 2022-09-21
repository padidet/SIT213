package sourcesTest;

import sources.*;
import information.*;
import simulateur.ArgumentsException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SourceFixeTest {

	@Test
    public void testConstructeur() {
        System.out.println("Test Constucteur SourceFixe");
        String mess = "01011";
        SourceFixe instance;
		try {
			instance = new SourceFixe(mess);
			assertEquals(instance.getInformationGeneree().nbElements(), mess.length());
	        Boolean bits[] = {false, true, false, true, true};
	        Information<Boolean> information = new Information<>(bits);
	        assertEquals(instance.getInformationGeneree(), information);
		} catch (ArgumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        

        String mess2 = "1101011";
        SourceFixe instance2;
		try {
			instance2 = new SourceFixe(mess2);
			assertEquals(instance2.getInformationGeneree().nbElements(), mess2.length());
	        Boolean bits2[] = {true, true, false, true, false, true, true};
	        Information<Boolean> information2 = new Information<>(bits2);
	        assertEquals(instance2.getInformationGeneree(), information2);
		} catch (ArgumentsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
    }

}
