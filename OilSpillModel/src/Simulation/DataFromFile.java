package Simulation;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class DataFromFile {

    public static void _scanner(String patch, LinkedList<String> L){
        Scanner in = null;
        try {
            in = new Scanner(Paths.get(patch));

            while(in.hasNext()) {
                L.add(in.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Double> loadValues(String patch) {
        LinkedList<Double> Ld = new LinkedList<>();
        LinkedList<String> L = new LinkedList<>();
        _scanner(patch, L);

        for(int i=0; i<L.size(); i++) Ld.add(Double.parseDouble(L.get(i)));
        return Ld;
    }

    public static LinkedList<String> loadDirections(String patch) {
        LinkedList<String> L = new LinkedList<>();
        _scanner(patch, L);

        return L;
    }
}
