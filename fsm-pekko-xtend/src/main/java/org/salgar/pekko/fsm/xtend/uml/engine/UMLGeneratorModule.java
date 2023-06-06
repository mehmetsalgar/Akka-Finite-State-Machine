package org.salgar.pekko.fsm.xtend.uml.engine;

import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;
import org.eclipse.xtext.service.SingletonBinding;
import org.eclipse.xtext.validation.IResourceValidator;
import org.salgar.pekko.fsm.xtend.uml.validation.BasicConstraints;

public class UMLGeneratorModule extends AbstractGenericResourceRuntimeModule {
    protected Class<? extends IGenerator2> generator;

    public UMLGeneratorModule(Class<? extends IGenerator2> generator) {
        this.generator = generator;
    }

    public Class<? extends ResourceSet> bindResourceSet() {
        return ResourceSetImpl.class;
    }

    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return DefaultDeclarativeQualifiedNameProvider.class;
    }

    @Override
    protected String getLanguageName() {
        return "uml";
    }

    @Override
    protected String getFileExtensions() {
        return "uml";
    }

    public Class<? extends IResourceFactory> bindIResourceFactory() {
        return UMLResourceFactory.class;
    }

    public Class<? extends IResourceValidator> bindResourceValidator() {
        return ResourceValidatorImplExt.class;
    }

    public EValidator.Registry bindEValidatorRegistry() {
        return EValidator.Registry.INSTANCE;
    }

    @SingletonBinding(eager = true)
    public Class<? extends BasicConstraints> bindBasicConstraints() {
        return BasicConstraints.class;
    }

    public Class<? extends IGenerator2> bindIGenerator() {
        return generator;
    }

    @SingletonBinding
    public Class<? extends IOutputConfigurationProvider> bindIOutputConfigurationProvider() {
        return OutputConfigurationProvider.class;
    }
}