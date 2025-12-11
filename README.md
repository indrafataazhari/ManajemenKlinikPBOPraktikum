# 🏥 Sistem Manajemen Klinik

Aplikasi desktop berbasis Java Swing untuk mengelola administrasi klinik secara lengkap dengan fitur CRUD (Create, Read, Update, Delete) dan persistensi data menggunakan CSV.

## 📋 Deskripsi

Sistem Manajemen Klinik adalah aplikasi desktop yang dirancang untuk memudahkan pengelolaan data klinik meliputi pasien, dokter (umum dan spesialis), jadwal pemeriksaan, serta rekam medis. Aplikasi ini menggunakan arsitektur MVC (Model-View-Controller) dan dilengkapi dengan GUI berbasis Java Swing yang user-friendly.

## 📸 Screenshots

<details>
<summary>Klik untuk melihat screenshots aplikasi</summary>

### Tab Pasien
<<img width="1320" height="817" alt="image" src="https://github.com/user-attachments/assets/71806b71-e423-4f98-a12c-d80786370881" />
>

*Manajemen data pasien dengan form input dan tabel data*

### Tab Dokter
<<img width="1321" height="815" alt="image" src="https://github.com/user-attachments/assets/5f9a6130-71c8-479d-850a-2c9d9aa1a7f3" />
>

*Manajemen dokter dengan support dokter spesialis dan sertifikasi*

### Tab Jadwal Pemeriksaan
<<img width="1318" height="796" alt="image" src="https://github.com/user-attachments/assets/bc8586a0-ad67-4df8-9622-26ba76042e98" />
>

*Penjadwalan pemeriksaan dengan dropdown pasien dan dokter*

### Tab Rekam Medis
<<img width="1320" height="826" alt="image" src="https://github.com/user-attachments/assets/17161c55-c38b-4d56-9a49-06f7a5096dfc" />
>

*Pencatatan diagnosis dan riwayat medis pasien*

</details>

## ✨ Fitur Utama

### 👥 Manajemen Pasien
- Tambah, edit, dan hapus data pasien
- Pencarian pasien berdasarkan nama
- Auto-generate ID pasien (P1, P2, P3, ...)
- Menyimpan informasi: ID, nama, umur, dan alamat

### 👨‍⚕️ Manajemen Dokter
- Mendukung dokter umum dan dokter spesialis
- Manajemen sertifikasi untuk dokter spesialis
- Auto-generate ID dokter (D1, D2, D3, ...)
- Pencarian dokter berdasarkan nama
- Data tersimpan: ID, nama, umur, spesialis, dan sertifikasi

### 📅 Jadwal Pemeriksaan
- Penjadwalan pemeriksaan pasien dengan dokter
- Auto-generate ID jadwal (J1, J2, J3, ...)
- Menampilkan nama pasien dan dokter secara otomatis
- Format tanggal: YYYY-MM-DD

### 📝 Rekam Medis
- Pencatatan diagnosis pasien
- Auto-generate ID rekam medis (R1, R2, R3, ...)
- Relasi dengan data pasien
- Kemampuan edit dan hapus rekam medis

### 💾 Persistensi Data
- **Save All**: Menyimpan semua data ke file CSV
- **Load All**: Memuat data dari file CSV saat aplikasi dibuka
- Auto-load data saat aplikasi startup
- Data tersimpan di folder `data_clinic/`

## 🗂️ Struktur Project

```
ManajemenKlinik/
├── Source Packages/
│   ├── Controller/
│   │   └── KlinikController.java       # Business logic & CRUD operations
│   ├── Main/
│   │   └── KlinikApp.java              # GUI dengan Java Swing
│   ├── Model/
│   │   ├── Orang.java                  # Abstract class (parent)
│   │   ├── Dokter.java                 # Class dokter (extends Orang)
│   │   ├── DokterSpesialis.java        # Dokter spesialis (extends Dokter)
│   │   ├── Pasien.java                 # Class pasien (extends Orang)
│   │   ├── JadwalPemeriksaan.java      # Class jadwal
│   │   └── RekamMedis.java             # Class rekam medis
│   └── manajemenklinik/
├── Test Packages/
├── Libraries/
└── Test Libraries/
```

## 🏗️ Arsitektur & Design Pattern

### Arsitektur MVC (Model-View-Controller)
- **Model**: Class-class di package `Model` (Orang, Dokter, Pasien, dll)
- **View**: GUI Java Swing di `KlinikApp.java`
- **Controller**: Logic business di `KlinikController.java`

### OOP Concepts yang Digunakan
1. **Inheritance (Pewarisan)**
   - `Orang` → `Dokter` → `DokterSpesialis`
   - `Orang` → `Pasien`

