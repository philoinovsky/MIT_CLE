package edu.mit.compilers.compile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.asm.Addr;
import edu.mit.compilers.asm.Label;
import edu.mit.compilers.asm.asmUtils;
import edu.mit.compilers.asm.asm;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.defs.Defs;
import edu.mit.compilers.st.ST;
import edu.mit.compilers.tools.Er;
import edu.mit.compilers.grammar.*;

public class Structure {
    private static Boolean isBinaryAnyOp(AST t) {
        return 
        AstUtils.isBinaryOp(t)
        || AstUtils.isBinaryCompOp(t)
        || AstUtils.isBinaryBoolOp(t)
        || AstUtils.isBinaryIntCompOp(t);
    }

    // <expr>  => location
    // | method_call
    // | literal
    // | len ( id )
    // | expr bin_op expr
    // | - expr
    // | ! expr
    static String expr(AST t, ST st, List<String> codes) {
        if (t == null) {
            Program.result.add(null);
            return null;
        }
        if (isBinaryAnyOp(t) && t.getNumberOfChildren() == 2) {
            AST l = t.getFirstChild();
            AST r = l.getNextSibling();
            List<String> leftCodes = new ArrayList<>();
            List<String> rightCodes = new ArrayList<>();
            String lType = expr(l, st, leftCodes);
            String rType = expr(r, st, rightCodes);
            String returnType = null;
            if (AstUtils.isBinaryOp(t)) {
                if (lType != null && !Defs.equals(lType, rType)) {
                    System.err.printf("16 ");
                    Er.errType(l, lType, rType);
                }
                returnType = lType;
            } else if (AstUtils.isBinaryCompOp(t)) {
                if (lType != null && (!Defs.equals(lType, rType) || Defs.equals(Defs.DESC_TYPE_VOID, lType))) {
                    System.err.printf("31 ");
                    Er.errType(r, lType, rType);
                }
                returnType = Defs.DESC_TYPE_BOOL;    
            } else if (AstUtils.isBinaryBoolOp(t)) {
                if (lType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, lType)) {
                    System.err.printf("17 ");
                    Er.errType(l, Defs.DESC_TYPE_BOOL, lType);
                }
                if (rType != null && !Defs.equals(Defs.DESC_TYPE_BOOL, rType)) {
                    System.err.printf("18 ");
                    Er.errType(r, Defs.DESC_TYPE_BOOL, rType);
                }
                returnType = Defs.DESC_TYPE_BOOL;
            } else if (AstUtils.isBinaryIntCompOp(t)) {
                if (lType != null && !Defs.equals(Defs.DESC_TYPE_INT, lType)) {
                    System.err.printf("27 ");
                    Er.errType(l, Defs.DESC_TYPE_INT, lType);
                }
                if (rType != null && !Defs.equals(Defs.DESC_TYPE_INT, rType)) {
                    System.err.printf("28 ");
                    Er.errType(r, Defs.DESC_TYPE_INT, rType);
                }
                returnType = Defs.DESC_TYPE_BOOL;
            }
            if (Program.shouldCompile()) {
                // TODO
                Addr rAddr = Program.result.pop();
                Addr lAddr = Program.result.pop();
                Addr resAddr = st.resultAddr(lAddr, rAddr);
                List<String> glueCodes = new ArrayList<>();
                if (AstUtils.isBinaryBoolOp(t)) {
                    Label boolRes = new Label();
                    rightCodes.add(
                        asm.label(boolRes)
                    );
                    String jmpOp = t.getType() == DecafScannerTokenTypes.AND ? "jne" : "je";
                    leftCodes.add(
                        asm.jmp(jmpOp, boolRes)
                    );

                } else {
                    Collections.addAll(glueCodes,
                        asm.bin("movq", lAddr, resAddr),
                        asm.bin(asmUtils.binaryOpToken2Inst.get(t.getType()), rAddr, resAddr)
                    );
                }
                codes.addAll(leftCodes);
                codes.addAll(rightCodes);
                codes.addAll(glueCodes);
                Program.result.push(resAddr);
            }
            return returnType;
        }
        List<String> thisCodes = new ArrayList<>();
        switch(t.getType()) {
            case DecafScannerTokenTypes.ID:
                String type = st.getType(t.getText());
                if (type == null) {
                    type = Program.importST.getType(t.getText());
                    if (type == null) {
                        System.out.printf("19 ");
                        Er.errNotDefined(t, t.getText());
                        return Defs.DESC_TYPE_WILDCARD;
                    }
                }
                // array
                if (Defs.isArrayType(type)) {
                    if (t.getNumberOfChildren() == 0) {
                        return type;
                    }
                    return Element.arrayElement(t, st, codes);
                }
                // method
                if (Defs.isMethodType(type)) {
                    return Method.call(t, st, codes);
                }
                // var
                // TODO
                return type;
            case DecafScannerTokenTypes.INTLITERAL:
                return Element.intLiteral(t, false, codes);
            case DecafScannerTokenTypes.TK_true:
            case DecafScannerTokenTypes.TK_false:
                // TODO
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.MINUS:
                if (t.getNumberOfChildren() == 1 && t.getFirstChild().getType() == DecafScannerTokenTypes.INTLITERAL) {
                    return Element.intLiteral(t.getFirstChild(), true, codes);
                }
                String subType = expr(t.getFirstChild(), st, thisCodes);
                if (subType != null && !Defs.equals(Defs.DESC_TYPE_INT, subType)) {
                    System.err.printf("20 ");
                    Er.errType(t, Defs.DESC_TYPE_INT, subType);
                }
                if (Program.shouldCompile()) {
                    // TODO
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.EXCLAM:
                String subType0 = expr(t.getFirstChild(), st, thisCodes);
                if (subType0 != null && !Defs.equals(Defs.DESC_TYPE_BOOL, subType0)) {
                    System.err.printf("21 ");
                    Er.errType(t, Defs.DESC_TYPE_BOOL, subType0); 
                }
                if (Program.shouldCompile()) {
                    // TODO
                }
                return Defs.DESC_TYPE_BOOL;
            case DecafScannerTokenTypes.TK_len:
                AST c = t.getFirstChild();
                String subType1 = st.getType(c.getText());
                if (subType1 == null || !Defs.isArrayType(subType1)) {
                    System.err.printf("22 ");
                    Er.errNotDefined(c, c.getText());
                }
                if (Program.shouldCompile()) {
                    // TODO
                }
                return Defs.DESC_TYPE_INT;
            case DecafScannerTokenTypes.STRINGLITERAL:
                if (Program.shouldCompile()) {
                    // TODO
                }
                return Defs.TYPE_STRING_LITERAL;
            case DecafScannerTokenTypes.CHARLITERAL:
                if (Program.shouldCompile()) {
                    // TODO
                }
                return Defs.TYPE_CHAR_LITERAL;
            case DecafScannerTokenTypes.COLON:
                return Operation.relOps(t, st, thisCodes);
        }
        return null;
    }

