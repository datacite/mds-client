package org.datacite.mds.client.ui.workers;

import javax.swing.JPanel;


public class DummyWorkerPanel extends WorkerPanel {

    /**
     * Create the panel.
     */
    public DummyWorkerPanel() {
    }

    @Override
    public Worker getNewWorker() {
        return new Worker() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; !isCancelled() && i < 100000000; i++) {
                    int progess = (int) (i / 1000000);
                    setProgress(progess);
                    if (i % 100000 == 0) {
                        log(String.valueOf(i));
                    }
                    Math.sqrt(i % 50);
                }
                return null;
            }
        };
    }
}
