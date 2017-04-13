import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class DataLoader {

    // Arrays of files that hold the trainData & testData.txt names
    private File[] trainingFilesList;
    private File[] testingFilesList;
    private ArrayList<File> fileNames = new ArrayList();

    // Dictionary that holds the words in train and test and their count
    private ArrayList<String> allWords = new ArrayList<>();
    private int[][] emailWordFreq;
    private int numberOfTrainingEmails;


    public ArrayList<String> getAllWords() {
        return allWords;
    }

    public int[][] getEmailWordFreq() {
        return emailWordFreq;
    }

    DataLoader() {
        long startTime = System.nanoTime();
        // loading the .txt locations
        File trainingFolderPath = new File(System.getProperty("user.dir") + "/trainData");
        File testingFolderPath = new File(System.getProperty("user.dir") + "/testData");

        // loading the .txt names
        trainingFilesList = trainingFolderPath.listFiles();
        numberOfTrainingEmails = trainingFilesList.length;

        testingFilesList = testingFolderPath.listFiles();

        // loading the .txt data into the lists
        parseWords(trainingFilesList);
        parseWords(testingFilesList);

        emailWordFreq = new int[numberOfTrainingEmails][allWords.size() + 1];
        loadWordFreq(trainingFilesList);

        // Testing
        System.out.printf("There are %d total words. There are %d training emails.\n", allWords.size(), numberOfTrainingEmails);
        System.out.printf("Took %d seconds.", (System.nanoTime() - startTime) / 1000000000);
    }

    private void loadWordFreq(File[] fileNames) {
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
                    if (isAlphaNumeric(word)) emailWordFreq[i][index]++;
                }
            }
            if (isItSpam(fileNames[i].getName())) {
                emailWordFreq[i][emailWordFreq.length - 1] = 1;
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
