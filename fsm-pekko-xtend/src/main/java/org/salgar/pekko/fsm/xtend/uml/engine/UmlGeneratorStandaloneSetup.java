package org.salgar.pekko.fsm.xtend.uml.engine;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.generator.IGenerator2;
import org.salgar.pekko.fsm.xtend.uml.generator.FsmPekkoGlobalVariableProvider;
import org.salgar.pekko.fsm.xtend.uml.generator.IGlobalVariablesProvider;

public class UmlGeneratorStandaloneSetup implements ISetup {
    private Injector injector;
    private GeneratorConfig config;

    public void setConfig(GeneratorConfig config) {
        this.config = config;
    }

    public void setDoInit(boolean init) {
        createInjectorAndDoEMFRegistration();
    }

    public UmlGeneratorStandaloneSetup() {
    }

    private Module getDynamicModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(GeneratorConfig.class).toInstance(config);
                bind(IGlobalVariablesProvider.class).toInstance(new FsmPekkoGlobalVariableProvider());
            }
        };
    }

    @Override
    public Injector createInjectorAndDoEMFRegistration() {
        if(injector == null) {
            try {
                injector = Guice.createInjector(
                        new UMLGeneratorModule((Class<? extends IGenerator2>)Class.forName(config.getRootClass())), getDynamicModule());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            register(injector);
        }
        return injector;
    }

    public void register(Injector injector) {
        org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
        org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector.getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", resourceFactory);
        org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", serviceProvider);

        ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
        UMLResourcesUtil.init(resourceSet);
    }
}