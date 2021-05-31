package me.zombie_striker.mork.game;

public class GameState {

    private int playerx = 4;
    private int playery = 4;

    private char[][] tiles = new char[20][20];

    private final char[] TILES = new char[]{' ','A','#','%','O'};

    public GameState(){
        for(int x = 0; x < tiles.length; x++){
            for(int y =0; y < tiles.length; y++){
                tiles[x][y] = Math.random() < 0.75 ? TILES[0]: TILES[(int) (Math.random()*TILES.length)];
            }
        }
    }

    public void goUp() {
        playery++;
    }
    public void goDown() {
        playery--;
    }
    public void goLeft(){
        playerx--;
    }
    public void goRight(){
        playerx++;
    }
    public char getTileAt(int x, int y){
        return tiles[x][y];
    }
    public int getPlayerx(){
        return playerx;
    }
    public int getPlayery(){
        return playery;
    }


}
