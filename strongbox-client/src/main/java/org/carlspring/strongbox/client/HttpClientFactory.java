package org.carlspring.strongbox.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;

/**
 * @author mtodorov
 */
public class HttpClientFactory
{


    public static CloseableHttpClient createCloseableHttpClient(String storageId,
                                                                String repositoryId)
    {
        // TODO: Get the host from the configuration
        String hostname = null;

        // TODO: Get the username from the configuration
        String username = null;

        // TODO: Get the password from the configuration
        String password = null;

        HttpHost target = new HttpHost(hostname, 80, "http");
        HttpHost proxy = new HttpHost("127.0.0.1", 8180, "http");

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(proxy.getHostName(), AuthScope.ANY_PORT), credentials);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(proxy);
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        return clientBuilder.build();
    }


}
