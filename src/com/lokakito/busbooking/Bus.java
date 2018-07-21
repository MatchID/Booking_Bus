package com.lokakito.busbooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;
import com.s1creative.ecomplainadmin.ListView.BusListViewHistory;
import com.s1creative.ecomplainadmin.ListView.ListViewHistory;

@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) 
public class Bus extends Activity {

	public static String ID = "Id_Jadwal";
	public static String NAMA = "Nama_Bus";
	public static String ASAL = "Kota_Asal";
	public static String TUJUAN = "Kota_Tujuan";
	public static String HARI = "Hari";
	public static String JAM = "Jam";
	public static String KOSONG = "Kursi_Kosong";
	public static String JUMLAH = "Kursi_Pilih";
	public static String KODEBUS = "Kode_Bus";
	public static String TANGGAL = "Tanggal_Pesanan";
	public static String MAX = "Max_Penumpang";
	public static String No_Kursi = "No_Kursi";
	public static String HARGA = "Harga";
	   List<String> bangku = new ArrayList<String>();
       Alamat alamatlink;

	//String url="http://lokakito.com/projek/android/ican-uin/histori-list.php";

	SQLiteDatabase db=null;

	JSONArray artikel = null;
	BusListViewHistory adapter;
	ListView list;
	String kodeid;
    ArrayList<HashMap<String, String>> bus_detail = new ArrayList<HashMap<String, String>>();

	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus);  

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

	      Bundle kt=getIntent().getExtras();
	      String asal=kt.getString("Kota_Asal");
	      String tujuan=kt.getString("Kota_Tujuan");
	      String Hari=kt.getString("Hari");
	      String jam=kt.getString("Jam");
	      String penumpang=kt.getString("Kursi_Pilih");
	      String tanggal=kt.getString("Tanggal_Pesanan");
	      
        //mempersiapkan koneksi JSON
        alamatlink = new Alamat(asal, tujuan, Hari, jam, tanggal, penumpang);
        String link_url = alamatlink.getAlamat()+alamatlink.getCekRute();
        
        JSONParser jParser = new JSONParser();
        final JSONObject json = jParser.AmbilJson(link_url);
         	
        	//memasukkan data dari file JSON ke penampungan
        	try {
       		 //mempersiapkan koneksi JSON	               
        		artikel = json.getJSONArray("Jadwal");

        		for(int i = 0; i < artikel.length(); i++){
        			JSONObject ar = artikel.getJSONObject(i);

                    String sID = ar.getString("Id_Jadwal");
                    String sNAMA = ar.getString("Nama_Bus");
                    String sASAL = ar.getString("Kota_Asal");
                    String sTUJUAN = ar.getString("Kota_Tujuan");
                    String sHARI = ar.getString("Hari");
                    String sJAM = ar.getString("Jam");
                    String sKOSONG = ar.getString("Kursi_Kosong");
                    String sJUMLAH = ar.getString("Kursi_Pilih");
                    String sKODEBUS = ar.getString("Kode_Bus");
                    String sTANGGAL = ar.getString("Tanggal_Pesanan");
                    String sMAX = ar.getString("Max_Penumpang");
                    String sHARGA = ar.getString("Harga");

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(ID, sID);
                    map.put(NAMA, sNAMA);
                    map.put(ASAL, sASAL);
                    map.put(TUJUAN, sTUJUAN);
                    map.put(HARI, sHARI);
                    map.put(JAM, sJAM);
                    map.put(KOSONG, sKOSONG);
                    map.put(JUMLAH, sJUMLAH);
                    map.put(KODEBUS, sKODEBUS);
                    map.put(TANGGAL, sTANGGAL);
                    map.put(MAX, sMAX);
                    map.put(HARGA, sHARGA);
                    
                    bus_detail.add(map);

        		}
        		
           } 
       	catch (JSONException e) {
               e.printStackTrace();
           }
           

        	list=(ListView)findViewById(R.id.histori);
        adapter=new BusListViewHistory(this, bus_detail); 
        list.setAdapter(adapter);
        
        
        list=(ListView)findViewById(R.id.histori);
        list.setOnItemClickListener(new OnItemClickListener() {
        	
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            String sID = ((TextView) view.findViewById(R.id.idjadwal)).getText().toString();
            String sNAMA =((TextView) view.findViewById(R.id.namabus)).getText().toString();
            String sASAL = ((TextView) view.findViewById(R.id.asal)).getText().toString();
            String sTUJUAN = ((TextView) view.findViewById(R.id.tujuan)).getText().toString();
            String sHARI = ((TextView) view.findViewById(R.id.hari)).getText().toString();
            String sJAM = ((TextView) view.findViewById(R.id.jam)).getText().toString();
            //String sKOSONG = ((TextView) view.findViewById(R.id.kode)).getText().toString();
            String sKOSONG = "null";
            String sJUMLAH = ((TextView) view.findViewById(R.id.jumlah)).getText().toString();
            String sKODEBUS = ((TextView) view.findViewById(R.id.kodebus)).getText().toString();
            String sTANGGAL = ((TextView) view.findViewById(R.id.tanggala)).getText().toString();
            String sMAX = ((TextView) view.findViewById(R.id.max)).getText().toString();
            String sKURSI = ((TextView) view.findViewById(R.id.kursi)).getText().toString();
            String sHARGA = ((TextView) view.findViewById(R.id.harga)).getText().toString();
                //mempersiapkan untuk membuka activity baru berdasarkan ID Kategori
			String b=sKURSI.toString();
		    String[] c=b.split(",");

            //Toast.makeText(getApplicationContext(), sID+" = "+sNAMA+" = "+sASAL+" = "+sTUJUAN+" = "+sHARI+" = "+sJAM+" = "+sJUMLAH+" = "+sKODEBUS+" = "+sTANGGAL+" = " + sMAX+" = "+sKURSI , Toast.LENGTH_LONG).show();

            db.execSQL("update tb_tr_booking set " +
                    "Id_Jadwal='"+ sID +"', " +
            		"Nama_Bus='"+ sNAMA +"', " +
                    "Kota_Asal='"+ sASAL +"', " +
            		"Kota_Tujuan='"+ sTUJUAN +"', " +
                    "Hari='"+ sHARI +"', " +
            		"Jam='"+ sJAM +"', " +
                    "Kursi_Kosong='"+ sKOSONG +"', " +
                    "Kursi_Pilih='"+ sJUMLAH +"', " +
            		"Kode_Bus='"+ sKODEBUS +"', " +
                    "Tanggal_Pesanan='"+ sTANGGAL +"', " +
            		"Max_Penumpang='"+ sMAX +"', " +
                    "bangku='"+ sKURSI +"', " +
            		"pilih='"+ c.length +"', " +
            		"harga='"+ sHARGA +"' " +
                    "where id='1'");
            
            Intent in = new Intent(Bus.this, SeatSelectionActivityNew.class);
            startActivity(in);
            Toast.makeText(getApplicationContext(), "Silahkan Pilih Bangku ", Toast.LENGTH_LONG).show();

            }

        });
	}
}
