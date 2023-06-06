package org.salgar.fsm.pekko.elasticsearch;

public interface OffsetState {

    /**
     * Marks the offset as processed (ready to report to preCommit)
     */
    void markProcessed();

    /**
     * @return true if record is processed
     */
    boolean isProcessed();

    /**
     *
     * @return current offset
     */
    String offset();
}