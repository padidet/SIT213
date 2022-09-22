package transmetteurs;

import java.util.Random;

import javax.tools.Tool;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurAnalogiqueBruite extends Transmetteur<Float,Float>{
	
	protected Information<Float> informationBruit;
    protected float SNR;
    protected int nbEch;
	
    public TransmetteurAnalogiqueBruite(float SNR, int nbEch) {
        super();
        
        this.nbEch = nbEch;
        this.SNR = SNR;
        informationBruit = null;
    }

	
	@Override
	public void emettre() throws InformationNonConformeException {
		ajouterBruit();
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
		
	}
	
	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		this.informationRecue = information;
		emettre();
	}
	
	protected  void ajouterBruit() {
		
		
		float sigma = calculSigma();
		
		 if(SNR == -1) {
			 informationEmise = informationRecue;
		 }
		 else {
			 for(float s : informationRecue) {
					s+=genererBruit(sigma); 
					informationBruit.add(s); 
				}
		 }
	}
	
	protected float genererBruit(float sigmaB) {
		Random a1 = new Random();
		Random a2 = new Random();
			
		return (float)(sigmaB*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat()));
		
	}
	
	public float calculSigma() {
		
		float sigma;
		
		float ps = calculPuissance(informationRecue);
		
		float logSnr = (float)Math.pow(10, SNR/10);
		
		sigma = (float)Math.sqrt((ps*nbEch)/(2*logSnr));
		
		return sigma;
	}
	
public float calculPuissance(Information<Float> informationAnalogique) {
		
		float puissanceMoyenneSignal=0;
		float sommeSignaux=0;
		
		for(float f : informationAnalogique) {
			sommeSignaux += Math.pow(f,2);
		}
		
		puissanceMoyenneSignal = sommeSignaux/informationAnalogique.nbElements(); // 1/K
		
		return puissanceMoyenneSignal;
	}

}
