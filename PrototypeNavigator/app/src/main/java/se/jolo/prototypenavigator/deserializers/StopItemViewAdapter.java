package se.jolo.prototypenavigator.deserializers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.model.StopPointItem;


/**
 * Created by Holstad on 29/02/16.
 */
public class StopItemViewAdapter extends RecyclerView.Adapter<StopItemViewAdapter.ViewHolder> {

    private final List<StopPointItem> stopPointItems;
    private final CollapsingToolbarLayout collapsingToolbarLayout;

    public StopItemViewAdapter(List<StopPointItem> stopPointItems, CollapsingToolbarLayout collapsingToolbarLayout) {
        this.stopPointItems = stopPointItems;
        this.collapsingToolbarLayout=collapsingToolbarLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = stopPointItems.get(position);
        holder.mIdView.setText(stopPointItems.get(position).getName());
        holder.mContentView.setText("54");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsingToolbarLayout.setTitle(holder.mItem.getName());
                /*Context context = v.getContext();
                Intent intent = new Intent(context, RouteDetailActivity.class);
                intent.putExtra(RouteDetailFragment.ARG_ITEM_ID, holder.routeItem.getUuid());
                context.startActivity(intent);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return stopPointItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public StopPointItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.sumOdrF);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}