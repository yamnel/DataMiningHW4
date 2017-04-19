class KNN {
    private int[][] trainingData;
    private int[] spamWordCount;
    private int[] notSpamWordCount;
    private int classIndex;
    private boolean filtered = false;

    KNN(DataLoader dl, boolean filtered) {
        long startTime = System.nanoTime();
        System.out.print("Initializing KNN... ");

        trainingData = dl.getTrainingData();
        spamWordCount = dl.getSpamWordCount();
        notSpamWordCount = dl.getNotSpamWordCount();
        classIndex = dl.getWordList().size();
        this.filtered = filtered;

        System.out.printf("done. Took %d seconds.\n", (System.nanoTime() - startTime) / 1000000000);
    }

    int[] run(int[][] testData, int n) {
        long startTime = System.nanoTime();
        System.out.printf("Running KNN(n=%d) on test data... ", n);

        // Build the results array of { TP + FP, TP + FP + TN + FN }
        int[] results = new int[2];
        int actualClass;
        for (int[] email : testData) {
            actualClass = email[classIndex];
            results[0] += getClass(email, n) == actualClass ? 1 : 0;
            results[1]++;
        }

        System.out.printf("done. Took %d seconds.\n",
                (System.nanoTime() - startTime) / 1000000000);
        System.out.printf("Classified %d records with %.2f%c accuracy.\n",
                testData.length,
                (double) results[0] / (double) results[1] * 100,
                '%');
        return results;
    }

    private double cosSimilarity(int[] doc1, int[] doc2) {
        assert doc1.length == doc2.length;
        double numerator = 0.0;
        double denominator;
        double doc1Norm = 0.0;
        double doc2Norm = 0.0;

        // Find sum of products of elements
        for (int index = 0; index < doc1.length - 1; index++) {
            if (spamWordCount[index] + notSpamWordCount[index] < 50 && filtered) {
                continue;
            }
            numerator += doc1[index] * doc2[index];
            doc1Norm += doc1[index] * doc1[index];
            doc2Norm += doc2[index] * doc2[index];
        }

        doc1Norm = Math.sqrt(doc1Norm);
        doc2Norm = Math.sqrt(doc2Norm);

        // Find denominator
        denominator = doc1Norm * doc2Norm;

        return numerator / denominator;
    }

    int getClass(int[] testEmail, int n) {
        double[][] nearestNeighbors = getNearestNeighbors(testEmail, n);
        double cls = 0.0;

        for (double[] neighbor : nearestNeighbors) {
            cls += trainingData[(int) neighbor[0]][classIndex];
        }

        return (int) Math.round(cls / n);
    }

    private double[][] getNearestNeighbors(int[] testEmail, int n) {
        double similarity;
        double[][] result = new double[n][2];
        for (double[] r : result) {
            r[0] = -1.0;
            r[1] = -1.0;
        }

        for (int index = 0; index < trainingData.length; index++) {
            similarity = cosSimilarity(testEmail, trainingData[index]);

            for (int index2 = 0; index2 < result.length; index2++) {
                if (result[index2][1] == -1.0) {
                    result[index2][0] = index;
                    result[index2][1] = similarity;
                    break;
                }

                if (result[index2][1] < similarity) {
                    double[] temp = {index, similarity};
                    double[] temp2 = new double[2];
                    for (int index3 = index2; index3 < result.length; index3++) {
                        temp2[0] = result[index3][0];
                        temp2[1] = result[index3][1];
                        result[index3] = temp;
                        temp = new double[2];
                        temp[0] = temp2[0];
                        temp[1] = temp2[1];
                    }

                    break;
                }
            }
        }

        return result;
    }

    private double[] getAllDistances(int[][] testData) {
        double[] result = new double[testData.length];
        for (int index = 0; index < testData.length; index++) {
            result[index] = cosSimilarity(trainingData[index], testData[index]);
        }
        return result;
    }
}
