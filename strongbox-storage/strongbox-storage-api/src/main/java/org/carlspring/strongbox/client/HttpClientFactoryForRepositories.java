package org.carlspring.strongbox.client;

import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.configuration.ProxyConfiguration;
import org.carlspring.strongbox.storage.DataCenter;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mtodorov
 */
@Component
public class HttpClientFactoryForRepositories
        extends HttpClientFactory
{

    @Autowired
    private ConfigurationManager configurationManager;

    @Autowired
    private DataCenter dataCenter;


    public CloseableHttpClient createCloseableHttpClient(String storageId, String repositoryId)
    {
        final Storage storage = dataCenter.getStorage(storageId);
        final Repository repository = storage.getRepository(repositoryId);

        String proxyUsername = null;
        String proxyPassword = null;
        String proxyScheme = null;
        String proxyHost = null;
        int proxyPort = 80;

        final ProxyConfiguration proxyConfiguration = repository.getProxyConfiguration() != null ?
                                                      repository.getProxyConfiguration() :
                                                      configurationManager.getConfiguration().getProxyConfiguration();

        if (proxyConfiguration != null)
        {
            proxyUsername = proxyConfiguration.getUsername();
            proxyPassword = proxyConfiguration.getPassword();
            proxyHost = proxyConfiguration.getHost();
            proxyPort = proxyConfiguration.getPort();
            proxyScheme = proxyConfiguration.getScheme();
        }

        return createCloseableHttpClient(proxyUsername,
                                         proxyPassword,
                                         proxyHost,
                                         proxyPort,
                                         proxyScheme);
    }

}
