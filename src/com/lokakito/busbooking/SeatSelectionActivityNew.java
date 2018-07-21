package com.lokakito.busbooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lokakito.busbooking.LoginActivity.Masuk;
import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;
import com.s1creative.ecomplainadmin.ListView.CustomGridViewAdapter;
import com.s1creative.ecomplainadmin.ListView.Item;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class SeatSelectionActivityNew extends Activity implements OnItemClickListener
{
	SQLiteDatabase db=null;
	   JSONArray artikel = null;
	   Alamat alamatlink;
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	   List<String> bangkua = new ArrayList<String>();
	CustomGridViewAdapter customGridAdapter;
	public static String Id_Tr = "Id_Tr";
	public Bitmap seatIcon;
	public Bitmap seatSelect;
	public Bitmap seatBooked;
	String ID, IDJADWAL, NAMA, ASAL, TUJUAN, HARI, JAM, KOSONG, JUMLAH, KODEBUS, TANGGAL, MAX, KURSIPILIH, bangku, pilih, cek, harga, isi_data;
	int JUMLAHKURSI, MAXP, INTKURSI=0, totharga;
	String link_url="",uid;
	Button done;
	ProgressDialog pDialog;

	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seat_selection_screen);

        db=openOrCreateDatabase("dbsesi",MODE_PRIVATE,null); 
        Cursor ca=db.rawQuery("DELETE FROM tb_kursi_bk", null);
        ca.moveToFirst();
        Cursor c=db.rawQuery("select * from tb_tr_booking where id='1'", null);
        c.moveToFirst();
        Cursor idkode=db.rawQuery("select * from tb_akun where id='1'", null);
        idkode.moveToFirst();
        

        uid = idkode.getString(1).toString().trim();
        
        ID = c.getString(1).toString().trim();
        NAMA = c.getString(2).toString().trim();
        ASAL = c.getString(3).toString().trim();
        TUJUAN = c.getString(4).toString().trim();
        HARI = c.getString(5).toString().trim();
        JAM = c.getString(6).toString().trim();
        KOSONG = c.getString(7).toString().trim();
        JUMLAH = c.getString(8).toString().trim();
        KODEBUS = c.getString(9).toString().trim();
        TANGGAL = c.getString(10).toString().trim();
        MAX = c.getString(11).toString().trim();
        bangku = c.getString(12).toString().trim();
        pilih = c.getString(13).toString().trim();
        harga = c.getString(14).toString().trim();
        
        totharga = Integer.parseInt(harga) * Integer.parseInt(JUMLAH);
        
      //mengecek versi android untuk membuka koneksi internet
        if (android.os.Build.VERSION.SDK_INT > 9)
	 {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }
        //mempersiapkan koneksi JSON    
	      alamatlink = new Alamat(ID, TANGGAL);
	      String link_url = alamatlink.getAlamat()+alamatlink.getCekKursi(); 
	      
        JSONParser jParser = new JSONParser();
        final JSONObject json = jParser.AmbilJson(link_url);
         	
        	//memasukkan data dari file JSON ke penampungan
        	try {
       		 //mempersiapkan koneksi JSON	            
        		
        		artikel = json.getJSONArray("Kursi");

        		for(int i = 0; i < artikel.length(); i++){
        			JSONObject ar = artikel.getJSONObject(i);

        		      bangkua.add(ar.getString("No_Kursi"));
        		      cek = ar.getString("No_Kursi");

        		}
        		
           } 
       	catch (JSONException e) {
               e.printStackTrace();
           }

			String b=bangkua.toString();
		    String[] cc=b.split(",");
            //Toast.makeText(getApplicationContext(), "Isi ="+ bangkua.toString() + " a =" + cc[0] + " b =" + cc[1], Toast.LENGTH_LONG).show();
		   
	      JUMLAHKURSI = Integer.parseInt(JUMLAH);
	      MAXP = Integer.parseInt(MAX);

		seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_avl);
		seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_std);
		seatBooked = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_bkd);
		totalSeat(MAXP);

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.seatrow_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(this);
		
		if(cek.equals("0")){
		}else{
		for(int i = 0; i < cc.length; i++){
			int a = 1;
			int bs = Integer.parseInt(cc[i].replaceAll("[^A-Za-z0-9]", ""))-a;
				seatBook(bs);
		}
		}

		
		done = (Button)findViewById(R.id.doneButton);
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Cursor xx=db.rawQuery("select group_concat(id) as j from tb_kursi_bk",null);
					xx.moveToFirst();
					Cursor xxq=db.rawQuery("select count(id) as j from tb_kursi_bk",null);
					xxq.moveToFirst();
					
					if(Integer.parseInt(xxq.getString(0)) == Integer.parseInt(JUMLAH)){
			        	/*
			        	 Toast.makeText(getApplicationContext(), "Isi = "+ xx.getString(0) + 
			        	 
			        			" User ID = "+ uid + " Total Harga = " + totharga +
			        			" Jumlah data = " + xxq.getString(0) + " ID = "+ ID + 
			        			" Jumlah dipesan = "+ JUMLAH + " Tanggal Pesanan = " + TANGGAL +
			        			" Kode Bus = "+ KODEBUS, Toast.LENGTH_LONG).show();
			        	*/
						IDJADWAL = ID;
						isi_data = xx.getString(0);
						new Masuk().execute();
					}else{
						//int aa = Integer.parseInt(xxq.getString(0));
						if(Integer.parseInt(xxq.getString(0)) == 0){
							int ss = Integer.parseInt(JUMLAH);
							Toast.makeText(getApplicationContext(), "Pilih "+ss+" Kursi Anda", Toast.LENGTH_LONG).show();
						}else{
							int ss = Integer.parseInt(JUMLAH) - Integer.parseInt(xxq.getString(0));
							Toast.makeText(getApplicationContext(), "Pilih Kursi "+ss+" Lagi", Toast.LENGTH_LONG).show();
						}
					}
			}
		});
		
	}

	public void totalSeat(int n)
	{
		for (int i = 1; i <= n; ++i)
		{
			gridArray.add(new Item(seatIcon, "Kursi " + i));

		}
	}

	public void seatSelected(int pos)
	{
		gridArray.remove(pos);
		gridArray.add(pos, new Item(seatSelect, "Dipilih"));
		customGridAdapter.notifyDataSetChanged();
		int posisi = pos + 1;
		
        db.execSQL("insert into tb_kursi_bk values( '"+posisi+"')");
	}
	
	public void seatBook(int pos)
	{
		gridArray.remove(pos);
		gridArray.add(pos, new Item(seatBooked, "Terisi"));
		customGridAdapter.notifyDataSetChanged();
	}

	public void seatDeselcted(int pos)
	{

		gridArray.remove(pos);
		int i = pos + 1;
		gridArray.add(pos, new Item(seatIcon, "Bangku " + i));
		customGridAdapter.notifyDataSetChanged();
		int posisi = pos + 1;

        db.execSQL("delete from tb_kursi_bk where id='"+posisi+"'");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{

		Item item = gridArray.get(position);
		Bitmap seatcompare = item.getImage();
		
		if (seatcompare == seatIcon){
			if(INTKURSI < JUMLAHKURSI){
				seatSelected(position);
				INTKURSI = INTKURSI + 1;
				//KURSIPILIH = KURSIPILIH + "," + position;
			}else{
				
			}
		}else if (seatcompare == seatBooked){
			
		}
		else{
			seatDeselcted(position);
			INTKURSI = INTKURSI - 1;

		}
		

	}
 
    public class Masuk extends AsyncTask<String, String, String> {
		 
        String success, IdK, ID, NAMA, HP, EMAIL;
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();           
            pDialog = new ProgressDialog(SeatSelectionActivityNew.this);
            pDialog.setMessage("Silahkan Tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected String doInBackground(String... arg0) {
        	
            alamatlink = new Alamat(isi_data, uid, Integer.toString(totharga), TANGGAL, KODEBUS, IDJADWAL, JUMLAH);
            link_url = alamatlink.getAlamat()+alamatlink.getBookingData();

            JSONParser jParser = new JSONParser();
            final JSONObject json = jParser.AmbilJson(link_url);
             	
            	//memasukkan data dari file JSON ke penampungan
            	try {
           		 //mempersiapkan koneksi JSON	            
            		
            		artikel = json.getJSONArray("Proses");

            		for(int i = 0; i < artikel.length(); i++){
            			JSONObject ar = artikel.getJSONObject(i);

                          success = ar.getString("success");
                          ID = ar.getString("Id_Tr");

            		}
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // mengakhiri dialog ketika proses penguploadan pertnyaan selesai
            pDialog.dismiss();
 
            if (success.equals("1")){
				Intent a = new Intent(SeatSelectionActivityNew.this, Detail_Histori.class);
				a.putExtra(Id_Tr, ID);
				startActivity(a);
				finish();
            	Toast.makeText(getApplicationContext(), "Pemesanan Tiket Bus Berhasil", Toast.LENGTH_LONG).show();
            	 
            }else if (success.equals("01"))
            {
                Toast.makeText(getApplicationContext(), "Maaf, Kursi Telah Di Ambil"+link_url, Toast.LENGTH_LONG).show();
 
            }
        }
	}

}
