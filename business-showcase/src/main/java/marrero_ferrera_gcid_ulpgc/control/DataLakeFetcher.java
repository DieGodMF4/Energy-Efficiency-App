package marrero_ferrera_gcid_ulpgc.control;

import marrero_ferrera_gcid_ulpgc.model.Model;

public class DataLakeFetcher implements Fetcher {
    private String additionalPath;
    private final String topicName;
    private final String date;

    public DataLakeFetcher(String topicName, String date){
        this.additionalPath = "";
        this.topicName = topicName;
        this.date = date;
    }

    public DataLakeFetcher setAdditionalPath(String additionalPath) {
        this.additionalPath = additionalPath;
        return this;
    }

    @Override
    public Model fetchFilesJson() {
    }
}
