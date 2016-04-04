package se.jolo.prototypenavigator.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.OdrRecipient;
import se.jolo.prototypenavigator.model.StopPointItem;

/*The StopPointViewAdapter class sets up a list with StopPointItems*/
public class StopItemViewAdapter extends RecyclerView.Adapter<StopItemViewAdapter.ViewHolder> {

    private Point point;
    private Activity activity;
    private final List<StopPointItem> stopPointItems;
    private final CollapsingToolbarLayout collapsingToolbarLayout;

    public StopItemViewAdapter(List<StopPointItem> stopPointItems, CollapsingToolbarLayout collapsingToolbarLayout,Activity activity) {
        this.stopPointItems = stopPointItems;
        this.collapsingToolbarLayout=collapsingToolbarLayout;
        this.activity = activity;
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
        holder.idView.setText(StringUtils.capitalize(stopPointItems.get(position).getName().toLowerCase()));
        odrChecker(holder.stopPointItem,holder);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsingToolbarLayout.setTitle(StringUtils.capitalize(holder.stopPointItem.getName().toLowerCase()));
                showPopup(activity, holder.stopPointItem, v.getPivotY(), v.getPivotX());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stopPointItems.size();
    }

    /*Setting up the popup*/
    private void showPopup(final Activity activity, StopPointItem stopPointItem,float y,float x) {
        point = new Point();
        point.set((int) x, (int) y);
        // Inflate the popup_layout.xml
        //LinearLayout viewGroup = (LinearLayout) activity.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout,null);
        TextView textView = (TextView) layout.findViewById(R.id.popupText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setMaxHeight(700);

        // Setting text in popup
        textView.setText(stopPointItem.toString());

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(activity);
       // popup.setAnimationStyle(R.style.animationName);
        popup.setContentView(layout);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location.
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        //Getting screen size.
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = (int)(size.y*0.8);

        popup.update(0, 0,width,height);

        // Getting a reference to Close button, and close the popup when clicked.
        final ImageView close = (ImageView) layout.findViewById(R.id.close);
        Log.d("popup", popup.isShowing() + "");

        final Animation animation = AnimationUtils.loadAnimation(activity, R.anim.rotate_around_center_point);
        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(animation);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.clearAnimation();
                        popup.dismiss();
                        break;
                }
                return true;
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView idView;
        public final TextView sumOdrH;
        public final TextView sumOdrV;
        public final TextView sumOdrF;
        public final TextView sumOdrK;
        public final TextView sumOdrT;
        public final TextView sumOdrL;
        public StopPointItem stopPointItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            idView = (TextView) view.findViewById(R.id.id);
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
}