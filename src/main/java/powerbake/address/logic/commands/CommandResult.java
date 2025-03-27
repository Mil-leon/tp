package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import powerbake.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    /** The application is running to show client. */
    private static boolean showClient = true;

    /** The application is running to show pastry. */
    private static boolean showPastry = false;

    /** The application is running to show order. */
    private static boolean showOrder = false;

    private static int orderIndex = 0;

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean showClient,
            boolean showPastry, boolean showOrder, int orderIndex) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        CommandResult.showClient = showClient;
        CommandResult.showPastry = showPastry;
        CommandResult.showOrder = showOrder;
        CommandResult.orderIndex = orderIndex;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean showClient,
            boolean showPastry, boolean showOrder) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        CommandResult.showClient = showClient;
        CommandResult.showPastry = showPastry;
        CommandResult.showOrder = showOrder;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowClient() {
        return CommandResult.showClient;
    }

    public boolean isShowPastry() {
        return CommandResult.showPastry;
    }

    public boolean isShowOrder() {
        return CommandResult.showOrder;
    }

    public int getOrderIndex() {
        return CommandResult.orderIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, showClient, showPastry, showOrder, orderIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("showClient", showClient)
                .add("showPastry", showPastry)
                .add("showOrder", showOrder)
                .add("orderIndex", orderIndex)
                .toString();
    }

}
