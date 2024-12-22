public class Person {
    protected int id; 
    // Atribut id digunakan untuk menyimpan identitas unik setiap objek Person. 
    // Kata kunci `protected` memungkinkan akses langsung oleh subclass seperti Donatur.

    protected String name; 
    // Atribut name digunakan untuk menyimpan nama individu. 
    // Sama seperti id, aksesnya diperbolehkan oleh subclass.

    // Constructor untuk menginisialisasi objek Person dengan id dan name.
    public Person(int id, String name) { 
        this.id = id;
        this.name = name;
    }

    public String getName() { 
        // Getter untuk mengambil nilai name.
        return name;
    }

    public int getId() { 
        // Getter untuk mengambil nilai id.
        return id;
    }
}
