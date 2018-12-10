import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.MFCCHelper;

public class myMFCCdistance extends MFCCHelper {

	@Override
	public float distance(MFCC mfcc1, MFCC mfcc2) {
		// compute the distance between 2 MFCC
		float rez = 0;
		for (int i = 0; i< mfcc1.getLength(); i++){
			
			rez+=Math.pow(mfcc1.getCoef(i)-mfcc2.getCoef(i),2);
			
		}
		return (float) Math.sqrt(rez);
	}

	@Override
	public float norm(MFCC mfcc) {
		// return the value of the  MFCC norm (which is the coefficient 0 of the MFCC)  
		// this measure permits detecting if it's a word or a silence
		
		return mfcc.getCoef(0);
	}

	@Override
	public MFCC unnoise(MFCC mfcc, MFCC noise) {
		// suppress the noise from the MFCC in input parameter
		// substract each noise coefficient at each MFCC coefficient in input parameter 
		
		
		return null;
	}

}
