package org.salgar.pekko.fsm.xtend.uml.templates.model.pojo

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class PojoGenerator extends AbstractGenerator {
    @Inject PojoTemplate pojoTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        pojoTemplate.doGenerate(input, fsa)
    }
}