package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.xtext.generator.IGeneratorContext;


public class FsmPekkoGlobalVariableProvider implements IGlobalVariablesProvider {
    @Override
    public String getGlobalVariable(IGeneratorContext ctx, String key) {
        FsmPekkoGeneratorContext fsmPekkoGeneratorContext = (FsmPekkoGeneratorContext) ctx;

        Object variable = fsmPekkoGeneratorContext.getVariables().get(key);

        return variable.toString();
    }
}