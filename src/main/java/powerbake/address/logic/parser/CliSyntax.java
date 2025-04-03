package powerbake.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_CLIENT_NOSPACE = new Prefix("client");
    public static final Prefix PREFIX_PASTRY_NOSPACE = new Prefix("pastry");
    public static final Prefix PREFIX_ORDER_NOSPACE = new Prefix("order");
    public static final Prefix PREFIX_CLIENT = new Prefix("client ");
    public static final Prefix PREFIX_PASTRY = new Prefix("pastry ");
    public static final Prefix PREFIX_ORDER = new Prefix("order ");
    public static final Prefix PREFIX_PHONE = new Prefix("-p ");
    public static final Prefix PREFIX_EMAIL = new Prefix("-e ");
    public static final Prefix PREFIX_ADDRESS = new Prefix("-a ");
    public static final Prefix PREFIX_TAG = new Prefix("-t ");
    public static final Prefix PREFIX_PRICE = new Prefix("-pr ");
    public static final Prefix PREFIX_NAME = new Prefix("-n ");
    public static final Prefix PREFIX_PASTRY_NAME = new Prefix("-pn ");
    public static final Prefix PREFIX_QUANTITY = new Prefix("-q ");
    public static final Prefix PREFIX_STATUS = new Prefix("-s ");
}
