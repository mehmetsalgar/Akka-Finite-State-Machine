package org.salgar.pekko.fsm.foureyes.notifier;

import java.util.List;

public interface NotifierService {
    void notify(List<String> notificationTargets, String message);
    List<String> calculateRecipientList(String targetGroup);
}