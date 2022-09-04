package sources;

import information.Information;

/**
 * Une source qui envoie toujours le même message
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe () {
        informationGeneree = new Information<Boolean>();
        informationGeneree.add(true);
        informationGeneree.add(false);
        informationGeneree.add(true);
        informationGeneree.add(true);
        informationGeneree.add(false);
        informationGeneree.add(true);
    }

}
