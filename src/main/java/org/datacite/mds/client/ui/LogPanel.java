package org.datacite.mds.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel {

    private JTextArea logArea;
    private JCheckBox chckbxAutoScroll;

    /**
     * Create the panel.
     */
    public LogPanel() {
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        logArea = new JTextArea();
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setEditable(false);
        scrollPane.setViewportView(logArea);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);
        FlowLayout fl_panel = new FlowLayout(FlowLayout.LEFT, 5, 5);
        panel.setLayout(fl_panel);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logArea.setText(null);
            }
        });
        panel.add(btnClear);

        chckbxAutoScroll = new JCheckBox("auto scroll to bottom");
        chckbxAutoScroll.setSelected(true);
        panel.add(chckbxAutoScroll);

    }

    public void log(Object obj) {
        logArea.append(obj + "\n");
        if (chckbxAutoScroll.isSelected())
            logArea.setCaretPosition(logArea.getText().length());
    }

}
