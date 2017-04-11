import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {
    static File[] trainingFiles;
    static HashMap<String, Integer> words = new HashMap();

    public Main() {
    }

    public static void main(String[] args) {
        File trainingFolderPath = new File(System.getProperty("user.dir") + "/trainData");
        trainingFiles = trainingFolderPath.listFiles();
        loadData(trainingFiles);
        System.out.println(words.size());
        words.keySet().forEach((key) -> System.out.println(key + "->" + words.get(key)));
    }

    static void increaseCountOf(String word) {
        if(words.containsKey(word)) {
            Integer temp = (Integer)words.get(word);
            temp = Integer.valueOf(temp.intValue() + 1);
            words.put(word, temp);
        } else {
            words.put(word, Integer.valueOf(1));
        }

    }

    static void loadData(File[] containingFolder) {
        List<String> fileNames = new ArrayList();
        Scanner lineScanr = null;
        File[] var3 = containingFolder;
        int var4 = containingFolder.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            if(file.isFile()) {
                fileNames.add(file.getName());

                try {
                    lineScanr = new Scanner(file);
                } catch (FileNotFoundException var9) {
                    var9.printStackTrace();
                }

                while(lineScanr.hasNext()) {
                    Scanner wordScanr = new Scanner(lineScanr.nextLine());

                    while(wordScanr.hasNext()) {
                        String word = wordScanr.next();
                        increaseCountOf(word);
                    }
                }
            }
        }

    }
}
