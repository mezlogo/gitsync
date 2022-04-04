package mezlogo.gitsync.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertFilesToGitTest {
    private static final CommandLine sut = new CommandLine(new JustSut());
    private static final String expected = """
            Missing required parameter: '<files>'
            Usage: <main class> <files>...
                  <files>...
            """;

    @TempDir
    File tmp;

    static File createFile(File parent, String name, String content) {
        File file = new File(parent, name);
        try {
            Files.writeString(file.toPath(), content, Charset.defaultCharset());
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_throw_when_file_is_not_a_dir() {
        var file = createFile(tmp, "hello.txt", "Hello, World!");
        StringWriter sw = new StringWriter();
        sut.setErr(new PrintWriter(sw));
        String absolutePath = file.getAbsolutePath();
        int exitCode = sut.execute(absolutePath);

        assertAll(
                () -> assertEquals(-1, exitCode),
                () -> assertEquals("java.lang.IllegalArgumentException: Expected: [" + absolutePath + "] to be a dir", sw.toString().trim())
        );
    }

    @Test
    void should_throw_when_file_does_not_exist() {
        StringWriter sw = new StringWriter();
        sut.setErr(new PrintWriter(sw));
        int exitCode = sut.execute("/path/does/not/exist");

        assertAll(
                () -> assertEquals(-1, exitCode),
                () -> assertEquals("java.lang.IllegalArgumentException: Expected: [/path/does/not/exist] to be a dir", sw.toString().trim())
        );
    }

    @Test
    void should_throw_when_ZERO_args() {
        StringWriter sw = new StringWriter();
        sut.setErr(new PrintWriter(sw));
        int exitCode = sut.execute();
        assertAll(
                () -> assertEquals(2, exitCode),
                () -> assertEquals(expected, sw.toString())
        );
    }

    static class JustSut extends ConvertFilesToGit implements Callable<Integer> {
        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        @Override
        public Integer call() throws Exception {
            try {
                parse();
                return 0;
            } catch (RuntimeException e) {
                spec.commandLine().getErr().println(e);
                return -1;
            }
        }
    }
}
