package omrkhld.com.koboldfightclub.Manager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import omrkhld.com.koboldfightclub.POJO.Encounter;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 6/10/2016.
 */

public class EncBuildRealmAdapter extends RealmRecyclerViewAdapter<Encounter, EncBuildRealmAdapter.ViewHolder> {

    public static final String TAG = "EncounterAdapter";
    private final AppCompatActivity activity;

    public EncBuildRealmAdapter(AppCompatActivity activity, OrderedRealmCollection<Encounter> data) {
        super(activity, data, true);
        this.activity = activity;
    }

    @Override
    public EncBuildRealmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_enc_build, parent, false);
        return new EncBuildRealmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EncBuildRealmAdapter.ViewHolder holder, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
