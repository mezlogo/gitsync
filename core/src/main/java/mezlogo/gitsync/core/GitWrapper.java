package mezlogo.gitsync.core;

public class GitWrapper {
    private final GitHelper git;

    public GitWrapper(GitHelper git) {
        this.git = git;
    }

    public GitReport buildReport() {
        String branch = git.branch().join();
        String tag = git.tag().join();
        String remoteTag = git.remoteTag().join();
        String status = git.status().join();
        Long lastFetch = git.lastFetch();
        return new GitReport(branch, tag, remoteTag, status,  lastFetch);
    }
}
