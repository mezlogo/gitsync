package mezlogo.gitsync.cli.commands;

import mezlogo.gitsync.cli.ConvertFilesToGit;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "report")
public class ReportCommand extends ConvertFilesToGit implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
