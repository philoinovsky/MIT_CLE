package edu.mit.compilers.tools;

import antlr.collections.AST;
import edu.mit.compilers.defs.VarType;

// Error Reporter
public class Err {
    private static boolean trace = false;
    private static boolean error = false;

    public static final void setTrace() {
        trace = true;
    }

    public static final void setError() {
        error = true;
    }

    public static final boolean hasError() {
        return error;
    }

    public static final void errMainNotDefined(AST t) {
        setError();
        if (!trace) {
            return;
        }
        System.err.printf("line %d, col %d: main() is not defined\n", -1, -1);
    }

    public static final void report(AST t, String fmt, Object... args) {
        setError();
        if (!trace) {
            return;
        }
        System.err.printf("line %d, col %d: ", t.getLine(), t.getColumn());
        System.err.printf(fmt, args);
    }

    public static final void errNotDefined(AST t, String var) { 
        report(t, "not defined $%s\n", var);
    }

    public static final void errDuplicatedDeclaration(AST t, String var) {
        report(t, "$%s is already defined\n", var);
    }

    public static final void errArrayIndexNotInt(AST t, String arrayID, VarType type) {
        report(t, "array $%s index is not <int>, given <%s>\n", arrayID, type);
    }

    public static final void errType(AST t, VarType expectedType, VarType givenType) {
        report(t, "expect type <%s>, given <%s>\n", expectedType, givenType);
    }

    public static final void errNotArrayType(AST t, VarType givenType) {
        report(t, "expect <%s[]>, given <%s>\n", givenType, givenType);
    }

    public static final void errBreak(AST t) {
        report(t, "not break inside loop\n");
    }

    public static final void errContinue(AST t) {
        report(t, "not continue inside loop\n");
    }

    public static final void errBadImport(AST t, String var) {
        report(t, "bad import name $%s\n", var);
    }

    public static final void errBadArrayCap(AST t) {
        report(t, "bad array capacity\n");
    }

    public static final void errArrayArgsMismatch(AST t) {
        report(t, "array args length mismatch\n");
    }

    public static final void errVarIsNotArray(AST t, String var) {
        report(t, "$%s cannot be accessed by subscript\n", var);
    }

    public static final void errIntegerTooLarge(AST t, String intLiteral) {
        report(t, "int literal '%s' is too large\n", intLiteral);
    }

    public static final void errMalformedMain(AST t, VarType returnType, int paramCount) {
        report(t, "main returns %s and has %d params\n", returnType, paramCount);
    }
}
