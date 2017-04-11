import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {
    // Arrays of files that hold the trainData & testData.txt names
    static File[] trainingFiles;
    static File[] testingFiles;

    // Dictionary that holds the words in train and test and their count
    static HashMap<String, Integer> trainWords = new HashMap();
    static HashMap<String, Integer> testWords = new HashMap();


    public Main() {
    }

    public static void main(String[] args) {
        // loading the .txt locations
        File trainingFolderPath = new File(System.getProperty("user.dir") + "/trainData");
        File testingFolderPath = new File(System.getProperty("user.dir") + "/testData");

        // loading the .txt names
        trainingFiles = trainingFolderPath.listFiles();
        testingFiles = testingFolderPath.listFiles();


        // loading the .txt data into the Dictionaries
        loadData(trainingFiles, trainWords);
        loadData(testingFiles, testWords);

        // TESTING PRINT
        System.err.println(trainWords.size());
        trainWords.keySet().forEach((key) -> System.out.println(key + " -> " + trainWords.get(key)));

        System.err.println("\n\n"+testWords.size());
        testWords.keySet().forEach((key) -> System.out.println(key + " -> " + testWords.get(key)));
    }

    // increases count of @word in @dictionary
    static void increaseCountOf(String word, HashMap<String, Integer> dictionary) {
        if(dictionary.containsKey(word)) {
            Integer temp = dictionary.get(word);
            temp = temp + 1;
            dictionary.put(word, temp);
        } else {
            dictionary.put(word, 1);
        }

    }


    // loads data from @containingFolders into @dictionary
    static void loadData(File[] containingFolder, HashMap<String, Integer> dictionary) {
//        ArrayList fileNames = new ArrayList();  // Testing
        Scanner lineScanr = null;


        for(int fileNumber = 0; fileNumber < containingFolder.length; ++fileNumber) {
            File file = containingFolder[fileNumber];

            if(file.isFile()) {  // if the file is valid
//                fileNames.add(file.getName());  // Testing
                try {
                    lineScanr = new Scanner(file);
                } catch (FileNotFoundException var9) {
                    var9.printStackTrace();
                }

                while(lineScanr.hasNext()) {
                    Scanner wordScanr = new Scanner(lineScanr.nextLine());

                    while(wordScanr.hasNext()) {
                        String word = wordScanr.next();
                        increaseCountOf(word, dictionary);
                    }
                }
            }
        }

    }
}
