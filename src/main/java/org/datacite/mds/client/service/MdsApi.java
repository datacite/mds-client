package org.datacite.mds.client.service;

import org.apache.http.HttpException;
import org.apache.http.StatusLine;

public abstract class MdsApi {
    public abstract void setCredentials(String symbol, String password) throws HttpException;
    
    public abstract void setTestMode(boolean testMode);
    
    public abstract StatusLine mintDoi(String doi, String url) throws HttpException;
    
    public abstract StatusLine uploadMetadata(byte[] xml) throws HttpException;
    
    public abstract boolean isLoggedIn();
    
    public abstract String getSymbol();
    
    private static MdsApi instance;
    
    public static MdsApi getInstance() {
        if (instance == null)
            instance = new MdsApiImpl();
        return instance;
    }
}