2. **Abstraction**
   - Abstract class `Orang` dengan abstract method `info()`

3. **Encapsulation**
   - Private/protected attributes dengan getter/setter

4. **Polymorphism**
   - Override method `info()` di setiap class turunan

## 🛠️ Teknologi

- **Bahasa**: Java
- **GUI Framework**: Java Swing
- **IDE**: NetBeans (dapat juga menggunakan IntelliJ IDEA atau Eclipse)
- **Persistensi Data**: CSV (Comma-Separated Values)
- **Arsitektur**: MVC (Model-View-Controller)


## 📖 Cara Penggunaan

### 1. Menambah Pasien
1. Buka tab **Pasien**
2. Isi form: Nama, Umur, Alamat
3. Klik tombol **Tambah**
4. Data akan muncul di tabel

### 2. Menambah Dokter
1. Buka tab **Dokter**
2. Isi form: Nama, Umur, Spesialis
3. (Opsional) Isi Sertifikasi untuk dokter spesialis
4. Klik tombol **Tambah**

### 3. Membuat Jadwal Pemeriksaan
1. Buka tab **Jadwal**
2. Pilih Pasien dari dropdown
3. Pilih Dokter dari dropdown
4. Isi tanggal (format: YYYY-MM-DD)
5. Klik **Tambah Jadwal**

### 4. Menambah Rekam Medis
1. Buka tab **Rekam Medis**
2. Pilih Pasien dari dropdown
3. Isi Diagnosis
4. Klik **Tambah Rekam**

### 5. Menyimpan Data
- Menu **File** → **Save All**
- Data akan tersimpan di folder `data_clinic/`

### 6. Memuat Data
- Menu **File** → **Load All**
- Data otomatis dimuat saat aplikasi startup

## 💡 Fitur Tambahan

### Double-Click to Edit
- Double-click pada baris tabel untuk mengisi form dengan data tersebut
- Memudahkan proses editing data

### Auto-Refresh
- Setiap perubahan data langsung terlihat di tabel
- Tombol **Refresh** untuk reload data manual

### Pencarian
- Fitur search berdasarkan nama
- Tersedia di tab Pasien dan Dokter

### Validasi Input
- Validasi umur (harus angka)
- Validasi field kosong
- Konfirmasi sebelum hapus data

## 📊 Format Data CSV

### pasien.csv
```csv
P1,Ahmad Santoso,35,Jl. Merdeka No. 10
P2,Azfa,20,Praja Mukti
```

### dokter.csv
```csv
D1,Dr. Agus,45,Umum,
D2,Dr. Citra Dewi,38,Kardiologi,Sertifikat PERKI
```

### jadwal.csv
```csv
J1,P1,D1,2024-12-15
J2,P2,D2,2024-12-16
```

### rekam.csv
```csv
R1,P1,Hipertensi ringan - tekanan darah 140/90
R2,P2,Pemeriksaan jantung rutin - kondisi normal
```

## 📝 Contoh Kode

### Membuat Objek Pasien
```java
Pasien pasien = new Pasien("P1", "Ahmad Santoso", 35, "Jl. Merdeka No. 10");
pasien.info(); // Output: Pasien: Ahmad Santoso umur: 35 alamat: Jl. Merdeka No. 10
```

### Membuat Objek Dokter Spesialis
```java
DokterSpesialis dokter = new DokterSpesialis(
    "D1", 
    "Dr. Citra Dewi", 
    38, 
    "Kardiologi", 
    "Sertifikat PERKI"
);
dokter.info(); // Output: Dokter Spesialis: Dr. Citra Dewi | Kardiologi | Sert: Sertifikat PERKI
```

### Membuat Jadwal Pemeriksaan
```java
JadwalPemeriksaan jadwal = new JadwalPemeriksaan("J1", "P1", "D1", "2024-12-15");
```

### Membuat Rekam Medis
```java
RekamMedis rekam = new RekamMedis("R1", "P1", "Hipertensi ringan");
```

## 🔧 Pengembangan Lebih Lanjut

Beberapa ide untuk pengembangan:

- [ ] Integrasi dengan database (MySQL/PostgreSQL)
- [ ] Export data ke PDF
- [ ] Laporan statistik klinik
- [ ] Sistem autentikasi user
- [ ] Notifikasi jadwal pemeriksaan
- [ ] Riwayat perubahan data (audit log)
- [ ] Pembayaran dan billing
- [ ] Dashboard analytics

## Link Youtube
- https://youtu.be/fIZR5HNRodc 
## Kelompok 7

**Nama Kita**
- Azfa Rahma Putra Susanto - L0324008
- Hammam Ibnu Adi'abdillah - L0324015
- Indra Fata Azhari        - L0324017

