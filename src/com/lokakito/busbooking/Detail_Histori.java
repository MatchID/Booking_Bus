package com.lokakito.busbooking;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lokakito.busbooking.GO_Boking.Masuk;
import com.s1creative.ecomplainadmin.JSON.Alamat;
import com.s1creative.ecomplainadmin.JSON.JSONParser;
import com.s1creative.ecomplainadmin.ListView.ListViewBank;
import com.s1creative.ecomplainadmin.ListView.ListViewHistory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Detail_Histori extends Activity {

	public static String Id_Tr = "Id_Tr";
	public static String Id_Jadwal = "Id_Jadwal";
	public static String Jumlah_Kursi = "Jumlah_Kursi";
	public static String Tanggal_Pesan = "Tanggal_Pesan";
	public static String Jam_Pesan = "Jam_Pesan";
	public static String Tanggal_Pemesanan = "Tanggal_Pemesanan";
	public static String Status_Pesanan = "Status_Pesanan";
	public static String Id_Rute = "Id_Rute";
	public static String Id_Bus = "Id_Bus";
	public static String Kota_Asal = "Kota_Asal";
	public static String Kota_Tujuan = "Kota_Tujuan";
	public static String Harga = "Harga";
	public static String Max_Penumpang = "Max_Penumpang";
	public static String Hari = "Hari";
	public static String Jam = "Jam";
	public static String Nama_Bus = "Nama_Bus";
	public static String No_Kursi = "No_Kursi";
	public static String NAMA = "Nama_Pemilik";
	public static String NOREK = "No_Rek";
	public static String GAMBAR = "Singkatan";
    Alamat alamatlink;

	String kode,statusa,kodetr;
	//String url="http://lokakito.com/projek/android/ican-uin/histori-detail.php";

	SQLiteDatabase db=null;

	JSONArray artikel = null;
	ListViewBank adapter;
	ListView list;
	String kodeid;
    ArrayList<HashMap<String, String>> bank = new ArrayList<HashMap<String, String>>();
    
    Button proses;

	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_histori); 

	      Bundle kt=getIntent().getExtras();
	      kode=kt.getString(Id_Tr);

	      proses = (Button)findViewById(R.id.proses);
         	list=(ListView)findViewById(R.id.histori);
		 
        //mengecek versi android untuk membuka koneksi internet
        if (android.os.Build.VERSION.SDK_INT > 9)
	 {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	 }
		
        //mempersiapkan koneksi JSON
        alamatlink = new Alamat(kode);
        String link_url = alamatlink.getAlamat()+alamatlink.getHistoriDetail();
        
        JSONParser jParser = new JSONParser();
        final JSONObject json = jParser.AmbilJson(link_url);
         	
        	//memasukkan data dari file JSON ke penampungan
        	try {
       		 //mempersiapkan koneksi JSON	               
               artikel = json.getJSONArray("Histori");

               for(int i = 0; i < artikel.length(); i++){
                   JSONObject ar = artikel.getJSONObject(i);

                   String IdTr = ar.getString(Id_Tr);
                   String IdJadwal = ar.getString(Id_Jadwal);
                   String JumlahKursi = ar.getString(Jumlah_Kursi);
                   String TanggalPesan = ar.getString(Tanggal_Pesan);
                   String JamPesan = ar.getString(Jam_Pesan);
                   String TanggalPemesanan = ar.getString(Tanggal_Pemesanan);
                   String StatusPesanan = ar.getString(Status_Pesanan);
                   String IdRute = ar.getString(Id_Rute);
                   String IdBus = ar.getString(Id_Bus);
                   String KotaAsal = ar.getString(Kota_Asal);
                   String KotaTujuan = ar.getString(Kota_Tujuan);
                   String HargaA = ar.getString(Harga);
                   String MaxPenumpang = ar.getString(Max_Penumpang);
                   String HariA = ar.getString(Hari);
                   String JamA = ar.getString(Jam);
                   String NamaBus = ar.getString(Nama_Bus);
                   
                   TextView namabus = (TextView)findViewById(R.id.bus);
                   TextView hari = (TextView)findViewById(R.id.hari);
                   TextView kota = (TextView)findViewById(R.id.kota);
                   TextView idtr = (TextView)findViewById(R.id.idtr);
                   TextView status = (TextView)findViewById(R.id.status);
                   TextView tanggal = (TextView)findViewById(R.id.tanggal);
                   TextView jam = (TextView)findViewById(R.id.jam);
                   TextView jumlahkursi = (TextView)findViewById(R.id.jumlahkursi);
                   TextView totalh = (TextView)findViewById(R.id.tot);
                   TextView kodeid = (TextView)findViewById(R.id.kodeid);
                   TextView pemberitahuan = (TextView)findViewById(R.id.pemberitahuan);
                   
                   kodetr = IdTr;
                   
                   if(StatusPesanan.equals("B")){
                	   statusa = "Menunggu Pembayaran";
                   }else if(StatusPesanan.equals("C")){
                	   statusa = "Batal";
                	   list.setVisibility(View.INVISIBLE);
                	   proses.setVisibility(View.INVISIBLE);
                   }else if(StatusPesanan.equals("K")){
                	   statusa = "Menunggu Verifikasi";
                	   list.setVisibility(View.INVISIBLE);
                	   proses.setVisibility(View.INVISIBLE);
                   }else if(StatusPesanan.equals("T")){
                	   statusa = "Verifikasi Gagal";
                	   proses.setVisibility(View.INVISIBLE);
                	   list.setVisibility(View.INVISIBLE);
                	   pemberitahuan.setText("Maaf, Pembayaran anda tidak bisa di proses karena ada kesalahan meliputi Gambar Kurang Jelas, Jumlah Uang Kurang dan sebagainya.\nSilahkan hubungi admin di report@busboking.com untuk info lebih lanjut.");
                   }if(StatusPesanan.equals("L")){
                	   statusa = "Lunas";
                	   list.setVisibility(View.INVISIBLE);
                	   proses.setVisibility(View.INVISIBLE);
                   }
                   namabus.setText(NamaBus);
                   hari.setText(HariA);
                   kota.setText(KotaAsal + " -> " +KotaTujuan);
                   idtr.setText("#" + IdTr);
                   status.setText(statusa);
                   tanggal.setText(TanggalPemesanan);
                   jam.setText(JamA);
                   jumlahkursi.setText("Jumlah Kursi : " + JumlahKursi + " Buah");
                   totalh.setText("Total Harga = " + HargaA);
                   kodeid.setText(IdTr);

               }
               
               artikel = json.getJSONArray("Kursi");
               int n = 0;
               for(int i = 0; i < artikel.length(); i++){
            	   n++;
                   JSONObject ar = artikel.getJSONObject(i);

                   TextView nokursi = (TextView)findViewById(R.id.nokursi);
                   String NoKursi = ar.getString(No_Kursi);
                   if( n == 1 ){
                       nokursi.setText(NoKursi);
                   }else{
                       nokursi.setText(nokursi.getText() + ", " + NoKursi);
                   }

               }
           } 
       	catch (JSONException e) {
               e.printStackTrace();
           }
       		
               //mempersiapkan koneksi JSON
               alamatlink = new Alamat();
               link_url = alamatlink.getAlamat()+alamatlink.getBank();
               
               JSONParser jParser2 = new JSONParser();
               final JSONObject json2 = jParser2.AmbilJson(link_url);
                	
               	//memasukkan data dari file JSON ke penampungan
               	try {
              		 //mempersiapkan koneksi JSON	               
                      artikel = json2.getJSONArray("Bank");

                      for(int i = 0; i < artikel.length(); i++){
                          JSONObject ar = artikel.getJSONObject(i);

                          String NAMAa = ar.getString(NAMA);
                          String NOREKa = ar.getString(NOREK);
                          String GAMBARa = ar.getString(GAMBAR);

                          HashMap<String, String> map = new HashMap<String, String>();

                          map.put(NAMA, NAMAa);
                          map.put(NOREK, NOREKa);
                          map.put(GAMBAR, GAMBARa);

                          bank.add(map);
                      }
                  } 
              	catch (JSONException e) {
                      e.printStackTrace();
                  }
                  

               adapter=new ListViewBank(this, bank); 
               list.setAdapter(adapter);

               findViewById(R.id.proses).setOnClickListener(new android.view.View.OnClickListener() {
         		@Override
         		public void onClick(View arg0) {
        			Intent a=new Intent(Detail_Histori.this, Pembayaran.class);
                    a.putExtra("Id_Tr", kodetr);
        			startActivity(a);
         		}
         	});
           
	}
}
