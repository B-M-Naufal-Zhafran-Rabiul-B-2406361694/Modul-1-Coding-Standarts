# Refleksi 1

## Prinsip Clean Code yang Diterapkan
- Separation of concerns: controller menangani HTTP, service menangani aturan bisnis, repository menangani penyimpanan data.
- Single responsibility: setiap kelas punya peran yang fokus (ProductController, ProductServiceImpl, ProductRepository).
- Penamaan jelas: method seperti `create`, `edit`, `delete`, `findAll` menjelaskan intent.
- Method pendek: handler ringkas dan mendelegasikan ke service.
- Konsistensi ID: pembuatan UUID ada di service supaya logika terpusat.

## Praktik Secure Coding yang Diterapkan
- Validasi ketersediaan data saat edit: service memastikan produk ada sebelum update.
- HTTP verb sesuai aksi: POST untuk create/edit dan DELETE untuk delete agar perubahan state tidak lewat GET.

## Kesalahan yang ditemukan dan Peningkatan yang dapat dilakukan (Menggunakan bantuan AI (tidak 100%) untuk mengidentifikasi kesalahan ) 
- Belum ada validasi input: nama kosong atau quantity negatif belum dicek. Bisa tambahkan `@Valid` dan `BindingResult` untuk menampilkan error di form.
- Edit GET tanpa null handling: jika ID tidak ditemukan, view menerima `null`. Lebih baik redirect ke list atau return 404.
- Tipe input quantity: lebih baik gunakan `type="number"` plus min/max agar mengurangi input tidak valid.
- Redundansi di repository: service sudah cek eksistensi, repository masih melakukan `getProductById` lagi. Bisa disederhanakan.
- Gaya response delete: saat ini status diatur via servlet response. Lebih rapi jika menggunakan `ResponseStatusException` atau response DTO yang konsisten.
- Typo minor di UI: teks seperti "Product' List" dan placeholder perlu dibetulkan.

# Refleksi 2

## Unit Test
Setelah menulis unit test, saya merasa sangat capek karna harus memikirkan banyak scenario. Menurut saya sendiri tidak ada jumlah pasti untuk unit test pada sebuah class, karena unit test menyesuaikan berapa jumlah method atau lebih tepatnya menyesuaikan kemungkinan flow scenario. seperti yang dijelaskan, untuk memastikan bahwa unit test kita cukup untuk memverifikasi program kita, kita dapat melihat coverage dari unit-test kita. nah tapi muncullah pertanyaan **If you have 100% code coverage, does
that mean your code has no bugs or errors?.** Jawabannya tidak, coverage cuman memastikan bahwa baris kode atau branch logic pernah dijalankan.

## Case Study
Jika saya membuat functional test baru dengan setup dan variabel yang sama, kodenya akan terasa kurang bersih karena terjadi duplikasi (repeated setup, baseUrl, driver, locator). Ini menurunkan maintainability. Potensi issue lain menurut saya: magic strings (selector/id), test data hard-coded, serta ketergantungan urutan data yang membuat test rapuh. Perbaikan yang bisa dilakukan:

- Buat base class/utility untuk setup (@BeforeEach, baseUrl, helper openCreateProductPage(), createProduct()).
- Definisikan locator dan test data sebagai constant agar tidak tersebar.
- Gunakan Page Object untuk halaman create/list agar selector terpusat dan mudah dirawat.
- Gunakan helper assert (misal assertProductListed(name, qty)) untuk mengurangi repetisi.
- Pertimbangkan parameterized test jika skenario mirip dengan data berbeda.


# Refleksi Module 2

## Strategi memperbaiki Code Quality Issue
Tentunya hal yang pertama saya lakukan adalah menganalisis error yang saya dapatkan dengan menggunakan LLM, contohnya pada saat sonarcloud saya bermasalah: hal pertama yang saya lakukan adalah mengirimkan errornya ke LLM, dari situ saya tahu bahwa masalahnya adalah sonar token belum diinput, setelah saya masukkan sonar_token dll ke secret variables, sonarcloud bisa berjalan dengan baik. Contoh lainnya ketika CodeQl saya gagal berjalan, saya memasukkan error tersebut ke LLM, lalu mendapati masalahnya bahwa ada 2 codeQl yang berjalan, setelah itu saya mematikan(disable) codeQl standart yang ada di settings repository, lalu membuat file codeQl.yml

##


