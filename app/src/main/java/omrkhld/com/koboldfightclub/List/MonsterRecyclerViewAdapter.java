package omrkhld.com.koboldfightclub.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import omrkhld.com.koboldfightclub.Monster;
import omrkhld.com.koboldfightclub.R;

public class MonsterRecyclerViewAdapter extends RealmRecyclerViewAdapter<Monster, MonsterRecyclerViewAdapter.ViewHolder> {
    private final Activity activity;
    private SharedPreferences pclevels;
    private int numPlayers = 1;
    private int avgLvl = 1;

    public MonsterRecyclerViewAdapter(Activity activity, OrderedRealmCollection<Monster> data) {
        super(activity, data, true);
        this.activity = activity;
        pclevels = context.getSharedPreferences(context.getString(R.string.pref_pc_levels), 0);
        numPlayers = pclevels.getInt("numPlayers", 1);
        avgLvl = pclevels.getInt("avgLvl", 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_monster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getData().get(position);
        Monster obj = getData().get(position);
        holder.name.setText(obj.getName());
        float cr = obj.getCR();
        String crText = "";
        if (cr == 0.125f) {
            crText = "1/8";
        } else if (cr == 0.25f) {
            crText = "1/4";
        } else if (cr == 0.5f) {
            crText = "1/2";
        } else {
            crText = String.format("%d", (int) cr);
        }
        holder.cr.setText(crText);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Monster mItem;

        @BindView(R.id.monster_name) TextView name;
        @BindView(R.id.monster_cr) TextView cr;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
