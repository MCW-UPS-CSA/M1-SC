import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.Field;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;
import fr.enseeiht.danck.voice_analyzer.defaults.DTWHelperDefault;

//extra imports
import java.io.FilenameFilter;
import java.util.Arrays;

public class myDTWtest {
		
// Function that permits computing the length of the Fields of MFCC (number of MFCC in a Field) 
	static int FieldLength(String fileName) throws IOException {
		int counter= 0;
		File file = new File(System.getProperty("user.dir") + fileName);
		for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
			counter++;
		}
		return 2*Math.floorDiv(counter, 512);
	}
	
// Main
	public static void main(String[] args) throws IOException, InterruptedException {
		DTWHelper myDTWHelper= new myDTW(); //new DTWHelperDefault();
		
		// Path to audio files and file filters
		String [] base = {"/test_res/audio/basic/","/test_res/audio/basic/"};
		String [] [] baseFilter = {{"P01", "csv"},{ "M01", "csv"}};
		/*******************************************************
	     *** This section deals with loading data references ***
	     *******************************************************/	
		String PATH = base[0];//"/test_res/myRecordings/";
	    String[] fileFilter = baseFilter[0];
	    FilenameFilter FILTER = new FilenameFilter() {public boolean accept(File file, String name) {if (name.startsWith(fileFilter[0]) && name.endsWith(fileFilter[1])) {return true;} else {return false;}}};
	    
	    File myDirectory = new File("."+PATH);
	    File[] myFiles = myDirectory.listFiles(FILTER);
	    
	    if(myFiles.length < 1) {System.out.println("No reference file was loaded!");}//guard for knowing if any file is loaded
	    
	    String[] CSVfilesR = new String[myFiles.length];
	
	    for(int i = 0; i < myFiles.length; i++) {
	    	CSVfilesR[i]= myFiles[i].getName();
	    }
	    Arrays.sort(CSVfilesR); //files are now alphabetically sorted
	    /**********************************************************
	     *** This section deals with processing data references ***
	     **********************************************************/
	    WindowMaker windowMaker;
	    int [] MFCCLengthR = new int[CSVfilesR.length];
	    int i=0,j=0,l=0;
	    Extractor extractor = Extractor.getExtractor();
	    List<String> files = new ArrayList<>();
	    MFCC[][] mfccsReference = new MFCC[CSVfilesR.length][];
	    Field [] referenceField = new Field[CSVfilesR.length];
	    //Building the iterator
	    System.out.println("Loaded Reference Files:");
	    for(i = 0; i < CSVfilesR.length; i++) {
	    	System.out.print(" " + CSVfilesR[i]);
	    	// Step 1. Reading reference file
	    	files = new ArrayList<>();
	    	files.add(PATH + CSVfilesR[i]);
	    	windowMaker = new MultipleFileWindowMaker(files);
	    	// Step 2. Recovery of MFCCs from the reference word
	    	MFCCLengthR[i] = FieldLength(PATH+CSVfilesR[i]);
	    	mfccsReference[i] = new MFCC[MFCCLengthR[i]];
	    	for (j = 0; j < MFCCLengthR[i]; j++) {
	    		mfccsReference[i][j] = extractor.nextMFCC(windowMaker);
	        }
	    	// Step 3. Construction of the reference Field (MFCC set)
	    	referenceField[i] = new Field(mfccsReference[i]);
	    }
	    /*************************************************************************
	     *** This section deals with displaying one of the imported references ***
	     *************************************************************************/
	    /*
		// Field and each MFCC display for one reference
		System.out.println(referenceField.toString());
		for (int i =0; i<referenceField[0].getLength(); i++ )
			System.out.println(i+": "+referenceField[0].getMFCC(i).toString());
	     */
		/******************************************************************************
	     *** This section deals with testing the distance between 2 data references ***
	     ******************************************************************************/
		// For example, we can test that the distance between alpha and alpha is 0
		float mydistanceReferenceReference= myDTWHelper.DTWDistance(referenceField[0], referenceField[0]);
		System.out.println("\nmyDTW - One Calculated Distance Reference-Reference  : " + mydistanceReferenceReference);
	
		/****************************************************************
		 * ************************************************************ *
	     *** This section deals with testing words against references ***
	     * ************************************************************ *
	     ****************************************************************/
		
		/**************************************************
	     *** This section deals with loading data tests ***
	     **************************************************/	
		String PATH2 = base[1]; //"/test_res/myRecordings/";
	    String[] fileFilter2 = baseFilter[1];//{"N0", "csv"}; // Specify the sets to be loaded
	    FilenameFilter FILTER2 = new FilenameFilter() {public boolean accept(File file, String name) {if (name.startsWith(fileFilter2[0]) && name.endsWith(fileFilter2[1])) {return true;} else {return false;}}};
	    
	    File myDirectoryT = new File("."+PATH2);
	    File[] myFilesT = myDirectoryT.listFiles(FILTER2);
	    
	    if(myFilesT.length < 1) {System.out.println("No test file was loaded!");}//guard for knowing if any file is loaded
	    
	    String[] CSVfilesT = new String[myFilesT.length];
	
	    for(i = 0; i < CSVfilesT.length; i++) {
	    	CSVfilesT[i]= myFilesT[i].getName();
	    }
	    Arrays.sort(CSVfilesT); //files are now alphabetically sorted
	    /*****************************************************
	     *** This section deals with processing Data Tests ***
	     *****************************************************/
	    int MFCCLengthT = 0;
	    MFCC[] mfccsTest = new MFCC[1];
	    Field [] testField = new Field[CSVfilesT.length];
	    //Building the iterator
	    System.out.println("Loaded Test Files:");
	    for(i = 0; i < CSVfilesT.length; i++) {
	    	System.out.print(" " + CSVfilesT[i]);
	    	// Step 1. Reading reference file
	    	files = new ArrayList<>();
	    	files.add(PATH + CSVfilesT[i]);
	    	windowMaker = new MultipleFileWindowMaker(files);
	    	// Step 2. Recovery of MFCCs from the reference word
	    	MFCCLengthT = FieldLength(PATH2+CSVfilesT[i]);
	    	mfccsTest = new MFCC[MFCCLengthT];
	    	for (j = 0; j < MFCCLengthT; j++) {
	    		mfccsTest[j] = extractor.nextMFCC(windowMaker);
	        }
	    	// Step 3. Construction of the alpha Field (MFCC set)
	    	testField[i] = new Field(mfccsTest);
	    }
	    /*************************************************************
	     *** This section deals with displaying one of the results ***
	     *************************************************************/
	    float mydistanceReferenceTest = myDTWHelper.DTWDistance(referenceField[0], testField[0]);	
		System.out.println("\nmyDTW - One Calculated Distance Reference-TestWord : " + mydistanceReferenceTest);
		
		/****************************************************
		 * ************************************************ *
	     *** This section deals with the Confusion Matrix ***
	     * ************************************************ *
	     ****************************************************/
		System.out.println("");
		//in progress
		float errorDif = 0.1f; //not used for now - will probably delete later anyway
		float previousBestMatchValue = Float.MAX_VALUE;
		int previousBestMatchPosition = 0;
	    float [][] RT = new float[CSVfilesR.length][CSVfilesT.length];
	    int [ ] [ ] CM = new int[CSVfilesR.length][CSVfilesT.length];
	    
	    for (j = 0; j < CSVfilesT.length; j++) {
	    	previousBestMatchValue = Float.MAX_VALUE; previousBestMatchPosition = 0;
	    	for (i = 0; i < CSVfilesR.length; i++) {
	    		RT[i][j] = myDTWHelper.DTWDistance(referenceField[i], testField[j]);
	    		if(RT[i][j]<previousBestMatchValue) {
	    			previousBestMatchValue = RT[i][j];
	    			previousBestMatchPosition = i;
	    		}
	    	}
	    	CM[previousBestMatchPosition][j] += 1;
	    }
	    /********************************************
	     *** Display and calculations for display ***
	     ********************************************/
	    int maxLen = 0; // display calculations
	    for (i = 0; i < CSVfilesR.length; i++) {
	    	if (maxLen < CSVfilesR[i].length()) maxLen = CSVfilesR[i].length();
	    }
	    
	    if (CSVfilesR.length >= CSVfilesT.length) { // display of confusion matrix logic
	    	System.out.println("Confusion matrix:");
	    } else System.out.println("Individual tests' closeness to reference:");
	    
	    System.out.print("Colums:"); for(l = 0; l < maxLen -7 -8 -3; l++ ) System.out.print(" ");
	    for (i=0; i< CSVfilesT.length; i++) {
	    	System.out.print(" " + CSVfilesT[i].substring(4, CSVfilesT[i].length()-8));
	    }
	    // Display of the obtained matrix
	    System.out.print("\nLines:\n");
	    for (i = 0; i < CM.length; i++) {
	    	System.out.print(CSVfilesR[i].substring(4, CSVfilesR[i].length()-8) + ":");
	    	for(l = 0; l < maxLen - CSVfilesR[i].length(); l++ ) System.out.print(" ");
	    	for (j = 0; j < CM[0].length; j++) {
	    		System.out.print(" " + CM[i][j]);
	    		for(l = 0; l < CSVfilesT[j].length()-13; l++ ) System.out.print(" ");
	    	}System.out.println("");
		}
	    /************************************************************************************
	     *** If there are more samples than references, this becomes the confusion matrix ***
	     ************************************************************************************/
	    if (CSVfilesR.length < CSVfilesT.length) { // if there are more samples than references, this section will be displayed
		    int repeating_samples = CSVfilesR.length;
		    if (CSVfilesT.length % CSVfilesR.length != 0) { //warning if data structure is incorrect
		    	System.out.println("\nWARNING! The Confusion matrix won't be build properly because of incorrect data structure! Possibly missing files, bad naming or filters!\n\n");	
		    }
		    // building the new confusion matrix if the testing data has more samples than references
		    int [] [] confusionMatrix = new int [CSVfilesR.length] [CSVfilesR.length];
		    for (i = 0; i< CSVfilesR.length; i++) {
		    	for (j = 0; j< CSVfilesT.length; j++) {
		    		confusionMatrix[i][j%repeating_samples] += CM[i][j];
		    	}
		    }
		    
		    // display and related calculations
		    for(l = 0; l < maxLen; l++ ) System.out.print(" -");
		    System.out.println("\nConfusion Matrix:");
		    System.out.print("Colums:"); for(l = 0; l < maxLen -7 -8 -3; l++ ) System.out.print(" ");
		    for (i=0; i< CSVfilesR.length; i++) {
		    	System.out.print(" " + CSVfilesT[i].substring(4, CSVfilesT[i].length()-8));
		    }
		    System.out.print("\nLines:\n");
		    for (i = 0; i < confusionMatrix.length; i++) {
		    	System.out.print(CSVfilesR[i].substring(4, CSVfilesR[i].length()-8) + ":");
		    	for(l = 0; l < maxLen - CSVfilesR[i].length(); l++ ) System.out.print(" "); //display improvement
		    	for (j = 0; j < confusionMatrix[0].length; j++) {
		    		System.out.print(" " + confusionMatrix[i][j]);
		    		for(l = 0; l < CSVfilesT[j].length()-13; l++ ) System.out.print(" "); //display improvement
		    	}System.out.println("");
			}
		    // reminder of warning
		    if (CSVfilesT.length % CSVfilesR.length != 0) {
		    	System.out.println("\nWARNING! The Confusion matrix wasn't built properly because of incorrect data structure! Possibly missing files, bad naming or filters!\n\n");	
		    }
	    }
	    
	}
}

