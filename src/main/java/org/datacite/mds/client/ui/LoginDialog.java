package org.datacite.mds.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.http.HttpException;
import org.datacite.mds.client.service.MdsApi;

public class LoginDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField symbolField;
    private JPasswordField passwordField;
    private final Action actionLogin = new SwingAction();
    
    private MdsApi mdsApi = MdsApi.getInstance();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            LoginDialog dialog = new LoginDialog();
            dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public LoginDialog() {
        setModal(true);
        setTitle("Login");
        setResizable(false);
        setBounds(100, 100, 313, 132);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        FormLayout fl_contentPanel = new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,});
        contentPanel.setLayout(fl_contentPanel);
        {
            JLabel lblSymbol = new JLabel("Symbol");
            contentPanel.add(lblSymbol, "2, 2");
        }
        {
            symbolField = new JTextField();
            contentPanel.add(symbolField, "4, 2, fill, default");
            symbolField.setColumns(10);
        }
        {
            JLabel lblPassword = new JLabel("Password");
            contentPanel.add(lblPassword, "2, 4, right, default");
        }
        {
            passwordField = new JPasswordField();
            contentPanel.add(passwordField, "4, 4, fill, default");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setAction(actionLogin);
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    private class SwingAction extends AbstractAction {
        public SwingAction() {
            putValue(NAME, "Login");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }
        public void actionPerformed(ActionEvent e) {
            String symbol = symbolField.getText();
            String password = passwordField.getText();
            try {
                mdsApi.setCredentials(symbol, password);
                dispose();
            } catch (HttpException ex) {
                JOptionPane.showMessageDialog(getParent(), ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}
