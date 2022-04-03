package org.salgar.akka.fsm.xtend.uml.generator;

import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtext.generator.IGeneratorContext;


public class FsmAkkaGlobalVariableProvider implements IGlobalVariablesProvider {
    @Override
    public String getGlobalVariable(IGeneratorContext ctx, String key) {
        FsmAkkaGeneratorContext fsmAkkaGeneratorContext = (FsmAkkaGeneratorContext) ctx;

        Variable variable = fsmAkkaGeneratorContext.getVariables().get(key);

        return variable.getValue().toString();
    }
}