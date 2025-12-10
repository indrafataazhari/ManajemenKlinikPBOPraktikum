package klinik.model;

public class JadwalPemeriksaan {
    private String id; // unique id for schedule
    private String idPasien;
    private String idDokter;
    private String tanggal; // simple string for date, e.g. "2025-11-17"

    public JadwalPemeriksaan(String id, String idPasien, String idDokter, String tanggal) {
        this.id = id;
        this.idPasien = idPasien;
        this.idDokter = idDokter;
        this.tanggal = tanggal;
    }

    public String getId() { return id; }
    public String getIdPasien() { return idPasien; }
    public String getIdDokter() { return idDokter; }
    public String getTanggal() { return tanggal; }

    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
}

