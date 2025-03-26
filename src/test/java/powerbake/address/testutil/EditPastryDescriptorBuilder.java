package powerbake.address.testutil;

import powerbake.address.logic.commands.EditCommand.EditPastryDescriptor;
import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;

/**
 * A utility class to help with building EditPastryDescriptor objects.
 */
public class EditPastryDescriptorBuilder {

    private EditPastryDescriptor descriptor;

    public EditPastryDescriptorBuilder() {
        descriptor = new EditPastryDescriptor();
    }

    public EditPastryDescriptorBuilder(EditPastryDescriptor descriptor) {
        this.descriptor = new EditPastryDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPastryDescriptor} with fields containing {@code person}'s details
     */
    public EditPastryDescriptorBuilder(Pastry pastry) {
        descriptor = new EditPastryDescriptor();
        descriptor.setName(pastry.getName());
        descriptor.setPrice(pastry.getPrice());
    }

    /**
     * Sets the {@code Name} of the {@code EditPastryDescriptor} that we are building.
     */
    public EditPastryDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPastryDescriptor} that we are building.
     */
    public EditPastryDescriptorBuilder withPrice(String price) {
        descriptor.setPrice(new Price(price));
        return this;
    }


    public EditPastryDescriptor build() {
        return descriptor;
    }
}
