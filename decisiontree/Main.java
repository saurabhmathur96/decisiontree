package decisiontree;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    final String INPUT_FILE = "car.data";
    final int TARGET_INDEX = 6;

    ArrayList<Instance> examples = new ArrayList<Instance>();
    try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE)))) {
      String line;
      while((line = br.readLine()) != null) {
        Instance example = new Instance(line, TARGET_INDEX);
        examples.add(example);
        System.out.println(example);
      }
    } catch (IOException ioex) {
      ioex.printStackTrace();
    }

    ID3Classifier classifier = new ID3Classifier();
    classifier.train(examples);

    System.out.println(classifier);

    String [] input = {"vhigh","vhigh","2","2","small","low"};

    System.out.println("predicted value : " + classifier.predict(new ArrayList<String>(Arrays.asList(input))));
    System.out.println("actual value : unacc");
  }
}
