import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DonaturManager implements DonaturManagement { 
    // Class ini menggunakan interface `DonaturManagement` dan mengimplementasikan metode-metodenya.

    private List<Donatur> donaturList = new ArrayList<>();
    // Menggunakan Collection Framework, yaitu `List` dengan implementasi `ArrayList` untuk menyimpan data donatur.

    @Override
    public void addDonatur(Donatur donatur) {
        donaturList.add(donatur); 
        // Menambahkan objek donatur ke dalam list. Bagian dari operasi Create.
        System.out.println("Donatur added: " + donatur);
    }

    @Override
    public void updateDonatur(int id, String name, double amount) {
        // Perulangan untuk mencari donatur berdasarkan ID.
        for (Donatur d : donaturList) { 
            if (d.getId() == id) { 
                // Percabangan untuk memeriksa apakah ID cocok.
                d.name = name; // Akses langsung ke field `name` (tidak disarankan, gunakan setter).
                d.setDonationAmount(amount); // Menggunakan setter untuk memperbarui jumlah donasi.
                System.out.println("Donatur updated: " + d);
                return; 
            }
        }
        System.out.println("Donatur with ID " + id + " not found.");
    }

    @Override
    public Donatur getDonatur(int id) {
        for (Donatur d : donaturList) { 
            // Perulangan untuk mencari donatur berdasarkan ID.
            if (d.getId() == id) {
                return d;
            }
        }
        System.out.println("Donatur with ID " + id + " not found.");
        return null; 
    }

    @Override
    public void deleteDonatur(int id) {
        Iterator<Donatur> iterator = donaturList.iterator();
        while (iterator.hasNext()) { 
            // Menggunakan iterator untuk menghapus objek dari Collection Framework.
            Donatur d = iterator.next();
            if (d.getId() == id) { 
                // Percabangan untuk memeriksa ID.
                iterator.remove(); 
                System.out.println("Donatur deleted: " + d);
                return; 
            }
        }
        System.out.println("Donatur with ID " + id + " not found.");
    }

    @Override
    public void displayAllDonatur() {
        if (donaturList.isEmpty()) { 
            // Percabangan untuk memeriksa apakah list kosong.
            System.out.println("No donatur found.");
        } else {
            donaturList.forEach(System.out::println); 
            // Menampilkan semua data donatur menggunakan lambda expression.
        }
    }

    public void saveToDatabase() { 
        // Fungsi ini menggunakan JDBC untuk menyimpan data ke database.
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/donatur_db", "root", "")) {
            // Membuka koneksi ke database menggunakan JDBC.
            String query = "INSERT INTO donatur (id, name, donation_amount, donation_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (Donatur d : donaturList) { 
                // Perulangan untuk memasukkan setiap objek donatur ke database.
                pstmt.setInt(1, d.getId());
                pstmt.setString(2, d.getName());
                pstmt.setDouble(3, d.getDonationAmount());
                pstmt.setDate(4, new java.sql.Date(d.getDonationDate().getTime())); 
                // Manipulasi method Date untuk konversi ke tipe `java.sql.Date`.
                pstmt.executeUpdate(); 
            }
            System.out.println("Data saved to database.");
        } catch (SQLException e) { 
            // Exception handling untuk menangkap kesalahan SQL.
            e.printStackTrace();
        }
    }
}