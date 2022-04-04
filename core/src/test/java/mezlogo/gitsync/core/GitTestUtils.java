package mezlogo.gitsync.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GitTestUtils {
    private final List<String> gitRepoPath;

    public GitTestUtils(List<String> gitRepoPath) {
        this.gitRepoPath = gitRepoPath;
    }

    public static GitTestUtils fromFile(File file) {
        return new GitTestUtils(List.of("-C", file.getAbsolutePath()));
    }

    private static CompletableFuture<String> exec(List<String> programm) {
        try {
            CompletableFuture<Process> processCompletableFuture = new ProcessBuilder(programm).start().onExit();
            return processCompletableFuture.thenApply(result -> {
                BufferedReader output = new BufferedReader(new InputStreamReader(result.getInputStream()));
                return output.lines().collect(Collectors.joining());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map.Entry<File, File> createDefaultRepoAndClone(File parent) {
        var remote = new File(parent, "remote");
        remote.mkdir();
        createDefaultRepo(remote);
        var result = new File(parent, "local");
        clone(remote, result);
        return Map.entry(remote, result);
    }

    public static GitTestUtils createDefaultRepo(File parent) {
        replaceFile(parent, "hello.txt", "world");
        return new GitTestUtils(List.of("-C", parent.getAbsolutePath()))
                .init()
                .add(".")
                .commit("init commit");
    }

    public static void replaceFile(File parent, String name, String content) {
        var newFile = new File(parent, name);
        try {
            Files.writeString(newFile.toPath(), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        var tmp = new File("/tmp/mytest" + System.currentTimeMillis());
        tmp.mkdirs();
        var newFile = new File(tmp, "hello.txt");
        Files.writeString(newFile.toPath(), "helloworld");
        new GitTestUtils(List.of("-C", tmp.getAbsolutePath())).init();
        System.out.println("EXIT");
    }

    public static GitTestUtils clone(File from, File to) {
        exec(List.of("git", "clone", "file://" + from.getAbsolutePath(), to.getAbsolutePath())).join();
        return new GitTestUtils(List.of("-C", to.getAbsolutePath()));
    }

    private List<String> buildCommand(String arg) {
        return buildCommand(arg.split(" "));
    }

    private List<String> buildCommand(String... args) {
        ArrayList<String> result = new ArrayList<>();
        result.add("git");
        result.addAll(gitRepoPath);
        result.addAll(Arrays.stream(args).toList());
        return result;
    }

    public GitTestUtils init() {
        exec(buildCommand("init")).join();
        return this;
    }

    public GitTestUtils add(String file) {
        exec(buildCommand("add", file)).join();
        return this;
    }

    public GitTestUtils commit(String msg) {
        exec(buildCommand("commit", "-m", msg)).join();
        return this;
    }
}
