package com.s1creative.ecomplainadmin.ListView;

/**
 * @author M. Ebni Hannibal <ebni.match@gmail.com>
 * 
 * Website: http://www.ebni-match.hol.es
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.lokakito.busbooking.Detail_Histori;
import com.lokakito.busbooking.Histori;
import com.lokakito.busbooking.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewBank extends BaseAdapter {
    
    Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    String gambar;
    
    public ListViewBank(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {
		} inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.bank_list, null);

	        TextView AR_Nama=(TextView)vi.findViewById(R.id.nama);
	        TextView AR_NoRek=(TextView)vi.findViewById(R.id.norek);
	        ImageView AR_Gambar=(ImageView)vi.findViewById(R.id.gambar);
	        
	        HashMap<String, String> artikel = new HashMap<String, String>();
	        artikel = data.get(position);

	        AR_Nama.setText(artikel.get(Detail_Histori.NAMA));
	        AR_NoRek.setText(artikel.get(Detail_Histori.NOREK));
	        if (artikel.get(Detail_Histori.GAMBAR).equals("bni")){
		        AR_Gambar.setImageResource(R.drawable.bni);
	        }else if (artikel.get(Detail_Histori.GAMBAR).equals("mandiri")){
		        AR_Gambar.setImageResource(R.drawable.mandiri2);
	        }

	        
	        return vi;
    }
}