package mezlogo.gitsync.cli;

import mezlogo.gitsync.core.GitHelper;
import mezlogo.gitsync.core.GitWrapper;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ConvertFilesToGit {
    @CommandLine.Parameters(index = "0", arity = "1..*")
    public List<String> files;

    public List<GitWrapper> parse() {
        List<GitWrapper> result = new ArrayList<>(files.size());
        for (String file : files) {
            File realFile = new File(file);
            if (!realFile.isDirectory()) {
                throw new IllegalArgumentException("Expected: [" + file + "] to be a dir");
            }
            result.add(new GitWrapper(GitHelper.build(realFile)));
        }
        return result;
    }
}
