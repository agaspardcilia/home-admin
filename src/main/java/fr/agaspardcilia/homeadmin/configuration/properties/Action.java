package fr.agaspardcilia.homeadmin.configuration.properties;

import lombok.Data;

import java.time.Duration;

/**
 * Application properties for the action business.
 */
@Data
public class Action {
    /**
     * Path to runnable directory.
     */
    private String runnableDir;
    /**
     * Execution timeout.
     */
    private Duration executionTimeout;
}
