# Binus LMS Automation Testing 🚀

Proyek otomatisasi pengujian (Automation Testing) untuk platform **Binus LMS** (`https://lms.binus.ac.id/lms/dashboard`) berbasis **Java**, **Selenium WebDriver**, dan **JUnit 5**.

---

## 📌 Prasyarat (Prerequisites)

1. **Java JDK** (versi 11 atau lebih tinggi)
2. **Google Chrome** (versi terbaru)
3. **IDE**: IntelliJ IDEA / Eclipse / VS Code (atau Maven)

---

## 🔑 Langkah Pertama & Autentikasi (First-Run Setup)

Program ini menggunakan profil Chrome otomatis (`.chrome-binus-profile`) agar sesi login Anda tersimpan secara aman dan permanen.

> ### ⚠️ PENTING: Login Pertama Kali (First Run)
> 1. Saat Anda menjalankan pengujian untuk **pertama kali**, jendela browser Google Chrome akan terbuka secara otomatis.
> 2. Jika muncul halaman login Microsoft / BINUS:
>    - Masukkan **Email BINUS** dan **Password** Anda.
>    - Selesaikan verifikasi 2FA / Authenticator (jika ada).
>    - Pilih **"Yes"** pada opsi *"Stay signed in?"*.
> 3. Setelah berhasil masuk ke Dashboard LMS, pengujian akan otomatis dilanjutkan.
> 4. **Untuk pengujian berikutnya**: Browser akan **langsung masuk secara otomatis** tanpa perlu melakukan login ulang!

---

## 🧪 Skenario Pengujian (Test Cases)

Semua skenario pengujian didefinisikan dalam file [`BinusMayaTest.java`](file:///src/test/java/BinusMayaTest.java):

| Test Case | Metode | Deskripsi |
| :--- | :--- | :--- |
| **Test 1** | `testOpenSchedule()` | Membuka sidebar, mengklik menu **Schedule** (`/lms/schedule`), dan memverifikasi halaman jadwal terbuka. |
| **Test 2** | `testOpenCoursesAndSelectAutomationTesting()` | Membuka sidebar **Courses** (`/lms/course`), memilih dan mengklik kartu mata kuliah **Automation Testing**, serta memverifikasi halaman sesi materi. |
| **Test 3** | `testOpenLatestForumInDashboard()` | Mengakses Dashboard, mencari widget **Latest Forum Posts**, mengklik postingan thread forum terbaru, dan memverifikasi halaman diskusi forum terbuka. |

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
