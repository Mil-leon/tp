package powerbake.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_QUANTITY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import powerbake.address.commons.core.index.Index;
import powerbake.address.commons.util.StringUtil;
import powerbake.address.logic.Messages;
import powerbake.address.logic.parser.exceptions.ParseException;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.pastry.Price;
import powerbake.address.model.person.Address;
import powerbake.address.model.person.Email;
import powerbake.address.model.person.Name;
import powerbake.address.model.person.Phone;
import powerbake.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static powerbake.address.model.pastry.Name parsePastryName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new powerbake.address.model.pastry.Name(trimmedName);
    }

    /**
     * Parses a {@code String price} into a {@code Price}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code price} is invalid.
     */
    public static Price parsePrice(String price) throws ParseException {
        requireNonNull(price);
        String trimmedPrice = price.trim();
        if (!Price.isValidPrice(trimmedPrice)) {
            throw new ParseException(Price.MESSAGE_CONSTRAINTS);
        }
        return new Price(trimmedPrice);
    }

    /**
     * Parses a {@code String orderInfo} into a {@code ArrayList<String>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code orderInfo} is invalid.
     */
    public static ArrayList<String> parseUnformattedOrder(String orderInfo) throws ParseException {
        requireNonNull(orderInfo);
        String trimmedOrder = orderInfo.trim();
        String[] splitOrder = trimmedOrder.split(PREFIX_QUANTITY.getPrefix());
        // quantity must be exactly 1
        if (splitOrder.length != 2) {
            // change this in v1.5
            throw new ParseException("Order must contain at least one quantity");
        }
        String trimmedPastryName = splitOrder[0].trim();
        String trimmedQuantity = splitOrder[1].trim();
        // check if quantity is an integer
        if (!StringUtil.isValidIntegerQuantity(trimmedQuantity)) {
            throw new ParseException("Quantity must be a positive integer between 1 - 999");
        }
        ArrayList<String> unformattedOrder = new ArrayList<>() {
            {
                add(trimmedPastryName);
                add(trimmedQuantity);
            }
        };
        return unformattedOrder;
    }

    /**
     * Parses {@code Collection<String> order} into a {@code ArrayList<ArrayList<String>>}.
     */
    public static ArrayList<ArrayList<String>> parseUnformattedOrders(Collection<String> orders) throws ParseException {
        requireNonNull(orders);
        final ArrayList<ArrayList<String>> unformattedOrderList = new ArrayList<>();
        for (String order : orders) {
            ArrayList<String> unformattedOrder = parseUnformattedOrder(order);
            unformattedOrderList.add(unformattedOrder);
        }
        return unformattedOrderList;
    }

    /**
     * Parses a {@code String price} into a {@code Price}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code price} is invalid.
     */
    public static OrderStatus parseOrderStatus(String status) throws ParseException {
        requireNonNull(status);
        String trimmedStatus = status.trim();
        if (!Order.isValidStatus(trimmedStatus.toUpperCase())) {
            throw new ParseException(Messages.MESSAGE_CONSTRAINTS_ORDERSTATUS);
        }

        return OrderStatus.valueOf(trimmedStatus.toUpperCase());
    }
}
