package decisiontree;

/**
 * Stores a single training instance consisting of values and a label.
 */
public class Instance {
    private String[] values;
    private String label;

    public Instance(String input, int targetIndex) {
        String [] splitInput = input.split("\\s*,\\s*");
        values = new String[splitInput.length-1];

        int valueIndex = 0;
        for (int i=0; i<splitInput.length; i++) {
            if (i != targetIndex) {
                this.values[valueIndex] = splitInput[i];
                valueIndex ++;
            } else {
                this.label = splitInput[i];
            }
        }
    }

    public String getValue(int index) {
        return this.values[index];
    }

    public int nClasses() {
        return this.values.length;
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return String.format("([%s], %s)", String.join(",", this.values), this.label);
    }
}