# Refleksi 1

Repository ini menambahkan fitur edit dan delete untuk produk. Berikut refleksi singkat tentang kualitas kode dan keamanan.

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
- Belum ada validasi input: nama kosong atau quantity negatif belum dicek. Tambahkan `@Valid` dan `BindingResult` untuk menampilkan error di form.
- Edit GET tanpa null handling: jika ID tidak ditemukan, view menerima `null`. Lebih baik redirect ke list atau return 404.
- Tipe input quantity: gunakan `type="number"` plus min/max agar mengurangi input tidak valid.
- Redundansi di repository: service sudah cek eksistensi, repository masih melakukan `getProductById` lagi. Bisa disederhanakan.
- Gaya response delete: saat ini status diatur via servlet response. Lebih rapi gunakan `ResponseStatusException` atau response DTO yang konsisten.
- CSRF: bila nanti memakai Spring Security, AJAX delete harus menyertakan CSRF token.
- Thread safety: list in-memory tidak thread-safe. Untuk concurrency, gunakan koleksi concurrent atau database.
- Typo minor di UI: teks seperti "Product' List" dan placeholder perlu dibetulkan.

