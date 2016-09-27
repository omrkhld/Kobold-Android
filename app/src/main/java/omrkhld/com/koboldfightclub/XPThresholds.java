package omrkhld.com.koboldfightclub;

import java.util.ArrayList;

/**
 * Created by Omar on 27/9/2016.
 */

public class XPThresholds {

    private static XPThresholds mInstance = null;
    private ArrayList<ArrayList<Integer>> thresholds;

    private XPThresholds() {
        thresholds = new ArrayList<>();
    }

    public static XPThresholds getInstance() {
        if (mInstance == null) {
            mInstance = new XPThresholds();
        }
        return mInstance;
    }

    public ArrayList<ArrayList<Integer>> getThresholds() {
        return thresholds;
    }

    public void setThresholds(ArrayList<ArrayList<Integer>> t) {
        thresholds = t;
    }
}
