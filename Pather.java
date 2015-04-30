/**
 * Author: Anthony Lau
 */
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

public class Pather {
    public static void main(String[] args) throws IOException {
        List<Point> hashCoordinates;
        List<Point> starCoordinates;

        String inputFileName = args[0];
        String outputFilename = args[1];

        hashCoordinates = getHashCoordinates(inputFileName);
        starCoordinates = getStarCoordinates(hashCoordinates);

        if(starCoordinates.size() < 1) {
            throw new IOException("Input file must have 2 or more hashes");
        }
        printOutputFile(starCoordinates, inputFileName, outputFilename);
    }

    public static List getHashCoordinates(String inputFileName) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(inputFileName), "UTF-8"));
        String line = reader.readLine();

        List<Point> results = new ArrayList<Point>();
        int currentRow = 0;

        //grab length of first line to compare with rest to ensure input is rectangular
        int lineCount = line.length();

        while(line != null) {
            if(line.length() != lineCount) {
                throw new IOException("Invalid input file: not rectangular");
            }
            for(int i =0;i<line.length();i++) {
                if(line.substring(i,i+1).equals("#")) {
                    Point temp = new Point();
                    temp.x = i;
                    temp.y = currentRow;
                    results.add(temp);
                }
                else if(! line.substring(i,i+1).equals(".")) {
                    throw new IOException("Invalid Character in input file");
                }
            }
            currentRow++;
            line = reader.readLine();
        }
        return results;
    }

    public static List getStarCoordinates(List<Point> hashCoordinates) {
        List<Point> results = new ArrayList<Point>();
        Point p1;
        Point p2;

        for(int i=0; i<hashCoordinates.size()-1; i++)  {
            p1 = hashCoordinates.get(i);
            p2 = hashCoordinates.get(i+1);

            //If the two hashes are on the same X axis (row)
            if(p1.getY() == p2.getY()) {
                int p1x = (int) p1.getX();
                int p2x = (int) p2.getX();
                for(int j=p1x+1;j<p2x;j++) {
                    Point newStarPoint = new Point(j, (int)p1.getY());
                    results.add(newStarPoint);
                }
            }
            //If two hashes are on the same Y axis (column)
            else if(p1.getX() == p2.getX()) {
                int p1y = (int) p1.getY();
                int p2y = (int) p2.getY();
                for(int j=p1y+1;j<p2y;j++) {
                    Point newStarPoint = new Point((int)p1.getX(), j);
                    //Check if star points exist already before adding in case you went left before right on same line
                    if(!results.contains(newStarPoint)) {
                        results.add(newStarPoint);
                    }
                }
            }
            //Hashes are not on the same row or column
            else {
                int p1x = (int) p1.getX();
                int p2x = (int) p2.getX();
                int p1y = (int) p1.getY();
                int p2y = (int) p2.getY();

                //Go Downwards towards second hash until y value is <= to p2's y value, starting from p1's y value + 1
                for(int j = p1y+1; j<=p2y;j++) {
                    Point newStarPoint = new Point(p1x, j);
                    results.add(newStarPoint);
                }
                //Go Across towards second hash figure out whether to go left or right
                //If p1's x value is greater than p2's x value go left
                if(p1x > p2x) {
                    for(int j=p1x-1;j>p2x;j--) {
                        Point newStarPoint = new Point(j, p2y);
                        results.add(newStarPoint);
                    }
                }
                //Else go right
                else {
                    for(int j=p1x+1;j<p2x;j++) {
                        Point newStartPoint = new Point(j, p2y);
                        results.add(newStartPoint);
                    }
                }

            }
        }

        return results;
    }

    public static void printOutputFile(List<Point> starCoordinates,String inputFileName, String outputFileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        String line = reader.readLine();
        PrintWriter writer = new PrintWriter(outputFileName);
        int y = 0;
        Point currentPoint = new Point();
        while(line != null) {
            for(int i = 0; i < line.length(); i++) {
                currentPoint.setLocation(i, y);
                if(starCoordinates.contains(currentPoint)) {
                    writer.print("*");
                } else {
                    writer.print(line.charAt(i));
                }

            }
            writer.println();
            y++;
            line = reader.readLine();
        }
        writer.close();
    }
}
