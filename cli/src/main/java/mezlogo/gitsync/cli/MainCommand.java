package mezlogo.gitsync.cli;

import mezlogo.gitsync.cli.commands.ReportCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "gitsync",
        subcommands = {CommandLine.HelpCommand.class, ReportCommand.class},
        mixinStandardHelpOptions = true
)
public class MainCommand {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCommand()).execute(args);
        System.exit(exitCode);
    }
}
