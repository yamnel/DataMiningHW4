import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;


public class NaiveBayes {
    private int[][] trainData;
    private int[][] testData;
    private ArrayList<String> wordList;     // list of all possible words
    private final int[] classes = {0, 1};   // TODO: change to enum
    private int[] spamWordCount;            // number of spam e-mails with a unique word
    private int[] notSpamWordCount;         // number of nonspam e-mails with a unique word
    private double m;                       // constant for m-element probability
    private double p;                       // constant for m-element probability
    int numOfSpamEmailInTrainingData;
    int numOfNonSpamEmailInTrainingData;
    double spamClassProbability;
    double notSpamClassProbability;


    NaiveBayes(final DataLoader dataLoader) {
        long startTime = System.nanoTime();
        System.out.print("Initializing Naive-Bayes Class... ");
        trainData = dataLoader.getTrainingData();
        testData = dataLoader.getTestData();
        wordList = dataLoader.getAllWords();

        // Init the arrays that will hold the number of e-mails where each word occurs
        spamWordCount = new int[wordList.size()];
        notSpamWordCount = new int[wordList.size()];

        // For each word in the list of all words count the number of e-mails with each word and store it in an array
        for (String word : wordList) {
            spamWordCount[wordList.indexOf(word)] = getWordCount(wordList.indexOf(word), 1);
            notSpamWordCount[wordList.indexOf(word)] = getWordCount(wordList.indexOf(word), 0);
        }

        // Determine # of e-mails in each class
        numOfSpamEmailInTrainingData = getWordCount(wordList.size()-1, 1);
        numOfNonSpamEmailInTrainingData = getWordCount(wordList.size()-1, 0);

        // Determine class probability
        spamClassProbability = mElementProbability(numOfSpamEmailInTrainingData, trainData.length);
        notSpamClassProbability = mElementProbability(numOfNonSpamEmailInTrainingData, trainData.length);

        // Determine constants for m-element probability
        m = (sumIntArray(spamWordCount) + sumIntArray(notSpamWordCount)) / trainData.length;
        p = 1 / wordList.size();

        System.out.printf("Done. Took %d second(s).\n", (System.nanoTime() - startTime) / 1000000000);

        for(int[] email : testData){
            System.out.printf("Predicted Class: %d, Actual Class: %d\n", getClass(email), email[email.length-1]);
        }
        // TODO classify test emails
        // TODO measure accuracy
    }

    public int getClass(final int[] testEmailData) {
        double isSpamProbability = Math.log10(spamClassProbability);
        double isNotspamProbability = Math.log10(notSpamClassProbability);

        for(int index = 0; index < testEmailData.length - 2; index++){
            isSpamProbability += Math.log10(mElementProbability(spamWordCount[index], numOfSpamEmailInTrainingData));
            isNotspamProbability += Math.log10(mElementProbability(notSpamWordCount[index], numOfNonSpamEmailInTrainingData));
        }

        return (isSpamProbability > isNotspamProbability) ? 1 : 0;
    }

    private double mElementProbability(int nic, int nc){
        return (nic + m * p)/(nc + m);
    }

    /**
     * Returns the number of e-mails of a certain class that contains a certain word
     */
    private int getWordCount(final int wordIndex, final int cls) {
        int count = 0;
        int emailCls;

        for (int[] email : trainData) {
            emailCls = email[email.length - 1];
            if (email[wordIndex] > 0 && emailCls == cls) count++;
        }

        return count;
    }

    /**
     * Computes the sum of all elements in an integer arrary, e.g. {2,4,1,3} = 10
     * @param arr array to be summed
     * @return sum of elements
     */
    private int sumIntArray(int[] arr) {
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }

        return sum;
    }

}
