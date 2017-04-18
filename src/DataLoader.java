import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class DataLoader {
    private static final String TEST_DATA_PATH = System.getProperty("user.dir") + "/testData";
    private static final String TRAIN_DATA_PATH = System.getProperty("user.dir") + "/trainData";
    private static final File[] TRAINING_FILES = new File(TRAIN_DATA_PATH).listFiles();
    private static final File[] TESTING_FILES = new File(TEST_DATA_PATH).listFiles();
    private ArrayList<String> wordList;
    private int[][] trainingData;
    private int[][] testingData;
    private int[] spamWordCount;
    private int[] notSpamWordCount;
    private int numSpamTrainingEmails = 0;
    private int numNotSpamTrainingEmails = 0;
    private boolean filtered = false;

    DataLoader(boolean filtered) {
        // Print process information
        long startTime = System.nanoTime();
        System.out.print("Loading data files... ");

        // Set whether to filter out symbols
        this.filtered = filtered;

        // Init list to hold all words
        wordList = new ArrayList<>();

        // Scan each training e-mail and add the unique words to the wordList
        loadWordList(TRAINING_FILES);
//        loadWordList(TESTING_FILES);

        // Init arrays holding the number of emails with each word
        spamWordCount = new int[wordList.size()];
        notSpamWordCount = new int[wordList.size()];

        // Generate arrays for each e-mail holding the word count for each word
        trainingData = loadWordCounts(TRAINING_FILES);
        testingData = loadWordCounts(TESTING_FILES);

        System.out.printf("done. Took %d seconds.\n", (System.nanoTime() - startTime) / 1000000000);
        System.out.printf("Loaded %d training files and %d test files.\n", TRAINING_FILES.length, TESTING_FILES.length);
        System.out.printf("Found %d unique words.\n", wordList.size());
    }

    private void loadWordList(File[] fileList) {
        for (File email : fileList) {
            try (BufferedReader br = new BufferedReader(new FileReader(email))) {
                String line;
                while ((line = br.readLine()) != null) {
                    for (String word : line.split(" ")) {
                        // If word not in word list, add it
                        if (!wordList.contains(word)) {
                            if (!filtered || isAlphaNum(word)) {
                                wordList.add(word);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("File Read Error - " + email.getName());
            }
        }
    }

    private int[][] loadWordCounts(File[] fileList) {
        int[][] dataArray = new int[fileList.length][wordList.size() + 1];
        int classIndex = wordList.size();
        int cls;
        boolean isTrainingFiles = Arrays.equals(TRAINING_FILES, fileList);
        for (int index = 0; index < fileList.length; index++) {
            cls = fileList[index].getName().startsWith("sp") ? 1 : 0;
            ArrayList<String> emailWordList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileList[index]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    for (String word : line.split(" ")) {
                        int wordIndex = wordList.indexOf(word);
                        // If word not in cls word list, don't increment that word's counter. This means the word wasn't
                        // found during the first scan of the files. This should only happen when this method is called
                        // on the test data since some words in the test data won't be in the training data, unless
                        // loadWordList is called on the test data
                        if (wordList.contains(word)) {
                            // Add one to that word's counter for this e-mail
                            dataArray[index][wordIndex] += 1;

                            // If this word hasn't been encountered in this e-mail before
                            // add one to the word's counter for this email's class array
                            // and add the word to this email's word list, so it's not counted next time
                            if (!emailWordList.contains(word) && isTrainingFiles) {
                                if (cls == 0) {
                                    notSpamWordCount[wordIndex]++;
                                } else {
                                    spamWordCount[wordIndex]++;
                                }
                                emailWordList.add(word);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("File Eead Error - " + fileList[index].getName());
            }

            // Set last element in array to 1 if this email is spam (i.e. filename begins with sp)
            dataArray[index][classIndex] = cls;

            // Add one to the total count of e-mails per class
            if (isTrainingFiles) {
                if (cls == 0) {
                    numNotSpamTrainingEmails++;
                } else {
                    numSpamTrainingEmails++;
                }
            }
        }
        return dataArray;
    }

    private String stripSymbols(String str){
        return str.replaceAll("\\W", "");
    }

    private boolean isAlphaNum(String str) {
        return str.matches("^[\\w]+'?[\\w]*:?$");
    }

    int[][] getTrainingData() {
        return trainingData;
    }

    int[][] getTestingData() {
        return testingData;
    }

    ArrayList<String> getWordList() {
        return wordList;
    }

    int[] getSpamWordCount() {
        return spamWordCount;
    }

    int[] getNotSpamWordCount() {
        return notSpamWordCount;
    }

    int getNumSpamTrainingEmails() {
        return numSpamTrainingEmails;
    }

    int getNumNotSpamTrainingEmails() {
        return numNotSpamTrainingEmails;
    }
}
