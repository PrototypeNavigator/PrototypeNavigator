package se.jolo.prototypenavigator.deserializers;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.OdrRecipient;
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
        holder.stopPointItem = stopPointItems.get(position);
        holder.mIdView.setText(stopPointItems.get(position).getName());
        odrChecker(holder.stopPointItem,holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsingToolbarLayout.setTitle(holder.stopPointItem.getName());
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
        public final TextView sumOdrH;
        public final TextView sumOdrV;
        public final TextView sumOdrF;
        public final TextView sumOdrK;
        public final TextView sumOdrT;
        public final TextView sumOdrL;
        public StopPointItem stopPointItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            sumOdrH = (TextView) view.findViewById(R.id.sumOdrH);
            sumOdrV = (TextView) view.findViewById(R.id.sumOdrV);
            sumOdrF = (TextView) view.findViewById(R.id.sumOdrF);
            sumOdrK = (TextView) view.findViewById(R.id.sumOdrK);
            sumOdrT = (TextView) view.findViewById(R.id.sumOdrT);
            sumOdrL = (TextView) view.findViewById(R.id.sumOdrL);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + sumOdrH.getText() + "'";
        }
    }

    public void odrChecker(StopPointItem stopPointItem,ViewHolder holder){
            List<DeliveryPoint> deliveryPoints= stopPointItem.getDeliveryPoints();
            if(deliveryPoints!=null){
                for(DeliveryPoint d: deliveryPoints){
                    List <OdrRecipient> odrRecipients = d.getOdrRecipients();
                    for(OdrRecipient o : odrRecipients){
                        switch (o.getType()){
                            case "H":
                                holder.sumOdrH.setText(o.getAmount()+"");
                                break;
                            case "V":
                                holder.sumOdrV.setText(o.getAmount()+"");
                                break;
                            case "F":
                                holder.sumOdrF.setText(o.getAmount()+"");
                                break;
                            case "K":
                                holder.sumOdrK.setText(o.getAmount()+"");
                                break;
                            case "T":
                                holder.sumOdrT.setText(o.getAmount()+"");
                                break;
                            case "L":
                                holder.sumOdrL.setText(o.getAmount()+"");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }


    }
/*                     <deliveryPoints>
                        <deliveryPoint>
                            <odr>true</odr>
                            <odrRecipients>
                                <odrRecipient>
                                    <amount>2</amount>
                                    <type>F</type>
                                </odrRecipient>
                                <odrRecipient>
                                    <amount>2</amount>
                                    <type>V</type>
                                </odrRecipient>
                            </odrRecipients>
                            <residents>
                                <resident>
                                    <firstname>Kalle</firstname>
                                    <lastname>Anka</lastname>
                                </resident>
                            </residents>
                        </deliveryPoint>
                    </deliveryPoints>
*/
}