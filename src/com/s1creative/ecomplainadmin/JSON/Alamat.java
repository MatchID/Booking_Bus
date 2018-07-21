package com.s1creative.ecomplainadmin.JSON;

import java.net.URLEncoder;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by MatchID on 28/11/2016.
 */
public class Alamat {
	public String hosting = "http://lokakito.com/projek/android/ican-uin/"; //online
    //public String hosting = "http://192.168.1.120/lokakito.com/projek/android/ican-uin/"; //localhost, hostpot hp
    public String alamat;
    public String bank_list, pembayaran, histori_detail, histori_list, list, kursicek, cekrute, booking_data, daftar, login;

    // constructor tanpa POST
    public Alamat() {
        this.alamat = hosting;
        this.list = "list-tujuan.php";
        this.daftar = "";
        this.bank_list = "bank-list.php";
        this.pembayaran = "pembayaran.php";
    }

    // constructor untuk POST
    public Alamat(String kode) {
        this.alamat = hosting;
        this.histori_detail = "histori-detail.php?kode="+kode;
        this.histori_list = "histori-list.php?kode="+kode;
    }

    // constructor untuk POST login
    public Alamat(String id, String pass) {
        this.alamat = hosting;
        this.login = "login.php?uname="+id+"&pass="+pass;
        this.kursicek = "kursi-cek.php?idjadwal="+id+"&tanggal="+pass;
    }

    // constructor untuk POST daftar
    public Alamat(String nama, String email, String uname, String pass, String tell) {
        this.alamat = hosting;
        this.daftar = "daftar-akun.php?nama="+nama+"&uname="+uname+"&email="+email+"&telepon="+tell+"&pass="+pass;
    }

    public Alamat(String isi_data, String uid, String totharga, String TANGGAL, String KODEBUS, String ID, String JUMLAH) {
        this.alamat = hosting;
        this.booking_data = "booking-data.php?isi_data="+isi_data+"&uid="+uid+"&total_harga="+totharga+"&tanggal_pesanan="+TANGGAL+"&kode_bus="+KODEBUS+"&id_jadwal="+ID+"&jumlah="+JUMLAH;
    }

    // constructor untuk POST login
    public Alamat(String asal, String tujuan, String hari, String jam, String tanggal, String jumlah) {
        this.alamat = hosting;
        this.cekrute = "tujuan-pilih.php?asal="+URLEncoder.encode(asal)+"&tujuan="+URLEncoder.encode(tujuan)+"&hari="+hari+"&tanggal="+tanggal+"&jumlah="+jumlah;
    }


    // getting Alamat
    public String getAlamat () {
        return this.alamat;
    }

    public String getPembayaran () {
        return this.pembayaran;
    }
    
    public String getList () {
        return this.list;
    }
    
    public String getDaftar () {
        return this.daftar;
    }

    public String getLogin () {
        return this.login;
    }

    public String getCekRute () {
        return this.cekrute;
    }
    
    public String getCekKursi () {
        return this.kursicek;
    }
    
    public String getHistoriDetail () {
        return this.histori_detail;
    }
    
    public String getHistoriList () {
        return this.histori_list;
    }
    
    public String getBookingData () {
        return this.booking_data;
    }
    
    public String getBank () {
        return this.bank_list;
    }

}

