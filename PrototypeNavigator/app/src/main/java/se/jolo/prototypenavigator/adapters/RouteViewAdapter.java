package se.jolo.prototypenavigator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.activities.RouteDetailActivity;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.singleton.RouteHolder;


/**
 * Created by Holstad on 29/02/16.
 */
public class RouteViewAdapter
        extends RecyclerView.Adapter<RouteViewAdapter.ViewHolder> {

    private Route route;

    public RouteViewAdapter() {
        route = RouteHolder.INSTANCE.getRoute();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.routeItem = route.getRouteItems().get(position);
        holder.mIdView.setText(route.getRouteItems().get(position).getStopPointItems().get(0).getDeliveryAddress());
        //holder.sumOdrH.setText(route.getRouteItems().get(position).getStopPointItems().get(0).getDeliveryAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, RouteDetailActivity.class);
                    intent.putExtra(RouteDetailActivity.ARG_ITEM_ID, holder.routeItem.getPrimaryStopPointItemUuid());
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return route.getRouteItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public RouteItem routeItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}