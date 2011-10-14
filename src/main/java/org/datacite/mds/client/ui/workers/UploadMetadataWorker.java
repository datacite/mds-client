package org.datacite.mds.client.ui.workers;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.http.StatusLine;
import org.datacite.mds.client.service.MdsApi;

public class UploadMetadataWorker extends Worker<File> {
    
    MdsApi mdsApi = MdsApi.getInstance();

    public UploadMetadataWorker() {
        super("Upload Metadata");
    }

    @Override
    void doInBackground(File file) {
        log("uploading " + file);
        try {
            byte[] xml = FileUtils.readFileToByteArray(file);
            StatusLine status = mdsApi.uploadMetadata(xml);
            log(status);
        } catch (Exception ex) {
            log("failed: " + ex.getMessage());
        }
        log("");
    }

}
