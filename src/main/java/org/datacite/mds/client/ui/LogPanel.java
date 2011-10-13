package org.datacite.mds.client.ui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class LogPanel extends JPanel {

    private JTextArea logArea;

    /**
     * Create the panel.
     */
    public LogPanel() {
        setLayout(new GridLayout(1, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        logArea = new JTextArea();
        logArea.setEditable(false);
        scrollPane.setViewportView(logArea);

    }

    public void log(Object obj) {
        logArea.append(obj + "\n");
        logArea.setCaretPosition(logArea.getText().length());
    }

}
