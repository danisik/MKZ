package com.e.minesweeper.util;

import java.util.Random;

public class Generator {

    /**
     * Generate int array representing game area
     * @param bombnumber how many bomb in game area
     * @param width width of area
     * @param height height of area
     * @return int array
     */
    public static int[][] generate( int bombnumber , final int width , final int height){
        Random r = new Random();

        int [][] grid = new int[width][height];
        for( int x = 0 ; x< width ;x++ ){
            grid[x] = new int[height];
        }

        while( bombnumber > 0 ){
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            // -1 is the bomb
            if( grid[x][y] != -1 ){
                grid[x][y] = -1;
                bombnumber--;
            }
        }
        grid = calculateNeigbours(grid,width,height);

        return grid;
    }

    /**
     * Calculate how many bomb are near one box
     * @param grid game area
     * @param width width of game area
     * @param height height of game area
     * @return number of bombs near box
     */
    private static int[][] calculateNeigbours( int[][] grid , final int width , final int height){
        for( int x = 0 ; x < width ; x++){
            for( int y = 0 ; y < height ; y++){
                grid[x][y] = getNeighbourNumber(grid,x,y,width,height);
            }
        }

        return grid;
    }

    /**
     * How many bombs are near one box
     * @param grid game area
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of game area
     * @param height height of game area
     * @return count of bombs near one box
     */
    private static int getNeighbourNumber( final int grid[][] , final int x , final int y , final int width , final int height){
        if( grid[x][y] == -1 ){
            return -1;
        }

        int count = 0;

        if( isMineAt(grid,x - 1 ,y + 1,width,height)) count++; // top-left
        if( isMineAt(grid,x     ,y + 1,width,height)) count++; // top
        if( isMineAt(grid,x + 1 ,y + 1,width,height)) count++; // top-right
        if( isMineAt(grid,x - 1 ,y    ,width,height)) count++; // left
        if( isMineAt(grid,x + 1 ,y    ,width,height)) count++; // right
        if( isMineAt(grid,x - 1 ,y - 1,width,height)) count++; // bottom-left
        if( isMineAt(grid,x     ,y - 1,width,height)) count++; // bottom
        if( isMineAt(grid,x + 1 ,y - 1,width,height)) count++; // bottom-right

        return count;
    }

    /**
     * Check if bomb is on specific place
     * @param grid game area
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of game area
     * @param height height of game area
     * @return if box is bomb or not
     */
    private static boolean isMineAt( final int [][] grid, final int x , final int y , final int width , final int height){
        if( x >= 0 && y >= 0 && x < width && y < height ){
            if( grid[x][y] == -1 ){
                return true;
            }
        }
        return false;
    }

}
