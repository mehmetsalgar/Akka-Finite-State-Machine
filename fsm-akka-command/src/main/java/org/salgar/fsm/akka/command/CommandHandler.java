package org.salgar.fsm.akka.command;

import com.google.protobuf.Message;
import scala.concurrent.Future;

public interface CommandHandler<COMMAND extends Message, RESPONSE> {
    Future<RESPONSE> handleCommand(COMMAND command);
    String type();
}