package org.datacite.mds.client.ui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setEditable(false);
        scrollPane.setViewportView(logArea);

    }

    public void log(Object obj) {
        logArea.append(obj + "\n");
        logArea.setCaretPosition(logArea.getText().length());
    }

}
