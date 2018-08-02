package org.iata.bsplink.refund.loader.error;

/**
 * Describes where the validation error has occurred.
 */
public enum ValidationPhase {

    /**
     * The error occurred in the file format phase.
     */
    FILE,

    /**
     * The error occurred in the document transaction validation phase.
     */
    TRANSACTION,

    /**
     * The error occurred in the update phase as result of the API service call.
     */
    UPDATE,

    /**
     * None of the other.
     */
    NONE
}
