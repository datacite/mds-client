package org.datacite.mds.client.ui.workers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;

public class DoiMintingWorker extends Worker<String> {
    
    public final static String BEGIN_COMMENT_CHAR = "#"; 
    
    JTextArea doiTextArea;    

    public DoiMintingWorker(JTextArea doiTextArea) {
        super("Doi Minting");
        String text = doiTextArea.getText();
        List<String> list = new ArrayList<String>();
        String[] lines = StringUtils.splitByWholeSeparator(text, "\n");
        for (String line : lines) {
            line = StringUtils.trimToEmpty(line);
            if (!line.isEmpty() && !line.startsWith(BEGIN_COMMENT_CHAR))
                list.add(line);
        }
        setList(list);
    }

    @Override
    void doInBackground(String elem) throws Exception {
        String[] parts = elem.split(" ", 2);
        String doi = parts[0].trim();
        String url = parts.length > 1 ? parts[1].trim() : null;
        String testMode = mdsApi.isTestMode()?"test ":"";
        log("");
        log(testMode + "minting " + doi + " (" + url + ")");

        StatusLine status = mdsApi.mintDoi(doi, url);

        if (status.getStatusCode() == 201)
            log(status);
        else
            throw new Exception(status.toString());
    }
}
