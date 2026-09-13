# Binus LMS Automation Testing 🚀

Proyek otomatisasi pengujian (Automation Testing) yang dimulai dari portal **Binusmaya** (`https://binusmaya.binus.ac.id/home`), mengklik ikon **LMS**, lalu melakukan pengujian pada **Binus LMS** berbasis **Java**, **Selenium WebDriver**, dan **JUnit 5**.

---

## 📌 Prasyarat (Prerequisites)

1. **Java JDK** (versi 11 atau lebih tinggi)
2. **Google Chrome** (versi terbaru)
3. **IDE**: IntelliJ IDEA / Eclipse / VS Code (atau Maven)

---

## 🔑 Alur Kerja & Autentikasi (Workflow & Setup)

1. **Alur Navigasi Utama**:
   - Program membuka portal **Binusmaya** (`https://binusmaya.binus.ac.id/home`).
   - Mencari dan mengklik ikon/tombol **LMS**.
   - Otomatis berpindah tab ke **Binus LMS Dashboard** (`https://lms.binus.ac.id/lms/dashboard`).
   - Melanjutkan eksekusi skenario pengujian masing-masing.

2. **Login Pertama Kali (First Run)**:
   - Program menggunakan profil Chrome `.chrome-binus-profile` untuk menyimpan sesi login Anda secara aman.
   - Pada saat pertama kali dijalankan, jika diarahkan ke halaman login Microsoft/BINUS, silakan login satu kali dan pilih **"Yes"** pada opsi *"Stay signed in?"*.
   - Pada pengujian berikutnya, browser akan **langsung masuk secara otomatis** tanpa perlu login ulang!

---

## 🧪 Skenario Pengujian (Test Cases)

Semua skenario pengujian didefinisikan dalam file [`BinusMayaTest.java`](file:///src/test/java/BinusMayaTest.java):

| Test Case | Metode | Deskripsi Alur |
| :--- | :--- | :--- |
| **Test 1** | `testOpenSchedule()` | Masuk via Binusmaya -> Klik ikon LMS -> Buka sidebar dan klik menu **Schedule** (`/lms/schedule`) -> Verifikasi halaman jadwal. |
| **Test 2** | `testOpenCoursesAndSelectAutomationTesting()` | Masuk via Binusmaya -> Klik ikon LMS -> Buka menu **Courses** (`/lms/course`) -> Pilih mata kuliah **Automation Testing** -> Verifikasi halaman materi. |
| **Test 3** | `testOpenLatestForumInDashboard()` | Masuk via Binusmaya -> Klik ikon LMS -> Pada Dashboard, cari widget **Latest Forum Posts** -> Klik postingan thread forum terbaru -> Verifikasi halaman diskusi forum. |

---

## 🚀 Cara Menjalankan Pengujian

### Melalui IDE (IntelliJ IDEA / Eclipse / VS Code):
1. Buka file [`src/test/java/BinusMayaTest.java`](file:///src/test/java/BinusMayaTest.java).
2. Klik tombol **Run** (ikon segitiga hijau) di samping nama class `BinusMayaTest` untuk menjalankan seluruh pengujian, atau di samping masing-masing method `@Test` untuk menjalankan skenario tertentu.

### Melalui Terminal / Command Line:
```powershell
# Menjalankan seluruh test suite
mvn test

# Menjalankan test case tertentu
mvn test -Dtest=BinusMayaTest#testOpenSchedule
mvn test -Dtest=BinusMayaTest#testOpenCoursesAndSelectAutomationTesting
mvn test -Dtest=BinusMayaTest#testOpenLatestForumInDashboard
```

---

## ⚙️ Penutupan Browser (`@AfterEach`)

Setelah setiap pengujian selesai dieksekusi:
- Browser akan memberikan jeda waktu **3 detik** agar Anda dapat melihat hasil akhir pengujian pada layar.
- Setelah 3 detik, sesi browser akan ditutup secara otomatis (`driver.quit()`).
