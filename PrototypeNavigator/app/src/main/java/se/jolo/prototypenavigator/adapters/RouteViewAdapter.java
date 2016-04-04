package se.jolo.prototypenavigator.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.activities.StopPointDetail;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.singleton.RouteHolder;
/* The RouteViewAdapter class sets up a list with RouteItems. */
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
        holder.idView.setText(StringUtils.capitalize(route.getRouteItems().get(position).getStopPointItems().get(0).getDeliveryAddress().toLowerCase()));
        //holder.sumOdrH.setText(route.getRouteItems().get(position).getStopPointItems().get(0).getDeliveryAddress());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, StopPointDetail.class);
                intent.putExtra(StopPointDetail.ARG_ITEM_ID, holder.routeItem.getPrimaryStopPointItemUuid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return route.getRouteItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView idView;
        public final TextView contentView;
        public RouteItem routeItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            idView = (TextView) view.findViewById(R.id.id);
            contentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}