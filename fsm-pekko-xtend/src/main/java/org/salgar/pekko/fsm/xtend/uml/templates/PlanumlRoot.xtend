package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class PlanumlRoot extends AbstractGenerator {
	@Inject PlanumlTemplate planumlTemplate
	@Inject PlanumlActivityTemplate planumlActivityTemplate

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		planumlTemplate.doGenerate(input,fsa)
		planumlActivityTemplate.doGenerate(input,fsa)
	}
}