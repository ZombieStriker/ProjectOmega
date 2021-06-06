package com.projectomega.main.command;

/**
 * Represents an executable command.
 * <p>
 * This implementation allows for better containing to {@link OmegaCommand}s.
 */
public abstract class ExecutableCommand implements CommandExecutor {

    /**
     * The command information of this
     */
    private final OmegaCommand command = createCommand();

    /**
     * Creates the command information representing this {@link ExecutableCommand}.
     *
     * @return The command
     */
    protected abstract OmegaCommand createCommand();

    /**
     * Returns the {@link OmegaCommand} representing this {@link ExecutableCommand}.
     *
     * @return The command information.
     */
    public OmegaCommand getCommand() {
        return command;
    }
}
