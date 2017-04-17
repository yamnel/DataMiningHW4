import java.util.ArrayList;

class NaiveBayes {
    private DataLoader dataLoader;
    private int[][] trainingData;
    private int[] spamWordCount;
    private int[] notSpamWordCount;
    private double m;
    private double p;
    private ArrayList<String> wordList;
    private double spamClassProbability;
    private double notSpamClassProbability;

    NaiveBayes(DataLoader dl) {
        long startTime = System.nanoTime();
        System.out.print("Performing Naive-Bayes... ");

        dataLoader = dl;
        trainingData = dl.getTrainingData();
        wordList = dl.getWordList();
        spamWordCount = dl.getSpamWordCount();
        notSpamWordCount = dl.getNotSpamWordCount();
        m = (double) (sumIntArray(spamWordCount) + sumIntArray(notSpamWordCount)) / (double) trainingData.length;
        p = 1.0 / (double) wordList.size();
        spamClassProbability = mEstimateProbability((double) dataLoader.getNumSpamTrainingEmails(), (double) trainingData.length);
        notSpamClassProbability = mEstimateProbability((double) dataLoader.getNumNotSpamTrainingEmails(), (double) trainingData.length);


        int n = 0;
        for (int[] email : dl.getTestingData()) {
            System.out.printf("%d, Predicted Class: %d Actual Class: %d\n", n++, getClass(email), email[email.length - 1]);
        }

        System.out.printf("done. Took %d seconds.\n", (System.nanoTime() - startTime) / 1000000000);
    }

    private double mEstimateProbability(double nic, double nc) {
//        return Math.log10((nic + m * p) / (nc + m));
        return Math.log10(nic / nc);
    }


    int getClass(int[] email) {
        double spamProbability = this.spamClassProbability;
        double notSpamProbability = this.notSpamClassProbability;

        for (int index = 0; index < email.length - 1; index++) {
            spamProbability += mEstimateProbability(spamWordCount[index], dataLoader.getNumSpamTrainingEmails());
            notSpamProbability += mEstimateProbability(notSpamWordCount[index], dataLoader.getNumNotSpamTrainingEmails());
        }
        return spamProbability > notSpamProbability ? 1 : 0;
    }

    private double mEstimateProbability(int nic, int nc) {
        return mEstimateProbability((double) nic, (double) nc);
    }

    private int sumIntArray(int[] arr) {
        int sum = 0;
        for (int e : arr) {
            sum += e;
        }
        return sum;
    }

}
