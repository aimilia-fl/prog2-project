package gr.aueb.dmst.javengers.common.exceptions;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InvalidStopExceptionTest {
    @Test public void testGetMessage() {
        InvalidStopException stopException = new InvalidStopException("The requested floor does not exist!");
        assertEquals("The requested floor does not exist!", stopException.getMessage());
    }
}
