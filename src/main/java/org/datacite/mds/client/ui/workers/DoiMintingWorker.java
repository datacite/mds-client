package org.datacite.mds.client.ui.workers;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;
import org.datacite.mds.client.service.MdsApi;

public class DoiMintingWorker extends Worker {
    
    JTextArea doiTextArea;    

    public DoiMintingWorker(JTextArea doiTextArea) {
        super();
        this.doiTextArea = doiTextArea;
    }

    @Override
    protected Void doInBackground() throws Exception {
        String text = doiTextArea.getText();
        String[] lines = StringUtils.splitByWholeSeparator(text, "\n");
        log("==========");
        for (String line : lines) {
            String[] parts = line.split(" ", 2);
            String doi = parts[0].trim();
            String url = parts.length > 1 ? parts[1].trim() : null;
            log("minting " + doi + " (" + url + ")");
            try {
                StatusLine status = mdsApi.mintDoi(doi, url);
                log(status + "\n");
            } catch (HttpException ex) {
                log("failed " + ex.getMessage());
            }
        }
        return null;
    }
    
}
