package powerbake.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static powerbake.address.storage.JsonAdaptedPastry.MISSING_FIELD_MESSAGE_FORMAT;
import static powerbake.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.exceptions.IllegalValueException;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Price;

public class JsonAdaptedPastryTest {
    private static final String INVALID_NAME = " ";
    private static final String INVALID_PRICE = "as.df";

    private static final String VALID_NAME = "Cake";
    private static final String VALID_PRICE = "2.50";
    private static final Pastry VALID_Pastry = new Pastry(
            new Name(VALID_NAME), new Price(VALID_PRICE)
            );


    @Test
    public void toModelType_validPastryDetails_returnsPastry() throws Exception {
        JsonAdaptedPastry pastry = new JsonAdaptedPastry(VALID_NAME, VALID_PRICE);
        assertEquals(VALID_Pastry, pastry.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPastry pastry =
                new JsonAdaptedPastry(INVALID_NAME, VALID_PRICE);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, pastry::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPastry pastry = new JsonAdaptedPastry(null, VALID_PRICE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, pastry::toModelType);
    }

    @Test
    public void toModelType_invalidPrice_throwsIllegalValueException() {
        JsonAdaptedPastry pastry =
                new JsonAdaptedPastry(VALID_NAME, INVALID_PRICE);
        String expectedMessage = Price.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, pastry::toModelType);
    }

    @Test
    public void toModelType_nullPrice_throwsIllegalValueException() {
        JsonAdaptedPastry pastry = new JsonAdaptedPastry(VALID_NAME, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Price.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, pastry::toModelType);
    }

}
