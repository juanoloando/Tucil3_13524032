# Ice Sliding Puzzle Solver

Program ini dibuat untuk menyelesaikan permainan Ice Sliding Puzzle menggunakan algoritma UCS, GBFS, dan A* dengan antarmuka GUI berbasis JavaFX. Pengguna dapat memilih file input puzzle, memilih algoritma dan heuristik, melihat hasil solusi, total cost, jumlah iterasi, waktu eksekusi, serta playback langkah solusi secara visual.

## Requirement

Program ini membutuhkan:

- Java JDK 21
- Apache Maven 3.8 atau lebih baru
- Sistem operasi Windows atau Linux

## Cara Mengompilasi Program

Folder `target` hasil build Maven sudah disertakan pada repository, sehingga program dapat langsung dijalankan tanpa perlu melakukan kompilasi ulang.

Jika folder `target` tidak ada, terhapus, atau ingin melakukan build ulang program, jalankan perintah berikut pada root project:

```bash
mvn clean package
```

Perintah tersebut akan:

- mengompilasi source code,
- membuat file `.jar` aplikasi,
- menyalin dependency JavaFX runtime ke folder `target/lib`.

---

## Cara Menjalankan Program

### Windows

Program dapat dijalankan dengan:

```powershell
.\bin\ice-sliding-puzzle.bat
```

atau cukup double-click file:

```text
ice-sliding-puzzle.bat
```

---

### Linux / macOS

Jalankan perintah berikut pada terminal:

```bash
chmod +x ./bin/ice-sliding-puzzle.sh
./bin/ice-sliding-puzzle.sh
```

### Cara Menggunakan Program

1. Jalankan aplikasi.
2. Pilih file input puzzle `.txt`.
3. Pilih algoritma pencarian: `UCS`, `GBFS`, atau `A*`.
4. Jika diperlukan, pilih heuristik yang ingin digunakan.
5. Jalankan solver untuk mendapatkan solusi.
6. Lihat hasil berupa urutan langkah, total cost, banyak iterasi, dan waktu eksekusi.
7. Gunakan fitur playback untuk melihat solusi langkah demi langkah.
8. Simpan solusi atau statistik ke file jika diperlukan.

## Format Input Singkat

Format file input:

```text
N M
[baris peta 1]
[baris peta 2]
...
[baris peta N]
[cost baris 1]
...
[cost baris N]
```

Simbol penting:

- `Z` : posisi awal
- `O` : exit
- `X` : obstacle
- `L` : lava
- `*` : es
- `0`-`9` : checkpoint yang harus diambil berurutan

Contoh file input tersedia di folder `test`.

## Struktur Folder Penting

- `src` : source code program
- `src/resources` : file FXML, CSS, dan aset gambar
- `test` : contoh file input
- `bin` : launcher program untuk Windows dan Linux
- `target` : hasil build Maven
- `doc` : dokumentasi atau laporan

## Author / Identitas Pembuat

- Author: `Juan Oloando Simanungkalit`
- NIM: `13524032`
- Semester: `II / 2025-2026`
- Gmail: `juanoloando.s@gmail.com`
