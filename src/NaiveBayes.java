import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class NaiveBayes {
    private int[][] trainData;
    private ArrayList<String> wordList;     // list of all possible words
    private final int[] classes = {0, 1};   // TODO: change to enum
    private int[] spamWordCount;            // number of spam e-mails with a unique word
    private int[] notSpamWordCount;         // number of nonspam e-mails with a unique word

    NaiveBayes(final int[][] trainData, final ArrayList<String> wordList) {
        this.trainData = trainData;
        this.wordList = wordList;

        spamWordCount = new int[wordList.size()];
        notSpamWordCount = new int[wordList.size()];

        for (String word : wordList) {
            spamWordCount[wordList.indexOf(word)] = getWordCount(word, 1);
            notSpamWordCount[wordList.indexOf(word)] = getWordCount(word, 0);
        }
    }

    public int getClass(final String body) {
        return getClassProbability(body, 1) > getClassProbability(body, 0) ? 1 : 0;
    }

    private double getClassProbability(final String body, final int cls) {
        int[] classWordCount = cls == 0 ? notSpamWordCount : spamWordCount;
        int numEmailsInClass = sumIntArray(classWordCount);
        double probability = Math.log10(numEmailsInClass / trainData.length);

        for (String word : getUniqueWords(body)) {
            probability += Math.log10(classWordCount[wordList.indexOf(word)] / numEmailsInClass);
        }

        return probability;
    }

    private HashSet<String> getUniqueWords(final String body) {
        HashSet<String> uniqueWords = new HashSet<>();
        Collections.addAll(uniqueWords, body.split(" "));

        return uniqueWords;
    }

    // Returns the number of e-mails of a certain class that contains a certain word
    private int getWordCount(final String word, final int cls) {
        int wordIndex = wordList.indexOf(word);
        int count = 0;

        for (int[] email : trainData) {
            if (email[wordIndex] > 0 && email[email.length - 1] == cls) count++;
        }

        return count;
    }

    private int sumIntArray(int[] arr) {
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }

        return sum;
    }

}
