package seedu.stock.model.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.stock.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class QuantityAdderTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new QuantityAdder(null));
    }

    @Test
    public void constructor_invalidQuantity_throwsIllegalArgumentException() {
        String invalidQuantity = "";
        assertThrows(IllegalArgumentException.class, () -> new QuantityAdder(invalidQuantity));
    }

    @Test
    public void isValidValue() {
        // EP: null quantity adder
        assertThrows(NullPointerException.class, () -> QuantityAdder.isValidValue(null));

        // EP: invalid values
        assertFalse(QuantityAdder.isValidValue("")); // empty string
        assertFalse(QuantityAdder.isValidValue("    ")); // spaces only
        assertFalse(QuantityAdder.isValidValue("^#$%")); // only non-alphanumeric characters
        assertFalse(QuantityAdder.isValidValue("audrey")); // alphabets only
        assertFalse(QuantityAdder.isValidValue("2147483648")); // Overflow
        assertFalse(QuantityAdder.isValidValue("-2147483649")); // Underflow

        // EP: valid values
        assertTrue(QuantityAdder.isValidValue("0")); // zero
        assertTrue(QuantityAdder.isValidValue("14")); // small positive number
        assertTrue(QuantityAdder.isValidValue("1000000000")); // large positive number
        assertTrue(QuantityAdder.isValidValue("-69")); // small negative number
        assertTrue(QuantityAdder.isValidValue("-1000000007")); // large negative number
        assertTrue(QuantityAdder.isValidValue(Integer.toString(Integer.MAX_VALUE))); // Max integer value
        assertTrue(QuantityAdder.isValidValue(Integer.toString(Integer.MIN_VALUE))); // Min integer value
    }

    @Test
    public void incrementQuantity() {
        // EP: valid increment
        QuantityAdder quantityAdder = new QuantityAdder("+2103");
        Quantity toBeAddedInto = new Quantity("0");
        Quantity result = quantityAdder.incrementQuantity(toBeAddedInto).get();
        assertTrue(result.equals(new Quantity("2103")));

        // EP: valid decrement
        quantityAdder = new QuantityAdder("-100");
        toBeAddedInto = new Quantity("100");
        result = quantityAdder.incrementQuantity(toBeAddedInto).get();
        assertTrue(result.equals(new Quantity("0")));

        // EP: invalid increment (overflow)
        quantityAdder = new QuantityAdder("+2147483647");
        toBeAddedInto = new Quantity("2103");
        assertEquals(Optional.empty(), quantityAdder.incrementQuantity(toBeAddedInto));

        // EP: invalid decrement (quantity become negative)
        quantityAdder = new QuantityAdder("-1");
        toBeAddedInto = new Quantity("0");
        assertEquals(Optional.empty(), quantityAdder.incrementQuantity(toBeAddedInto));
    }

    @Test
    public void equals() {
        QuantityAdder quantityAdder = new QuantityAdder("100");
        QuantityAdder quantityAdderCopy = new QuantityAdder("100");
        QuantityAdder different = new QuantityAdder("-1");

        // EP: self
        assertTrue(quantityAdder.equals(quantityAdder));

        // EP: same value
        assertTrue(quantityAdder.equals(quantityAdderCopy));

        // EP: different value
        assertFalse(quantityAdder.equals(different));
    }
}
