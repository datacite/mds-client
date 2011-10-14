package org.datacite.mds.client.service;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;

public class MdsApiImpl extends MdsApi {

    public final static String MDS_URL = "https://mds.datacite.org";

    boolean testMode;

    DefaultHttpClient httpClient = new DefaultHttpClient();
    
    String symbol;

    @Override
    public StatusLine mintDoi(String doi, String url) throws HttpException {
        try {
            HttpPost post = new HttpPost(MDS_URL + "/doi?" + getTestModeParam());
            StringEntity entity = new StringEntity("doi=" + doi + "\n" + "url=" + url);
            post.setEntity(entity);
            return execute(post);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private String getTestModeParam() {
        return "testMode=" + testMode;
    }
    
    private StatusLine execute(HttpUriRequest request) throws HttpException {
        try {
            HttpResponse response = httpClient.execute(request);
            StatusLine statusLine = makeStatusLine(response);
            return statusLine;
        } catch (Exception e) {
            throw new HttpException("Error executing request", e);
        }
    }
    

    private StatusLine makeStatusLine(HttpResponse response) {
        StatusLine origStatusLine = response.getStatusLine();
        String reasonPhrase = origStatusLine.getReasonPhrase();
        try {
            if (response.getEntity() != null)
                reasonPhrase += ": " + StringUtils.chomp(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
        }
        
        StatusLine statusLine = new BasicStatusLine(origStatusLine.getProtocolVersion(),
                origStatusLine.getStatusCode(), reasonPhrase);
        return statusLine;
    }

    @Override
    public StatusLine uploadMetadata(byte[] xml) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCredentials(String symbol, String password) throws HttpException {
        this.symbol = null;
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(symbol, password);
        AuthScope authScope = new AuthScope(null, -1);
        httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
        testCredentials();
        this.symbol = symbol;
    }

    private void testCredentials() throws HttpException {
        HttpGet get = new HttpGet(MDS_URL + "/doi");
        StatusLine status = execute(get);
        if (status.getStatusCode() == 401)
            throw new AuthenticationException("unable to authenticate");
    }

    @Override
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public boolean isLoggedIn() {
        return this.symbol != null;
    }
}
