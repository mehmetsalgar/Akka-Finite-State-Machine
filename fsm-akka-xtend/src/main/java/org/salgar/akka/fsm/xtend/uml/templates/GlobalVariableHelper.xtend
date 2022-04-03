package org.salgar.akka.fsm.xtend.uml.templates

import com.google.inject.Inject
import org.eclipse.xtext.generator.IGeneratorContext
import org.salgar.akka.fsm.xtend.uml.generator.IGlobalVariablesProvider

class GlobalVariableHelper {
    @Inject IGlobalVariablesProvider globalVariableProvider

    def String getGlobalVariable(IGeneratorContext context, String key) {
        globalVariableProvider.getGlobalVariable(context, key)
    }
}