package omrkhld.com.koboldfightclub.Run;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 24/10/2016.
 */

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    @BindView(R.id.combatant_name) TextView name;
    @BindView(R.id.combatant_init) TextView init;
    @BindView(R.id.combatant_hp) TextView hp;

    public PlayerViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }
}