public class Runner {
    public static void main(String[] args) {
        int[] Ns = {1, 3, 5, 20};

        DataLoader dataLoader = new DataLoader(false);
        NaiveBayes naiveBayes = new NaiveBayes(dataLoader, false);
        naiveBayes.run(dataLoader.getTestingData());
        dataLoader = new DataLoader(true);
        naiveBayes = new NaiveBayes(dataLoader, true);
        naiveBayes.run(dataLoader.getTestingData());
        KNN knn = new KNN(dataLoader, false);
        for (int n : Ns) {
            knn.run(dataLoader.getTestingData(), n);
        }
        knn = new KNN(dataLoader, true);
        for (int n : Ns) {
            knn.run(dataLoader.getTestingData(), n);
        }

    }
}
