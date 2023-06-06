package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtext.generator.GeneratorContext;

import java.util.Map;

public class FsmPekkoGeneratorContext extends GeneratorContext {
    private Map<String, Variable> variables;

    public FsmPekkoGeneratorContext(Map<String, Variable> variables) {
        super();
        this.variables = variables;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }
}