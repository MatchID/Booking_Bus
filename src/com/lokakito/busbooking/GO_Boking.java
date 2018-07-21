package com.lokakito.busbooking;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List; 
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lokakito.busbooking.LoginActivity.Masuk;
import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

   @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") 
   public class GO_Boking extends Activity implements OnItemSelectedListener{
   String pilih="";	   
   String pilih2="";	
   Spinner spinner,spinner2,spinHari,spinPenumpang,spinWaktu;
   String ket="",Hari;
   ProgressDialog pDialog;
   WebView web;
   String sjam="";
   String stTanggal="";
   List<String> categories = new ArrayList<String>();
   List<String> daerah2 = new ArrayList<String>();
   List<String> hr = new ArrayList<String>();
   List<String> jmlah = new ArrayList<String>();
   List<String> waktu = new ArrayList<String>();
   List<String> bangku = new ArrayList<String>();
   JSONArray artikel = null;
   Alamat alamatlink;
   Calendar myCalendar = Calendar.getInstance();
   EditText edittext ;
   DatePickerDialog.OnDateSetListener date;
   String x="";
   int nom=0;
   @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.go_booking);

      
      Bundle kt=getIntent().getExtras();
      ket=kt.getString("ket");
      
      spinner  = (Spinner) findViewById(R.id.spinner1);
      spinner2 = (Spinner) findViewById(R.id.spinner2);
      spinPenumpang = (Spinner) findViewById(R.id.spinPenumpang);
      //spinWaktu = (Spinner) findViewById(R.id.spinWaktu);

      //mengecek versi android untuk membuka koneksi internet
      if (android.os.Build.VERSION.SDK_INT > 9)
	 {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }

      edittext = (EditText) findViewById(R.id.Birthday);

      date = new DatePickerDialog.OnDateSetListener() {

          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear,
                  int dayOfMonth) {
              // TODO Auto-generated method stub
              myCalendar.set(Calendar.YEAR, year);
              myCalendar.set(Calendar.MONTH, monthOfYear);
              myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              updateLabel();
          }

      };

         edittext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    DatePickerDialog dialog = new DatePickerDialog(GO_Boking.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
			    dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
			    dialog.show();
				
			}
          });
         
      
      findViewById(R.id.button1).setOnClickListener(new android.view.View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (edittext.getText().toString().trim().length() > 0){				
				bangku.clear();
				new Masuk().execute();
			}else{
				Toast.makeText(getApplicationContext(), "Pilih Tanggal Terlebih Dahulu", Toast.LENGTH_LONG).show();
			}
		}
	});
      

      alamatlink = new Alamat();
      String link_url = alamatlink.getAlamat()+alamatlink.list;

      //mempersiapkan koneksi JSON
      JSONParser jParser = new JSONParser();
      final JSONObject json = jParser.AmbilJson(link_url);
      
      if (cek_koneksi(this))
      {
      	
      	//memasukkan data dari file JSON ke penampungan
      	try {
     		 //mempersiapkan koneksi JSON	               
             artikel = json.getJSONArray("List");

             for(int i = 0; i < artikel.length(); i++){
                 JSONObject ar = artikel.getJSONObject(i);

                 String Kota_Asal = ar.getString("Kota_Asal");
                 String Kota_Tujuan = ar.getString("Kota_Tujuan");
                 

                 categories.add(Kota_Asal);
                 daerah2.add(Kota_Tujuan);
             }        
             
             artikel = json.getJSONArray("Waktu");

             for(int i = 0; i < artikel.length(); i++){
                 JSONObject ar = artikel.getJSONObject(i);
                 
                 String Waktub = ar.getString("Waktu");
                 
                 waktu.add(Waktub);
             }
         } 
     	catch (JSONException e) {
             e.printStackTrace();
         }
      	
      }
      else
      {
      	Toast.makeText(this,"Tidak ada koneksi",Toast.LENGTH_SHORT).show();
      }
      
      
      
      spinner.setOnItemSelectedListener(this);
      
      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner.setAdapter(dataAdapter);
      
      
      //spiner 2
      spinner2.setOnItemSelectedListener(this);
      
      ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, daerah2);
      dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner2.setAdapter(dataAdapter2);      
      

      
      
      //spinhari 2
      spinPenumpang.setOnItemSelectedListener(this);

      jmlah.add("1");
      jmlah.add("2");
      jmlah.add("3");
      jmlah.add("4");
      jmlah.add("5");
      jmlah.add("6");
      jmlah.add("7");
      ArrayAdapter<String> penumpang = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jmlah);
      penumpang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinPenumpang.setAdapter(penumpang);   
      
      
      //spinhari 2
      /*
      spinWaktu.setOnItemSelectedListener(this);

      ArrayAdapter<String> waktua = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waktu);
      waktua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinWaktu.setAdapter(waktua);    
      */
      
   }
   
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String item = parent.getItemAtPosition(position).toString();
      pilih=item.toString().trim();
    //  Toast.makeText(parent.getContext(), "Pilih : " + pilih, Toast.LENGTH_LONG).show();
   }
   public void onNothingSelected(AdapterView<?> arg0) {
   }
   
	
	 //mengecek status koneksi internet perangkat
