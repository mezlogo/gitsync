package mezlogo.template_cli.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GreetServiceTest {
    @Test
    void should_greet_bob() {
        Assertions.assertEquals("Hello, Bob!", new GreetService().greet("Bob"));
    }
}
