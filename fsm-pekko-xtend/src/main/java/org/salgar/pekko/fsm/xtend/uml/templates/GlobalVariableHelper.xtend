package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IGeneratorContext
import org.salgar.pekko.fsm.xtend.uml.generator.IGlobalVariablesProvider

import org.salgar.pekko.fsm.xtend.uml.engine.TemplateAutoInjector

class GlobalVariableHelper {
    @Inject IGlobalVariablesProvider globalVariableProvider

    def String getGlobalVariable(IGeneratorContext context, String key) {
        TemplateAutoInjector.injectTemplates(this)
        globalVariableProvider.getGlobalVariable(context, key)
    }
}