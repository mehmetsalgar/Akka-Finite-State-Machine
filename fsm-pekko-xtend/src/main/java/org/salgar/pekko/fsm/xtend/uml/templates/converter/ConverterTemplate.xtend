package org.salgar.pekko.fsm.xtend.uml.templates.converter

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ConverterTemplate {
	@Inject extension Naming
	@Inject extension ConverterHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.Class).filter[i|!(i instanceof org.eclipse.uml2.uml.StateMachine)]
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/converter/"+name+"Protobuf2PojoConverter.java", content)
            ]
    }

    def String generate(org.eclipse.uml2.uml.Class it) {
        if(getAllAttributes.filter(org.eclipse.uml2.uml.Property).size() == 1) {
            generateMSingleProperty
        } else {
            generateMultipleProperties
        }
    }

    def String generateMSingleProperty (org.eclipse.uml2.uml.Class it) '''
        «val org.eclipse.uml2.uml.Property property = getAllAttributes.filter(org.eclipse.uml2.uml.Property).get(0)»
        package «packageName».converter;

        import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Component;

        @Component
        @RequiredArgsConstructor
        public class «name»Protobuf2PojoConverter
                implements Protobuf2PojoConverter<«packageName».protobuf.«name», «calculateReturnType(property)»> {
            «FOR org.eclipse.uml2.uml.Property _property : getAllAttributes.filter(org.eclipse.uml2.uml.Property).filter[a|a.association!==null]»
                private final «packageName(_property.type)».converter.«_property.type.name»Protobuf2PojoConverter «_property.type.name.toLowerCase»Protobuf2PojoConverter;
            «ENDFOR»

            @Override
            public «calculateReturnType(property)» convert(«packageName».protobuf.«name» «name.toFirstLower») {
                return
                        «IF property.upper === -1»
                            «name.toFirstLower»
                                .get«property.association.name.toFirstUpper»List()
                                .stream()
                                .map(«property.type.name.toLowerCase»Protobuf2PojoConverter::convert)
                                .collect(java.util.stream.Collectors.toList())
                        «ELSEIF property.association === null»
                            «name.toFirstLower».get«property.name.toFirstUpper»()
                        «ELSE»
                            «property.type.name.toLowerCase»Protobuf2PojoConverter.convert(«name.toFirstLower».get«property.association.name.toFirstUpper»())
                        «ENDIF»
                    ;
            }

            @Override
            public String canConvert() {
                return PROTOBUF_PREFIX + «packageName».protobuf.«name».getDescriptor().getFullName();
            }

            @Override
            public Class<«packageName».protobuf.«name»> destinationType() {
                return «packageName».protobuf.«name».class;
            }
        }
    '''

    def String generateMultipleProperties (org.eclipse.uml2.uml.Class it) '''
        package «packageName».converter;

        import org.salgar.fsm.pekko.converter.Protobuf2PojoConverter;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Component;

        @Component
        @RequiredArgsConstructor
        public class «name»Protobuf2PojoConverter
                implements Protobuf2PojoConverter<«packageName».protobuf.«name», «packageName».model.«name»> {
            «FOR org.eclipse.uml2.uml.Property property : getAllAttributes.filter(org.eclipse.uml2.uml.Property).filter[a|a.association!==null]»
                private final «packageName(property.type)».converter.«property.type.name»Protobuf2PojoConverter «property.type.name.toLowerCase»Protobuf2PojoConverter;
            «ENDFOR»

            @Override
            public «packageName».model.«name» convert(«packageName».protobuf.«name» «name.toFirstLower») {
                return new «packageName».model.«name»(
                        «FOR org.eclipse.uml2.uml.Property property : getAllAttributes.filter(org.eclipse.uml2.uml.Property) SEPARATOR ","»
                            «IF property.upper === -1»
                                «name.toFirstLower»
                                    .get«property.association.name.toFirstUpper»List()
                                    .stream()
                                    .map(«property.type.name.toLowerCase»Protobuf2PojoConverter::convert)
                                    .collect(java.util.stream.Collectors.toList())
                            «ELSEIF property.association === null»
                                «name.toFirstLower».get«property.name.toFirstUpper»()
                            «ELSEIF (property.association.getMemberEnds().head.type as org.eclipse.uml2.uml.Class).getAllAttributes().size === 1»
                                new «packageName».model.«property.association.name.toFirstUpper»(«property.type.name.toLowerCase»Protobuf2PojoConverter.convert(«name.toFirstLower».get«property.association.name.toFirstUpper»()))
                            «ELSE»
                                «property.type.name.toLowerCase»Protobuf2PojoConverter.convert(«name.toFirstLower».get«property.association.name.toFirstUpper»())
                            «ENDIF»
                        «ENDFOR»
                        );
            }

            @Override
            public String canConvert() {
                return PROTOBUF_PREFIX + «packageName».protobuf.«name».getDescriptor().getFullName();
            }

            @Override
            public Class<«packageName».protobuf.«name»> destinationType() {
                return «packageName».protobuf.«name».class;
            }
        }
    '''
}