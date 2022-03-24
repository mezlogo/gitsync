package mezlogo.template_cli.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class MainCommandTest {
    private static final CommandLine sut = new CommandLine(new MainCommand());

    @Test
    public void hello_bob_should_return_error() {
        int exitCode = sut.execute("hello", "Bob");
        Assertions.assertEquals(-1, exitCode);
    }

    @Test
    public void hello_anon_should_return_error() {
        int exitCode = new CommandLine(new MainCommand()).execute("hello");
        Assertions.assertEquals(-1, exitCode);
    }

    @Test
    public void hello_bill_should_return_ok() {
        int exitCode = new CommandLine(new MainCommand()).execute("hello", "Bill");
        Assertions.assertEquals(0, exitCode);
    }

    @Test
    public void greet_bob_should_return_error() {
        int exitCode = sut.execute("greet", "Bob");
        Assertions.assertEquals(-1, exitCode);
    }

    @Test
    public void greet_anon_should_return_error() {
        int exitCode = new CommandLine(new MainCommand()).execute("greet");
        Assertions.assertEquals(-1, exitCode);
    }

    @Test
    public void greet_bill_should_return_ok() {
        int exitCode = new CommandLine(new MainCommand()).execute("greet", "Bill");
        Assertions.assertEquals(0, exitCode);
    }
}
