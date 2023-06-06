package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtext.generator.IGeneratorContext;


public class FsmPekkoGlobalVariableProvider implements IGlobalVariablesProvider {
    @Override
    public String getGlobalVariable(IGeneratorContext ctx, String key) {
        FsmPekkoGeneratorContext fsmPekkoGeneratorContext = (FsmPekkoGeneratorContext) ctx;

        Variable variable = fsmPekkoGeneratorContext.getVariables().get(key);

        return variable.getValue().toString();
    }
}