package org.datacite.mds.client.ui.workers;

import java.util.ArrayList;
import java.util.List;


public class DummyWorker extends Worker<Integer> {

    /**
     * Create the panel.
     */
    public DummyWorker() {
        super("Dummy Worker");
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++)
            list.add(new Integer(i));
        setList(list);
    }

    @Override
    void doInBackground(Integer i) {
        log(String.valueOf(i));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
    }
}
