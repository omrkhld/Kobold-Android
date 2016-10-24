package omrkhld.com.koboldfightclub.Run;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.POJO.Combatant;
import omrkhld.com.koboldfightclub.POJO.CombatantMonster;
import omrkhld.com.koboldfightclub.POJO.CombatantPlayer;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 22/10/2016.
 */

public class RunAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AppCompatActivity activity;
    private ArrayList<Combatant> combatants;

    private final int PC = 0, MON = 1;

    public RunAdapter(AppCompatActivity activity, ArrayList<Combatant> combatants) {
        this.activity = activity;
        this.combatants = combatants;
    }

    @Override
    public int getItemViewType(int position) {
        if (combatants.get(position) instanceof CombatantPlayer) {
            return PC;
        } else if (combatants.get(position) instanceof CombatantMonster) {
            return MON;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case PC:
                View vp = inflater.inflate(R.layout.item_combatant_player, parent, false);
                viewHolder = new PlayerViewHolder(vp);
                break;
            case MON:
                View vm = inflater.inflate(R.layout.item_combatant_monster, parent, false);
                viewHolder = new MonsterViewHolder(vm);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {
            case PC:
                PlayerViewHolder vp = (PlayerViewHolder) holder;
                configurePlayerViewHolder(vp, position);
                break;
            case MON:
                MonsterViewHolder vm = (MonsterViewHolder) holder;
                configureMonsterViewHolder(vm, position);
                break;
            default:
                break;
        }
    }

    private void configurePlayerViewHolder(PlayerViewHolder vp, int position) {
        final CombatantPlayer p = (CombatantPlayer) combatants.get(position);
        if (p != null) {
            vp.name.setText(p.name);
            vp.init.setText(Integer.toString(p.init));
            vp.hp.setText(Integer.toString(p.hp));
            vp.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = activity.getSupportFragmentManager();
                    EditPlayerDialogFragment dialog = EditPlayerDialogFragment.newInstance("Edit Player", p);
                    dialog.show(fm, "dialog_edit_player");
                }
            });
        }
    }

    private void configureMonsterViewHolder(MonsterViewHolder vm, int position) {
        final CombatantMonster m = (CombatantMonster) combatants.get(position);
        if (m != null) {
            vm.name.setText(m.name);
            vm.init.setText(Integer.toString(m.init));
            vm.hp.setText(Integer.toString(m.hp));
            vm.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = activity.getSupportFragmentManager();
                    EditMonsterDialogFragment dialog = EditMonsterDialogFragment.newInstance("Edit Monster", m);
                    dialog.show(fm, "dialog_edit_monster");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return combatants.size();
    }
}
