// Import library yang digunakan untuk operasi database (JDBC), pengolahan tanggal, dan manipulasi string
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;

public class DonaturApp {
    // Variabel koneksi database (menggunakan JDBC)
    private static final String URL = "jdbc:mysql://localhost:3306/donatur_tb"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    // Fungsi untuk membuka koneksi ke database (menggunakan JDBC)
    public static Connection getConnection() {
        try {
            // Membuka koneksi ke database MySQL
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (SQLException e) {
            System.out.println("[ERROR] Gagal terhubung ke database.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // DonaturManager manager = new DonaturManager(); // Objek dari kelas DonaturManager
        Scanner scanner = new Scanner(System.in); // Scanner untuk input pengguna
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Objek SimpleDateFormat untuk manipulasi tanggal

        // Data pengguna untuk login menggunakan collection HashMap
        HashMap<String, String> userDatabase = new HashMap<>(); // Collection framework: HashMap
        userDatabase.put("admin", "password123"); // Menambahkan data pengguna ke HashMap
        userDatabase.put("user", "user123");

        // Proses login dengan percabangan dan perulangan
        System.out.println("\n=== Login ===\n");
        boolean isAuthenticated = false; // Flag untuk status autentikasi
        for (int attempts = 0; attempts < 3; attempts++) { // Perulangan login maksimal 3 kali
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) { // Percabangan untuk validasi login
                isAuthenticated = true;
                System.out.println("\n[INFO] Login berhasil. Selamat datang, " + username + "!");
                break;
            } else {
                System.out.println("[ERROR] Username atau password salah. Silakan coba lagi.");
            }
        }

        if (!isAuthenticated) {
            System.out.println("[ERROR] Anda telah mencoba 3 kali. Aplikasi akan keluar.");
            scanner.close(); // Menutup scanner jika login gagal
            return;
        }

        // Koneksi ke database setelah login berhasil
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("[ERROR] Tidak dapat melanjutkan operasi tanpa koneksi database.");
            scanner.close();
            return;
        }

        // Perulangan menu utama aplikasi
        while (true) {
            // Menampilkan menu pilihan operasi dengan percabangan
            System.out.println("\n=== Sistem Manajemen Donatur Kampus ===\n");
            System.out.println("1. Tambah Donatur");
            System.out.println("2. Tampilkan Semua Donatur");
            System.out.println("3. Update Donatur");
            System.out.println("4. Hapus Donatur");
            System.out.println("5. Keluar\n");
            System.out.println("========================");
            System.out.print("Pilih opsi:  ");
            int pilihan = -1;

            // Validasi input pilihan dengan percabangan
            if (scanner.hasNextInt()) {
                pilihan = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("[ERROR] Input tidak valid! Harap masukkan angka.");
                scanner.nextLine(); // Bersihkan input
                continue; // Kembali ke awal loop
            }

            try {
                switch (pilihan) {
                    case 1: // Tambah Donatur (CRUD - Create)
                        System.out.print("Masukkan ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Masukkan Nama: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Masukkan Jumlah Donasi: Rp ");
                        double donationAmount = -1;
                        if (scanner.hasNextDouble()) {
                            donationAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                        } else {
                            System.out.println("[ERROR] Input tidak valid! Jumlah donasi harus angka.");
                            scanner.nextLine();
                            break;
                        }

                        System.out.print("Masukkan Tanggal Donasi (dd-MM-yyyy): ");
                        String dateInput = scanner.nextLine();
                        Date donationDate = sdf.parse(dateInput); // Manipulasi tanggal dengan SimpleDateFormat

                        // Menyimpan data donatur ke database (CRUD - Create)
                        String insertQuery = "INSERT INTO donatur (id, name, donation_amount, donation_date) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                            stmt.setInt(1, id);
                            stmt.setString(2, name);
                            stmt.setDouble(3, donationAmount);
                            stmt.setDate(4, new java.sql.Date(donationDate.getTime()));
                            stmt.executeUpdate();
                            System.out.println("[INFO] Donatur berhasil ditambahkan ke database!");
                        } catch (SQLException e) {
                            System.out.println("[ERROR] Gagal menambahkan donatur ke database.");
                            e.printStackTrace();
                        }
                        break;

                    case 2: // Tampilkan Semua Donatur (CRUD - Read)
                        System.out.println("\n=== Daftar Donatur ===");
                        String selectQuery = "SELECT * FROM donatur";
                        try (PreparedStatement stmt = connection.prepareStatement(selectQuery);
                             ResultSet resultSet = stmt.executeQuery()) {
                            System.out.printf("%-10s %-20s %-15s %-15s%n", "ID", "Nama", "Jumlah Donasi", "Tanggal Donasi");
                            int total = 0; // Perhitungan matematika (total donasi)

                            while (resultSet.next()) {
                                int donorId = resultSet.getInt("id");
                                String donorName = resultSet.getString("name");
                                double donorDonationAmount = resultSet.getDouble("donation_amount");
                                Date donorDonationDate = resultSet.getDate("donation_date");

                                System.out.printf("%-10d %-20s %-15.2f %-15s%n", donorId, donorName, donorDonationAmount, sdf.format(donorDonationDate));
                                total = (int) (total + donorDonationAmount); // Total donasi dihitung
                            }
                            System.out.println("\nTotal Donasi: Rp  " + total); // Menampilkan total donasi
                        } catch (SQLException e) {
                            System.out.println("[ERROR] Gagal menampilkan data donatur.");
                            e.printStackTrace();
                        }
                        break;

                    case 3: // Update Donatur (CRUD - Update)
                        System.out.print("Masukkan ID Donatur yang ingin diupdate: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Masukkan Nama Baru: ");
                        String newName = scanner.nextLine().trim();
                        if (!newName.isEmpty()) {
                            newName = newName.substring(0, 1).toUpperCase() + newName.substring(1).toLowerCase(); // Manipulasi string untuk format nama
                        }

                        System.out.print("Masukkan Jumlah Donasi Baru: ");
                        double newAmount = -1;
                        if (scanner.hasNextDouble()) {
                            newAmount = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                        } else {
                            System.out.println("[ERROR] Input tidak valid! Jumlah donasi harus angka.");
                            scanner.nextLine();
                            break;
                        }

                        String updateQuery = "UPDATE donatur SET name = ?, donation_amount = ? WHERE id = ?"; // SQL untuk update
                        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                            stmt.setString(1, newName);
                            stmt.setDouble(2, newAmount);
                            stmt.setInt(3, updateId);
                            stmt.executeUpdate();
                            System.out.println("[INFO] Donatur berhasil diupdate!");
                        } catch (SQLException e) {
                            System.out.println("[ERROR] Gagal mengupdate donatur.");
                            e.printStackTrace();
                        }
                        break;

                    case 4: // Hapus Donatur (CRUD - Delete)
                        System.out.print("Masukkan ID Donatur yang ingin dihapus: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String deleteQuery = "DELETE FROM donatur WHERE id = ?"; // SQL untuk delete
                        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                            stmt.setInt(1, deleteId);
                            stmt.executeUpdate();
                            System.out.println("[INFO] Donatur berhasil dihapus!");
                        } catch (SQLException e) {
                            System.out.println("[ERROR] Gagal menghapus donatur.");
                            e.printStackTrace();
                        }
                        break;

                    case 5: // Keluar
                        System.out.println("Keluar dari aplikasi. Terima kasih!");
                        scanner.close(); // Menutup scanner saat aplikasi keluar
                        try {
                            connection.close(); // Menutup koneksi setelah aplikasi selesai
                        } catch (SQLException e) {
                            System.out.println("[ERROR] Gagal menutup koneksi.");
                            e.printStackTrace();
                        }
                        return; // Keluar dari program

                    default:
                        System.out.println("[ERROR] Pilihan tidak valid. Silakan coba lagi.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Terjadi kesalahan: " + e.getMessage());
            }
        }
    }
}
