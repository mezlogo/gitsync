package mezlogo.gitsync.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

class GitHelperTest {

    @Nested
    class WithoutRemote {
        @TempDir
        File tempDir;
        GitHelper sut;

        @BeforeEach
        void before() {
            GitTestUtils.createDefaultRepo(tempDir);
            sut = GitHelper.build(tempDir);
        }

        @Test
        void should_return_branch_name_master() {
            Assertions.assertEquals("master", sut.branch().join());
        }

        @Test
        void should_return_changed_branch_name() {
            sut.createAndCheckout("newbranch").join();
            Assertions.assertEquals("newbranch", sut.branch().join());
        }

        @Test
        void should_return_my_tag() {
            sut.tag("v0.0.1").join();
            Assertions.assertEquals("v0.0.1", sut.tag().join());
        }

        @Test
        void should_return_null_when_no_tag() {
            Assertions.assertNull(sut.tag().join());
        }

        @Test
        void should_return_content_when_dirt() {
            GitTestUtils.replaceFile(tempDir, "name.txt", "Bill");
            Assertions.assertEquals("?? name.txt", sut.status().join());
        }

        @Test
        void should_return_null_when_no_dirt() {
            Assertions.assertNull(sut.status().join());
        }
    }

    @Nested
    class WithRemote {
        @TempDir
        File tempDir;

        GitHelper sut;

        File remote;
        File local;

        @BeforeEach
        void before() {
            var tuple = GitTestUtils.createDefaultRepoAndClone(tempDir);
            remote = tuple.getKey();
            local = tuple.getValue();
            sut = GitHelper.build(local);
        }

        @Test
        void remote_tag_should_return_tag_when_exists() {
            GitHelper.build(remote).tag("v0.0.2").join();
            sut.fetch().join();
            Assertions.assertEquals("v0.0.2", sut.remoteTag().join());
        }

        @Test
        void remote_tag_should_return_null_when_default() {
            Assertions.assertNull(sut.remoteTag().join());
        }

        @Test
        void should_return_zero_path_to_head_to_remote_when_default() {
            Assertions.assertEquals(0, sut.lengthOfNewRemoteCommits().join());
        }

        @Test
        void should_return_zero_path_from_head_to_remote_when_defualt() {
            Assertions.assertEquals(0, sut.lengthOfNewLocalCommits().join());
        }

        @Test
        void should_return_one_path_from_remote_to_head() {
            GitTestUtils.replaceFile(remote, "newfile", "newcontent");
            GitTestUtils.fromFile(remote).add(".").commit("newcommit");
            sut.fetch().join();
            Assertions.assertEquals(1, sut.lengthOfNewRemoteCommits().join());
        }

        @Test
        void should_return_one_path_from_head_to_remote() {
            GitTestUtils.replaceFile(local, "newfile", "newcontent");
            GitTestUtils.fromFile(local).add(".").commit("newcommit");
            Assertions.assertEquals(1, sut.lengthOfNewLocalCommits().join());
        }

        @Test
        void lastFetch_should_return_null_when_no_fetch() {
            Assertions.assertNull(sut.lastFetch());
        }

        @Test
        void lastFetch_should_return_notNull_when_fetched() {
            sut.fetch().join();
            Assertions.assertNotNull(sut.lastFetch());
        }
    }
}
