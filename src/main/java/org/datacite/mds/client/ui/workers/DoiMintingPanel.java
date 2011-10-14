package org.datacite.mds.client.ui.workers;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;
import org.datacite.mds.client.service.MdsApi;

public class DoiMintingPanel extends WorkerPanel {

    JTextArea doiTextArea;
    
    /**
     * Create the panel.
     */
    public DoiMintingPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        
        doiTextArea = new JTextArea();
        scrollPane.setViewportView(doiTextArea);

    }

    @Override
    public Worker getNewWorker() {
        return new DoiMintingWorker(doiTextArea);
    }
    
}
