package klinik.controller;

import klinik.model.*;
import java.util.*;
import java.io.*;

public class KlinikController {
    private ArrayList<Pasien> daftarPasien = new ArrayList<>();
    private ArrayList<Dokter> daftarDokter = new ArrayList<>();
    private ArrayList<JadwalPemeriksaan> daftarJadwal = new ArrayList<>();
    private ArrayList<RekamMedis> daftarRekam = new ArrayList<>();

    // ---------- ID helpers ----------
    private String nextId(String prefix, int next) {
        return prefix + next;
    }

    public String generateIdPasien() {
        return "P" + (daftarPasien.size() + 1);
    }
    public String generateIdDokter() { return "D" + (daftarDokter.size() + 1); }
    public String generateIdJadwal() { return "J" + (daftarJadwal.size() + 1); }
    public String generateIdRekam() { return "R" + (daftarRekam.size() + 1); }

    // ---------- Pasien CRUD ----------
    public void tambahPasien(Pasien p) { daftarPasien.add(p); }
    public void updatePasien(String id, String nama, int umur, String alamat) {
        Pasien p = cariPasienById(id);
        if (p != null) { p.setNama(nama); p.setUmur(umur); p.setAlamat(alamat); }
    }
    public void hapusPasien(String id) {
        daftarPasien.removeIf(p -> p.getId().equals(id));
        // remove related jadwal and rekam medis
        daftarJadwal.removeIf(j -> j.getIdPasien().equals(id));
        daftarRekam.removeIf(r -> r.getIdPasien().equals(id));
    }
    public Pasien cariPasienById(String id) {
        for (Pasien p : daftarPasien) if (p.getId().equals(id)) return p;
        return null;
    }
    public List<Pasien> cariPasienByNama(String nama){
        List<Pasien> res = new ArrayList<>();
        for(Pasien p: daftarPasien) if(p.getNama().toLowerCase().contains(nama.toLowerCase())) res.add(p);
        return res;
    }
    public List<Pasien> getAllPasien(){ return daftarPasien; }

    // ---------- Dokter CRUD ----------
    public void tambahDokter(Dokter d) { daftarDokter.add(d); }
    public void updateDokter(String id, String nama, int umur, String spesialis) {
        Dokter d = cariDokterById(id);
        if (d != null) { d.setNama(nama); d.setUmur(umur); d.setSpesialis(spesialis); }
    }
    public void hapusDokter(String id) {
        daftarDokter.removeIf(d -> d.getId().equals(id));
        daftarJadwal.removeIf(j -> j.getIdDokter().equals(id));
    }
    public Dokter cariDokterById(String id) {
        for (Dokter d : daftarDokter) if (d.getId().equals(id)) return d;
        return null;
    }
    public List<Dokter> getAllDokter(){ return daftarDokter; }

    // ---------- Jadwal ----------
    public void tambahJadwal(JadwalPemeriksaan j) { daftarJadwal.add(j); }
    public void hapusJadwal(String id) { daftarJadwal.removeIf(j -> j.getId().equals(id)); }
    public List<JadwalPemeriksaan> getAllJadwal(){ return daftarJadwal; }

    // ---------- Rekam Medis ----------
    public void tambahRekam(RekamMedis r) { daftarRekam.add(r); }
    public void updateRekam(String id, String diagnosa) {
        for (RekamMedis r : daftarRekam) if (r.getId().equals(id)) { r.setDiagnosis(diagnosa); break; }
    }
    public void hapusRekam(String id) { daftarRekam.removeIf(r -> r.getId().equals(id)); }
    public List<RekamMedis> getAllRekam(){ return daftarRekam; }
    public List<RekamMedis> cariRekamByPasien(String idPasien){
        List<RekamMedis> res = new ArrayList<>();
        for(RekamMedis r: daftarRekam) if(r.getIdPasien().equals(idPasien)) res.add(r);
        return res;
    }

  
    public void saveAllToCSV(String folderPath) throws IOException {
        File fdir = new File(folderPath);
        if (!fdir.exists()) fdir.mkdirs();

        // pasien.csv : id,nama,umur,alamat
        try (PrintWriter pw = new PrintWriter(new File(folderPath, "pasien.csv"))) {
            for (Pasien p : daftarPasien) {
                pw.println(String.join(",", escape(p.getId()), escape(p.getNama()), String.valueOf(p.getUmur()), escape(p.getAlamat())));
            }
        }

        // dokter.csv : id,nama,umur,spesialis,sertifikasi(if any)
        try (PrintWriter pw = new PrintWriter(new File(folderPath, "dokter.csv"))) {
            for (Dokter d : daftarDokter) {
                String sert = (d instanceof klinik.model.DokterSpesialis) ? ((klinik.model.DokterSpesialis)d).getSertifikasi() : "";
                pw.println(String.join(",", escape(d.getId()), escape(d.getNama()), String.valueOf(d.getUmur()), escape(d.getSpesialis()), escape(sert)));
            }
        }

        // jadwal.csv : id,idPasien,idDokter,tanggal
        try (PrintWriter pw = new PrintWriter(new File(folderPath, "jadwal.csv"))) {
            for (JadwalPemeriksaan j : daftarJadwal) {
                pw.println(String.join(",", escape(j.getId()), escape(j.getIdPasien()), escape(j.getIdDokter()), escape(j.getTanggal())));
            }
        }

        // rekam.csv : id,idPasien,diagnosis
        try (PrintWriter pw = new PrintWriter(new File(folderPath, "rekam.csv"))) {
            for (RekamMedis r : daftarRekam) {
                pw.println(String.join(",", escape(r.getId()), escape(r.getIdPasien()), escape(r.getDiagnosis())));
            }
        }
    }

    public void loadAllFromCSV(String folderPath) throws IOException {
        daftarPasien.clear(); daftarDokter.clear(); daftarJadwal.clear(); daftarRekam.clear();
        File fdir = new File(folderPath);
        if (!fdir.exists()) return;

        File fPasien = new File(folderPath, "pasien.csv");
        if (fPasien.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fPasien))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = splitCsv(line,4);
                    if (a.length >= 4) daftarPasien.add(new Pasien(a[0], a[1], Integer.parseInt(a[2]), a[3]));
                }
            }
        }

        File fDokter = new File(folderPath, "dokter.csv");
        if (fDokter.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fDokter))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = splitCsv(line,5);
                    if (a.length >= 5) {
                        String id = a[0], nama = a[1], umur = a[2], spes = a[3], sert = a[4];
                        if (sert != null && !sert.isEmpty()) daftarDokter.add(new klinik.model.DokterSpesialis(id,nama,Integer.parseInt(umur),spes,sert));
                        else daftarDokter.add(new Dokter(id,nama,Integer.parseInt(umur),spes));
                    }
                }
            }
        }

        File fJadwal = new File(folderPath, "jadwal.csv");
        if (fJadwal.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fJadwal))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = splitCsv(line,4);
                    if (a.length >= 4) daftarJadwal.add(new JadwalPemeriksaan(a[0], a[1], a[2], a[3]));
                }
            }
        }

        File fRekam = new File(folderPath, "rekam.csv");
        if (fRekam.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fRekam))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] a = splitCsv(line,3);
                    if (a.length >= 3) daftarRekam.add(new RekamMedis(a[0], a[1], a[2]));
                }
            }
        }
    }

    
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace(",", " ");
    }

    private String[] splitCsv(String line, int expected) {
        
        return line.split(",", -1);
    }
}
