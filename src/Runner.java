public class Runner {
    public static void main(String[] args) {

        DataLoader dataLoader = new DataLoader();
        NaiveBayes naiveBayes = new NaiveBayes(dataLoader);
        naiveBayes.run(dataLoader.getTestingData());

    }
}
