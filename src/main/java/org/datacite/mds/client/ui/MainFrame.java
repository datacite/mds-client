package org.datacite.mds.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.datacite.mds.client.service.MdsApi;
import org.datacite.mds.client.ui.workers.DoiMintingPanel;
import org.datacite.mds.client.ui.workers.DummyWorker;
import org.datacite.mds.client.ui.workers.UploadMetadataPanel;
import org.datacite.mds.client.ui.workers.Worker;
import org.datacite.mds.client.ui.workers.WorkerPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame {

    public static String VERSION = "0.1";

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
    private JPanel controlPanel;
    private JButton btnLogin;
    private JDialog loginDialog; 

    private MdsApi mdsApi = MdsApi.getInstance();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    //frame.dummyMode();
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
        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("Mds Client (version " + VERSION + ")");
        setBounds(100, 100, 450, 300);
        setMinimumSize(new Dimension(500, 500));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(0.4);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1.setBorder(null);
        contentPane.add(splitPane_1);

        JPanel controlAndLogPanel = new JPanel();
        splitPane_1.setRightComponent(controlAndLogPanel);
        controlAndLogPanel.setLayout(new BorderLayout(0, 5));

        controlPanel = new JPanel();
        controlAndLogPanel.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("max(30dlu;pref)"),
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(30dlu;pref)"), FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(30dlu;pref):grow"), FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(30dlu;pref)"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, }));

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        controlPanel.add(btnLogin, "1, 2");

        lblSymbol = new JLabel("");
        controlPanel.add(lblSymbol, "3, 2, 5, 1, left, default");

        btnExecute = new JButton("Execute");
        btnExecute.setEnabled(false);
        controlPanel.add(btnExecute, "1, 4, fill, fill");

        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mdsApi.setTestMode(chckbxTestMode.isSelected());
                Component selectedTab = tabbedPane.getSelectedComponent();
                if (!(selectedTab instanceof WorkerPanel))
                    throw new IllegalAccessError("selected tab not a worker panel");

                WorkerPanel workerPanel = (WorkerPanel) selectedTab;
                worker = workerPanel.getNewWorker();
                worker.setLogPanel(logPanel);
                setControlsEnabled(false);
                btnAbort.setEnabled(true);
                worker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("progress")) {
                            Integer progress = (Integer) evt.getNewValue();
                            progressBar.setValue(progress.intValue());
                        } else {
                            if (worker.isDone()) {
                                setControlsEnabled(true);
                                btnAbort.setEnabled(false);
                            }
                        }
                    }
                });
                worker.execute();

            }
        });

        chckbxTestMode = new JCheckBox("testMode");
        chckbxTestMode.setEnabled(false);
        controlPanel.add(chckbxTestMode, "3, 4");

        progressBar = new JProgressBar();
        controlPanel.add(progressBar, "5, 4, fill, default");

        btnAbort = new JButton("Abort");
        controlPanel.add(btnAbort, "7, 4, fill, fill");
        btnAbort.setEnabled(false);
        btnAbort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                worker.cancel(false);
            }
        });

        logPanel = new LogPanel();
        controlAndLogPanel.add(logPanel, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        splitPane_1.setLeftComponent(tabbedPane);

        JPanel doiMintingPanel = new DoiMintingPanel();
        tabbedPane.addTab("Mint DOIs", null, doiMintingPanel, null);

        JPanel uploadMetadataPanel = new UploadMetadataPanel();
        tabbedPane.addTab("Upload Metadata", null, uploadMetadataPanel, null);

        loginDialog = new LoginDialog();
        
        refreshUIafterLogin();
    }

    public void dummyMode() {
        try {
            setControlsEnabled(true);
            btnAbort.setEnabled(false);
            JPanel dummyPanel = new WorkerPanel() {
                public Worker getNewWorker() {
                    return new DummyWorker();
                }
            };
            tabbedPane.addTab("Dummy", null, dummyPanel, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean login() {
        loginDialog.setVisible(true);
        refreshUIafterLogin();
        return false;
    }
    
    private void refreshUIafterLogin() {
        setControlsEnabled(mdsApi.isLoggedIn());
        btnLogin.setEnabled(true);
        btnAbort.setEnabled(false);
        lblSymbol.setText(mdsApi.getSymbol());
    }

    private void setControlsEnabled(boolean enabled) {
        for (Component component : controlPanel.getComponents())
            component.setEnabled(enabled);
    }

    class MyWindowListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            if (worker == null || worker.isDone()) {
                dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(getParent(),
                        "Job is still running and will be aborted. Really quit?", "Job is still running",
                        JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    worker.cancel(false);
                    dispose();
                }
            }
            ;
        }

    }

}
