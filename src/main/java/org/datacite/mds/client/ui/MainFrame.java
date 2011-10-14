package org.datacite.mds.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.datacite.mds.client.service.MdsApi;
import org.datacite.mds.client.ui.workers.DoiMintingWorkerPanel;
import org.datacite.mds.client.ui.workers.DummyWorkerPanel;
import org.datacite.mds.client.ui.workers.Worker;
import org.datacite.mds.client.ui.workers.WorkerPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MainFrame extends JFrame {

    private JPanel contentPane;

    private Worker worker;
    private JSplitPane splitPane_1;
    private JTabbedPane tabbedPane;
    private JProgressBar progressBar;
    private LogPanel logPanel;
    private JButton btnExecute;
    private JButton btnAbort;
    private JLabel lblSymbol;
    private JCheckBox chckbxTestMode;
    
    private MdsApi mdsApi = MdsApi.getInstance();
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setMinimumSize(new Dimension(500, 400));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(0.5);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1.setBorder(null);
        contentPane.add(splitPane_1);

        JPanel controlPanel = new JPanel();
        splitPane_1.setRightComponent(controlPanel);
        controlPanel.setLayout(new BorderLayout(0, 5));

        JPanel panel_1 = new JPanel();
        controlPanel.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("max(30dlu;pref)"),
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(30dlu;pref)"), FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(30dlu;pref):grow"), FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(30dlu;pref)"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, }));

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel_1.add(btnLogin, "1, 2");

        lblSymbol = new JLabel("");
        panel_1.add(lblSymbol, "3, 2, 5, 1, left, default");

        btnExecute = new JButton("Execute");
        btnExecute.setEnabled(false);
        panel_1.add(btnExecute, "1, 4, fill, fill");

        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mdsApi.setTestMode(chckbxTestMode.isSelected());
                Component selectedTab = tabbedPane.getSelectedComponent();
                if (!(selectedTab instanceof WorkerPanel))
                    throw new IllegalAccessError("selected tab not a worker panel");

                WorkerPanel workerPanel = (WorkerPanel) selectedTab;
                worker = workerPanel.getNewWorker();
                worker.setLogPanel(logPanel);
                btnExecute.setEnabled(false);
                btnAbort.setEnabled(true);
                worker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("progress")) {
                            Integer progress = (Integer) evt.getNewValue();
                            progressBar.setValue(progress.intValue());
                        } else {
                            if (worker.isDone()) {
                                // progressBar.setValue(100);
                                btnExecute.setEnabled(true);
                                btnAbort.setEnabled(false);
                            }
                        }
                    }
                });
                worker.execute();

            }
        });

        chckbxTestMode = new JCheckBox("testMode");
        panel_1.add(chckbxTestMode, "3, 4");

        progressBar = new JProgressBar();
        panel_1.add(progressBar, "5, 4, fill, default");

        btnAbort = new JButton("Abort");
        panel_1.add(btnAbort, "7, 4, fill, fill");
        btnAbort.setEnabled(false);
        btnAbort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                worker.cancel(false);
            }
        });

        logPanel = new LogPanel();
        controlPanel.add(logPanel, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        splitPane_1.setLeftComponent(tabbedPane);

        JPanel doiMintingPanel = new DoiMintingWorkerPanel();
        tabbedPane.addTab("Mint DOIs", null, doiMintingPanel, null);

        JPanel dummyPanel = new DummyWorkerPanel();
        tabbedPane.addTab("Dummy", null, dummyPanel, null);

    }
    
    private boolean login() {
        JDialog loginDialog = new LoginDialog();
        loginDialog.setVisible(true);
        lblSymbol.setText(mdsApi.getSymbol());
        btnExecute.setEnabled(mdsApi.isLoggedIn());
        return false;
    }
}
