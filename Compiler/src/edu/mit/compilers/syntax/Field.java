package edu.mit.compilers.syntax;

import java.util.List;

import antlr.collections.AST;
import edu.mit.compilers.st.*;
import edu.mit.compilers.ast.AstUtils;
import edu.mit.compilers.compile.CompileField;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.tools.Err;

class Field {
    static final AST declare(AST t, List<String> codes) {
        for (; t != null && AstUtils.isType(t); t = t.getNextSibling()) {
            VarType type = null;
            for (AST c = t.getFirstChild(); c != null; c = c.getNextSibling()) {
                AST cc = c.getFirstChild();
                if (cc != null) {
                    // cc is not null -> is array
                    Integer cap = Integer.parseInt(cc.getText());
                    if (cap <= 0) {
                        Err.errBadArrayCap(cc);
                        cap = 1000000000;
                    }
                    if (Manager.isGlobal() && Program.importST.getMethod(c.getText()) != null) {
                        Err.errDuplicatedDeclaration(c, c.getText());
                        continue;
                    }
                    if (!Manager.push(new ArrayDesc(type, c.getText(), Long.valueOf(cap)), false)) {
                        Err.errDuplicatedDeclaration(c, c.getText());
                    }
                    CompileField.declareArray(c.getText(), cap, codes);
                    continue;
                }
                if (Program.importST.getMethod(c.getText()) != null) {
                    Err.errDuplicatedDeclaration(c, c.getText());
                    continue;
                }
                // cc is null -> it's single Variable
                if (!Manager.push(new VarDesc(type, c.getText()), false)) {
                    Err.errDuplicatedDeclaration(c, c.getText());
                }
                CompileField.declareVariable(c.getText(), codes);
            }
        }
        return t;
    }
}
