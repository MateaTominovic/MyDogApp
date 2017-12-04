package com.sbgmail.mateatominovic.mydogapp.activities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.HomeActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogMetaData;
import com.sbgmail.mateatominovic.mydogapp.activities.utility.UtilTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Enigma on 10.9.2017..
 */
public class ListViewAdapter extends BaseAdapter {
    ArrayList<DogMetaData> dataArrayList;
    int type;
    Context context;

    //konstruktor - nacrt za objekt
    public ListViewAdapter(ArrayList<DogMetaData> dataArrayList, int type, Context context) {
        this.dataArrayList = dataArrayList; // podaci
        this.type = type; // prema tipu cemo znati koju sliku treba staviti
        this.context = context; // sadrzi podatke o aktivitiju i okruznju
    }

    @Override
    public int getCount() {
        return dataArrayList.size(); // uzima size podataka u arrayu
    }

    @Override
    public Object getItem(int position) {
        return dataArrayList.get(position); // uzima jedan objekt iz liste
    }

    @Override
    public long getItemId(int position) {
        return position; // vraca poziciju
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ///////////////////////////// view holder pattern //////////////////////////////
        ViewHolder viewHolder;
        if(convertView == null){ // ako ne postoji redak u listi dio koda ispod ga kreira, okida se kod prvog pokretanja adaptera
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // dohvaca inflater
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);// kreira row ukoliko ga nema
            viewHolder = new ViewHolder(convertView); // napravi view holder
            convertView.setTag(viewHolder); // postavlja objekt kao tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); // dohvaca kreirani view holder
        }
        ///////////////////////// end ///////////////////////////////////////////
        viewHolder.tvDescription.setText(dataArrayList.get(position).getOpis()); // setanje podataka tj opisa
        viewHolder.llContainer.setBackgroundColor(setColor(UtilTime.isDone(dataArrayList.get(position).getTimestampMillis()))); // boja row u boju
        try {
            viewHolder.tvTime.setText(UtilTime.getFormatTimeFromTimeStamp(dataArrayList.get(position).getTimestamp())); // postavlja vrijeme
        } catch (ParseException e) { // caught exception i ispisuje ga u logcat-u
            e.printStackTrace();
        }

        int imageId;
        // trazi odgovarajucu sliku
        switch (dataArrayList.get(position).getType()){
            case HomeActivity.TYPE_MY_FEED:
                imageId = R.drawable.feed_dog;
                break;

            case HomeActivity.TYPE_MY_HEALTH:
                imageId = R.drawable.helth_icon;
                break;

            case HomeActivity.TYPE_MY_NOTIFICATION:
                imageId = R.drawable.notification;
                break;

            default:
                imageId = R.drawable.walk_dog;
                break;
        }

        viewHolder.ivImage.setImageResource(imageId); // postavlje ikonicu u image view
        return convertView; // vrati kreirani view
    }

    private int setColor(boolean done) {
        if(done){
            return ContextCompat.getColor(context, R.color.light_green); // ako je izvrseno boja row u zeleno
        } else {
            return ContextCompat.getColor(context, R.color.light_red); // boja u crveno ako nije izvrseno
        }
    }

    //view hodler patter - da zauzme onoliko prostora koliko ima viewa na ekranu
    private static class ViewHolder{
        LinearLayout llContainer;
        TextView tvDescription;
        TextView tvTime;
        ImageView ivImage;

        public ViewHolder(View view) {
            // radi objekte od UI elemenata
            this.tvDescription = (TextView) view.findViewById(R.id.tv_description);
            this.ivImage = (ImageView) view.findViewById(R.id.iv_image);
            this.tvTime = (TextView) view.findViewById(R.id.tv_time);
            this.llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        }
    }
}
