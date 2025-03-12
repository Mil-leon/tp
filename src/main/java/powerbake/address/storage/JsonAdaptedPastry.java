package powerbake.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;

/**
 * Jackson-friendly version of {@link Pastry}.
 */
public class JsonAdaptedPastry {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pastry's %s field is missing!";

    private final String name;
    private final String price;

    /**
     * Constructs a {@code JsonAdaptedPastry} with the given pastry details.
     */
    @JsonCreator
    public JsonAdaptedPastry(@JsonProperty("name") String name, @JsonProperty("price") String price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Converts a given {@code Pastry} into this class for Jackson use.
     */
    public JsonAdaptedPastry(Pastry source) {
        this.name = source.getName().name;
        this.price = source.getPrice().amount;
    }

    /**
     * Converts this Jackson-friendly adapted pastry object into the model's {@code Pastry} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted pastry.
     */
    public Pastry toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (price == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Price.class.getSimpleName()));
        }
        if (!Price.isValidPrice(price)) {
            throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
        }
        final Price modelPrice = new Price(price);

        return new Pastry(modelName, modelPrice);
    }
}
