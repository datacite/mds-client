package org.datacite.mds.client.ui.workers;

import javax.swing.SwingWorker;

import org.datacite.mds.client.service.MdsApi;
import org.datacite.mds.client.ui.LogPanel;

public abstract class Worker extends SwingWorker<Void, String> {

    MdsApi mdsApi = MdsApi.getInstance();

    LogPanel logPanel;

    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
    }

    void log(Object obj) {
        if (logPanel != null) {
            logPanel.log(obj);
        }
    }

    @Override
    protected void done() {
        // TODO Auto-generated method stub
        if (!isCancelled())
            setProgress(100);
    }

}
