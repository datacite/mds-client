package org.datacite.mds.client.service;

import org.apache.http.HttpException;
import org.apache.http.StatusLine;

public interface MdsApi {
    public void setCredentials(String symbol, String password) throws HttpException;
    
    public void setTestMode(boolean testMode);
    
    public StatusLine mintDoi(String doi, String url) throws HttpException;
    
    public StatusLine uploadMetadata(byte[] xml) throws HttpException;
}
