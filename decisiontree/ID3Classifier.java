package decisiontree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;


/**
 * Represents a node on the decision tree
 */
public class ID3Classifier {
  private ID3Classifier parent;
  private ArrayList<ID3Classifier> children;
  private HashMap<String, Integer> classCounts;
  private int splitFeature;
  private String splitFeatureValue;
  private String label;

  public ID3Classifier() {
    this.parent = null;
    this.children = new ArrayList<>();
    this.classCounts = null;
    this.splitFeature = -1;
    this.splitFeatureValue = null;
    this.label = null;
  }

  public void train(ArrayList<Instance> examples) {
    HashSet<Integer> features = new HashSet<>();
    for(int i=0; i<examples.get(0).nClasses(); i++) {
      features.add(i);
    }
    buildDecisionTree(examples, this, features);
  }


  public String predict(ArrayList<String> input) {
    if (this.children.size() == 0) {
      return this.label;
    }
    String value = input.get(this.splitFeature);
    for (ID3Classifier child : this.children) {
      if (!child.splitFeatureValue.equals(value)) {
        continue;
      }
      return child.predict(input);
    }
    return null;
  }

  private double[] dataToDistribution(ArrayList<Instance> examples) {
    ArrayList<String> allLabels = new ArrayList<>();
    for (Instance example : examples) {
      allLabels.add(example.getLabel());
    }
    int nEntries = allLabels.size();
    HashSet<String> possibleLabels = new HashSet<>(allLabels);
    double[] distribution = new double[possibleLabels.size()];
    int index = 0;
    for(String label : possibleLabels) {
      int frequency = Collections.frequency(allLabels, label);
      distribution[index] = ((double)frequency)/nEntries;
      index++;
    }
    return distribution;
  }

  private double computeEntropy(double[] distribution) {
    double entropy = 0.0;
    for(double p : distribution) {
      entropy -= p*Math.log(p)/Math.log(2);
    }

    return entropy;
  }

  private ArrayList<ArrayList<Instance>> splitData(ArrayList<Instance> examples, int index) {
    HashSet<String> attributeValues = new HashSet<>();
    for(Instance example : examples ) {
      attributeValues.add(example.getValue(index));
    }
    ArrayList<ArrayList<Instance>> splits = new  ArrayList<>();
    for(String value : attributeValues) {
      ArrayList<Instance> dataSubset = new ArrayList<>();
      for(Instance example : examples) {
        if (example.getValue(index).equals(value)) {
          dataSubset.add(example);
        }
      }
      splits.add(dataSubset);
    }
    return splits;
  }

  private double computeInformationGain(ArrayList<Instance> examples, int index) {
    double informationGain = computeEntropy(dataToDistribution(examples));
    for (ArrayList<Instance> subset : splitData(examples, index)) {
      if (subset.size()  > 0) {
        informationGain -= ((double)subset.size()/examples.size())*computeEntropy(dataToDistribution(subset));
      }
    }
    return informationGain;
  }

  private boolean isHomogeneous(ArrayList<Instance> examples) {
    HashSet<String> possibleLabels = new HashSet<>();
    for (Instance example : examples) {
      possibleLabels.add(example.getLabel());
    }
    return (possibleLabels.size() <= 1);
  }

  private ID3Classifier majorityVote(ArrayList<Instance> examples, ID3Classifier node) {
    ArrayList<String> labels = new ArrayList<>();
    for (Instance example : examples) {
      labels.add(example.getLabel());
    }

    HashSet<String> possibleLabels = new HashSet<>(labels);
    node.label = Collections.max(possibleLabels,
            (s1, s2) -> Integer.compare(Collections.frequency(labels, s1.toString()), Collections.frequency(labels, s2.toString()))
    );


    HashMap<String, Integer> classCounts = new HashMap<>();
    for (String label : possibleLabels) {
      classCounts.put(label, Collections.frequency(labels, label));
    }
    node.classCounts = classCounts;
    return node;

  }

  private ID3Classifier buildDecisionTree(ArrayList<Instance> examples, ID3Classifier root, HashSet<Integer> remainingFeatures) {
    if (isHomogeneous(examples)) {
      root.label = examples.get(0).getLabel();
      root.classCounts = new HashMap<>();
      root.classCounts.put(root.label, examples.size());
      return root;
    }

    if (remainingFeatures.size() == 0) {
      return majorityVote(examples, root);
    }

    int bestFeature = Collections.max(remainingFeatures, new Comparator<Integer>() {
      @Override
      public int compare(Integer feature1, Integer feature2) {

        double gain1 = computeInformationGain(examples, feature1);
        double gain2 = computeInformationGain(examples, feature2);
        return Double.compare(gain1, gain2);
      }
    });

    double bestGain = computeInformationGain(examples, bestFeature);
    if(bestGain == 0) {
      return majorityVote(examples, root);
    }

    root.splitFeature = bestFeature;

    for (ArrayList<Instance> subset : splitData(examples, bestFeature)) {
      ID3Classifier child = new ID3Classifier();
      child.parent = root;
      child.splitFeatureValue = subset.get(0).getValue(bestFeature);
      root.children.add(child);

      remainingFeatures.remove(bestFeature);
      buildDecisionTree(subset, child, remainingFeatures);
    }
    return root;
  }

  @Override
  public String toString() {
    return repr(this, "", true);
  }

  private String repr(ID3Classifier root, String indent, boolean isRoot) {
    if(root.children.size() == 0) {
      return String.format("%s%s, %s %s\n", indent, root.splitFeatureValue, root.label, root.classCounts);
    }

    StringBuilder buffer = new StringBuilder();
    if(isRoot) {
      buffer.append(String.format("%s%s\n", indent, root.splitFeature));
    } else {
      buffer.append(String.format("%s%s, %s\n", indent, root.splitFeatureValue, root.splitFeature));
    }

    for (ID3Classifier child : root.children) {
      buffer.append(repr(child, indent + "  ", false));
    }
    return buffer.toString();
  }


}
