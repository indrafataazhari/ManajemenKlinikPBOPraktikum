package klinik.view;

import klinik.controller.KlinikController;
import klinik.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

public class KlinikApp extends JFrame {

    private KlinikController controller = new KlinikController();
    private final String DATA_FOLDER = "data_clinic"; // folder untuk CSV

    // Table models
    private DefaultTableModel modelPasien;
    private DefaultTableModel modelDokter;
    private DefaultTableModel modelJadwal;
    private DefaultTableModel modelRekam;

    public KlinikApp() {
        setTitle("Sistem Manajemen Klinik - Lengkap");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Pasien", panelPasien());
        tabbedPane.add("Dokter", panelDokter());
        tabbedPane.add("Jadwal", panelJadwal());
        tabbedPane.add("Rekam Medis", panelRekam());

        // menu bar for save/load
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save All");
        JMenuItem load = new JMenuItem("Load All");
        file.add(save); file.add(load);
        mb.add(file);
        setJMenuBar(mb);

        save.addActionListener(e -> {
            try {
                controller.saveAllToCSV(DATA_FOLDER);
                JOptionPane.showMessageDialog(this, "Data tersimpan di folder: " + DATA_FOLDER);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage());
            }
        });

        load.addActionListener(e -> {
            try {
                controller.loadAllFromCSV(DATA_FOLDER);
                refreshAllTables();
                JOptionPane.showMessageDialog(this, "Data berhasil dimuat dari folder: " + DATA_FOLDER);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal load: " + ex.getMessage());
            }
        });

        add(tabbedPane);
       
