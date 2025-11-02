import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    // === Ukuran jendela permainan ===
    int width = 360;
    int height = 640;

    // === Referensi ke logika permainan ===
    private Logic logic;

    // Label untuk menampilkan skor
    private JLabel scoreLabel;

    // === Konstruktor ===
    public View(Logic logic) {
        this.logic = logic; // Menyimpan referensi ke objek Logic
        setPreferredSize(new Dimension(width, height)); // Mengatur ukuran panel
        setBackground(Color.cyan); // Warna langit latar belakang

        setFocusable(true); // Agar panel dapat menerima input keyboard
        addKeyListener(logic); // Mendaftarkan listener untuk tombol (misalnya spasi dan R)

        // Inisialisasi label skor
        scoreLabel = new JLabel("Score : 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(scoreLabel); // Tambahkan ke panel
    }

    // Mengembalikan label skor agar bisa diubah dari kelas Logic
    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Memanggil metode untuk menggambar elemen permainan
    }

    // === Menggambar elemen-elemen permainan di layar ===
    public void draw(Graphics g) {
        // Gambar pemain (burung)
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(
                player.getImage(),
                player.getPosX(),
                player.getPosY(),
                player.getWidth(),
                player.getHeight(),
                null
            );
        }

        // Gambar semua pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(
                    pipe.getImage(),
                    pipe.getPosX(),
                    pipe.getPosY(),
                    pipe.getWidth(),
                    pipe.getHeight(),
                    null
                );
            }
        }

        // Jika game over, tampilkan layar akhir
        if (logic.isGameOver()) {
            // Latar belakang semi-transparan untuk teks "Game Over"
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, height / 2 - 100, width, 200);

            // Teks "GAME OVER"
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String gameOverText = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, (width - textWidth) / 2, height / 2 - 40);

            // Menampilkan skor akhir
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String scoreText = "FINAL SCORE : " + logic.getScore() / 2;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, (width - scoreWidth) / 2, height / 2);

            // Petunjuk untuk restart
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String restartText = "Press 'R' to Restart";
            int restartWidth = g.getFontMetrics().stringWidth(restartText);
            g.drawString(restartText, (width - restartWidth) / 2, height / 2 + 40);
        }
    }
}
