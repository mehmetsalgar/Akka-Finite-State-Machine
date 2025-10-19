package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.xtext.generator.GeneratorContext;

import java.util.Map;

public class FsmPekkoGeneratorContext extends GeneratorContext {
    private Map<String, Object> variables;

    public FsmPekkoGeneratorContext(Map<String, Object> variables) {
        super();
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}