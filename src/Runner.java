public class Runner {
    public static void main(String[] args) {

        DataLoader dataLoader = new DataLoader(false);
        NaiveBayes naiveBayes = new NaiveBayes(dataLoader, false);
        naiveBayes.run(dataLoader.getTestingData());
        dataLoader = new DataLoader(true);
        naiveBayes = new NaiveBayes(dataLoader, true);
        naiveBayes.run(dataLoader.getTestingData());

    }
}
