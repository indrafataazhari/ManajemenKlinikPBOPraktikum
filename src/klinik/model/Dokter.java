package klinik.model;

public class Dokter extends Orang {
    protected String spesialis;

    public Dokter(String id, String nama, int umur, String spesialis) {
        super(id, nama, umur);
        this.spesialis = spesialis;
    }

    public String getSpesialis() { return spesialis; }
    public void setSpesialis(String spesialis) { this.spesialis = spesialis; }

    @Override
    public void info() {
        System.out.println("Dokter: " + nama + " spesialis: " + spesialis);
    }
}

