package powerbake.address.testutil;


import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;

/**
 * A utility class to help with building Pastry objects.
 */
public class PastryBuilder {

    public static final String DEFAULT_NAME = "Croissant";
    public static final String DEFAULT_PRICE = "3.50";

    private Name name;
    private Price price;

    /**
     * Creates a {@code PastryBuilder} with the default details.
     */
    public PastryBuilder() {
        name = new Name(DEFAULT_NAME);
        price = new Price(DEFAULT_PRICE);
    }

    /**
     * Initializes the PastryBuilder with the data of {@code pastryToCopy}.
     */
    public PastryBuilder(Pastry pastryToCopy) {
        name = pastryToCopy.getName();
        price = pastryToCopy.getPrice();
    }

    /**
     * Sets the {@code Name} of the {@code Pastry} that we are building.
     */
    public PastryBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }


    /**
     * Sets the {@code Price} of the {@code Pastry} that we are building.
     */
    public PastryBuilder withPrice(String price) {
        this.price = new Price(price);
        return this;
    }

    public Pastry build() {
        return new Pastry(name, price);
    }

}
