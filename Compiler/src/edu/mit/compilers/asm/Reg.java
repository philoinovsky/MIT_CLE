package edu.mit.compilers.asm;

// Register
public class Reg extends Oprand {
    public static final Reg rax = new Reg("rax");
    public static final Reg rcx = new Reg("rcx");
    public static final Reg rdx = new Reg("rdx");
    public static final Reg rbx = new Reg("rbx");
    public static final Reg rsp = new Reg("rsp");
    public static final Reg rbp = new Reg("rbp");
    public static final Reg rsi = new Reg("rsi");
    public static final Reg rdi = new Reg("rdi");
    public static final Reg eax = new Reg("eax");
    public static final Reg ecx = new Reg("ecx");
    public static final Reg edx = new Reg("edx");
    public static final Reg ebx = new Reg("ebx");
    public static final Reg esp = new Reg("esp");
    public static final Reg ebp = new Reg("ebp");
    public static final Reg esi = new Reg("esi");
    public static final Reg edi = new Reg("edi");
    public static final Reg al = new Reg("al");

    public static final Reg r8 = new Reg("r8");
    public static final Reg r9 = new Reg("r9");
    public static final Reg r10 = new Reg("r10");
    public static final Reg r11 = new Reg("r11");
    public static final Reg r12 = new Reg("r12");
    public static final Reg r13 = new Reg("r13");
    public static final Reg r14 = new Reg("r14");
    public static final Reg r15 = new Reg("r15");

    public static final Reg r8d = new Reg("r8d");
    public static final Reg r9d = new Reg("r9d");
    public static final Reg r10d = new Reg("r10d");
    public static final Reg r11d = new Reg("r11d");
    public static final Reg r12d = new Reg("r12d");
    public static final Reg r13d = new Reg("r13d");
    public static final Reg r14d = new Reg("r14d");
    public static final Reg r15d = new Reg("r15d");

    private String regName;
    // use as tmp
    private String tmpName = "";

    public Reg(String regName) {
        this.regName = regName;
    }

    public Reg(Reg parent, String tmpName) {
        this.regName = parent.getRegName();
        this.tmpName = tmpName;
    }

    public String getRegName() {
        return this.regName;
    }

    @Override
    public String toString() {
        return String.format("%%%s", this.regName);
    }

    @Override
    public String getName() {
        return tmpName;
    }
}
