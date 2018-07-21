package com.lokakito.busbooking;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;
import com.s1creative.ecomplainadmin.ListView.ListViewHistory;

@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) 
public class Histori extends Activity {

	public static String AR_KODE = "Id_Tr";
	public static String AR_KOTA_ASAL = "Kota_Asal";
	public static String AR_KOTA_TUJUAN = "Kota_Tujuan";
	public static String AR_TANGGAL = "Tanggal_Pemesanan";
	public static String AR_STATUS = "Status_Pesanan";
	public static String Tujuan_Kota = "Tujuan_Kota";
    Alamat alamatlink;

	//String url="http://lokakito.com/projek/android/ican-uin/histori-list.php";

	SQLiteDatabase db=null;

	JSONArray artikel = null;
	ListViewHistory adapter;
	ListView list;
	String kodeid;
    ArrayList<HashMap<String, String>> histori_pesanan = new ArrayList<HashMap<String, String>>();

	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histori);  

        db=openOrCreateDatabase("dbsesi",MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select * from tb_akun where id='1'", null);
        c.moveToFirst();

        kodeid = c.getString(1).toString().trim();
		 
        //mengecek versi android untuk membuka koneksi internet
        if (android.os.Build.VERSION.SDK_INT > 9)
	 {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }
		
        //mempersiapkan koneksi JSON
        alamatlink = new Alamat(kodeid);
        String link_url = alamatlink.getAlamat()+alamatlink.getHistoriList();
        
        JSONParser jParser = new JSONParser();
        final JSONObject json = jParser.AmbilJson(link_url);
         	
        	//memasukkan data dari file JSON ke penampungan
        	try {
       		 //mempersiapkan koneksi JSON	               
               artikel = json.getJSONArray("Histori");

               for(int i = 0; i < artikel.length(); i++){
                   JSONObject ar = artikel.getJSONObject(i);

                   String id = ar.getString(AR_KODE);
                   String kota_asal = ar.getString(AR_KOTA_ASAL);
                   String kota_tujuan = ar.getString(AR_KOTA_TUJUAN);
                   String tanggal = ar.getString(AR_TANGGAL);
                   String status = ar.getString(AR_STATUS);
                   String Tujuan = ar.getString(Tujuan_Kota);

                   HashMap<String, String> map = new HashMap<String, String>();

                   map.put(AR_KODE, id);
                   map.put(AR_KOTA_ASAL, kota_asal);
                   map.put(AR_KOTA_TUJUAN, kota_tujuan);
                   map.put(AR_TANGGAL, tanggal);
                   map.put(AR_STATUS, status);
                   map.put(Tujuan_Kota, Tujuan);

                   histori_pesanan.add(map);
               }
           } 
       	catch (JSONException e) {
               e.printStackTrace();
           }
           

        list=(ListView)findViewById(R.id.histori);
        adapter=new ListViewHistory(this, histori_pesanan); 
        list.setAdapter(adapter);
        
        
        list=(ListView)findViewById(R.id.histori);
        list.setOnItemClickListener(new OnItemClickListener() {
        	
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            String kode = ((TextView) view.findViewById(R.id.kode)).getText().toString();
                //mempersiapkan untuk membuka activity baru berdasarkan ID Kategori
            Intent in = new Intent(Histori.this, Detail_Histori.class);
            in.putExtra(AR_KODE, kode);
            startActivity(in);

            }

        });
	}
}
