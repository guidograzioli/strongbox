package org.carlspring.strongbox.storage.services;

import org.carlspring.maven.commons.util.ArtifactUtils;
import org.carlspring.strongbox.artifact.generator.ArtifactGenerator;
import org.carlspring.strongbox.client.ArtifactOperationException;
import org.carlspring.strongbox.storage.indexing.RepositoryIndexManager;
import org.carlspring.strongbox.storage.indexing.RepositoryIndexer;
import org.carlspring.strongbox.storage.indexing.SearchRequest;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.index.ArtifactInfo;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertTrue;

/**
 * @author mtodorov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/strongbox-*-context.xml", "classpath*:/META-INF/spring/strongbox-*-context.xml"})
public class ArtifactSearchServiceImplTest
{

    private static final File REPOSITORY_BASEDIR = new File("target/storages/storage0/releases");

    private static final File INDEX_DIR = new File(REPOSITORY_BASEDIR, ".index");

    @Autowired
    private RepositoryIndexManager repositoryIndexManager;

    @Autowired
    private ArtifactSearchService artifactSearchService;

    private static ArtifactGenerator generator = new ArtifactGenerator(REPOSITORY_BASEDIR.getAbsolutePath());


    @Before
    public void init()
            throws NoSuchAlgorithmException,
                   XmlPullParserException,
                   IOException,
                   ArtifactOperationException
    {
        if (!INDEX_DIR.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            INDEX_DIR.mkdirs();
        }
    }

    @Test
    public void testIndex() throws Exception
    {
        Artifact artifact1 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.0:jar");
        Artifact artifact2 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.1:jar");
        Artifact artifact3 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.2:jar");

        generator.generate(artifact1);
        generator.generate(artifact2);
        generator.generate(artifact3);

        final RepositoryIndexer repositoryIndexer = repositoryIndexManager.getRepositoryIndex("storage0:releases");

        final int x = repositoryIndexer.index(new File("org/carlspring/strongbox/strongbox-commons"));

        Assert.assertEquals("6 artifacts expected!",
                            6,  // one is jar another pom, both would be added into the same Lucene document
                            x);

        Set<ArtifactInfo> search = repositoryIndexer.search("org.carlspring.strongbox",
                                                            "strongbox-commons",
                                                            null,
                                                            null,
                                                            null);

        if (!search.isEmpty())
        {
            System.out.println("Search results:");

            for (final ArtifactInfo ai : search)
            {
                System.out.println(" - " + ai.getGroupId() + " / " + ai.getArtifactId() + " / " + ai.getVersion() + " / " + ai.getDescription());
            }
        }

        Assert.assertEquals("Only three versions of the strongbox-commons artifact were expected!", 3, search.size());

        search = repositoryIndexer.search("org.carlspring.strongbox", "strongbox-commons", "1.0", "jar", null);
        Assert.assertEquals("org.carlspring.strongbox:strongbox-commons:1.0 was not found!", 1, search.size());

        search = repositoryIndexer.search("+g:org.carlspring.strongbox +a:strongbox-commons +v:1.0");
        Assert.assertEquals("org.carlspring.strongbox:strongbox-commons:1.0 was not found!", 1, search.size());

        repositoryIndexer.delete(search);
        search = repositoryIndexer.search("org.carlspring.strongbox", "strongbox-commons", "1.0", null, null);

        Assert.assertEquals("org.carlspring.strongbox:strongbox-commons:1.0 should have been deleted!", 0,
                            search.size());
    }

    @Test
    public void testContains() throws Exception
    {
        Artifact artifact1 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.0.1:jar");
        Artifact artifact2 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.1.1:jar");
        Artifact artifact3 = ArtifactUtils.getArtifactFromGAVTC("org.carlspring.strongbox:strongbox-commons:1.2.1:jar");

        generator.generate(artifact1, new String[] { "javadoc", "tests" });
        generator.generate(artifact2, new String[] { "javadoc", "tests", "site" });
        generator.generate(artifact3, new String[] { "javadoc", "tests", "site" });

        final RepositoryIndexer repositoryIndexer = repositoryIndexManager.getRepositoryIndex("storage0:releases");

        final int x = repositoryIndexer.index(new File("org/carlspring/strongbox/strongbox-commons"));

        // assertTrue("Incorrect number of artifacts found!", x >= 3);

        SearchRequest request = new SearchRequest("storage0",
                                                  "releases",
                                                  "+g:org.carlspring.strongbox +a:strongbox-commons +v:1.0.1 +p:jar");

        assertTrue("Could not find an artifact based on the search criteria!", artifactSearchService.contains(request));

        artifactSearchService.search(request);
    }

}
