package org.datacite.mds.client.ui.workers;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;
import org.datacite.mds.client.service.MdsApi;

public class DoiMintingWorkerPanel extends WorkerPanel {

    JTextArea doiTextArea;
    
    /**
     * Create the panel.
     */
    public DoiMintingWorkerPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        
        doiTextArea = new JTextArea();
        scrollPane.setViewportView(doiTextArea);

    }

    @Override
    public Worker getNewWorker() {
        return new Worker() {
            @Override
            protected Void doInBackground() throws Exception {
                String text = doiTextArea.getText();
                String[] lines = StringUtils.splitByWholeSeparator(text, "\n");
                log("==========");
                mdsApi.setTestMode(true);
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
            
        };
        
    }
    
}
