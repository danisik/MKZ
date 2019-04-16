package com.e.minesweeper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.effect.Effect;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import com.e.minesweeper.util.Generator;
import com.e.minesweeper.views.grid.Cell;

public class GameEngine {
    private static GameEngine instance;

    public static final int BOMB_NUMBER = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 13;

    private static MediaPlayer player = null;
    private static final int WON_ID = R.raw.win;
    private static final int LOST_ID = R.raw.lose;
    private SoundPool soundPool = null;

    private Context context;

    private Cell[][] MinesweeperGrid = new Cell[WIDTH][HEIGHT];

    public static GameEngine getInstance() {
        if( instance == null ){
            instance = new GameEngine();
        }
        return instance;
    }

    private GameEngine(){

    }

    /**
     * Create a new game area
     * @param context app context
     */
    public void createGrid(Context context){
        this.context = context;

        // create the grid and store it
        int[][] GeneratedGrid = Generator.generate(BOMB_NUMBER,WIDTH, HEIGHT);
        setGrid(context,GeneratedGrid);
    }

    /**
     * Set new game area
     * @param context
     * @param grid
     */
    private void setGrid( final Context context, final int[][] grid ){
        for( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( MinesweeperGrid[x][y] == null ){
                    MinesweeperGrid[x][y] = new Cell( context , x,y);
                }
                MinesweeperGrid[x][y].setValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }

    /**
     * Return box at specific position
     * @param position position of box
     * @return box
     */
    public Cell getCellAt(int position) {
        int x = position % WIDTH;
        int y = position / WIDTH;

        return MinesweeperGrid[x][y];
    }

    /**
     * Return box at specific position
     * @param x x coordinate of box
     * @param y y coordinate of box
     * @return box
     */
    public Cell getCellAt( int x , int y ){
        return MinesweeperGrid[x][y];
    }

    /**
     * Action for each box, set flag or just reveal an unrevealed box
     * @param x x coordinate of box
     * @param y y coordinate of box
     */
    public void click( int x , int y ){
        if (MainActivity.flagEnabled) {
            if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !getCellAt(x, y).isClicked()) {
                flag(x, y);
            }
        }
        else {
            if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !getCellAt(x, y).isClicked()) {
                if (getCellAt(x, y).isFlagged()) return;
                getCellAt(x, y).setClicked();

                if (getCellAt(x, y).getValue() == 0) {
                    for (int xt = -1; xt <= 1; xt++) {
                        for (int yt = -1; yt <= 1; yt++) {
                            if (xt != yt) {
                                click(x + xt, y + yt);
                            }
                        }
                    }
                }

                if (getCellAt(x, y).isBomb()) {
                    onGameLost();
                }
            }
        }
        checkEnd();
    }

    /**
     * Check if game ends by checking if unrevealed boxes are empty and all bombs are flagged
     * @return true/false ending
     */
    private boolean checkEnd(){
        int bombNotFound = BOMB_NUMBER;
        int notRevealed = WIDTH * HEIGHT;
        for ( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( getCellAt(x,y).isRevealed() || getCellAt(x,y).isFlagged() ){
                    notRevealed--;
                }

                if( getCellAt(x,y).isFlagged() && getCellAt(x,y).isBomb() ){
                    bombNotFound--;
                }
            }
        }

        if( bombNotFound == 0 && notRevealed == 0 ){
            Toast.makeText(context,"Game won", Toast.LENGTH_SHORT).show();
            playSound(WON_ID);
        }
        return false;
    }

    private void playSound(int id) {
        player = MediaPlayer.create(context, id);
        player.start();
    }

    /**
     * Action when flag button was clicked -
     * @param x x coordinate of box
     * @param y y coordinate of box
     */
    public void flag( int x , int y ){

        boolean isFlagged = getCellAt(x,y).isFlagged();
        if (isFlagged) {
            MainActivity.setFlagsCount(MainActivity.getFlagsCount() + 1);
        }
        else {
            if (MainActivity.getFlagsCount() > 0) MainActivity.setFlagsCount(MainActivity.getFlagsCount() - 1);
            else return;
        }
        getCellAt(x,y).setFlagged(!isFlagged);
        getCellAt(x,y).invalidate();




    }

    /**
     * Reveal all boxes if game are ended by exploding bomb
     */
    private void onGameLost(){
        Toast.makeText(context,"Game lost", Toast.LENGTH_SHORT).show();

        for ( int x = 0 ; x < WIDTH ; x++ ) {
            for (int y = 0; y < HEIGHT; y++) {
                getCellAt(x,y).setRevealed();
            }
        }
        playSound(LOST_ID);
    }
}
