package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import powerbake.address.commons.core.index.Index;
import powerbake.address.commons.util.CollectionUtil;
import powerbake.address.commons.util.ToStringBuilder;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.order.Order;
import powerbake.address.model.order.OrderId;
import powerbake.address.model.order.OrderItem;
import powerbake.address.model.order.OrderStatus;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;
import powerbake.address.model.person.Address;
import powerbake.address.model.person.Email;
import powerbake.address.model.person.Person;
import powerbake.address.model.person.Phone;
import powerbake.address.model.tag.Tag;

/**
 * Represents a command to edit a client or pastry identified by its index number
 * and optionally update its details using provided parameters.
 *
 * The `EditCommand` handles two different types of entities: Client & Pastry.
 *
 * Command Usage:
 * edit client 1 -p 91234567 -e johndoe@example.com
 * edit pastry 2 -n Croissant -pr 3.50
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the client, pastry"
            + " or order status identified "
            + "by the index number used in the displayed list. "
            + "Existing values will be overwritten by the input values. \n"
            + "Parameters: TYPE (client/pastry), INDEX (must be a positive integer), FIELDS\n"
            + "Example for client: edit client 1 -n John -p 91234567 -e johndoe@example.com\n"
            + "Example for pastry: edit pastry 2 -n Croissant -pr 3.50\n"
            + "Example for order: edit order 2 -s delivered";

    public static final String MESSAGE_EDIT_CLIENT_SUCCESS = "Edited Client: %1$s";
    public static final String MESSAGE_EDIT_PASTRY_SUCCESS = "Edited Pastry: %1$s";
    public static final String MESSAGE_EDIT_ORDER_SUCCESS = "Edited Order: %1$s";
    public static final String MESSAGE_NOT_EDITED_CLIENT = "At least one field to edit must be provided. \n"
            + "-n: Name of the client to edit \n"
            + "-p: Phone of the client to edit \n"
            + "-e: Email of the client to edit \n"
            + "-a: Address of the client to edit \n"
            + "-t: Tags of the client to edit \n";

    public static final String MESSAGE_NOT_EDITED_PASTRY = "At least one field to edit must be provided. \n"
            + "-n: Name of the pastry to edit \n"
            + "-pr: Price of the pastry to edit \n";

    public static final String MESSAGE_NOT_EDITED_ORDER = "Please type in the Status to change to: "
            + "pending, processing, ready, delivered, cancelled";
    public static final String MESSAGE_DUPLICATE_CLIENT = "This client already exists in the address book.";
    public static final String MESSAGE_DUPLICATE_PASTRY = "This pastry already exists in the bakery.";
    public static final String MESSAGE_DUPLICATE_ORDER = "This order already exists in the bakery.";

    private final String entityType;
    private final Index index;
    private final Object editDescriptor;

    /**
     * Creates an EditCommand.
     *
     * @param entityType Entity to edit (client or pastry).
     * @param targetIndex Index of the entity to edit.
     * @param editDescriptor Details to edit the entity with.
     */
    public EditCommand(String entityType, Index targetIndex, Object editDescriptor, boolean isClient) {
        requireNonNull(entityType);
        requireNonNull(targetIndex);
        requireNonNull(editDescriptor);

        this.entityType = entityType.toLowerCase();
        this.index = targetIndex;
        if (isClient) {
            this.editDescriptor = new EditPersonDescriptor((EditPersonDescriptor) editDescriptor);
        } else if (entityType.equals("pastry")) {
            this.editDescriptor = new EditPastryDescriptor((EditPastryDescriptor) editDescriptor);
        } else {
            this.editDescriptor = new EditOrderDescriptor((EditOrderDescriptor) editDescriptor);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        switch (entityType) {
        case "client":
            return editClient(model);
        case "pastry":
            return editPastry(model);
        case "order":
            return editOrder(model);
        default:
            throw new CommandException(Messages.MESSAGE_INVALID_ENTITY);
        }
    }

    /**
     * Edits a client at the specified index and updates its fields.
     *
     * @param model The model containing the client list.
     * @return The result message indicating a successful edit.
     * @throws CommandException If the specified index is invalid or if it causes duplication.
     */
    private CommandResult editClient(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Person personToEdit = model.getFilteredPersonList().get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, (EditPersonDescriptor) editDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.setPerson(personToEdit, editedPerson);
        // Update all orders that reference this person
        Stream<Order> ordersStream = model.getFilteredOrderList().stream();
        ordersStream.filter((order) -> order.getCustomer().equals(personToEdit))
            .forEach((order) -> {
                model.setOrder(
                        order,
                        new Order(
                            order.getOrderId(),
                            editedPerson,
                            order.getOrderItems(),
                            order.getOrderDate(),
                            order.getStatus()
                            )
                    );
            }
            );

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Edits a pastry at the specified index and updates its fields.
     *
     * @param model The model containing the pastry list.
     * @return The result message indicating a successful edit.
     * @throws CommandException If the specified index is invalid or if it causes duplication.
     */
    private CommandResult editPastry(Model model) throws CommandException {
        List<Pastry> lastShownList = model.getFilteredPastryList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Pastry pastryToEdit = model.getFilteredPastryList().get(index.getZeroBased());
        Pastry editedPastry = createEditedPastry(pastryToEdit, (EditPastryDescriptor) editDescriptor);

        if (!pastryToEdit.isSamePastry(editedPastry) && model.hasPastry(editedPastry)) {
            throw new CommandException(MESSAGE_DUPLICATE_PASTRY);
        }

        model.setPastry(pastryToEdit, editedPastry);
        model.updateFilteredPastryList(Model.PREDICATE_SHOW_ALL_PASTRIES);
        return new CommandResult(String.format(MESSAGE_EDIT_PASTRY_SUCCESS, Messages.format(editedPastry)));
    }

    /**
     * Edits a pastry at the specified index and updates its fields.
     *
     * @param model The model containing the pastry list.
     * @return The result message indicating a successful edit.
     * @throws CommandException If the specified index is invalid or if it causes duplication.
     */
    private CommandResult editOrder(Model model) throws CommandException {
        List<Order> lastShownList = model.getFilteredOrderList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                        Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX,
                        Messages.MESSAGE_INVALID_INDEX)
                    );
        }

        Order orderToEdit = model.getFilteredOrderList().get(index.getZeroBased());
        Order editedOrder = createEditedOrder(orderToEdit, (EditOrderDescriptor) editDescriptor);

        if (!orderToEdit.isSameOrder(editedOrder) && model.hasOrder(editedOrder)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }

        model.setOrder(orderToEdit, editedOrder);
        model.updateFilteredOrderList(Model.PREDICATE_SHOW_ALL_ORDERS);
        return new CommandResult(String.format(MESSAGE_EDIT_ORDER_SUCCESS, Messages.format(editedOrder)));
    }

    /**
     * Creates a new Person object with updated details.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editDescriptor) {
        assert personToEdit != null;

        powerbake.address.model.person.Name updatedName = editDescriptor.getName()
                        .orElse(personToEdit.getName());
        Phone updatedPhone = editDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    /**
     * Creates a new Pastry object with updated details.
     */
    private static Pastry createEditedPastry(Pastry pastryToEdit, EditPastryDescriptor editDescriptor) {
        assert pastryToEdit != null;

        powerbake.address.model.pastry.Name updatedName = editDescriptor.getName().orElse(pastryToEdit.getName());
        Price updatedPrice = editDescriptor.getPrice().orElse(pastryToEdit.getPrice());

        return new Pastry(updatedName, updatedPrice);
    }

    /**
     * Creates a new Order object with updated details.
     */
    private static Order createEditedOrder(Order orderToEdit, EditOrderDescriptor editDescriptor) {
        assert orderToEdit != null;

        OrderId orderId = orderToEdit.getOrderId();
        powerbake.address.model.person.Person customer = orderToEdit.getCustomer();
        List<OrderItem> orderItems = orderToEdit.getOrderItems();
        LocalDateTime dateTime = orderToEdit.getOrderDate();
        OrderStatus updatedStatus = editDescriptor.getStatus().orElse(orderToEdit.getStatus());

        return new Order(orderId, customer, orderItems, dateTime, updatedStatus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && entityType.equals(otherEditCommand.entityType)
                && Objects.equals(editDescriptor, otherEditCommand.editDescriptor);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", (EditPersonDescriptor) editDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private powerbake.address.model.person.Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(powerbake.address.model.person.Name name) {
            this.name = name;
        }

        public Optional<powerbake.address.model.person.Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor otherEditPersonDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .toString();
        }
    }

    /**
     * Descriptor for editing the details of a pastry.
     */
    public static class EditPastryDescriptor {
        private powerbake.address.model.pastry.Name name;
        private Price price;

        /**
         * Constructs an empty {@code EditPastryDescriptor}.
         */
        public EditPastryDescriptor() {}

        /**
         * Constructs a new {@code EditPastryDescriptor} copied from another {@code EditPastryDescriptor}.
         *
         * @param toCopy The {@code EditPastryDescriptor} to copy from.
         */
        public EditPastryDescriptor(EditPastryDescriptor toCopy) {
            setName(toCopy.name);
            setPrice(toCopy.price);
        }

        /**
         * Checks if any field in this descriptor has been edited.
         *
         * @return {@code true} if at least one field is edited, {@code false} otherwise.
         */
        public boolean isAnyFieldEdited() {
            return name != null || price != null;
        }

        /**
         * Sets the name of the pastry.
         *
         * @param name The name to be set.
         */
        public void setName(powerbake.address.model.pastry.Name name) {
            this.name = name;
        }

        /**
         * Retrieves the name of the pastry, if set.
         *
         * @return An {@code Optional} containing the pastry name, or an empty {@code Optional} if not set.
         */
        public Optional<powerbake.address.model.pastry.Name> getName() {
            return Optional.ofNullable(name);
        }

        /**
         * Sets the price of the pastry.
         *
         * @param price The price to be set.
         */
        public void setPrice(Price price) {
            this.price = price;
        }

        /**
         * Retrieves the price of the pastry, if set.
         *
         * @return An {@code Optional} containing the pastry price, or an empty {@code Optional} if not set.
         */
        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        /**
         * Compares this {@code EditPastryDescriptor} to another object for equality.
         *
         * @param other The other object to compare to.
         * @return {@code true} if the other object is an instance of {@code EditPastryDescriptor} and
         *         has the same name and price, {@code false} otherwise.
         */
        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditPastryDescriptor e)) {
                return false;
            }
            return getName().equals(e.getName())
                    && getPrice().equals(e.getPrice());
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("price", price)
                    .toString();
        }
    }

    /**
     * Descriptor for editing the details of an order.
     */
    public static class EditOrderDescriptor {
        private OrderStatus status;

        /**
         * Constructs an empty {@code EditOrderDescriptor}.
         */
        public EditOrderDescriptor() {}

        /**
         * Constructs a new {@code EditOrderDescriptor} copied from another {@code EditOrderDescriptor}.
         *
         * @param toCopy The {@code EditOrderDescriptor} to copy from.
         */
        public EditOrderDescriptor(EditOrderDescriptor toCopy) {
            setStatus(toCopy.status);
        }

        /**
         * Checks if any field in this descriptor has been edited.
         *
         * @return {@code true} if at least one field is edited, {@code false} otherwise.
         */
        public boolean isAnyFieldEdited() {
            return status != null;
        }

        /**
         * Sets the name of the pastry.
         *
         * @param status The name to be set.
         */
        public void setStatus(OrderStatus status) {
            this.status = status;
        }

        /**
         * Retrieves the name of the pastry, if set.
         *
         * @return An {@code Optional} containing the pastry name, or an empty {@code Optional} if not set.
         */
        public Optional<OrderStatus> getStatus() {
            return Optional.ofNullable(status);
        }

        /**
         * Compares this {@code EditPastryDescriptor} to another object for equality.
         *
         * @param other The other object to compare to.
         * @return {@code true} if the other object is an instance of {@code EditPastryDescriptor} and
         *         has the same name and price, {@code false} otherwise.
         */
        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditOrderDescriptor e)) {
                return false;
            }
            return getStatus().equals(e.getStatus())
                    && getStatus().equals(e.getStatus());
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("status", status)
                    .toString();
        }
    }
}
