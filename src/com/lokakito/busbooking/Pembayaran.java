package com.lokakito.busbooking;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List; 
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lokakito.busbooking.GO_Boking.Masuk;
import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

   @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") 
   public class Pembayaran extends Activity implements OnItemSelectedListener{
   String pilih="";	   
   String pilih2="";	
   Spinner spinner,spinner2,spinHari,spinPenumpang,spinWaktu;
   String ket="",Hari;
   JSONParser jParser = new JSONParser();
   ProgressDialog pDialog;
   WebView web;
   String sjam="";
   String stTanggal="";
   String[] bankkirim;
   List<String> bank = new ArrayList<String>();
   JSONArray artikel = null;
   Alamat alamatlink;
   Calendar myCalendar = Calendar.getInstance();
   EditText edittext ;
   DatePickerDialog.OnDateSetListener date;
   String x="";
   int nom=0;
   TextView kodetrtampil, kodetra, bankuser, norekuser, berita;
   ImageView image;
   private int PICK_IMAGE_REQUEST = 1;
   String gambarkomplain = "kosong";
   private Bitmap bitmap;
   Button done;

   String strTR,strBANK,strNOREK,strBERITA,strBANKKIRIM;
   
   @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.pembayaran);

      bankuser = (TextView)findViewById(R.id.bank);
      norekuser = (TextView)findViewById(R.id.norek);
      berita = (TextView)findViewById(R.id.berita);
      
      done = (Button)findViewById(R.id.done);
      
      Bundle kt=getIntent().getExtras();
      ket=kt.getString("Id_Tr");
      
      kodetrtampil = (TextView)findViewById(R.id.textView3);
      kodetrtampil.setText("#"+ket);

      kodetra = (TextView)findViewById(R.id.notr);
      kodetra.setText(ket);
      
      spinner  = (Spinner) findViewById(R.id.spinner1);

      //mengecek versi android untuk membuka koneksi internet
      if (android.os.Build.VERSION.SDK_INT > 9)
	 {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }      

      alamatlink = new Alamat();
      String link_url = alamatlink.getAlamat()+alamatlink.getBank();

      //mempersiapkan koneksi JSON
      JSONParser jParser = new JSONParser();
      final JSONObject json = jParser.AmbilJson(link_url);
      
      if (cek_koneksi(this))
      {
      	
      	//memasukkan data dari file JSON ke penampungan
      	try {
     		 //mempersiapkan koneksi JSON	               
             artikel = json.getJSONArray("Bank");

             for(int i = 0; i < artikel.length(); i++){
                 JSONObject ar = artikel.getJSONObject(i);

                 String Bank = ar.getString("Nama_Bank");
                 String Norek = ar.getString("No_Rek");
                 

                 bank.add(Norek+", "+Bank);
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
      
      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bank);
      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner.setAdapter(dataAdapter);
      
      
      image = (ImageView)findViewById(R.id.imageView1);

      image.setOnClickListener(new View.OnClickListener(){

          @Override
          public void onClick(View arg0) {
          	showFileChooser();
          }
      });

      done.setOnClickListener(new android.view.View.OnClickListener() {
  		@Override
  		public void onClick(View arg0) {
  			if (bankuser.getText().toString().trim().length() == 0
  					|| norekuser.getText().toString().trim().length() == 0
  					|| berita.getText().toString().trim().length() == 0
  					|| gambarkomplain.equals("kosong")){
  				Toast.makeText(getApplicationContext(), "Lengkapi berkas dan Gambar", Toast.LENGTH_LONG).show();
  			}else{
  				new Konfirmasi().execute();
  			}
  		}
  	});
      
      
      
       
   }
   
   public String getStringImage(Bitmap bmp){
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       bmp.compress(Bitmap.CompressFormat.JPEG, 6, baos);
       byte[] imageBytes = baos.toByteArray();
       String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
       return encodedImage;
   }


   private void showFileChooser() {
           Intent intent = new Intent();
           intent.setType("image/*");
           intent.setAction(Intent.ACTION_GET_CONTENT);
           startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
           Uri filePath = data.getData();
           try {
               //Getting the Bitmap from Gallery
               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               //Setting the Bitmap to ImageView
               image.setImageBitmap(bitmap);
               gambarkomplain = "ada";

           } catch (IOException e) {
               e.printStackTrace();
           }
       }

   }
   
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String item = parent.getItemAtPosition(position).toString();
      pilih=item.toString().trim();
      bankkirim=pilih.split(",");
      //Toast.makeText(parent.getContext(), "Pilih : " + bankkirim[0], Toast.LENGTH_LONG).show();
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



public class Konfirmasi extends AsyncTask<String, String, String>
{

    String success;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();           
        pDialog = new ProgressDialog(Pembayaran.this);
        pDialog.setMessage("Proses Konfirmasi Pembayaran...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        //Converting Bitmap to String
        String image = getStringImage(bitmap);

        strTR = kodetra.getText().toString();
        strBANK = bankuser.getText().toString();
        strNOREK = norekuser.getText().toString();
        strBERITA = berita.getText().toString();
        strBANKKIRIM = bankkirim[0];

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tr", strTR));
        params.add(new BasicNameValuePair("bank", strBANK));
        params.add(new BasicNameValuePair("no_rek", strNOREK));
        params.add(new BasicNameValuePair("berita", strBERITA));
        params.add(new BasicNameValuePair("bank_kirim", strBANKKIRIM));
        params.add(new BasicNameValuePair("gambar", image));
        
        alamatlink = new Alamat();
        String link_url = alamatlink.getAlamat()+alamatlink.getPembayaran();

        JSONObject json = jParser.makeHttpRequest(link_url, "POST", params);

        try {
            success = json.getString("success");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }
    protected void onPostExecute(String file_url) {
        // mengakhiri dialog ketika proses penguploadan pertnyaan selesai
        pDialog.dismiss();

        if (success.equals("1")) 
        {
            Toast.makeText(getApplicationContext(), "Pembayaran telah diproses", Toast.LENGTH_LONG).show();
        } 
        else
        {
            Toast.makeText(getApplicationContext(), "Pembayaran gagal diproses tr="+strTR+" bank="+strBANK+" norek="+strNOREK+" berita="+strBERITA+" bankkirim="+strBANKKIRIM+" sukses="+success, Toast.LENGTH_LONG).show();

        }
    }
}

}