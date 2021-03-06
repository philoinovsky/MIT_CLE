package edu.mit.compilers.asm.basic;

public class Label {
    private static int globalConstNumber = 0;
    private static int globalNumber = 0;
    private final String name;

    public Label() {
        this.name = String.format(".L%d", globalNumber++);
    }

    public Label(Boolean isConst) {
        if (!isConst) {
            this.name = String.format(".L%d", globalNumber++);
            return;
        }
        this.name = String.format(".LC%d", globalConstNumber++);
    }

    public Label(String name) {
        this.name = name;
    }

    public Boolean equals(Label rhs) {
        return this.name.equals(rhs.name);
    }

    public String toString() {
        return this.name;
    }
}
