package org.datacite.mds.client.ui.workers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UploadMetadataPanel extends WorkerPanel {

    DefaultListModel listOfFiles;
    JList list;
    
    JFileChooser fileChooser;    

    /**
     * Create the panel.
     */
    public UploadMetadataPanel() {
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

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        
        JButton btnNewButton = new JButton("Load Files");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

    @SuppressWarnings("unchecked")
    @Override
    public Worker getNewWorker() {
        UploadMetadataWorker worker = new UploadMetadataWorker();
        List<File> files = (List<File>) Collections.list(listOfFiles.elements());
        worker.setList(files);
        return worker;
    }

}
