import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class Logic implements ActionListener, KeyListener {
    // === Ukuran frame utama ===
    int frameWidth = 360; 
    int frameHeight = 640;

    // === Ukuran dan posisi awal pemain ===
    int playerWidth = 34;
    int playerHeight = 24;
    int playerStartPosX = (frameWidth - playerWidth) / 2;
    int playerStartPosY = (frameHeight - playerHeight) / 2;

    // === Ukuran dan posisi awal pipa ===
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    // === Komponen utama permainan ===
    View view;
    Image birdImage;
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    // === Timer untuk loop game dan spawn pipa ===
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1; // Gaya gravitasi yang mempengaruhi burung

    int pipeVelocityX = -2; // Kecepatan gerak pipa (ke kiri)

    boolean gameOver = false; // Status permainan
    int score = 0; // Skor pemain
    JLabel scoreLabel; // Label untuk menampilkan skor di layar

    public Logic() {
        // Memuat gambar burung
        birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        // Memuat gambar pipa atas dan bawah
        lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();
        pipes = new ArrayList<>();

        // Timer untuk menambahkan pipa baru setiap 3 detik
        pipesCooldown = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pipa");
                placePipes();
            }
        });
        pipesCooldown.start();

        // Timer utama game loop (60 FPS)
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    // Setter untuk menghubungkan view dengan logika permainan
    public void setView(View view){
        this.view = view;
        this.scoreLabel = view.getScoreLabel();
    }

    // Getter untuk objek Player
    public Player getPlayer(){
        return player;
    }

    // Getter untuk daftar pipa
    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    // Menempatkan pipa baru secara acak
    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4; // Jarak antara pipa atas dan bawah

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // Menggerakkan objek pemain dan pipa
    public void move(){
        if(gameOver) return; // Jika game over, hentikan pergerakan

        // Pergerakan pemain dipengaruhi gravitasi
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0)); // Cegah keluar dari atas layar

        // Loop semua pipa
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX); // Gerakkan pipa ke kiri

            // Deteksi tabrakan antara pemain dan pipa
            Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());

            if(playerRect.intersects(pipeRect)){
                gameOver = true; // Jika tabrakan, game over
            }

            // Menambah skor ketika pemain melewati pipa
            if(!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()){
                pipe.setPassed(true);
                score++;
                scoreLabel.setText("Score : " + score / 2); // Dibagi 2 karena ada pipa atas & bawah
            }
        }

        // Jika pemain jatuh ke bawah layar
        if(player.getPosY() + player.getHeight() >= frameHeight){
            player.setPosY(frameHeight - player.getHeight());
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // Dipanggil setiap frame (oleh gameLoop)
        move();
        if(view != null){
            view.repaint(); // Menggambar ulang tampilan
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}

    // Menangani input keyboard
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            // Tekan spasi untuk terbang ke atas
            player.setVelocityY(-10);
        }

        if(e.getKeyCode() == KeyEvent.VK_R && gameOver){
            // Tekan R untuk restart setelah game over
            restartGame();
        }
    }

    public void keyReleased(KeyEvent e){}

    // Mengatur ulang permainan ke kondisi awal
    private void restartGame(){
        gameOver = false;
        score = 0;
        player.setPosX((frameWidth - player.getWidth()) / 2);
        player.setPosY((frameHeight - player.getHeight()) / 2);
        player.setVelocityY(0);
        pipes.clear();
        scoreLabel.setText("Score : " + score);
    }

    // Mengecek apakah game sudah berakhir
    public boolean isGameOver(){
        return gameOver;
    }

    // Mengambil nilai skor saat ini
    public int getScore(){
        return score;
    }
}
