package edu.mit.compilers.asm.basic;

// Number
public class Bool extends Oprand {
    Boolean b;

    public Bool(Boolean b) {
        this.b = b;
    }

    public Bool exclam() {
        return new Bool(!this.b);
    }

    @Override
    public String toString() {
        return this.b ? "$1" : "$0";
    }

    @Override
    public String getName() {
        return "";
    }
}
