package mezlogo.gitsync.core;

public class GitReport {
    public final String branch;
    public final String tag;
    public final String remoteTag;
    public final String status;
    public final Long lastFetch;

    public GitReport(String branch, String tag, String remoteTag, String status, Long lastFetch) {
        this.branch = branch;
        this.tag = tag;
        this.remoteTag = remoteTag;
        this.status = status;
        this.lastFetch = lastFetch;
    }
}
