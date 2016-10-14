package omrkhld.com.koboldfightclub.ExpandableRecyclerView.ViewHolders;

/**
 * Created by Omar on 10/10/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import omrkhld.com.koboldfightclub.ExpandableRecyclerView.Listeners.OnGroupClickListener;
import omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup;

/**
 * ViewHolder for the {@link ExpandableGroup#title} in a {@link omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup}
 *
 * The current implementation does now allow for sub {@link View} of the parent view to trigger
 * a collapse / expand. *Only* click events on the parent {@link View} will trigger a collapse or
 * expand
 */
public abstract class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnGroupClickListener listener;

    public GroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (listener.onGroupClick(getAdapterPosition())) {
                expand();
            } else {
                collapse();
            }
        }
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    public void expand() {}

    public void collapse() {}
}