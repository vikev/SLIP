package net.vikev.android.plates.fragments;
 

 
import java.util.List;

import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Scale;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
import com.android.volley.toolbox.ImageLoader;

 
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Scale> scaleItems;
 //   ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public CustomListAdapter(Activity activity, List<Scale> scaleItems) {
        this.activity = activity;
        this.scaleItems = scaleItems;
    }
 
    @Override
    public int getCount() {
        return scaleItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return scaleItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
 
   ////     if (imageLoader == null)
   //         imageLoader = AppController.getInstance().getImageLoader();
  //      NetworkImageView thumbNail = (NetworkImageView) convertView
  //              .findViewById(R.id.thumbnail);
        TextView ID = (TextView) convertView.findViewById(R.id.ID);
        TextView Quantity = (TextView) convertView.findViewById(R.id.Quantity);
        TextView Name = (TextView) convertView.findViewById(R.id.Name);
        TextView PicLoc = (TextView) convertView.findViewById(R.id.PicLoc);
 
        Scale thisScale = scaleItems.get(position);
 
        // thumbnail image
      //  thumbNail.setImageUrl(thisScale.getThumbnailUrl(), imageLoader);
         
   
        ID.setText(thisScale.getId());
         
     
        Quantity.setText(Integer.toString(thisScale.getQuantity()));
         
    
        Name.setText(thisScale.getItem().getName());
         
        // release year
       // PicLoc.setText(thisScale.getItem().getImage_location());
 
        return convertView;
    }
 
}