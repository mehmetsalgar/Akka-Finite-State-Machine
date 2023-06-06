package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class AsciidocRoot extends AbstractGenerator {
	@Inject AsciidocFsmTemplate asciidocFsmTemplate
	// add more templates here

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		asciidocFsmTemplate.doGenerate(input,fsa)
	}
}