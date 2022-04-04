package mezlogo.gitsync.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GitWrapperTest {
    @TempDir
    File tempDir;

    GitWrapper sut;

    File local;
    File remote;

    @BeforeEach
    void before() {
        var tuple = GitTestUtils.createDefaultRepoAndClone(tempDir);
        remote = tuple.getKey();
        local = tuple.getValue();
        sut = new GitWrapper(GitHelper.build(local));
    }

    @Test
    void report_should_contain_default_values() {
        var actual = sut.buildReport();
        Assertions.assertAll(
                () -> assertEquals("master", actual.branch),
                () -> assertNull(actual.remoteTag),
                () -> assertNull(actual.lastFetch),
                () -> assertNull(actual.remoteTag),
                () -> assertNull(actual.status)
        );
    }
}
