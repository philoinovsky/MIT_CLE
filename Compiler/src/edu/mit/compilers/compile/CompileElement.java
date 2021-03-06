package edu.mit.compilers.compile;

import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.asm.basic.RegAddr;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileElement {
    public static void arrayElement(ActionType action, ArrayDesc desc, ABlock codes) {
        if (!Program.shouldCompile()) return;
        String varName = String.format("%s[]", desc.getText());
        Oprand index = Manager.tmpPop();
        Reg resReg = Manager.newTmpReg();
        Reg indexReg = Manager.newTmpReg(resReg);
        Integer offset = desc.getAddr().getOffset();
        codes.add(
            asm.bin("movq", index, indexReg),
            asm.bin("cmpq", new Num(desc.getCap()), indexReg),
            asm.jmp("jge", Defs.EXIT_ARRAY_OUTBOUND_LABEL),
            asm.bin("cmpq", new Num(0L), indexReg),
            asm.jmp("jl", Defs.EXIT_ARRAY_OUTBOUND_LABEL)
        );
        if (desc.getAddr().isGlobal()) {
            codes.add(
                asm.bin("leaq", new RegAddr(indexReg, varName), indexReg),
                asm.bin("leaq", desc.getAddr(), resReg)
            );
            if (action == ActionType.STORE) {
                Manager.tmpPush(new RegAddr(indexReg, resReg));
            } else {
                codes.add(
                    asm.bin("movq", new RegAddr(indexReg, resReg), resReg)
                );
                Manager.tmpPush(resReg);
            }
        } else {
            if (action == ActionType.STORE) {
                Manager.tmpPush(new RegAddr(offset, indexReg, varName));
            } else {
                codes.add(
                    asm.bin("movq", new RegAddr(offset, indexReg, varName), resReg)
                );
                Manager.tmpPush(resReg);
            }
        }
    }
}
