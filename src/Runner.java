public class Runner {
    public static void main(String[] args) {
        int[] Ns = {1, 3, 5, 20};

        // Load all the files and build the arrays without filtering
        DataLoader dataLoader = new DataLoader(false);
        // Init and run NaiveBayes without filtering
        NaiveBayes naiveBayes = new NaiveBayes(dataLoader, false);
        naiveBayes.run(dataLoader.getTestingData());

        // Init and run KNN without filtering
        KNN knn = new KNN(dataLoader, false);
        for (int n : Ns) {
            knn.run(dataLoader.getTestingData(), n);
        }

        // Load all the files and build the arrays with filtering
        dataLoader = new DataLoader(true);

        // Init and run Naive Bayes with filtering
        naiveBayes = new NaiveBayes(dataLoader, true);
        naiveBayes.run(dataLoader.getTestingData());

        // Init and run KNN with filtering
        knn = new KNN(dataLoader, true);
        for (int n : Ns) {
            knn.run(dataLoader.getTestingData(), n);
        }

    }
}
