# NEXUS BOT Simulator (Android)

Project Android Kotlin + Jetpack Compose. Project ini adalah **simulator**, bukan bot eksekusi transaksi.

## Batasan
- Tidak terhubung ke akun Stockity atau broker.
- Tidak membaca harga Stockity / harga pasar real-time.
- Tidak mengirim order BUY/SELL uang sungguhan.
- Harga dan hasil menang/kalah di versi awal ini adalah data sintetis/demonstrasi.
- Modal awal simulasi Rp140.000, nilai simulasi Rp14.000, interval 5 detik.

## Build APK melalui GitHub Actions (tanpa Android Studio di HP)
1. Buat akun / login ke GitHub.
2. Buat repository baru, misalnya `NexusBotSimulator`.
3. Upload seluruh isi folder project ini ke repository, termasuk folder tersembunyi `.github/workflows`.
   - Cara paling mudah: ekstrak ZIP di komputer, lalu upload isi folder project ke GitHub.
   - Jika menggunakan HP, aktifkan tampilan situs desktop di browser saat membuka GitHub.
4. Pastikan file `.github/workflows/android.yml` ada di repository pada branch `main`.
5. Buka tab **Actions** pada repository.
6. Pilih workflow **Build Android APK**, lalu tekan **Run workflow** jika belum berjalan otomatis.
7. Tunggu sampai proses selesai dengan tanda centang hijau.
8. Buka hasil run workflow, cari bagian **Artifacts**, lalu download `NexusBotSimulator-debug-apk`.
9. Ekstrak ZIP artifact tersebut. Di dalamnya ada `app-debug.apk`; buka file itu untuk memasang aplikasi.

## Catatan
- APK yang dihasilkan adalah debug APK untuk pengujian pribadi, bukan rilis Play Store.
- GitHub Actions menjalankan build di cloud; proses build memerlukan koneksi internet.
