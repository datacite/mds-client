package org.datacite.mds.client.ui.workers;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        StringBuilder text = new StringBuilder();
        text.append("# simple enter one space seperated DOI URL pair per line:\n");
        text.append("#   10.5072/test  http://example.com\n");
        text.append("#   10.5072/test2 http://example.com/path\n");
        text.append("# lines starting with hash sign are ignored\n");
        doiTextArea.setText(text.toString());
        scrollPane.setViewportView(doiTextArea);

    }

    @Override
    public Worker getNewWorker() {
        return new DoiMintingWorker(doiTextArea);
    }

}
