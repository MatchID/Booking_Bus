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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewHistory extends BaseAdapter {
    
    Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public ListViewHistory(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.list_item, null);

	        TextView AR_Kota=(TextView)vi.findViewById(R.id.judul);
	        TextView AR_Status=(TextView)vi.findViewById(R.id.status);
	        TextView AR_Kode=(TextView)vi.findViewById(R.id.kode);
	        TextView AR_Tanggal=(TextView)vi.findViewById(R.id.tanggal);
	        
	        HashMap<String, String> artikel = new HashMap<String, String>();
	        artikel = data.get(position);

	        AR_Kode.setText(artikel.get(Histori.AR_KODE));
	        AR_Tanggal.setText("Tanggal : " + artikel.get(Histori.AR_TANGGAL));
	        AR_Status.setText(artikel.get(Histori.AR_STATUS));
	        AR_Kota.setText(artikel.get(Histori.Tujuan_Kota));

	        
	        return vi;
    }
}