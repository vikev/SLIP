package net.vikev.android.plates.fragments;

import java.util.List;

import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.services.ScalesService;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Scale> scaleItems;

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
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView ID = (TextView) convertView.findViewById(R.id.ID);
        TextView Quantity = (TextView) convertView.findViewById(R.id.Quantity);
        TextView Name = (TextView) convertView.findViewById(R.id.Name);
        TextView PicLoc = (TextView) convertView.findViewById(R.id.PicLoc);

        Bitmap d;
        Scale thisScale = scaleItems.get(position);

        if (thisScale.getItem() != null) {
            d = BitmapFactory.decodeResource(activity.getResources(), R.drawable.not_found);
            int picture_ID;
            try {
                picture_ID = thisScale.getItem().getImage_location();
            } catch (Exception e) {
                picture_ID = 0;
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
            if (picture_ID == 0) {
                d = BitmapFactory.decodeResource(activity.getResources(), R.drawable.not_found);

            } else if (picture_ID == 1) {
                d = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bottle);
            } else if (picture_ID == 2) {
                d = BitmapFactory.decodeResource(activity.getResources(), R.drawable.box);
            }

            Paint temp = new Paint();
            temp.setStrokeWidth(0);
            temp.setColor(Color.WHITE);
            Bitmap tempBitmap = Bitmap.createBitmap(d.getWidth(), d.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(tempBitmap);
            double mass = ((double) thisScale.getQuantity() / (double) thisScale.getItem().getQuantity());

            c.drawRect(0, 0, d.getWidth(), (int) (d.getHeight() * (1 - mass)), temp);
            c.drawBitmap(d, 0, 0, temp);
            ID.setText(thisScale.getName());

            image.setImageBitmap(tempBitmap);
            Quantity.setText(Integer.toString((Math.min(thisScale.getQuantity(), thisScale.getItem().getQuantity()) * 100 / thisScale
                    .getItem().getQuantity())) + "%");

            Name.setText(thisScale.getItem().getName());

            return convertView;
        } else {
            ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
            d = BitmapFactory.decodeResource(activity.getResources(), R.drawable.not_found);
            Paint temp = new Paint();
            temp.setStrokeWidth(0);
            temp.setColor(Color.WHITE);
            Bitmap tempBitmap = Bitmap.createBitmap(d.getWidth(), d.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(tempBitmap);

            c.drawRect(0, 0, d.getWidth(), (d.getHeight()), temp);
            c.drawBitmap(d, 0, 0, temp);
            ID.setText(thisScale.getName());

            image.setImageBitmap(tempBitmap);
            Quantity.setText("0");

            Name.setText("");
            return convertView;

        }

    }

}
