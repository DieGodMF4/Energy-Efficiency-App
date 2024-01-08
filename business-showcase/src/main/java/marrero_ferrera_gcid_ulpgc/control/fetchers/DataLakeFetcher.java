package marrero_ferrera_gcid_ulpgc.control.fetchers;

import marrero_ferrera_gcid_ulpgc.control.MyManagerException;

public interface DataLakeFetcher {
    void fetchFiles() throws MyManagerException;
    boolean fileExists() throws MyManagerException;
}
