package omrkhld.com.koboldfightclub.Manager;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import omrkhld.com.koboldfightclub.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 27/9/2016.
 */

public class PCRecyclerViewAdapter extends RealmRecyclerViewAdapter<Player, PCRecyclerViewAdapter.ViewHolder> {
    private final Activity activity;

    public PCRecyclerViewAdapter(Activity activity, OrderedRealmCollection<Player> data) {
        super(activity, data, true);
        this.activity = activity;
    }

    @Override
    public PCRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_player, parent, false);
        return new PCRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getData().get(position);
        Player obj = getData().get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Player mItem;

        @BindView(R.id.player_name) TextView name;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
