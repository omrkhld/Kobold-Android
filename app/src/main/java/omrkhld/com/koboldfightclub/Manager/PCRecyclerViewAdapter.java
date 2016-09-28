package omrkhld.com.koboldfightclub.Manager;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private final String TAG = "PCAdapter";
    private final AppCompatActivity activity;

    public PCRecyclerViewAdapter(AppCompatActivity activity, OrderedRealmCollection<Player> data) {
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
        final Player player = getData().get(position);
        holder.name.setText(player.getName());
        holder.lvl.setText(Integer.toString(player.getLevel()));
        holder.hp.setText(Integer.toString(player.getHP()));
        holder.init.setText(Integer.toString(player.getInitMod()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = activity.getSupportFragmentManager();
                AddPlayerDialogFragment dialog = AddPlayerDialogFragment.newInstance("Add Player", player);
                dialog.setTargetFragment(fm.findFragmentById(R.id.view_pager), 300);
                dialog.show(fm, "fragment_dialog_add_player");
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.player_name) TextView name;
        @BindView(R.id.player_lvl) TextView lvl;
        @BindView(R.id.player_hp) TextView hp;
        @BindView(R.id.player_init) TextView init;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}
