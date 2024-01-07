package marrero_ferrera_gcid_ulpgc.control;

public interface Fetcher {
    void fetchFiles() throws MyManagerException;

    boolean fileExists() throws MyManagerException;
}
