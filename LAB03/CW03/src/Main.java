import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("Zadanie 1");
        System.out.println("Próbowałem zrobić coś z zadaniem pierwszym, ale komletnie mi się nie udawało więc jest tylko zadanie 2");
        System.out.println();


        System.out.println("Zadanie 2");
        String sciezkaDoSystemuDecyzyjnego = "D:\\CW03\\CW03\\class_materials\\SystemDecyzyjny.txt";
        ArrayList<String> linia = czytaj_linie(sciezkaDoSystemuDecyzyjnego);
        int xSize = linia.get(0).split(" ").length;
        int ySize = linia.size();
        int[][] atrybuty = new int[ySize][xSize-1];
        int[] decyzje = new int[ySize];
        for (int i = 0; i < ySize; ++i) {
            String [] line = linia.get(i).split(" ");
            for (int j = 0; j < xSize-1; ++j)
                atrybuty[i][j] = Integer.parseInt(line[j]);
            decyzje[i] = Integer.parseInt(line[xSize-1]);
        }
        System.out.println("System decyzyjny:");
        rysujDecisionSystem(atrybuty, decyzje);
        System.out.println("\nReguly:");
        sequentialCovering(atrybuty, decyzje);
    }

    public static ArrayList<String> czytaj_linie(String path) {
        ArrayList<String> wyjscie = new ArrayList<>();
        try {
            File obj = new File(path);
            Scanner myReader = new Scanner(obj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                wyjscie.add(data);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return wyjscie;
    }
    public static void rysujDecisionSystem(int [][] atrybuty, int [] decyzje) {
        int xSize = atrybuty[0].length + 1;
        int ySize = atrybuty.length;
        System.out.print("   ");
        for(int i = 0; i < xSize-1; ++i) {
            System.out.print(" a"+(i+1)+" ");
        }
        System.out.println(" d");

        for(int i = 0; i < ySize; ++i) {
            System.out.print("o"+(i+1)+" ");
            for(int j = 0; j < xSize-1; ++j) {
                System.out.print("  "+atrybuty[i][j]+" ");
            }
            System.out.print(" "+decyzje[i]+" ");
            System.out.println();
        }
    }
    public static void sequentialCovering(int [][] atrybuty, int [] decyzje) {
        int ySize = atrybuty.length;

        int liczbaAtrybutow = atrybuty[0].length;
        ArrayList<int[]> kombinacje = new ArrayList<>();
        ArrayList<int[]> ElementyZN = new ArrayList<>();
        for(int i = 1; i <= liczbaAtrybutow; ++i) {
            ElementyZN = generujKombinacje(liczbaAtrybutow, i);
            for(int [] j : ElementyZN)
                kombinacje.add(j);
        }

        ArrayList<Integer> rozwazania = new ArrayList<>();
        for(int i = 0; i < ySize; ++i)
            rozwazania.add(i);

        for(int kombinacjeIteracji = 0; kombinacjeIteracji < kombinacje.size() && rozwazania.size() > 0; ++kombinacjeIteracji) {
            for(int i = 0; i < rozwazania.size(); ++i) {
                boolean prawidlowy = true;
                int obj = rozwazania.get(i);
                for(int j = 0; j < ySize; ++j) {
                    boolean teSameAtrybutyWartosci = true;
                    for(int k : kombinacje.get(kombinacjeIteracji))
                        if(atrybuty[obj][k] != atrybuty[j][k])
                            teSameAtrybutyWartosci = false;
                    if(teSameAtrybutyWartosci)
                        if(decyzje[obj] != decyzje[j])
                            prawidlowy = false;
                }
                if(prawidlowy) {
                    System.out.print("o"+(obj+1)+": ");
                    for(int j = 0; j < kombinacje.get(kombinacjeIteracji).length; ++j) {
                        int attribute = kombinacje.get(kombinacjeIteracji)[j];
                        System.out.print("(a"+(attribute+1)+" = "+atrybuty[obj][attribute]+") ");
                        if(j < kombinacje.get(kombinacjeIteracji).length-1)
                            System.out.print("AND ");
                    }
                    System.out.print("--> d = "+decyzje[obj]);
                    System.out.print(" wyrzucamy z rozwazan");
                    ArrayList<Integer> doUsunieciaZRozwazan = new ArrayList<>();
                    for(int j = 0; j < ySize; ++j) {
                        boolean teSameAtrybutyWartosci = true;
                        for(int k : kombinacje.get(kombinacjeIteracji))
                            if(atrybuty[obj][k] != atrybuty[j][k])
                                teSameAtrybutyWartosci = false;
                        if(teSameAtrybutyWartosci)
                            doUsunieciaZRozwazan.add(j);
                    }
                    if(doUsunieciaZRozwazan.size() > 1)
                        System.out.print(" : ");
                    else
                        System.out.print(" : ");
                    for(int j : doUsunieciaZRozwazan)
                        System.out.print("o"+(j+1)+" ");
                    System.out.println();
                    for(int k = doUsunieciaZRozwazan.size()-1; k >= 0; --k) {
                        rozwazania.remove(doUsunieciaZRozwazan.get(k));
                    }
                    if(doUsunieciaZRozwazan.size() > 0) {
                        i = -1;
                    }
                }
            }
        }
    }
    private static void generujKombinacjeHelper(ArrayList<int[]> kombinacje, int [] data, int start, int end, int index) {
        if (index == data.length) {
            int[] kombinacja = data.clone();
            kombinacje.add(kombinacja);
        } else if (start <= end) {
            data[index] = start;
            generujKombinacjeHelper(kombinacje, data, start + 1, end, index + 1);
            generujKombinacjeHelper(kombinacje, data, start + 1, end, index);
        }
    }
    public static ArrayList<int[]> generujKombinacje(int n, int r) {
        ArrayList<int[]> kombinacje = new ArrayList<>();
        generujKombinacjeHelper(kombinacje, new int[r], 0, n-1, 0);
        return kombinacje;
    }
}
