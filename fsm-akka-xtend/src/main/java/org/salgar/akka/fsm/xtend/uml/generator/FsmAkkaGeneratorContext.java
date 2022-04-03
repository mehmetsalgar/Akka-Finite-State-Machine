package org.salgar.akka.fsm.xtend.uml.generator;

import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtext.generator.GeneratorContext;

import java.util.Map;

public class FsmAkkaGeneratorContext extends GeneratorContext {
    private Map<String, Variable> variables;

    public FsmAkkaGeneratorContext(Map<String, Variable> variables) {
        super();
        this.variables = variables;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }
}