        File fdir = new File(DATA_FOLDER);
        if (fdir.exists()) {
            try { controller.loadAllFromCSV(DATA_FOLDER); } catch (Exception ignored) {}
        }
    }

    // --------------------- Panel Pasien ---------------------
    private JPanel panelPasien() {
        JPanel p = new JPanel(new BorderLayout());

        // top form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Nama:"), c);
        c.gridx = 1; JTextField txtNama = new JTextField(20); form.add(txtNama, c);
        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Umur:"), c);
        c.gridx = 1; JTextField txtUmur = new JTextField(5); form.add(txtUmur, c);
        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Alamat:"), c);
        c.gridx = 1; JTextField txtAlamat = new JTextField(20); form.add(txtAlamat, c);

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnCari = new JButton("Cari (nama)");
        JButton btnRefresh = new JButton("Refresh");

        JPanel btns = new JPanel();
        btns.add(btnTambah); btns.add(btnEdit); btns.add(btnHapus); btns.add(btnCari); btns.add(btnRefresh);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; form.add(btns, c);

        p.add(form, BorderLayout.NORTH);

        // table
        modelPasien = new DefaultTableModel(new String[]{"ID","Nama","Umur","Alamat"}, 0);
        JTable tabel = new JTable(modelPasien);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);

        // actions
        btnTambah.addActionListener(e -> {
            String id = controller.generateIdPasien();
            String nama = txtNama.getText().trim();
            String umurS = txtUmur.getText().trim();
            String alamat = txtAlamat.getText().trim();
            if (nama.isEmpty() || umurS.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi nama & umur!"); return; }
            try {
                int umur = Integer.parseInt(umurS);
                Pasien p1 = new Pasien(id, nama, umur, alamat);
                controller.tambahPasien(p1);
                modelPasien.addRow(new Object[]{id, nama, umur, alamat});
                txtNama.setText(""); txtUmur.setText(""); txtAlamat.setText("");
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this,"Umur harus angka"); }
        });

        btnEdit.addActionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelPasien.getValueAt(row,0);
            String nama = txtNama.getText().trim();
            String umurS = txtUmur.getText().trim();
            String alamat = txtAlamat.getText().trim();
            if (nama.isEmpty() || umurS.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi nama & umur!"); return; }
            try {
                int umur = Integer.parseInt(umurS);
                controller.updatePasien(id, nama, umur, alamat);
                modelPasien.setValueAt(nama, row, 1);
                modelPasien.setValueAt(umur, row, 2);
                modelPasien.setValueAt(alamat, row, 3);
                txtNama.setText(""); txtUmur.setText(""); txtAlamat.setText("");
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this,"Umur harus angka"); }
        });

        btnHapus.addActionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelPasien.getValueAt(row,0);
            if (JOptionPane.showConfirmDialog(this, "Hapus pasien " + id + " ?") == JOptionPane.YES_OPTION) {
                controller.hapusPasien(id);
                modelPasien.removeRow(row);
            }
        });

        btnCari.addActionListener(e -> {
            String key = JOptionPane.showInputDialog(this, "Masukkan nama atau bagian nama:");
            if (key == null) return;
            List<Pasien> res = controller.cariPasienByNama(key);
            modelPasien.setRowCount(0);
            for (Pasien px : res) modelPasien.addRow(new Object[]{px.getId(), px.getNama(), px.getUmur(), px.getAlamat()});
        });

        btnRefresh.addActionListener(e -> refreshPasienTable());

        
        tabel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount()==2) {
                    int r = tabel.getSelectedRow();
                    if (r != -1) {
                        txtNama.setText((String)modelPasien.getValueAt(r,1));
                        txtUmur.setText(String.valueOf(modelPasien.getValueAt(r,2)));
                        txtAlamat.setText((String)modelPasien.getValueAt(r,3));
                    }
                }
            }
        });

        
        refreshPasienTable();
        return p;
    }

    // --------------------- Panel Dokter ---------------------
    private JPanel panelDokter() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6);
        c.gridx=0; c.gridy=0; form.add(new JLabel("Nama:"),c);
        c.gridx=1; JTextField txtNama = new JTextField(20); form.add(txtNama,c);
        c.gridx=0; c.gridy=1; form.add(new JLabel("Umur:"),c);
        c.gridx=1; JTextField txtUmur = new JTextField(5); form.add(txtUmur,c);
        c.gridx=0; c.gridy=2; form.add(new JLabel("Spesialis:"),c);
        c.gridx=1; JTextField txtSpes = new JTextField(15); form.add(txtSpes,c);
        c.gridx=0; c.gridy=3; form.add(new JLabel("Sertifikasi (opsional):"),c);
        c.gridx=1; JTextField txtSert = new JTextField(15); form.add(txtSert,c);

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnCari = new JButton("Cari (nama)");
        JButton btnRefresh = new JButton("Refresh");
        JPanel btns = new JPanel(); btns.add(btnTambah); btns.add(btnEdit); btns.add(btnHapus); btns.add(btnCari); btns.add(btnRefresh);
        c.gridx=0; c.gridy=4; c.gridwidth=2; form.add(btns,c);
        p.add(form, BorderLayout.NORTH);

        modelDokter = new DefaultTableModel(new String[]{"ID","Nama","Umur","Spesialis","Sertifikasi"},0);
        JTable tabel = new JTable(modelDokter);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> {
            String id = controller.generateIdDokter();
            String nama = txtNama.getText().trim();
            String umurS = txtUmur.getText().trim();
            String spes = txtSpes.getText().trim();
            String sert = txtSert.getText().trim();
            if (nama.isEmpty() || umurS.isEmpty() ) { JOptionPane.showMessageDialog(this,"Isi nama & umur!"); return; }
            try {
                int umur = Integer.parseInt(umurS);
                Dokter d;
                if (!sert.isEmpty()) d = new DokterSpesialis(id,nama,umur,spes,sert);
                else d = new Dokter(id,nama,umur,spes);
                controller.tambahDokter(d);
                modelDokter.addRow(new Object[]{id,nama,umur,spes,sert});
                txtNama.setText(""); txtUmur.setText(""); txtSpes.setText(""); txtSert.setText("");
            } catch(NumberFormatException ex){ JOptionPane.showMessageDialog(this,"Umur harus angka"); }
        });

        btnEdit.addActionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelDokter.getValueAt(row,0);
            String nama = txtNama.getText().trim();
            String umurS = txtUmur.getText().trim();
            String spes = txtSpes.getText().trim();
            String sert = txtSert.getText().trim();
            if (nama.isEmpty() || umurS.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi nama & umur!"); return; }
            try {
                int umur = Integer.parseInt(umurS);
                controller.updateDokter(id, nama, umur, spes);
                // replace in model: if sertifikasi exists we will rebuild as DokterSpesialis in controller only for storage.
                modelDokter.setValueAt(nama, row, 1);
                modelDokter.setValueAt(umur, row, 2);
                modelDokter.setValueAt(spes, row, 3);
                modelDokter.setValueAt(sert, row, 4);
                txtNama.setText(""); txtUmur.setText(""); txtSpes.setText(""); txtSert.setText("");
            } catch(NumberFormatException ex) { JOptionPane.showMessageDialog(this,"Umur harus angka"); }
        });

        btnHapus.addActionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelDokter.getValueAt(row,0);
            if (JOptionPane.showConfirmDialog(this, "Hapus dokter " + id + " ?") == JOptionPane.YES_OPTION) {
                controller.hapusDokter(id);
                modelDokter.removeRow(row);
            }
        });

        btnCari.addActionListener(e -> {
            String key = JOptionPane.showInputDialog(this, "Masukkan nama atau bagian nama:");
            if (key == null) return;
            modelDokter.setRowCount(0);
            for (Dokter d : controller.getAllDokter()) {
                if (d.getNama().toLowerCase().contains(key.toLowerCase())) {
                    String sert = (d instanceof DokterSpesialis) ? ((DokterSpesialis)d).getSertifikasi() : "";
                    modelDokter.addRow(new Object[]{d.getId(), d.getNama(), d.getUmur(), d.getSpesialis(), sert});
                }
            }
        });

        btnRefresh.addActionListener(e -> refreshDokterTable());

        tabel.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount()==2) {
                    int r = tabel.getSelectedRow();
                    if (r!=-1) {
                        txtNama.setText((String)modelDokter.getValueAt(r,1));
                        txtUmur.setText(String.valueOf(modelDokter.getValueAt(r,2)));
                        txtSpes.setText((String)modelDokter.getValueAt(r,3));
                        txtSert.setText((String)modelDokter.getValueAt(r,4));
                    }
                }
            }
        });

        refreshDokterTable();
        return p;
    }

    // --------------------- Panel Jadwal ---------------------
    private JPanel panelJadwal() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6);
        c.gridx=0; c.gridy=0; form.add(new JLabel("Pilih Pasien (ID) :"), c);
        c.gridx=1; JComboBox<String> cbPasien = new JComboBox<>(); form.add(cbPasien, c);
        c.gridx=0; c.gridy=1; form.add(new JLabel("Pilih Dokter (ID) :"), c);
        c.gridx=1; JComboBox<String> cbDokter = new JComboBox<>(); form.add(cbDokter, c);
        c.gridx=0; c.gridy=2; form.add(new JLabel("Tanggal (YYYY-MM-DD):"), c);
        c.gridx=1; JTextField txtTanggal = new JTextField(12); form.add(txtTanggal, c);
        JButton btnTambah = new JButton("Tambah Jadwal");
        JButton btnHapus = new JButton("Hapus Jadwal");
        JButton btnRefresh = new JButton("Refresh");
        JPanel btns = new JPanel(); btns.add(btnTambah); btns.add(btnHapus); btns.add(btnRefresh);
        c.gridx=0; c.gridy=3; c.gridwidth=2; form.add(btns, c);
        p.add(form, BorderLayout.NORTH);

        modelJadwal = new DefaultTableModel(new String[]{"ID","ID Pasien","Nama Pasien","ID Dokter","Nama Dokter","Tanggal"},0);
        JTable tabel = new JTable(modelJadwal);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);

       
        Runnable populateCombos = () -> {
            cbPasien.removeAllItems();
            for (Pasien pa : controller.getAllPasien()) cbPasien.addItem(pa.getId() + " - " + pa.getNama());
            cbDokter.removeAllItems();
            for (Dokter d : controller.getAllDokter()) cbDokter.addItem(d.getId() + " - " + d.getNama());
        };

        btnTambah.addActionListener(e -> {
            if (cbPasien.getItemCount()==0 || cbDokter.getItemCount()==0) { JOptionPane.showMessageDialog(this,"Pastikan ada pasien & dokter terlebih dahulu"); return; }
            String idJ = controller.generateIdJadwal();
            String idPas = ((String)cbPasien.getSelectedItem()).split(" - ")[0];
            String idDok = ((String)cbDokter.getSelectedItem()).split(" - ")[0];
            String tanggal = txtTanggal.getText().trim();
            if (tanggal.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi tanggal!"); return; }
            JadwalPemeriksaan j = new JadwalPemeriksaan(idJ, idPas, idDok, tanggal);
            controller.tambahJadwal(j);
            // find names
            Pasien pBy = controller.cariPasienById(idPas);
            Dokter dBy = controller.cariDokterById(idDok);
            modelJadwal.addRow(new Object[]{idJ, idPas, pBy!=null? pBy.getNama() : "", idDok, dBy!=null? dBy.getNama() : "", tanggal});
            txtTanggal.setText("");
        });

        btnHapus.addActionListener(e -> {
            int r = tabel.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this,"Pilih jadwal"); return; }
            String id = (String)modelJadwal.getValueAt(r,0);
            if (JOptionPane.showConfirmDialog(this,"Hapus jadwal "+id+"?")==JOptionPane.YES_OPTION) {
                controller.hapusJadwal(id);
                modelJadwal.removeRow(r);
            }
        });

        btnRefresh.addActionListener(e -> { populateCombos.run(); refreshJadwalTable(); });

        refreshJadwalTable();
        populateCombos.run();
        return p;
    }

    // --------------------- Panel Rekam Medis ---------------------
    private JPanel panelRekam() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6);

        c.gridx=0; c.gridy=0; form.add(new JLabel("Pilih Pasien (ID):"), c);
        c.gridx=1; JComboBox<String> cbPasien = new JComboBox<>(); form.add(cbPasien, c);
        c.gridx=0; c.gridy=1; form.add(new JLabel("Diagnosis:"), c);
        c.gridx=1; JTextField txtDiag = new JTextField(25); form.add(txtDiag, c);
        JButton btnTambah = new JButton("Tambah Rekam");
        JButton btnEdit = new JButton("Edit Rekam");
        JButton btnHapus = new JButton("Hapus Rekam");
        JButton btnRefresh = new JButton("Refresh");
        JPanel btns = new JPanel(); btns.add(btnTambah); btns.add(btnEdit); btns.add(btnHapus); btns.add(btnRefresh);
        c.gridx=0; c.gridy=2; c.gridwidth=2; form.add(btns, c);
        p.add(form, BorderLayout.NORTH);

        modelRekam = new DefaultTableModel(new String[]{"ID","ID Pasien","Nama Pasien","Diagnosis"},0);
        JTable tabel = new JTable(modelRekam);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);

        Runnable populateCb = () -> {
            cbPasien.removeAllItems();
            for (Pasien pa : controller.getAllPasien()) cbPasien.addItem(pa.getId() + " - " + pa.getNama());
        };

        btnTambah.addActionListener(e -> {
            if (cbPasien.getItemCount()==0) { JOptionPane.showMessageDialog(this,"Tambahkan pasien dulu"); return; }
            String id = controller.generateIdRekam();
            String idPas = ((String)cbPasien.getSelectedItem()).split(" - ")[0];
            String diag = txtDiag.getText().trim();
            if (diag.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi diagnosis"); return; }
            RekamMedis r = new RekamMedis(id,idPas,diag);
            controller.tambahRekam(r);
            Pasien pby = controller.cariPasienById(idPas);
            modelRekam.addRow(new Object[]{id,idPas, pby!=null? pby.getNama() : "", diag});
            txtDiag.setText("");
        });

        btnEdit.addActionListener(e -> {
            int r = tabel.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelRekam.getValueAt(r,0);
            String diag = txtDiag.getText().trim();
            if (diag.isEmpty()) { JOptionPane.showMessageDialog(this,"Isi diagnosis"); return; }
            controller.updateRekam(id, diag);
            modelRekam.setValueAt(diag, r, 3);
            txtDiag.setText("");
        });

        btnHapus.addActionListener(e -> {
            int r = tabel.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this,"Pilih baris dulu"); return; }
            String id = (String) modelRekam.getValueAt(r,0);
            if (JOptionPane.showConfirmDialog(this,"Hapus rekam "+id+"?")==JOptionPane.YES_OPTION) {
                controller.hapusRekam(id);
                modelRekam.removeRow(r);
            }
        });

        btnRefresh.addActionListener(e -> { populateCb.run(); refreshRekamTable(); });

        
        tabel.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount()==2) {
                    int r = tabel.getSelectedRow();
                    if (r!=-1) {
                        txtDiag.setText((String)modelRekam.getValueAt(r,3));
                    }
                }
            }
        });

        populateCb.run();
        refreshRekamTable();
        return p;
    }

    // --------------------- Refresh helpers ---------------------
    private void refreshAllTables() {
        refreshPasienTable();
        refreshDokterTable();
        refreshJadwalTable();
        refreshRekamTable();
    }

    private void refreshPasienTable() {
        modelPasien.setRowCount(0);
        for (Pasien p : controller.getAllPasien()) modelPasien.addRow(new Object[]{p.getId(), p.getNama(), p.getUmur(), p.getAlamat()});
    }
    private void refreshDokterTable() {
        modelDokter.setRowCount(0);
        for (Dokter d : controller.getAllDokter()) {
            String sert = (d instanceof DokterSpesialis) ? ((DokterSpesialis)d).getSertifikasi() : "";
            modelDokter.addRow(new Object[]{d.getId(), d.getNama(), d.getUmur(), d.getSpesialis(), sert});
        }
    }
    private void refreshJadwalTable() {
        modelJadwal.setRowCount(0);
        for (JadwalPemeriksaan j : controller.getAllJadwal()) {
            Pasien p = controller.cariPasienById(j.getIdPasien());
            Dokter d = controller.cariDokterById(j.getIdDokter());
            modelJadwal.addRow(new Object[]{j.getId(), j.getIdPasien(), p!=null? p.getNama() : "", j.getIdDokter(), d!=null? d.getNama() : "", j.getTanggal()});
        }
    }
    private void refreshRekamTable() {
        modelRekam.setRowCount(0);
        for (RekamMedis r : controller.getAllRekam()) {
            Pasien p = controller.cariPasienById(r.getIdPasien());
            modelRekam.addRow(new Object[]{r.getId(), r.getIdPasien(), p!=null? p.getNama() : "", r.getDiagnosis()});
        }
    }

    // --------------------- MAIN ---------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KlinikApp app = new KlinikApp();
            app.setVisible(true);
        });
    }
}
