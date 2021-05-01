package edu.mit.compilers.compile;

import java.util.List;

import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Num;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.st.MethodUtils;
import edu.mit.compilers.syntax.Program;

public class CompileField {
    public static final void declareArray(String name, Integer size, Integer cap, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (MethodUtils.isGlobal()) {
            codes.addAll(
                asm.globalDecl(name, size * cap)
            );
        } else {
            Integer topOffset = MethodUtils.getDesc(name).getAddr().getOffset();
            for (int i = 0; i < cap; i++) {
                codes.add(
                    asm.bin("movq", new Num(0L), new Addr(topOffset + 8 * i, "array init"))
                );
            }
        }
    }

    public static final void declareVariable(String name, Integer size, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (MethodUtils.isGlobal()) {
            codes.addAll(
                asm.globalDecl(name, size)
            );
        } else {
            codes.add(
                asm.bin("movq", new Num(0L), MethodUtils.getDesc(name).getAddr())
            );
        }
    }
}