package edu.mit.compilers.st;

import edu.mit.compilers.asm.Addr;

// ClassDesc -> "class name" "name", "fieldST" "methodST"
// MethodDesc -> "return type" "name", "localST"
// TypeDesc -> "type" "int" | "bool" | "$class"
// ArrayDesc -> "array" "int"
// ParamDesc -> "int" "name"
// LocalDesc -> "int" "name"
// ThisDesc -> "this" "classdesc name", ""
public abstract class Descriptor {
    private String type;
    private String text;
    private Addr addr;

    Descriptor(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public abstract String findVar(String text);
    public abstract String findMethod(String text);

    public void setAddr(Addr addr) {
        this.addr = addr;
    }

    public String getType() {
        return this.type;
    }
    
    public String getText() {
        return this.text;
    }

    public Addr getAddr() {
        return this.addr;
    }
}
