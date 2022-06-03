package rechistory.core;

public interface ExposureHistoryManager {

    public ExposureHistory getExposureHistory(String uid);

    public ExposureHistory newExposureHistory(String uid);

}
