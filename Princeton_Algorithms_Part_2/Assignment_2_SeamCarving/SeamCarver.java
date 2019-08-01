import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import edu.princeton.cs.algs4.StdOut;
import java.lang.IndexOutOfBoundsException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

public class SeamCarver {
    private Picture     pictureObj;
    private double[][]  energyDistanceMatrix;
    private int         currentHeight;
    private int         currentWidth;
    
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        pictureObj             = new Picture(picture);
        int height             = pictureObj.height();
        int width              = pictureObj.width();
        
        currentHeight          = height;
        currentWidth           = width;
        
        energyDistanceMatrix          = new double[height][width];
        
        int row, column;
        for (row=0; row<height; row++) {
            energyDistanceMatrix[row][0] = 1000;
            energyDistanceMatrix[row][width-1] = 1000;
        }
        
        for (column=0; column<width; column++) {
            energyDistanceMatrix[0][column] = 1000;
            energyDistanceMatrix[height-1][column] = 1000;
        }
    }
    
    // current picture
    public Picture picture() {
        return (new Picture(pictureObj));
    }
    
    // width of current picture
    public     int width() {
        return currentWidth;
    }
    
    // height of current picture
    public     int height() {
        return currentHeight;
    }
    
    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        int row = y;
        int column = x;
        
        if ((row > (currentHeight-1)) || (row < 0))
            throw new IndexOutOfBoundsException("Row out of bounds\n");
        else if ((column > (currentWidth-1)) || (column < 0))
            throw new IndexOutOfBoundsException("Column out of bounds\n");
        
        double energy = 0.0;
        
        if ((row == 0) || (row == currentHeight-1))
            return 1000;
        else if ((column == 0) || (column == currentWidth-1))
            return 1000;
        else {
            int rx, ry, gx, gy, bx, by;
            
            rx = pictureObj.get(column+1, row).getRed() - pictureObj.get(column-1, row).getRed();
            gx = pictureObj.get(column+1, row).getGreen() - pictureObj.get(column-1, row).getGreen();
            bx = pictureObj.get(column+1, row).getBlue() - pictureObj.get(column-1, row).getBlue();
            
            ry = pictureObj.get(column, row+1).getRed() - pictureObj.get(column, row-1).getRed();
            gy = pictureObj.get(column, row+1).getGreen() - pictureObj.get(column, row-1).getGreen();
            by = pictureObj.get(column, row+1).getBlue() - pictureObj.get(column, row-1).getBlue();
            
            energy = rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by;
            energy = Math.sqrt(energy);            
        }
        
        return energy;
    }
    
        
    private int findMinHorizontalNeighbor(int row, int column) {
        if (row == 0) {
            double a = energyDistanceMatrix[row][column-1];
            double b = energyDistanceMatrix[row+1][column-1];
            return ((a < b) ? row : row+1);
        } else if (row == (currentHeight-1)){
            double a = energyDistanceMatrix[row-1][column-1];
            double b = energyDistanceMatrix[row][column-1];
            return ((a < b) ? row-1 : row);
        } else {
            double a = energyDistanceMatrix[row-1][column-1];
            double b = energyDistanceMatrix[row][column-1];
            double c = energyDistanceMatrix[row+1][column-1];
        
            return ((a < b) ? ((a < c) ? (row-1) : (row+1)) : ((b < c) ? (row) : (row+1)));
        }
    }
    
    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        int[] horizontalSeam = new int[currentWidth];
        
        int row, column;
        
        for (column=0; column<currentWidth; column++)
            horizontalSeam[column] = 0;
        
        for (row=0; row<currentHeight; row++) {
            energyDistanceMatrix[row][0] = 1000;
        }
        
        for (column=1; column<currentWidth-1; column++) {
            int horizontalSeamRow = 0;
            for (row=0; ((row<currentHeight) && (currentHeight > 1)); row++) {
                energyDistanceMatrix[row][column] = energy(column, row);
                
                int lowestEnergyNeighborRow = findMinHorizontalNeighbor(row, column);
                energyDistanceMatrix[row][column] += energyDistanceMatrix[lowestEnergyNeighborRow][column-1];
                if ((horizontalSeamRow == 0) 
                    || (energyDistanceMatrix[row][column] < energyDistanceMatrix[horizontalSeamRow][column]))
                    horizontalSeamRow = row;
            }
            horizontalSeam[column] = horizontalSeamRow;
        } 
        
        if (currentHeight > 1) {
            for (column=currentWidth-2; column>1; column--) {
                horizontalSeam[column-1] = findMinHorizontalNeighbor(horizontalSeam[column], column);
            }
        }
        if (currentWidth > 2) {
            horizontalSeam[0] = horizontalSeam[1];
            horizontalSeam[currentWidth-1] = horizontalSeam[currentWidth-2];
        }
        
        return horizontalSeam;
    }
    
    private int findMinVerticalNeighbor(int row, int column) {
        if (column == 0) {
            double a = energyDistanceMatrix[row-1][column];
            double b = energyDistanceMatrix[row-1][column+1];
            return ((a < b) ? column : column+1);
        } else if (column == (currentWidth-1)){
            double a = energyDistanceMatrix[row-1][column-1];
            double b = energyDistanceMatrix[row-1][column];
            return ((a < b) ? column-1 : column);
        } else {
            double a = energyDistanceMatrix[row-1][column-1];
            double b = energyDistanceMatrix[row-1][column];
            double c = energyDistanceMatrix[row-1][column+1];
        
            return ((a < b) ? ((a < c) ? (column-1) : (column+1)) : ((b < c) ? (column) : (column+1)));
        }
    }
    
    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {
        int[] verticalSeam = new int[currentHeight];
        
        int row, column;
        
        for (row=0; row<currentHeight; row++)
            verticalSeam[row] = 0;
        
        for (column=0; column<currentWidth; column++)
            energyDistanceMatrix[0][column] = 1000;
        
        for (row=1; row<currentHeight-1; row++) {
            int verticalSeamColumn = 0;
            for (column=0; (column<currentWidth && currentWidth>1); column++) {
                energyDistanceMatrix[row][column] = energy(column, row);
                
                int lowestEnergyNeighborColumn = findMinVerticalNeighbor(row, column);
                energyDistanceMatrix[row][column] += energyDistanceMatrix[row-1][lowestEnergyNeighborColumn];
                
                if ((verticalSeamColumn == 0) 
                    || (energyDistanceMatrix[row][column] < energyDistanceMatrix[row][verticalSeamColumn]))
                    verticalSeamColumn = column;
            }
            verticalSeam[row] = verticalSeamColumn;
        }
        
        if (currentWidth > 1) {
            for (row=currentHeight-2; row>1; row--) {
                verticalSeam[row-1] = findMinVerticalNeighbor(row, verticalSeam[row]);
            }
        }
        
        if (currentHeight > 2) {
            verticalSeam[0] = verticalSeam[1];
            verticalSeam[currentHeight-1] = verticalSeam[currentHeight-2];
        } 
        return verticalSeam;
    }
    
    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new NullPointerException("Seam to remove is null\n");
        else if (currentHeight <= 1)
            throw new IllegalArgumentException("Image size doesn't permit removing horizontal seam\n");
        else if (seam.length < currentWidth)
            throw new IllegalArgumentException("Incorrect length of the seam\n"); 
        int row, column;
        int prevSeamRow = -1;
        for (column=0; column<currentWidth; column++) {
            if ((seam[column] < 0)
                || (seam[column] > (currentHeight-1)))
                throw new IllegalArgumentException("Invalid seam array\n");
            else if (prevSeamRow == -1) {
                prevSeamRow = seam[column];
                continue;
            } else if (Math.abs(prevSeamRow - seam[column]) > 1)
                throw new IllegalArgumentException("Invalid seam array\n");
            
            prevSeamRow = seam[column];
        }
            Picture newPictureObj = new Picture(currentWidth, currentHeight-1);
        for (column=0; column<currentWidth; column++) {
            for (row=seam[column]; row<currentHeight-1; row++) {
                pictureObj.set(column, row, pictureObj.get(column, row+1));
            }
        }
        
        for (column=0; column<currentWidth; column++) {
            for (row=0; row<currentHeight-1; row++) {
                newPictureObj.set(column, row, pictureObj.get(column, row));
            }
        }
        pictureObj = newPictureObj;
        currentHeight = currentHeight - 1;        
    }
    
    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new NullPointerException("Seam to remove is null\n");
        else if (currentWidth <= 1)
            throw new IllegalArgumentException("Image size doesn't permit removing vertical seam\n");  
        else if (seam.length < currentHeight)
            throw new IllegalArgumentException("Incorrect length of the seam\n");
        
        int row, column;
        int prevSeamColumn = -1;
        for (row=0; row<currentHeight; row++) {
            if ((seam[row] < 0) || (seam[row] > (currentWidth-1)))
                throw new IllegalArgumentException("Invalid seam array\n");
            else if (prevSeamColumn == -1) {
                prevSeamColumn = seam[row];
                continue;
            } else if (Math.abs(prevSeamColumn - seam[row]) > 1)
                throw new IllegalArgumentException("Invalid seam array\n");
            
            prevSeamColumn = seam[row];
        }
        Picture newPictureObj = new Picture(currentWidth-1, currentHeight);
        for (row=0; row<currentHeight; row++) {
            for (column=seam[row]; column<currentWidth-1; column++) {
                pictureObj.set(column, row, pictureObj.get(column+1, row));
            }
        }
        
        for (row=0; row<currentHeight; row++) {
            for (column=0; column<currentWidth-1; column++) {
                newPictureObj.set(column, row, pictureObj.get(column, row));
            }
        }
        pictureObj = newPictureObj;
        currentWidth = currentWidth - 1;
    }
    
    public static void main(String[] args) {
    }
}