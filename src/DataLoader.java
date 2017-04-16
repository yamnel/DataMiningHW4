import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class DataLoader {

    // Arrays of files that hold the trainData & testData.txt names
    private File[] trainingFilesList;
    private File[] testingFilesList;
    private ArrayList<File> fileNames = new ArrayList();

    // List of all possible words from the given test and training data
    private ArrayList<String> allWords = new ArrayList<>();
    /*  Holds the frequency of words per e-mail. The last element stores whether the e-mail is spam (1) or not spam (0)
        Example Structure:
        {
            {0,1,0,4,1},
            {2,4,1,0,0},
            {0,0,5,0,0}
        }
        Each word is tied to a specific index, which can be found using allWords.indexOf(someWord).
    */
    private int[][] trainingData;
    private int[][] testData;

    // Holds the number of e-mails for each set. Stored in a variable to reduce calls of length method
    private int numberOfTrainingEmails;
    private int numberofTestEmails;

    DataLoader() {
        // Start clock to time process of loading data
        long startTime = System.nanoTime();
        System.out.println("Loading data... ");

        // loading the .txt locations
        File trainingFolderPath = new File(System.getProperty("user.dir") + "/trainData");
        File testingFolderPath = new File(System.getProperty("user.dir") + "/testData");

        // loading the lists of file names for the training and test e-mail files
        trainingFilesList = trainingFolderPath.listFiles();
        numberOfTrainingEmails = trainingFilesList.length;
        testingFilesList = testingFolderPath.listFiles();
        numberofTestEmails = testingFilesList.length;

        // Go through each file in the training and testing list and add the unique words to the list of all words
        parseWords(trainingFilesList);
        parseWords(testingFilesList);

        // Initialize arrays for each training e-mail holding the counts of each word
        trainingData = new int[numberOfTrainingEmails][allWords.size() + 1];
        loadWordFreq(trainingData, trainingFilesList);

        // Initialize arrays for each test e-mail holding the counts of each word
        testData = new int[numberofTestEmails][allWords.size() + 1];
        loadWordFreq(testData, testingFilesList);


        // Print process results
        System.out.printf("Done. Took %d second(s).\n", (System.nanoTime() - startTime) / 1000000000);
        System.out.printf("There are %d total unique words. There are %d training e-mails and %d test e-mails.\n",
                allWords.size(),
                numberOfTrainingEmails,
                numberofTestEmails);
    }


    ArrayList<String> getAllWords() {
        return allWords;
    }

    int[][] getTestData() {
        return testData;
    }

    int[][] getTrainingData() {
        return trainingData;
    }

    private void loadWordFreq(int[][] wordFreqArray, File[] fileNames) {
        Scanner lineScanr = null;
//        for (File file : fileNames) {
        for (int i = 0; i < fileNames.length; i++) {
            try {
                lineScanr = new Scanner(fileNames[i]); // load the line
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (lineScanr.hasNext()) {
                Scanner wordScanr = new Scanner(lineScanr.nextLine()); // load the word

                while (wordScanr.hasNext()) { // for every word
                    String word = wordScanr.next();

                    int index = allWords.indexOf(word);
                    if (isAlphaNumeric(word)) wordFreqArray[i][index]++;
                }
            }

            // If filename starts with sp then it's spam, and set last element in array to 1 (meaning that e-mail is spam)
            if (isItSpam(fileNames[i].getName())) {
                wordFreqArray[i][allWords.size()-1] = 1;
            }
        }
    }

    // loads data from @containingFolders into @dictionary
    private void parseWords(File[] containingFolder) {
        Scanner lineScanr = null;

        // go through all .txt files
        for (File file : containingFolder) {
            if (file.isFile()) {  // if the file is valid
                fileNames.add(file);

                try {
                    lineScanr = new Scanner(file); // load the line
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                while (lineScanr.hasNext()) { // for every line
                    Scanner wordScanr = new Scanner(lineScanr.nextLine()); // load the word

                    while (wordScanr.hasNext()) { // for every word
                        String word = wordScanr.next();

                        if (!allWords.contains(word) && isAlphaNumeric(word)) {
                            allWords.add(word);
                        }

                    }// end of words loop
                }// end of line loop
            }
        }// end of .txt loop
    }


    private boolean isItSpam(String filename) {
        return filename.startsWith("sp");
    }

    private boolean isAlphaNumeric(String str) { // checks for alphanumerics on apostrophes
        return str.matches("^[\\p{Alnum}|']+$");
    }

}
