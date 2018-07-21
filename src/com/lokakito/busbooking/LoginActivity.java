package com.lokakito.busbooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") 
public class LoginActivity extends Activity {

Button btn_booking,btn_ticket;
String success,url;
String ust;

EditText Username,Password;
EditText password,username, alamat, email, telepon, nama;
JSONParser jParser = new JSONParser();
ProgressDialog pDialog;
String idakun;
public static final String AR_ID_Akun = "idakun";

Alamat alamatlink;
String url_login, urldaftar;

SQLiteDatabase db=null;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Dialog dialog = new Dialog(this);

		Username=(EditText)findViewById(R.id.Username);
		Password=(EditText)findViewById(R.id.password);
		
        db=openOrCreateDatabase("dbsesi",MODE_PRIVATE,null);

        //mengecek versi android untuk membuka koneksi internet
        if (android.os.Build.VERSION.SDK_INT > 9)
	 {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }
        
        // toombol login
       findViewById(R.id.btn_login).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (Username.getText().toString().trim().length() > 0
						&& Password.getText().toString().trim().length() > 0) 
				{
					new Masuk().execute();
				} 
				else 
				{
					Toast.makeText(getApplicationContext(), "Username/password masih kosong gan.!!", Toast.LENGTH_LONG).show();
				}
				
			}
		
	});//batas tombol login
     
       
     
       
       //tombol daftar
     findViewById(R.id.btn_daftar).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
						dialog.setContentView(R.layout.daftar_baru);
				        
				        nama = (EditText)dialog.findViewById(R.id.Nama);       
				        email = (EditText)dialog.findViewById(R.id.Email);
				        telepon = (EditText)dialog.findViewById(R.id.Telepon); 
				        username= (EditText)dialog.findViewById(R.id.Username);
				        password= (EditText)dialog.findViewById(R.id.Password);
				        
				        //untuk mengambil email playstore pengguna
				        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level minimal 8
				        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
				        for (Account account : accounts) {
				            if (emailPattern.matcher(account.name).matches()) {
				                String possibleEmail = account.name;
				                email.setText(possibleEmail);
				            }
				        }

						
						dialog.show();
						dialog.findViewById(R.id.daftar).setOnClickListener(new android.view.View.OnClickListener() {
							public void onClick(View arg0) {
								
									if (nama.getText().toString().trim().length() > 0
											&& email.getText().toString().trim().length() > 0
											&& telepon.getText().toString().trim().length() > 0
											&& username.getText().toString().trim().length() > 0
											&& password.getText().toString().trim().length() > 0) 
												{
													new input().execute();
													dialog.cancel();
												} 
												else 
												{
													Toast.makeText(getApplicationContext(), "Form masih kosong ...!!", Toast.LENGTH_LONG).show();
												}
							}
							

						    class input extends AsyncTask<String, String, String>
						    {
						    	 
						        String success;
						 
						        @Override
						        protected void onPreExecute() {
						            super.onPreExecute();           
						            pDialog = new ProgressDialog(LoginActivity.this);
						            pDialog.setMessage("Proses Pendaftarn...");
						            pDialog.setIndeterminate(false);
						            pDialog.setCancelable(false);
						            pDialog.show();
						        }
						 
						        @Override
						        protected String doInBackground(String... arg0) {
						            String strEMAIL = email.getText().toString();
						            String strPASSWORD = password.getText().toString();
						            String strNAMA = nama.getText().toString();
						            String strUNAME = username.getText().toString();
						            String strTELEPON = telepon.getText().toString();
						 
						            List<NameValuePair> params = new ArrayList<NameValuePair>();
						            params.add(new BasicNameValuePair("nama", strNAMA));
						            params.add(new BasicNameValuePair("email", strEMAIL));
						            params.add(new BasicNameValuePair("uname", strUNAME));
						            params.add(new BasicNameValuePair("pass", strPASSWORD));
						            params.add(new BasicNameValuePair("telepon", strTELEPON));

						            alamatlink = new Alamat(strNAMA, strEMAIL, strUNAME, strPASSWORD, strTELEPON);
						            String link_url_daftar = alamatlink.getAlamat()+alamatlink.getDaftar();
						            
						            //JSONObject json = jParser.makeHttpRequest(urldaftar, "POST", params);
						            JSONParser jParser = new JSONParser();
						            JSONObject json = jParser.AmbilJson(link_url_daftar);
						 
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
						            	telepon.getText().clear();
						            	nama.getText().clear();
						            	username.getText().clear();
						            	password.getText().clear();
						            	Toast.makeText(getApplicationContext(), "Link Aktivasi Akun Telah Dikirim Ke Email Yang Di Daftarkan", Toast.LENGTH_LONG).show();
						                
						            } 
						            else if (success.equals("01")) 
						            {
						                Toast.makeText(getApplicationContext(), "Email Telah Terdaftar", Toast.LENGTH_LONG).show();
						 
						            }
						            else if (success.equals("0")) 
						            {
						                Toast.makeText(getApplicationContext(), "Pendaftaran Gagal, Gangguan Server", Toast.LENGTH_LONG).show();
						 
						            }
						        }
						    }
							
							
						});
						
						dialog.findViewById(R.id.button2).setOnClickListener(new android.view.View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								dialog.cancel();
								
							}
						});			
						
						
			}
	});
       
       
              
    }
    
 
    public class Masuk extends AsyncTask<String, String, String> 
	{
		 
        String success, IdK, ID, NAMA, HP, EMAIL;
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();           
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Silahkan Tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected String doInBackground(String... arg0) {
            String strPASSWORD = Password.getText().toString();
            String strUNAME = Username.getText().toString();
 
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uname", strUNAME));
            params.add(new BasicNameValuePair("pass", strPASSWORD));
            
            alamatlink = new Alamat(strUNAME, strPASSWORD);
            String link_url = alamatlink.getAlamat()+alamatlink.getLogin();
            
            //JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.AmbilJson(link_url);
 
            try {
                success = json.getString("success");
                ID = json.getString("Id_User");
                NAMA = json.getString("Nama_User");
                HP = json.getString("Hp_User");
                EMAIL = json.getString("Email_User");
 
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // mengakhiri dialog ketika proses penguploadan pertnyaan selesai
            pDialog.dismiss();
 
            if (success.equals("1")){
                db.execSQL("update tb_akun set " +
                        "id_akun='"+ ID +"', " +
                		"nama_akun='"+ NAMA +"', " +
                        "hp='"+ HP +"', " +
                		"email='"+ EMAIL +"' " +
                        "where id='1'");

				Intent a = new Intent(LoginActivity.this, Utama.class);
				startActivity(a);
				finish();
            	Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
            	 
            }else if (success.equals("01"))
            {
                Toast.makeText(getApplicationContext(), "Login gagal, Akun anda belum aktif", Toast.LENGTH_LONG).show();
 
            }else if (success.equals("0"))
            {
                Toast.makeText(getApplicationContext(), "Login gagal, Username atau Password Salah", Toast.LENGTH_LONG).show();
 
            }
        }
	}
    
  
    
}
