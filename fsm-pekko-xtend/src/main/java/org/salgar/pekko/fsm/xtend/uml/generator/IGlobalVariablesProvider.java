package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.xtext.generator.IGeneratorContext;

public interface IGlobalVariablesProvider {
    String getGlobalVariable(IGeneratorContext ctx, String key);
}