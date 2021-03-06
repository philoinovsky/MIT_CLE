package edu.mit.compilers.syntax;

import antlr.collections.AST;
import edu.mit.compilers.asm.ABlock;
import edu.mit.compilers.asm.basic.Num;
import edu.mit.compilers.compile.CompileElement;
import edu.mit.compilers.defs.VarType;
import edu.mit.compilers.defs.Defs.ActionType;
import edu.mit.compilers.st.ArrayDesc;
import edu.mit.compilers.st.Manager;
import edu.mit.compilers.tools.Err;

public class Element {
    static VarType arrayElement(AST t, ActionType action, ABlock codes) {
        ArrayDesc desc = Manager.getArray(t.getText());
        if (desc == null) {
            Err.errNotDefined(t, t.getText());
            return null;
        }
        VarType type = desc.getType();
        AST c = t.getFirstChild();
        VarType indexType = Structure.expr(c, ActionType.LOAD, codes);
        if (!indexType.isInt()) {
            Err.errArrayIndexNotInt(t, desc.getText(), indexType);
            return type.plain();
        }
        CompileElement.arrayElement(action, desc, codes);
        return type.plain();
    }

    static VarType intLiteral(AST t, boolean isNegative) {
        String number;
        Long result = null;
        if (isNegative) {
            number = "-" + t.getText();
        } else {
            number = t.getText();
        }
        try {
            result = Long.parseLong(number);
        } catch (NumberFormatException e) {
            try {
                result = Long.decode(number);
            } catch (Exception ee) {
                Err.errIntegerTooLarge(t, number);
            }
        }
        Manager.tmpPush(new Num(result));
        return VarType.INT;
    }
}
