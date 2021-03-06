package edu.mit.compilers.asm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ABlock {
    private int varOffset;

    private List<ALine> aLines = new ArrayList<>();

    public ABlock() {}
    public ABlock(int varOffset) {
        this.varOffset = varOffset;
    }

    public void add(ALine... lines) {
        for (int i = 0; i < lines.length; i++)
            this.aLines.add(lines[i]);
    }

    public void addAll(ABlock rhs) {
        this.aLines.addAll(rhs.aLines);
    }

    public void addAll(List<ALine> aLines) {
        this.aLines.addAll(aLines);
    }

    public void addLeftAll(ABlock rhs) {
        this.aLines.addAll(0, rhs.aLines);
    }

    public ALine get(int i) {
        return aLines.get(i);
    }

    public List<ALine> subLines(int start, int end) {
        List<ALine> res = new ArrayList<>();
        for (int i = start; i < end; i++) {
            res.add(this.aLines.get(i));
        }
        return res;
    }

    public int getVarOffset() {
        return this.varOffset;
    }

    public int size() {
        return aLines.size();
    }

    public void clear() {
        this.aLines.clear();
    }

    public void print(PrintStream stream) {
        for (ALine line: this.aLines) {
            stream.println(line);
        }
        // this.aLines.forEach(stream::println);
    }
}
