package edu.mit.compilers.optimizer;

import java.util.List;

import edu.mit.compilers.asm.AProgram;
import edu.mit.compilers.cfg.CMethod;
import edu.mit.compilers.defs.Defs;

public class Optimizer {
    private Optimizer() {}

    public static AProgram optimize(AProgram program) {
        if (!Defs.isAnyOptimizationEnabled()) return program;
        List<CMethod> methods;
        // TODO: maybe split ABlock and get Available Expression
        methods = program.split();
        if (Defs.isGCSEEnabled()) {
            methods.forEach(GCSE::globalCommonSubexpressionElimination);
        }
        // TODO: recover methods to ABlock
        program.join(methods);
        return program;
    }
}