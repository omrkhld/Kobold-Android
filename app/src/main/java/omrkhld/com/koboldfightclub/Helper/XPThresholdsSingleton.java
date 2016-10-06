package omrkhld.com.koboldfightclub.Helper;

import java.util.ArrayList;

/**
 * Created by Omar on 27/9/2016.
 */

public class XPThresholdsSingleton {

    private static XPThresholdsSingleton mInstance = null;
    private ArrayList<ArrayList<Integer>> thresholds;

    private XPThresholdsSingleton() {
        thresholds = new ArrayList<>();
    }

    public static XPThresholdsSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new XPThresholdsSingleton();
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
