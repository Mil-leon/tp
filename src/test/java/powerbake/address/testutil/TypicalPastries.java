
package powerbake.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPastries {

    public static final Pastry CROISSANT = new Pastry(new Name("Croissant"), new Price("4.50"));
    public static final Pastry BAGEL = new Pastry(new Name("Bagel"), new Price("2.50"));
    public static final Pastry CAKE = new Pastry(new Name("Cake"), new Price("8.00"));
    public static final Pastry SAUSAGEBUN = new Pastry(new Name("Sausage Bun"), new Price("3.00"));

    private TypicalPastries() {} // prevents instantiation

    public static List<Pastry> getTypicalPastries() {
        return new ArrayList<>(Arrays.asList(CROISSANT, BAGEL, CAKE, SAUSAGEBUN));
    }
}
