package klinik.model;

public class RekamMedis {
    private String id; // unique record id
    private String idPasien;
    private String diagnosis;

    public RekamMedis(String id, String idPasien, String diagnosis) {
        this.id = id;
        this.idPasien = idPasien;
        this.diagnosis = diagnosis;
    }

    public String getId() { return id; }
    public String getIdPasien() { return idPasien; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}

