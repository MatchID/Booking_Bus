package com.s1creative.ecomplainadmin.ListView;

/**
 * @author M. Ebni Hannibal <ebni.match@gmail.com>
 * 
 * Website: http://www.ebni-match.hol.es
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.lokakito.busbooking.Bus;
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

public class BusListViewHistory extends BaseAdapter {
    
    Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public BusListViewHistory(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.bus_item, null);

	        TextView AR_Status=(TextView)vi.findViewById(R.id.status);
	        TextView AR_Harga=(TextView)vi.findViewById(R.id.harga);
	        TextView AR_Tanggal=(TextView)vi.findViewById(R.id.tanggal);
	        
            TextView sID = (TextView) vi.findViewById(R.id.idjadwal);
            TextView sNAMA =(TextView) vi.findViewById(R.id.namabus);
            TextView sASAL = (TextView) vi.findViewById(R.id.asal);
            TextView sTUJUAN = (TextView) vi.findViewById(R.id.tujuan);
            TextView sHARI = (TextView) vi.findViewById(R.id.hari);
            TextView sJAM = (TextView) vi.findViewById(R.id.jam);
            TextView sJAMTAMPIL = (TextView) vi.findViewById(R.id.jamtampil);
            //String sKOSONG = ((TextView) view.findViewById(R.id.kode)).getText().toString();
            //TextView sKOSONG = "null";
            TextView sJUMLAH = (TextView) vi.findViewById(R.id.jumlah);
            TextView sKODEBUS = (TextView) vi.findViewById(R.id.kodebus);
            TextView sTANGGAL = (TextView) vi.findViewById(R.id.tanggala);
            TextView sMAX = (TextView) vi.findViewById(R.id.max);
            TextView sKURSI = (TextView) vi.findViewById(R.id.kursi);
	        
	        HashMap<String, String> artikel = new HashMap<String, String>();
	        artikel = data.get(position);

	        AR_Tanggal.setText("Tanggal : " + artikel.get(Bus.TANGGAL));
	        sJAMTAMPIL.setText("Jam : " + artikel.get(Bus.JAM));
	        AR_Status.setText(artikel.get(Bus.HARGA));
	        AR_Harga.setText(artikel.get(Bus.HARGA));
	        sNAMA.setText(artikel.get(Bus.NAMA));
	        sID.setText(artikel.get(Bus.ID));
	        sASAL.setText(artikel.get(Bus.ASAL));
	        sTUJUAN.setText(artikel.get(Bus.TUJUAN));
	        sHARI.setText(artikel.get(Bus.HARI));
	        sJAM.setText(artikel.get(Bus.JAM));
	        sJUMLAH.setText(artikel.get(Bus.JUMLAH));
	        sKODEBUS.setText(artikel.get(Bus.KODEBUS));
	        sTANGGAL.setText(artikel.get(Bus.TANGGAL));
	        sMAX.setText(artikel.get(Bus.MAX));
	        //sKURSI.setText(artikel.get(Bus.No_Kursi));
	        
	        return vi;
    }
}