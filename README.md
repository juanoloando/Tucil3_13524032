# Ice Sliding Puzzle Solver

Program ini dibuat untuk menyelesaikan permainan Ice Sliding Puzzle menggunakan algoritma UCS, GBFS, dan A* dengan antarmuka GUI berbasis JavaFX. Pengguna dapat memilih file input puzzle, memilih algoritma dan heuristik, melihat hasil solusi, total cost, jumlah iterasi, waktu eksekusi, serta playback langkah solusi secara visual.

## Requirement

Program ini membutuhkan:

- Java JDK 21
- Apache Maven 3.8 atau lebih baru
- Sistem operasi Windows atau Linux

## Cara Mengompilasi Program

Setelah melakukan `git clone`,  harus selalu menjalankan proses build terlebih dahulu pada root project:

```bash
mvn clean package
```

Perintah tersebut akan:

- mengompilasi source code,
- membuat file `.jar` aplikasi,
- menyalin dependency JavaFX runtime ke folder `target/lib`.

Catatan:

- Langkah `mvn clean package` wajib dijalankan setiap kali repository baru selesai di-clone.
- Hal ini diperlukan agar isi folder `target` dan dependency JavaFX menyesuaikan sistem operasi dan environment pada device yang digunakan.

---

## Cara Menjalankan Program

### Windows

Setelah `mvn clean package` selesai dijalankan, program dapat dijalankan dengan:

```powershell
.\bin\ice-sliding-puzzle.bat
```

atau cukup double-click file:

```text
ice-sliding-puzzle.bat
```

---

### Linux / macOS

Setelah `mvn clean package` selesai dijalankan, jalankan perintah berikut pada terminal:

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

## Struktur Folder 

```text
.
├── bin
│   ├── ice-sliding-puzzle.sh
│   └── ice-sliding-puzzle.bat
│
├── doc
│   └── laporan.pdf
│
├── src
│   ├── algorithm
│   │   ├── AStar.java
│   │   ├── GBFS.java
│   │   ├── UCS.java
│   │   ├── Heuristic.java
│   │   └── SlideMechanics.java
│   │
│   ├── controller
│   │   ├── AppController.java
│   │   ├── PlaybackController.java
│   │   └── SolverController.java
│   │
│   ├── model
│   │   ├── Direction.java
│   │   ├── PuzzleGrid.java
│   │   ├── SearchResult.java
│   │   ├── SolutionPath.java
│   │   ├── State.java
│   │   └── Tile.java
│   │
│   ├── resources
│   │   ├── assets
│   │   ├── css
│   │   └── fxml
│   │
│   ├── util
│   │   ├── FileParser.java
│   │   ├── GridValidator.java
│   │   └── SolutionWriter.java
│   │
│   └── view
│       ├── MainApp.java
│       ├── MenuView.java
│       ├── PuzzleView.java
│       └── PlaybackView.java
│
├── target
│   ├── classes
│   ├── lib
│   └── ice-sliding-puzzle-1.0.0.jar
│
├── test 
│
├── pom.xml
└── README.md
```

## Author / Identitas Pembuat

- Author: `Juan Oloando Simanungkalit`
- NIM: `13524032`
- Semester: `II / 2025-2026`
- Gmail: `juanoloando.s@gmail.com`
