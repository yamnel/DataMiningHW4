/**
 * Created by kohbo on 4/13/2017.
 */
public class Runner {
    DataLoader dataLoader;
    NaiveBayes naiveBayes;

    public static void main(String[] args) {
        Runner runner = new Runner();

        runner.dataLoader = new DataLoader();
        runner.naiveBayes = new NaiveBayes(runner.dataLoader.getEmailWordFreq(), runner.dataLoader.getAllWords());
    }
}
