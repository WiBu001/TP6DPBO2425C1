import javax.swing.*;

public class App {
    // Main program
    public static void main(String[] args){
        // Menjalankan pembuatan GUI di event dispatch thread agar lebih aman untuk Swing
        SwingUtilities.invokeLater(() -> new MainMenu());
    }

    public static void startGame() {
        // Membuat jendela utama game Flappy Bird
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Menutup aplikasi saat jendela ditutup
        frame.setSize(360, 640); // Mengatur ukuran jendela
        frame.setLocationRelativeTo(null); // Menempatkan jendela di tengah layar
        frame.setResizable(false); // Tidak dapat diubah ukurannya

        // Membuat instance kelas Logic untuk mengatur gameplay
        Logic logika = new Logic();

        // Membuat instance tampilan (View) dan menghubungkannya dengan logika permainan
        View tampilan = new View(logika);
        logika.setView(tampilan); // Mengatur tampilan yang akan digunakan oleh logika

        // Memberi fokus ke tampilan agar dapat menerima input (misalnya tombol keyboard)
        tampilan.requestFocus();

        // Menambahkan tampilan ke dalam frame
        frame.add(tampilan);
        frame.pack(); // Menyesuaikan ukuran frame dengan komponen di dalamnya
        frame.setVisible(true); // Menampilkan jendela
    }
}