package org.salgar.pekko.fsm.xtend.uml.engine;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.xtext.service.OperationCanceledError;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.validation.ResourceValidatorImpl;

import java.util.Collections;
import java.util.List;

public class ResourceValidatorImplExt extends ResourceValidatorImpl {
    @Override
    public List<Issue> validate(Resource resource, CheckMode mode, CancelIndicator mon)
            throws OperationCanceledError {
        if (resource.getContents().get(0) instanceof Profile) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}