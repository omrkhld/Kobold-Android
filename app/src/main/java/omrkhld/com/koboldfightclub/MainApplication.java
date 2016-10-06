package omrkhld.com.koboldfightclub;

import android.app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import omrkhld.com.koboldfightclub.Helper.XPThresholdsSingleton;
import omrkhld.com.koboldfightclub.POJO.Monster;

/**
 * Created by Omar on 2/8/2016.
 */
public class MainApplication extends Application {
    private ArrayList<ArrayList<Integer>> thresholds = new ArrayList<ArrayList<Integer>>();
    private InputStream is;
    private RealmConfiguration monstersConfig;
    private Realm monstersRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            is = getAssets().open("thresholds.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<Integer> row = new ArrayList<>();
                String[] rowData = line.split(" ");
                row.add(Integer.parseInt(rowData[1]));
                row.add(Integer.parseInt(rowData[2]));
                row.add(Integer.parseInt(rowData[3]));
                row.add(Integer.parseInt(rowData[4]));
                thresholds.add(row);
            }
            XPThresholdsSingleton.getInstance().setThresholds(thresholds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Realm.init(this);

        monstersConfig = new RealmConfiguration.Builder()
                .name(getString(R.string.monsters_realm))
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(monstersConfig); // Clean slate
        monstersRealm = Realm.getInstance(monstersConfig);

        try {
            loadJsonFromStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJsonFromStream() throws IOException {
        monstersRealm.beginTransaction();
        InputStream stream = getAssets().open("Monsters.json");
        try {
            monstersRealm.createAllFromJson(Monster.class, stream);
            monstersRealm.commitTransaction();
        } catch (IOException e) {
            monstersRealm.cancelTransaction();
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
