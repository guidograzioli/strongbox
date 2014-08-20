package org.carlspring.strongbox.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author mtodorov
 */
@XStreamAlias("remote-repository")
public class RemoteRepository
{

    /**
     * A representation of the url as a URL object.
     * This should only really be used internally.
     */
    @XStreamOmitField
    private URL internalURL;

    @XStreamAlias(value = "url")
    private String url;

    @XStreamAlias(value = "download-remote-indexes")
    private boolean downloadRemoteIndexes;

    @XStreamAlias(value = "auto-blocking")
    private boolean autoBlocking;

    @XStreamAlias(value = "checksum-validation")
    private boolean checksumValidation;

    @XStreamAlias(value = "username")
    private String username;

    @XStreamAlias(value = "password")
    private String password;

    @XStreamAlias(value = "checksum-policy")
    private String checksumPolicy;


    public RemoteRepository()
    {
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
            throws MalformedURLException
    {
        this.url = url;
        this.internalURL = new URL(url);
    }

    public boolean isDownloadRemoteIndexes()
    {
        return downloadRemoteIndexes;
    }

    public void setDownloadRemoteIndexes(boolean downloadRemoteIndexes)
    {
        this.downloadRemoteIndexes = downloadRemoteIndexes;
    }

    public boolean isAutoBlocking()
    {
        return autoBlocking;
    }

    public void setAutoBlocking(boolean autoBlocking)
    {
        this.autoBlocking = autoBlocking;
    }

    public boolean isChecksumValidation()
    {
        return checksumValidation;
    }

    public void setChecksumValidation(boolean checksumValidation)
    {
        this.checksumValidation = checksumValidation;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getChecksumPolicy()
    {
        return checksumPolicy;
    }

    public void setChecksumPolicy(String checksumPolicy)
    {
        this.checksumPolicy = checksumPolicy;
    }

    public URL getInternalURL()
    {
        return internalURL;
    }

    public void setInternalURL(URL internalURL)
    {
        this.internalURL = internalURL;
    }

    public String getScheme()
            throws MalformedURLException
    {
        return new URL(url).getProtocol();
    }

    public String getHost()
            throws MalformedURLException
    {
        return new URL(url).getHost();
    }

    public int getPort()
            throws MalformedURLException
    {
        return new URL(url).getPort();
    }

}
