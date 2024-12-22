import java.text.SimpleDateFormat;
import java.util.Date;

public class Donatur extends Person { // Menggunakan inheritance, di mana Donatur merupakan subclass dari superclass Person.
    private double donationAmount;
    private Date donationDate;

    // Constructor yang digunakan untuk menginisialisasi objek Donatur.
    public Donatur(int id, String name, double donationAmount, Date donationDate) {
        super(id, name); // Memanfaatkan konstruktor dari superclass Person.
        this.donationAmount = donationAmount;
        this.donationDate = donationDate; 
    }

    // Getter untuk donationAmount, menunjukkan encapsulation.
    public double getDonationAmount() {
        return donationAmount;
    }

    // Setter untuk donationAmount, memungkinkan modifikasi data.
    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    // Getter untuk donationDate.
    public Date getDonationDate() {
        return donationDate;
    }

    // Overriding method toString untuk manipulasi data menjadi string.
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Contoh manipulasi method Date.
        return "ID: " + id + ", Name: " + name + ", Donation: " + donationAmount + ", Date: " + sdf.format(donationDate);
    }
}