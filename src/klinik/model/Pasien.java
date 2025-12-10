package klinik.model;

public class Pasien extends Orang {
    private String alamat;

    public Pasien(String id, String nama, int umur, String alamat) {
        super(id, nama, umur);
        this.alamat = alamat;
    }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public void info() {
        System.out.println("Pasien: " + nama + " umur: " + umur + " alamat: " + alamat);
    }
}

