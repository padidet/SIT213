package sourcesTest;

import sources.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import information.*;
class SourceAleatoireTest {

	 @Test
	    public void testConstructeur() {
	        int mess_length = 1024;
	        SourceAleatoire instance = new SourceAleatoire(mess_length);

	        assertEquals(instance.getInformationGeneree().nbElements(), mess_length);

	        int mess_with_seed_length = 300;
	        int mess_with_seed_seed = 42;
	        SourceAleatoire instance_with_seed1 = new SourceAleatoire(mess_with_seed_length, mess_with_seed_seed);
	        assertEquals(instance_with_seed1.getInformationGeneree().nbElements(), mess_with_seed_length);
	        SourceAleatoire instance_with_seed2 = new SourceAleatoire(mess_with_seed_length, mess_with_seed_seed);
	        assertEquals(instance_with_seed2.getInformationGeneree().nbElements(), mess_with_seed_length);
	        
	        assertEquals(instance_with_seed2.getInformationGeneree(), instance_with_seed1.getInformationGeneree());

	    }

}
