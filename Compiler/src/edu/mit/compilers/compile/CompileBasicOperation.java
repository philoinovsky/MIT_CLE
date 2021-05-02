package edu.mit.compilers.compile;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.asm.asm;
import edu.mit.compilers.asm.basic.Label;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.asm.basic.Oprand;
import edu.mit.compilers.asm.basic.Reg;
import edu.mit.compilers.grammar.DecafParserTokenTypes;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.syntax.Program;

public class CompileBasicOperation {
    public static void binaryAssign(String op, List<String> codes) {
        if (!Program.shouldCompile()) return;
        if (op.equals("=")) {
            Reg tmpReg = Manager.newTmpReg();
            Oprand rAddr = Manager.tmpPop();
            Oprand lAddr = Manager.tmpPop();
            if (rAddr instanceof Num) {
                codes.add(
                    asm.bin("movq", rAddr, lAddr)  
                );
            } else {
                Collections.addAll(codes,
                    asm.bin("movq", rAddr, tmpReg),
                    asm.bin("movq", tmpReg, lAddr)
                ); 
            }
        } else {
            String asmOp = op.equals("+=") ? "addq" : "subq";
            Reg tmpReg = Manager.newTmpReg();
            Oprand rAddr = Manager.tmpPop();
            Oprand lAddr = Manager.tmpPop();
            if (rAddr instanceof Num) {
                codes.add(
                    asm.bin(asmOp, rAddr, lAddr)
                );
            } else {
                Collections.addAll(codes,
                    asm.bin("movq", lAddr, tmpReg),
                    asm.bin(asmOp, rAddr, tmpReg),
                    asm.bin("movq", tmpReg, lAddr)  
                );
            }
        }
    }

    public static void unaryAssign(Integer operator, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Oprand lAddr = Manager.tmpPop();
        String op = operator == DecafParserTokenTypes.INCRE ? "addq" : "subq";
        codes.add(
            asm.bin(op, new Num(1L), lAddr)  
        );
    }

    public static void relOps(List<String> codesCondition, List<String> codesIfExecution, List<String> codesElseExecution, List<String> codes) {
        if (!Program.shouldCompile()) return;
        Label ifExecutionEndLabel = new Label();
        Label ifElseEndLabel = new Label();
        Reg resultReg = Manager.newTmpReg();
        Oprand elseOp = Manager.tmpPop();
        Oprand ifOp = Manager.tmpPop();
        Oprand conditionOp = Manager.tmpPop();
        Reg condition;
        if (conditionOp instanceof Reg) {
            condition = (Reg)conditionOp;
        } else {
            condition = Manager.newTmpReg();
            codesCondition.add(
                asm.bin("movq", conditionOp, condition)    
            );
        }
        Collections.addAll(codesCondition,
            asm.bin("cmp", new Num(0L), condition.bite()),
            asm.jmp("je", ifExecutionEndLabel)
        );
        Collections.addAll(codesIfExecution,
            asm.bin("movq", ifOp, resultReg),
            asm.jmp("jmp", ifElseEndLabel)
        );
        codesElseExecution.add(
            asm.bin("movq", elseOp, resultReg)
        );
        codes.add(asm.cmt("ternary - start"));
        codes.add(asm.cmt("ternary - condition"));
        codes.addAll(codesCondition);
        codes.add(asm.cmt("ternary - if execution"));
        codes.addAll(codesIfExecution);
        codes.add(asm.label(ifExecutionEndLabel));
        codes.add(asm.cmt("ternary - else execution"));
        codes.addAll(codesElseExecution);
        codes.add(asm.label(ifElseEndLabel));
        codes.add(asm.cmt("ternary - end"));
        Manager.tmpPush(resultReg);
    }
}
