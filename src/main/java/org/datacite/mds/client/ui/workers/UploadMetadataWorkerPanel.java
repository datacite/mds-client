package org.datacite.mds.client.ui.workers;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

import org.datacite.mds.client.ui.LoginDialog;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JList;
import javax.swing.JScrollPane;

public class UploadMetadataWorkerPanel extends WorkerPanel {

    DefaultListModel listOfFiles;
    JList list;

    /**
     * Create the panel.
     */
    public UploadMetadataWorkerPanel() {
        setLayout(new BorderLayout(0, 0));

        final JPanel panel = new JPanel();
        add(panel, BorderLayout.EAST);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.BUTTON_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));

        JButton btnNewButton = new JButton("Load Files");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.showOpenDialog(panel);
                File[] files = fileChooser.getSelectedFiles();
                for (File file : files)
                    listOfFiles.addElement(file);
            }
        });
        panel.add(btnNewButton, "2, 2, fill, top");

        JButton btnNewButton_1 = new JButton("Clear List");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listOfFiles.clear();
            }
        });
        panel.add(btnNewButton_1, "2, 4, fill, top");
        
        JButton btnRemoveSelected = new JButton("Remove Selected");
        btnRemoveSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Object obj : list.getSelectedValues())
                    listOfFiles.removeElement(obj);
            }
        });
        panel.add(btnRemoveSelected, "2, 6");

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        listOfFiles = new DefaultListModel();

        list = new JList(listOfFiles);
        scrollPane.setViewportView(list);

    }

    @Override
    public Worker getNewWorker() {
        // TODO Auto-generated method stub
        return null;
    }

}
