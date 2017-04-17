import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class DataLoader {
    private static final String TEST_DATA_PATH = System.getProperty("user.dir") + "/testData";
    private static final String TRAIN_DATA_PATH = System.getProperty("user.dir") + "/trainData";
    private static final File[] TRAINING_FILES = new File(TEST_DATA_PATH).listFiles();
    private static final File[] TESTING_FILES = new File(TRAIN_DATA_PATH).listFiles();
    private ArrayList<String> wordList;
    private int[][] trainingData;
    private int[][] testingData;
    private int[] spamWordCount;
    private int[] notspamWordCount;

    DataLoader() {
        // Print process information
        long startTime = System.nanoTime();
        System.out.print("Loading data files... ");

        // Init list to hold all words
        wordList = new ArrayList<>();

        // Scan each training e-mail and add the unique words to the wordList
        loadWordList(TRAINING_FILES);
//        loadWordList(TESTING_FILES);

        // Init arrays holding the number of emails with each word
        spamWordCount = new int[wordList.size()];
        notspamWordCount = new int[wordList.size()];

        // Generate arrays for each e-mail holding the word count for each word
        trainingData = new int[TRAINING_FILES.length][wordList.size() + 1];
        loadWordCounts(trainingData, TRAINING_FILES);
        testingData = new int[TESTING_FILES.length][wordList.size() + 1];
        loadWordCounts(testingData, TESTING_FILES);

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
                        if (wordList.indexOf(word) == -1) {
                            wordList.add(word);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("File Read Error - " + email.getName());
            }
        }
    }

    private void loadWordCounts(int[][] dataArray, File[] fileList) {
        int classIndex = wordList.size() - 1;
        int cls;
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
                        // on the test data since some words in the test data won't be in the training data.
                        if (wordIndex != -1) {
                            // Add one to that word's counter for this e-mail
                            dataArray[index][wordIndex] += 1;

                            // If this word hasn't been encountered in this e-mail before
                            // Add one to the word's counter for this email's class array
                            // and add the word to the word list, so it's not counted next time
                            if (emailWordList.indexOf(word) == -1) {
                                (cls == 0 ? notspamWordCount : spamWordCount)[wordIndex] += 1;
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
        }
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
        return notspamWordCount;
    }
}
