import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import java.util.ArrayList;


@RunWith(Arquillian.class)
public class NaiveBayesTest {
    private NaiveBayes nb;

    private final ArrayList<String> wordList = new ArrayList<>();

    private final int[][] wordCounts = {
            {0, 2, 1, 0},
            {1, 4, 0, 1},
            {5, 2, 0, 1}
    };

    @org.junit.Before
    public void setUp() throws Exception {
        wordList.add("money");
        wordList.add("hello");
        wordList.add("pamela");

    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(NaiveBayes.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }


}
