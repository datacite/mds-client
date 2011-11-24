package org.datacite.mds.client.ui.workers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.apache.http.StatusLine;
import org.datacite.mds.client.service.MdsApi;

public class UploadMetadataWorker extends Worker<File> {

    MdsApi mdsApi = MdsApi.getInstance();

    public UploadMetadataWorker() {
        super("Upload Metadata");
    }

    @Override
    void doInBackground(File file) throws Exception {
        String testMode = mdsApi.isTestMode() ? "test " : "";
        log("");
        log(testMode + "uploading " + file);
        byte[] xml = FileUtils.readFileToByteArray(file);
        StatusLine status = mdsApi.uploadMetadata(xml);
        
        if (status.getStatusCode() == 201)
            log(status);
        else
            throw new Exception(status.toString());

    }

}
