import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Field;
import fr.enseeiht.danck.voice_analyzer.MFCC;

public class myDTW extends DTWHelper {
	public float minOf3(float a, float b, float c) {
		if ((a<b) && (a<c)) {return a;}
		else if (b<c) {return b;}
		else {return c;}
		
	}
	public float Gij( float matrix[][],myMFCCdistance mfcc,Field x, Field y, float w[], int i, int j) {
		float r1 = matrix[i-1][j] + w[0]*mfcc.distance(x.getMFCC(i-1),y.getMFCC(j-1)),
		r2 = matrix[i-1][j-1] + w[1]*mfcc.distance(x.getMFCC(i-1),y.getMFCC(j-1)),
		r3 = matrix[i][j-1] + w[2]*mfcc.distance(x.getMFCC(i-1),y.getMFCC(j-1));
		return minOf3(r1,r2,r3);
	}
	@Override
	public float DTWDistance(Field x, Field y) {
		// Method which computes the DTW score between two sets of MFCC		
		float [ ] [ ] g = new float [x.getLength()+1] [y.getLength()+1];
		float [ ] w = {1.4f,0.9f,1.15f};
		g[0][0]=0;
		myMFCCdistance mfcc = new myMFCCdistance();
		for (int k= 1; k <= y.getLength(); k++) {
			g[0][k] = Float.MAX_VALUE;
		}
		for (int i = 1; i <= x.getLength(); i++){
			g[i][0] = Float.MAX_VALUE;
			for(int j = 1; j <= y.getLength(); j++){
				g[i][j] = Gij(g,mfcc,x,y,w,i,j);
			}
		}/*
		for(int i=1; i<g.length; i++) {
			for (int j = 1; j<g[i].length; j++) {
				System.out.print(" " + g[i][j]);
			}
			System.out.println(" \n");
		}*/
		return g[x.getLength()][y.getLength()] / (x.getLength() + y.getLength());
	}
}
