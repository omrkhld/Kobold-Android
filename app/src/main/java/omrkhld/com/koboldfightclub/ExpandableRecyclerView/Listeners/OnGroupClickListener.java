package omrkhld.com.koboldfightclub.ExpandableRecyclerView.Listeners;

/**
 * Created by Omar on 10/10/2016.
 */

public interface OnGroupClickListener {

    /**
     * @param flatPos the flat position (raw index within the list of visible items in the
     * RecyclerView of a GroupViewHolder)
     * @return true if click expanded group, false if click collapsed group
     */
    boolean onGroupClick(int flatPos);
}