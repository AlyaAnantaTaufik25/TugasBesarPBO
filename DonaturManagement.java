public interface DonaturManagement { 
    // Interface ini mendefinisikan kontrak untuk mengelola objek `Donatur`.
    
    void addDonatur(Donatur donatur); 
    // Metode ini merupakan bagian dari fungsi Create dalam operasi CRUD.

    void updateDonatur(int id, String name, double amount); 
    // Metode ini merupakan bagian dari fungsi Update dalam operasi CRUD.

    Donatur getDonatur(int id); 
    // Metode ini merupakan bagian dari fungsi Read dalam operasi CRUD.

    void deleteDonatur(int id); 
    // Metode ini merupakan bagian dari fungsi Delete dalam operasi CRUD.

    void displayAllDonatur(); 
    // Metode ini untuk menampilkan semua objek `Donatur`, memanfaatkan Collection Framework.
}