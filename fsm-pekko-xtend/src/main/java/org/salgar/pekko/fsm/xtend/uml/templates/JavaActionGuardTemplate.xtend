package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class JavaActionGuardTemplate extends AbstractGenerator {
	@Inject JavaActionTemplate javaActionTemplate
	@Inject JavaGuardTemplate javaGuardTemplate

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
	    for(sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter(s | s.isActive)) {
	        javaActionTemplate.generate(sm, fsa, context)
	        javaGuardTemplate.generate(sm, fsa, context)
	    }
	}
}