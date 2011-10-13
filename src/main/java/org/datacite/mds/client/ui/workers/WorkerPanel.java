package org.datacite.mds.client.ui.workers;

import javax.swing.JPanel;

public abstract class WorkerPanel extends JPanel {
    public abstract Worker getNewWorker();
}
