package org.salgar.fsm.pekko.command;

import com.google.protobuf.Message;
import scala.concurrent.Future;

public interface CommandHandler<COMMAND extends Message, RESPONSE> {
    Future<RESPONSE> handleCommand(COMMAND command);
    String type();
}