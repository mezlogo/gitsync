package mezlogo.gitsync.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GitHelper {
    private final List<String> gitRepoPath;
    private final File gitDir;

    public GitHelper(List<String> gitRepoPath, File gitDir) {
        this.gitRepoPath = gitRepoPath;
        this.gitDir = gitDir;
    }

    public static GitHelper build(File work, File git) {
        return new GitHelper(List.of("--work-tree=" + work.getAbsolutePath(), "--git-dir=" + git.getAbsolutePath()), git);
    }

    public static GitHelper build(File dir) {
        return new GitHelper(List.of("-C", dir.getAbsolutePath()), new File(dir, ".git"));
    }

    private static CompletableFuture<String> exec(List<String> programm) {
        try {
            return new ProcessBuilder(programm).start().onExit().thenApply(process -> {
                var isError = 0 != process.exitValue();
                var reader = new BufferedReader(new InputStreamReader(isError ? process.getErrorStream() : process.getInputStream()));
                var result = reader.lines().collect(Collectors.joining());
                if (isError) {
                    throw new RuntimeException(result);
                }
                return result;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public CompletableFuture<String> branch() {
        return exec(buildCommand("branch --show-current"));
    }

    public CompletableFuture<String> tag() {
        return exec(buildCommand("describe --abbrev=0 --tags HEAD")).exceptionally(e -> null);
    }

    public CompletableFuture<String> remoteTag() {
        return exec(buildCommand("describe --abbrev=0 --tags origin/master")).exceptionally(e -> null);
    }

    public CompletableFuture<String> checkout(String name) {
        return exec(buildCommand("checkout", name));
    }

    public CompletableFuture<String> createAndCheckout(String name) {
        return exec(buildCommand("checkout", "-b", name));
    }

    public CompletableFuture<String> tag(String tagName) {
        return exec(buildCommand("tag -a " + tagName + " -m newtag"));
    }

    public CompletableFuture<String> status() {
        return exec(buildCommand("status -s")).thenApply(result -> result.isBlank() ? null : result);
    }

    public CompletableFuture<Integer> lengthOfNewLocalCommits() {
        return exec(buildCommand("rev-list --count origin/master..HEAD")).thenApply(Integer::valueOf);
    }

    public CompletableFuture<Integer> lengthOfNewRemoteCommits() {
        return exec(buildCommand("rev-list --count HEAD..origin/master")).thenApply(Integer::valueOf);
    }

    public CompletableFuture<String> fetch() {
        return exec(buildCommand("fetch --prune"));
    }

    public Long lastFetch() {
        File fetchHead = new File(gitDir, "FETCH_HEAD");
        return fetchHead.exists() ? fetchHead.lastModified() : null;
    }
}