private boolean cek_koneksi(Context cek) {
		// TODO Auto-generated method stub
   	ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
   	NetworkInfo info = cm.getActiveNetworkInfo();
   	
   	if (info != null && info.isConnected())
   	{
   		return true;
   	}
   	else
   	{
   		return false;
   	}
		
	}


private void updateLabel() {
    String myFormat = "yyyy-MM-dd"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    edittext.setText(sdf.format(myCalendar.getTime()));
    stTanggal = sdf.format(myCalendar.getTime());
    
    SimpleDateFormat inFormat = new SimpleDateFormat(myFormat);
    Date date = null;
	try {
		date = inFormat.parse(sdf.format(myCalendar.getTime()));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
    String goal = outFormat.format(date);
    Hari = goal;

    //Toast.makeText(getApplicationContext(),"Hari : "+goal, Toast.LENGTH_LONG).show();
    }


public class Masuk extends AsyncTask<String, String, String> 
{

    String success, IdK, ID, NAMA, ASAL, TUJUAN, HARI, JAM, KOSONG, JUMLAH, KODEBUS, TANGGAL, MAX, No_Kursi;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();           
        pDialog = new ProgressDialog(GO_Boking.this);
        pDialog.setMessage("Silahkan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
	      String asal = spinner.getSelectedItem().toString();
	      String tujuan = spinner2.getSelectedItem().toString();
	      String penumpang = spinPenumpang.getSelectedItem().toString();
	      String tanggal = stTanggal;
	      //String jam = spinWaktu.getSelectedItem().toString();
        
        alamatlink = new Alamat(asal, tujuan, Hari, "", tanggal, penumpang);
        String link_url = alamatlink.getAlamat()+alamatlink.getCekRute();
        
        //JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.AmbilJson(link_url);

        try {
            
        		artikel = json.getJSONArray("Jadwal");

        		for(int i = 0; i < artikel.length(); i++){
        			JSONObject ar = artikel.getJSONObject(i);

                    ID = ar.getString("Id_Jadwal");
                    NAMA = ar.getString("Nama_Bus");
                    ASAL = ar.getString("Kota_Asal");
                    TUJUAN = ar.getString("Kota_Tujuan");
                    HARI = ar.getString("Hari");
                    JAM = ar.getString("Jam");
                    KOSONG = ar.getString("Kursi_Kosong");
                    JUMLAH = ar.getString("Kursi_Pilih");
                    KODEBUS = ar.getString("Kode_Bus");
                    TANGGAL = ar.getString("Tanggal_Pesanan");
                    MAX = ar.getString("Max_Penumpang");

        		}  
        		
        		artikel = json.getJSONArray("Kursi");

        		for(int i = 0; i < artikel.length(); i++){
        			JSONObject ar = artikel.getJSONObject(i);

        		      bangku.add(ar.getString("No_Kursi"));

        		}      

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(String file_url) {
        // mengakhiri dialog ketika proses penguploadan pertnyaan selesai
        pDialog.dismiss();

        if (ID.equals("null")){
        	Toast.makeText(getApplicationContext(), "Rute dan Jadwal Tidak Ditemukan", Toast.LENGTH_LONG).show();
        	 
        }else
        {
			String b=bangku.toString();
		    String[] c=b.split(",");
		    String xx=c.toString().replace("]","");
		    int aaa = c.length;
		    
			//Intent a=new Intent(GO_Boking.this, Kursi.class);
			Intent a=new Intent(GO_Boking.this, Bus.class);
            a.putExtra("Id_Jadwal", ID);
            a.putExtra("Nama_Bus", NAMA);
            a.putExtra("Kota_Asal", ASAL);
            a.putExtra("Kota_Tujuan", TUJUAN);
            a.putExtra("Hari", HARI);
            a.putExtra("Jam", JAM);
            a.putExtra("Kursi_Kosong", KOSONG);
            a.putExtra("Kursi_Pilih", JUMLAH);
            a.putExtra("Kode_Bus", KODEBUS);
            a.putExtra("Tanggal_Pesanan", TANGGAL);
            a.putExtra("Max_Penumpang", MAX);
            a.putExtra("bangku", bangku.toString());
            a.putExtra("pilih", Integer.toString(c.length));
			startActivity(a);
			//c[1].toString().replaceAll("[^A-Za-z0-9]", "")
            //Toast.makeText(getApplicationContext(), "Silahkan Pilih Bangku ", Toast.LENGTH_LONG).show();

        }
    }
}

}