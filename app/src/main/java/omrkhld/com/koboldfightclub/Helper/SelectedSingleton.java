package omrkhld.com.koboldfightclub.Helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Omar on 7/10/2016.
 */

public class SelectedSingleton {

    public static final String TAG = "SelectedSingleton";
    private static SelectedSingleton mInstance = null;
    private static ArrayList<String> names;
    private static HashMap<String, Integer> qtyMap;

    private SelectedSingleton() {
        names = new ArrayList<>();
        qtyMap = new HashMap<>();
    }

    public static SelectedSingleton newInstance() {
        mInstance = new SelectedSingleton();
        return mInstance;
    }

    public static SelectedSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new SelectedSingleton();
        }
        return mInstance;
    }

    public ArrayList<String> getNames() { return names; }
    public HashMap<String, Integer> getQtyMap() {
        return qtyMap;
    }

    public void addQty(String n) {
        if (qtyMap.containsKey(n)) {
            qtyMap.put(n, qtyMap.get(n)+1);
        } else {
            qtyMap.put(n, 1);
            names.add(n);
        }
    }
    public void removeQty(String n) {
        if (qtyMap.get(n) > 1) {
            qtyMap.put(n, qtyMap.get(n)-1);
        } else {
            qtyMap.remove(n);
            names.remove(n);
        }
    }
}
