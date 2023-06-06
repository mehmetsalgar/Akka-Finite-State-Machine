package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class TestActionGuardTemplate extends AbstractGenerator {
	@Inject TestActionTemplate testActionTemplate
	@Inject TestActionConfigTemplate testActionConfigTemplate;
	@Inject TestGuardTemplate testGuardTemplate
	@Inject TestGuardConfigTemplate testGuardConfigTemplate

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
	    for(sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine).filter(s | s.isActive)) {
	        testActionTemplate.generate(sm, fsa, context)
	        testActionConfigTemplate.generate(sm, fsa, context)
	        testGuardTemplate.generate(sm, fsa, context)
	        testGuardConfigTemplate.generate(sm, fsa, context)
	    }
	}
}