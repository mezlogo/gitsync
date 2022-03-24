package mezlogo.gitsync.cli.commands;

import mezlogo.gitsync.core.GreetService;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "greet")
public class GreetCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", defaultValue = "Bob")
    private String name;

    @Override
    public Integer call() throws Exception {
        String greet = new GreetService().greet(name);
        System.out.println(greet);
        return "Bob".equals(name) ? -1 : 0;
    }
}
