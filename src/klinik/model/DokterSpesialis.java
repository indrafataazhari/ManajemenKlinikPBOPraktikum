package klinik.model;

public class DokterSpesialis extends Dokter {
    private String sertifikasi;

    public DokterSpesialis(String id, String nama, int umur, String spesialis, String sertifikasi) {
        super(id, nama, umur, spesialis);
        this.sertifikasi = sertifikasi;
    }

    public String getSertifikasi() { return sertifikasi; }
    public void setSertifikasi(String sertifikasi) { this.sertifikasi = sertifikasi; }

    @Override
    public void info() {
        System.out.println("Dokter Spesialis: " + nama + " | " + spesialis + " | Sert: " + sertifikasi);
    }
}

