package org.salgar.pekko.fsm.xtend.uml.generator;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.xtend.expression.ExecutionContextImpl;
import org.eclipse.xtend.expression.ExpressionFacade;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtext.generator.GeneratorComponent;
import org.eclipse.xtext.generator.GeneratorContext;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.util.CancelIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://git.eclipse.org/c/m2t/org.eclipse.xpand.git/tree/plugins/org.eclipse.xpand/src/org/eclipse/xpand2/Generator.java?id=96b4920b9d735f05fd593761e956d2ac6a7696cd
 * https://git.eclipse.org/c/m2t/org.eclipse.xpand.git/tree/plugins/org.eclipse.xtend/src/org/eclipse/xtend/expression/AbstractExpressionsUsingWorkflowComponent.java
 *
 * @author msalgar
 */
public class FsmPekkoGeneratorComponent extends GeneratorComponent {
    private List<GlobalVarDef> globalVarDefs = new ArrayList<>();
    private final List<GlobalVar> globalVars = new ArrayList<>();
    protected final List<MetaModel> metaModels = new ArrayList<>();

    public static class GlobalVarDef {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public void addGlobalVarDef(GlobalVarDef def) {
        globalVarDefs.add(def);
    }

    public static class GlobalVar {
        private String name;
        private Object value;

        public void setName(final String name) {
            this.name = name;
        }

        public void setValue(final Object value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public void addGlobalVar(final GlobalVar var) {
        System.out.println("add name: " + var.getName() + "value: " + var.getValue());
        globalVars.add(var);
    }

    public void addMetaModel(final MetaModel metaModel) {
        assert metaModel != null;
        metaModels.add(metaModel);
    }

    @Override
    public void invoke(IWorkflowContext ctx) {
        GeneratorDelegate instance = getCompiler();
        IFileSystemAccess2 fileSystemAccess = getConfiguredFileSystemAccess();
        for (String slot : getSlotNames()) {
            Object object = ctx.get(slot);
            if (object == null) {
                throw new IllegalStateException("Slot '" + slot + "' was empty!");
            }
            if (object instanceof Iterable) {
                Iterable<?> iterable = (Iterable<?>) object;
                for (Object object2 : iterable) {
                    if (!(object2 instanceof Resource)) {
                        throw new IllegalStateException("Slot contents was not a Resource but a '" + object.getClass().getSimpleName() + "'!");
                    }
                    GeneratorContext context = new FsmPekkoGeneratorContext(getGlobalVars(ctx));
                    context.setCancelIndicator(CancelIndicator.NullImpl);
                    instance.generate((Resource) object2, fileSystemAccess, context);
                }
            } else if (object instanceof Resource) {
                instance.doGenerate((Resource) object, fileSystemAccess);
            } else {
                throw new IllegalStateException("Slot contents was not a Resource but a '" + object.getClass().getSimpleName() + "'!");
            }
        }
    }

    protected Map<String, Variable> getGlobalVars(final IWorkflowContext ctx) {
        final Map<String, Variable> result = new HashMap<>();

        if (!globalVarDefs.isEmpty()) {
            ExecutionContextImpl ec = new ExecutionContextImpl();

            for (String slot : ctx.getSlotNames()) {
                ec = (ExecutionContextImpl) ec.cloneWithVariable(new Variable(slot, ctx.get(slot)));
            }

            for (MetaModel mm : metaModels) {
                ec.registerMetaModel(mm);
            }

            final ExpressionFacade ef = new ExpressionFacade(ec);
            for (GlobalVarDef def : globalVarDefs) {
                final Object value = ef.evaluate(def.value);

                result.put(def.getName(), new Variable(def.getName(), value));
            }
        }
        for (GlobalVar gv : globalVars) {
            System.out.println("iterate" + gv.toString());
            result.put(gv.getName(), new Variable(gv.getName(), gv.getValue()));
        }
        return result;
    }
}
