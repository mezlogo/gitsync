package mezlogo.template_cli.cli;

import mezlogo.template_cli.cli.commands.GreetCommand;
import mezlogo.template_cli.cli.commands.HelloCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "mycliapp",
        subcommands = {CommandLine.HelpCommand.class, GreetCommand.class, HelloCommand.class},
        mixinStandardHelpOptions = true
)
public class MainCommand {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand()).execute(args);
        System.exit(exitCode);
    }
}
