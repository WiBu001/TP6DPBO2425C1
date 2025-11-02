import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    private Image birdImage;

    public MainMenu() {
        // Mengatur properti dasar jendela utama
        setTitle("Flappy Bird");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tutup program saat jendela ditutup
        setLocationRelativeTo(null); // Posisikan jendela di tengah layar
        setResizable(false); // Nonaktifkan pengubahan ukuran jendela

        // Panel utama tempat menggambar dan menempatkan tombol
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.cyan); // Warna latar belakang panel

                // Gambar burung di bagian tengah atas menu
                if (birdImage != null) {
                    int imgW = 64;
                    int imgH = 48;
                    g.drawImage(birdImage, (getWidth() - imgW) / 2, (getHeight() / 2) - 150, imgW, imgH, null);
                }

                // Gambar teks judul "Flappy Bird" di bagian atas
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                String title = "Flappy Bird";
                int titleWidth = g.getFontMetrics().stringWidth(title);
                g.drawString(title, (getWidth() - titleWidth) / 2, 120);
            }
        };
        panel.setLayout(null); // Menggunakan layout manual (absolute positioning)

        // Memuat gambar burung dari folder assets
        birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();

        // Membuat tombol "Start Game"
        JButton playButton = new JButton("Start Game");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setBackground(new Color(76, 175, 80)); // Warna hijau
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBounds((360 - 200) / 2, 350, 200, 50); // Posisi dan ukuran tombol
        panel.add(playButton);

        // Membuat tombol "Exit"
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setBackground(new Color(244, 67, 54)); // Warna merah
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBounds((360 - 200) / 2, 420, 200, 50);
        panel.add(exitButton);

        // Menambahkan panel ke jendela utama
        add(panel);

        // === Aksi tombol ===
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Tutup jendela menu utama
                App.startGame(); // Panggil fungsi untuk memulai game
            }
        });

        // Aksi untuk tombol Exit: keluar dari aplikasi
        exitButton.addActionListener(e -> System.exit(0));

        // Menampilkan jendela utama
        setVisible(true);
    }
}
