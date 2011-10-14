package org.datacite.mds.client.ui.workers;

import java.util.Arrays;

import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;

public class DoiMintingWorker extends Worker<String> {
    
    JTextArea doiTextArea;    

    public DoiMintingWorker(JTextArea doiTextArea) {
        super("Doi Minting");
        String text = doiTextArea.getText();
        String[] lines = StringUtils.splitByWholeSeparator(text, "\n");
        setList(Arrays.asList(lines));
    }

    @Override
    void doInBackground(String elem) {
        String[] parts = elem.split(" ", 2);
        String doi = parts[0].trim();
        String url = parts.length > 1 ? parts[1].trim() : null;
        log("minting " + doi + " (" + url + ")");
        try {
            StatusLine status = mdsApi.mintDoi(doi, url);
            log(status);
        } catch (HttpException ex) {
            log("failed " + ex.getMessage());
        }
        log("");
    }
    
}
