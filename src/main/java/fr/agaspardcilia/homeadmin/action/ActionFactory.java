package fr.agaspardcilia.homeadmin.action;

/**
 * A factory that produces {@link Action}s.
 */
public class ActionFactory {
    private ActionFactory() {
        // Do not instantiate! >:(
    }

    /**
     * Creates a minimal action from a runnable.
     * TODO: test me!
     *
     * @param runnable the runnable.
     * @return the action.
     */
    public static Action getAction(String runnable) {
        return Action.builder()
                .name(runnable)
                .runnableFileName(runnable)
                .runnableExists(true)
                .build();
    }

}
