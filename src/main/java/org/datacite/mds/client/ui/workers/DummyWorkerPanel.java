package org.datacite.mds.client.ui.workers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public class DummyWorkerPanel extends WorkerPanel {

    /**
     * Create the panel.
     */
    public DummyWorkerPanel() {
    }

    @Override
    public Worker getNewWorker() {
        Worker<Integer> worker = new Worker<Integer>("Dummy Worker") {
            @Override
            void doInBackground(Integer i) {
                if (i.longValue() % 10 == 0) {
                    log(String.valueOf(i));
                }
                Math.sqrt(i % 50);
            }
        };
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 1000; i++)
            list.add(new Integer(i));
        worker.setList(list);
        return worker;
    }
}
