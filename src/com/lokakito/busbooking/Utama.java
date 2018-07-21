package com.lokakito.busbooking;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

 

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
 
public class Utama extends Activity {

Button btn_booking,btn_ticket;
SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn_booking=(Button)findViewById(R.id.btn_login);
        btn_ticket=(Button)findViewById(R.id.btn_daftar);
        db=openOrCreateDatabase("dbsesi",MODE_PRIVATE,null);
        
        
        findViewById(R.id.booking).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent a=new Intent(Utama.this,GO_Boking.class);
				a.putExtra("ket","Booking");
				startActivity(a);
				
			}
		});
        findViewById(R.id.history).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent a=new Intent(Utama.this,Histori.class);
				startActivity(a);
				
			}
		}); 
        
        findViewById(R.id.logout).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent a=new Intent(Utama.this,LoginActivity.class);
                db.execSQL("update tb_akun set " +
                        "id_akun='null', " +
                		"nama_akun='', " +
                        "hp='', " +
                		"email='' " +
                        "where id='1'");
				//a.putExtra("ket","Booking");
				startActivity(a);
				finish();
				
			}
		}); 
              
    }
       
    
}




