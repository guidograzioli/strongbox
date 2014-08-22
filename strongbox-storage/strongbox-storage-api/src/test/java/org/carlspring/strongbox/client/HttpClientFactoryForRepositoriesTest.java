package org.carlspring.strongbox.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mtodorov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/strongbox-*-context.xml", "classpath*:/META-INF/spring/strongbox-*-context.xml"})
public class HttpClientFactoryForRepositoriesTest
{

    @Autowired
    private HttpClientFactoryForRepositories httpClientFactoryForRepositories;


    @Test
    public void testCreateCloseableHttpClient()
    {
        final CloseableHttpClient client = httpClientFactoryForRepositories.createCloseableHttpClient("storage0", "proxied-releases");

        System.out.println("");
    }

}
