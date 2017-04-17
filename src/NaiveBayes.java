import java.util.ArrayList;

class NaiveBayes {
    private DataLoader dataLoader;
    private int[][] testData;
    private int[][] trainingData;
    private int[] spamWordCount;
    private int[] notSpamWordCount;
    private double m;
    private double p;
    private ArrayList<String> wordList;

    NaiveBayes(DataLoader dl){
        long startTime = System.nanoTime();
        System.out.print("Performing Naive-Bayes... ");

        dataLoader = dl;
        trainingData = dl.getTrainingData();
        testData = dl.getTestingData();
        wordList = dl.getWordList();
        spamWordCount = dl.getSpamWordCount();
        notSpamWordCount = dl.getNotSpamWordCount();
        m = (double)(sumIntArray(spamWordCount) + sumIntArray(notSpamWordCount)) / (double)trainingData.length;
        p = 1.0 / (double)wordList.size();



        System.out.printf("done. Took %d seconds.\n", (System.nanoTime() - startTime)/1000000000);
    }

    private int sumIntArray(int[] arr){
        int sum = 0;
        for(int e : arr){
            sum += e;
        }
        return sum;
    }

}