    // if null -> return; if TK_else -> return current AST
    static AST block(AST t, ST st, List<String> codes) {
        // parse fields
        t = FieldDecl.parse(t, st, codes);
        // parse statements
        for (; t != null; t = t.getNextSibling()) {
            if (t.getType() == DecafScannerTokenTypes.TK_else) {
                return t;
            }
            parseStmt(t, st, codes);
        }
        return null;
    }

    static void parseStmt(AST t, ST st, List<String> codes) {
        switch (t.getType()) {
            case DecafScannerTokenTypes.ID:
                Method.call(t, st, codes);
                return;
            case DecafScannerTokenTypes.ASSIGN:
            case DecafScannerTokenTypes.PLUSASSIGN:
            case DecafScannerTokenTypes.MINUSASSIGN:
            case DecafScannerTokenTypes.INCRE:
            case DecafScannerTokenTypes.DECRE:
                Operation.moreAssign(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_if:
                ControlFlow.ifFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_for:
                ControlFlow.forFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_while:
                ControlFlow.whileFlow(t, st, codes);
                return;
            case DecafScannerTokenTypes.TK_break:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("23 ");
                    Er.errBreak(t);
                }
                return;
            case DecafScannerTokenTypes.TK_continue:
                if (!AstUtils.isLoop(st.getContext())) {
                    System.err.printf("24 ");
                    Er.errContinue(t);
                }
                return;
            case DecafScannerTokenTypes.TK_return:
                String expectedReturnType = st.getReturnType();
                if (expectedReturnType == null) {
                    System.err.printf("25 ");
                    Er.report(t, "null return");
                    return;
                }
                String actualReturnType = expr(t.getFirstChild(), st, codes);
                if (actualReturnType == null) {
                    actualReturnType = Defs.DESC_TYPE_VOID;
                }
                if (!Defs.equals(expectedReturnType, actualReturnType)) {
                    System.err.printf("26 ");
                    Er.errType(t, expectedReturnType, actualReturnType);
                }
                if (Program.shouldCompile()) {
                    // TODO
                }
                return;
        }
    }
}
