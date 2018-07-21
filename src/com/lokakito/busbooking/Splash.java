package com.lokakito.busbooking;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Splash extends Activity {

    SQLiteDatabase db=null;
    String kodeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
		
        db=openOrCreateDatabase("dbsesi",MODE_PRIVATE,null);
        db.execSQL("create table IF NOT EXISTS tb_kursi_bk (id TEXT)");
        Cursor ca=db.rawQuery("DELETE FROM tb_kursi_bk", null);
        ca.moveToFirst();
        db.execSQL("create table IF NOT EXISTS tb_akun (id TEXT, id_akun TEXT, nama_akun TEXT, hp TEXT, email TEXT)");
        db.execSQL("insert into tb_akun values( '1', 'null', 'null', 'null', 'null')");

        db.execSQL("create table IF NOT EXISTS tb_tr_booking (id TEXT, Id_Jadwal TEXT, Nama_Bus TEXT," +
        		" Kota_Asal TEXT, Kota_Tujuan TEXT," +
        		" Hari TEXT, Jam TEXT," +
        		" Kursi_Kosong TEXT, Kursi_Pilih TEXT," +
        		" Kode_Bus TEXT, Tanggal_Pesanan TEXT," +
        		" Max_Penumpang TEXT, bangku TEXT," +
        		" pilih TEXT, harga TEXT)");
        db.execSQL("insert into tb_tr_booking values( '1', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null', 'null')");
        
        Cursor c=db.rawQuery("select * from tb_akun where id='1'", null);
        c.moveToFirst();
        
        kodeid = c.getString(1).toString().trim();
        
        if(kodeid.equals("null")){
        	login();
        }else{
        	lanjut();
        }
	}

    private void login() {
           Intent b = new Intent(Splash.this, LoginActivity.class);
            startActivity(b);
            finish();
    }
	
    private void lanjut() {
           Intent b = new Intent(Splash.this, Utama.class);
            startActivity(b);
            finish();
    }
}
