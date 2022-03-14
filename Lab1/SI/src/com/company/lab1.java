import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class lab1 {

    public static void main(String[] args) {
        String path_to_csv = "D:\\SI\\dane\\Churn_Modelling.csv";
        String path_to_decision_system = "D:\\SI\\dane\\diabetes.txt";
        String path_to_types_in_decision_system = "D:\\SI\\dane\\diabetes-type.txt";

        String [][] decision_system = load_decision_system(read_lines(path_to_decision_system), " ");


        // Zadanie 3A
        List<String> decision_classes = zadanie_3_a(decision_system);
        System.out.println("\n Zadanie 3 A");
        System.out.println("Dostępne klasy decyzjnye:");
        for(String i : decision_classes)
            System.out.println("\t- "+i);


        // Zadanie 3B
        HashMap<String, Integer> size_of_decision_classes = zadanie_3_b(decision_system, decision_classes);
        System.out.println("\n Zadanie 3 B");
        System.out.println("Wielkości decyzyjnych klas:");
        for(String i : size_of_decision_classes.keySet()) {
            System.out.println("\t- "+i+" - "+size_of_decision_classes.get(i));
        }


        // Zadanie 3C
        String [] names_of_attributes = load_names_of_attributes(read_lines(path_to_types_in_decision_system));
        String [] type_of_attributes = load_type_of_attributes(read_lines(path_to_types_in_decision_system));
        int [] indexes_of_numeric = get_indexes_of_numeric_or_symbolic_attributes(type_of_attributes, "n");
        int [] indexes_of_symbolic = get_indexes_of_numeric_or_symbolic_attributes(type_of_attributes, "n");
        double [][] numeric_attributes = get_numeric_attributes(decision_system, indexes_of_numeric);
        double [][] max_and_min = zadanie_3_c(numeric_attributes);

        System.out.println("\n Zadanie 3 C");
        System.out.println("Max i min wartości poszczególnych atrybutów :");
        for(int i = 0; i < indexes_of_numeric.length; ++i) {
            System.out.println("\t"+names_of_attributes[indexes_of_numeric[i]]+":");
            System.out.println("\t\t- max - "+max_and_min[0][i]);
            System.out.println("\t\t- min - "+max_and_min[1][i]);
        }


        // Zadanie 3D
        int [] number_of_different_values =zadanie_3_d(decision_system);
        System.out.println("\n Zadanie 3 D");
        System.out.println("Liczba różnych dostępnych wartości dla atrybutów:");
        for(int i = 0; i < names_of_attributes.length; ++i)
            System.out.println("\t- "+names_of_attributes[i]+" - "+number_of_different_values[i]);


        // Zadanie 3E
        ArrayList<ArrayList<String>> different_values = zadanie_3_e(decision_system);
        System.out.println("\n Zadanie 3 E");
        System.out.println("Wszystkie różne dostępne wartości:");
        for(int i = 0; i < names_of_attributes.length; ++i)
            System.out.println("\t- "+names_of_attributes[i]+" - "+different_values.get(i));


        // Zadanie 3F
        System.out.println("\n Zadanie 3 F");
        System.out.println("Odchylenie standardowe dla atrybutów w systemie secyzyjnym:");
        double [] zadanie_3_f_first_part = std_dev_for_each_attribute_in_the_whole_system(numeric_attributes);
        for(int i = 0; i < numeric_attributes[0].length; ++i) {
            int index = indexes_of_numeric[i];
            System.out.println("\t- "+names_of_attributes[index]+" - "+zadanie_3_f_first_part[i]);
        }


        System.out.println("\n Odchylenie standardowe dla atrybutów w klasach decyzyjnych:");
        HashMap<String, double []> zadanie_3_f_second_part = std_dev_for_each_attribute_separately_for_each_decision_class(numeric_attributes, decision_system, decision_classes);
        for(String i : zadanie_3_f_second_part.keySet()) {
            System.out.println("\t- "+i+":");
            for(int j = 0; j < numeric_attributes[0].length; ++j) {
                int index = indexes_of_numeric[j];
                System.out.println("\t\t- "+names_of_attributes[index]+" - "+zadanie_3_f_second_part.get(i)[j]);
            }
        }


        // Zadanie 4A
        String [][] new_decision_system = zadanie_4_a(decision_system, indexes_of_numeric);
        double [][] new_numeric_attributes = get_numeric_attributes(new_decision_system, indexes_of_numeric);
        System.out.println("\n Zadanie 4 A");
        System.out.println("System decyzjny - najczęściej wystepujące wartości:");
        for(String [] i : new_decision_system) {
            for(String j : i)
                System.out.print(j+" ");
            System.out.println();
        }


        // Zadanie 4B
        double a = -1;
        double b = 1;
        new_numeric_attributes = zadanie_4_b(new_numeric_attributes, a, b);
        System.out.println("\n Zadanie 4 B");
        System.out.println("Wartość atrybutów numerycznych po znormalizowaniu - przedział <-1 , 1> :");
        for(double [] i : new_numeric_attributes) {
            for(double j : i)
                System.out.print(j+" ");
            System.out.println();
        }


        // Zadanie 4C
        System.out.println("\n Zadanie 4 C");
        System.out.println("Wartość atrybutów numerycznych po standaryzacji:");
        new_numeric_attributes = zadanie_4_c(new_numeric_attributes);
        for(double [] i : new_numeric_attributes) {
            for(double j : i)
                System.out.print(j+" ");
            System.out.println();
        }


        // Zadanie 4D
        String [][] csv = load_decision_system(read_lines(path_to_csv), ",");
        csv = zadanie_4_d(csv);
        System.out.println("\n Zadanie 4 D");
        System.out.println("Przeformatowane dane z pliku CM do postaci readable form:");
        for(String [] i : csv) {
            for(String j : i)
                System.out.print(j+" ");
            System.out.println();
        }
    }

    public static ArrayList<String> read_lines(String path) {
        ArrayList<String> output = new ArrayList<>();
        try {
            File obj = new File(path);
            Scanner myReader = new Scanner(obj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                output.add(data);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Błąd");
            e.printStackTrace();
        }
        return output;
    }


    public static String[][] load_decision_system (ArrayList<String> file, String delimiter) {
        int number_of_objects = file.size();
        int number_of_columns = file.get(0).split(delimiter).length;
        String [][] decision_system = new String[number_of_objects][number_of_columns];
        for(int i = 0; i < number_of_objects; ++i)
            decision_system[i] = file.get(i).split(delimiter);
        return decision_system;
    }


    public static List<String> zadanie_3_a (String[][] decision_system) {
        int number_of_columns = decision_system[0].length;
        int number_of_objects = decision_system.length;
        String [] decisions = new String[number_of_objects];
        for(int i = 0; i < number_of_objects; ++i)
            decisions[i] = decision_system[i][number_of_columns-1];
        return Arrays.stream(decisions).distinct().toList();
    }


    public static HashMap<String, Integer> zadanie_3_b(String[][] decision_system, List<String> decision_classes) {
        HashMap<String, Integer> number_of_objects_in_classes = new HashMap<>();
        int number_of_columns = decision_system[0].length;
        for(String i : decision_classes) {
            int sum = 0;
            for(String [] j : decision_system)
                if(j[number_of_columns-1].equals(i))
                    ++sum;
            number_of_objects_in_classes.put(i, sum);
        }
        return number_of_objects_in_classes;
    }


    public static String [] load_names_of_attributes(ArrayList<String> file) {
        String [] names = new String[file.size()];
        for(int i = 0; i < file.size(); ++i)
            names[i] = file.get(i).split(" ")[0];
        return names;
    }


    public static String [] load_type_of_attributes(ArrayList<String> file) {
        String [] types = new String[file.size()];
        for(int i = 0; i < file.size(); ++i)
            types[i] = file.get(i).split(" ")[1];
        return types;
    }


    public static int [] get_indexes_of_numeric_or_symbolic_attributes(String [] type_of_attributes, String symbol) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < type_of_attributes.length; ++i)
            if(type_of_attributes[i].equals(symbol))
                indexes.add(i);
        int [] array = new int[indexes.size()];
        for(int i = 0; i < indexes.size(); ++i)
            array[i] = indexes.get(i);
        return array;
    }


    public static double [][] get_numeric_attributes(String [][] decision_system, int [] indexes_of_numeric_attributes) {
        int number_of_objects = decision_system.length;
        int number_of_numeric_attributes = indexes_of_numeric_attributes.length;
        double [][] numeric_attributes = new double[number_of_objects][number_of_numeric_attributes];
        for(int i = 0; i < number_of_objects; ++i)
            for(int j = 0; j < number_of_numeric_attributes; ++j)
                numeric_attributes[i][j] = Double.parseDouble(decision_system[i][indexes_of_numeric_attributes[j]]);
        return numeric_attributes;
    }


    public static double [][] zadanie_3_c(double [][] numeric_attributes) {
        int number_of_objects = numeric_attributes.length;
        int number_of_numeric_attributes = numeric_attributes[0].length;
        double[][] max_and_min = new double[2][number_of_numeric_attributes];
        for (int i = 0; i < number_of_numeric_attributes; ++i) {
            double max = numeric_attributes[0][i];
            double min = numeric_attributes[0][i];
            for (int j = 1; j < number_of_objects; ++j) {
                if(numeric_attributes[j][i] > max)
                    max = numeric_attributes[j][i];
                if(numeric_attributes[j][i] < min)
                    min = numeric_attributes[j][i];
            }
            max_and_min[0][i] = max;
            max_and_min[1][i] = min;
        }
        return max_and_min;
    }


    public static ArrayList<ArrayList<String>> find_all_available_values_for_attributes (String [][] decision_system) {
        int number_of_objects = decision_system.length;
        int number_of_attributes = decision_system[0].length - 1;
        ArrayList<ArrayList<String>> available_values = new ArrayList<>();
        for(int i = 0; i < number_of_attributes; ++i) {
            String [] attribute = new String[number_of_objects];
            for(int j = 0; j < number_of_objects; ++j)
                attribute[j] = decision_system[j][i];
            available_values.add(Arrays.stream(attribute).distinct().collect(toCollection(ArrayList::new)));
        }
        return available_values;
    }


    public static int [] zadanie_3_d(String [][] decision_system) {
        ArrayList<ArrayList<String>> available_values = find_all_available_values_for_attributes(decision_system);
        int number_of_attributes = available_values.size();
        int [] number_of_different_values = new int[number_of_attributes];
        for(int i = 0; i < number_of_attributes; ++i)
            number_of_different_values[i] = available_values.get(i).size();
        return number_of_different_values;
    }


    public static ArrayList<ArrayList<String>> zadanie_3_e(String [][] decision_system) {
        return find_all_available_values_for_attributes(decision_system);
    }


    public static double mean(double [] array) {
        double sum = 0;
        for(double i : array)
            sum += i;
        return sum / array.length;
    }


    public static double variance (double [] array) {
        double numerator = 0;
        double mean = mean(array);
        for(double i : array)
            numerator += ((i - mean) * (i - mean));
        return numerator / array.length;
    }


    public static double std_dev(double [] array) {
        return Math.sqrt(variance(array));
    }
    public static double [] std_dev_for_each_attribute_in_the_whole_system(double [][] numeric_attributes) {
        int number_of_objects = numeric_attributes.length;
        int number_of_numeric_attributes = numeric_attributes[0].length;
        double [] output = new double[number_of_numeric_attributes];
        double [] array = new double[number_of_objects];
        for(int i = 0; i < number_of_numeric_attributes; ++i) {
            for(int j = 0; j < number_of_objects; ++j)
                array[j] = numeric_attributes[j][i];
            output[i] = std_dev(array);
        }
        return output;
    }


    public static HashMap<String, double []> std_dev_for_each_attribute_separately_for_each_decision_class(double [][] numeric_attributes, String [][] decision_system, List<String> decision_classes) {
        int number_of_numeric_attributes = numeric_attributes[0].length;
        int number_of_objects = decision_system.length;
        HashMap<String, double []> output = new HashMap<>();
        for(String i : decision_classes) {
            int number_of_objects_in_one_decision_class = 0;
            for(String [] j : decision_system)
                if(j[decision_system[0].length - 1].equals(i))
                    ++number_of_objects_in_one_decision_class;

            double [] std_dev_for_each_attribute = new double[number_of_numeric_attributes];
            double [] array = new double[number_of_objects_in_one_decision_class];
            for(int j = 0; j < number_of_numeric_attributes; ++j) {
                int index = 0;
                for(int k = 0; k < number_of_objects; ++k) {
                    if(decision_system[k][decision_system[0].length-1].equals(i)) {
                        array[index] = numeric_attributes[k][j];
                        ++index;
                    }
                }
                std_dev_for_each_attribute[j] = std_dev(array);
            }
            output.put(i, std_dev_for_each_attribute);
        }
        return output;
    }


    public static int [] generate_ten_pre_cent_of_missing_values(int number_of_objects) {
        int [] array = new int[(int)(0.1*number_of_objects)];
        for(int i = 0; i < array.length; ++i) {
            int value = (int) (Math.random()*number_of_objects);
            boolean unique = true;
            for(int j = 0; j < i && unique; ++j)
                if(array[j] == value)
                    unique = false;
            if(unique)
                array[i] = value;
            else
                --i;
        }
        return array;
    }


    public static String najpopularniejsza(String [] array) {
        HashMap<String, Integer> counter = new HashMap<>();
        for(String i : array)
            if(!i.equals("?"))
                if(counter.get(i) == null)
                    counter.put(i, 0);
                else
                    counter.replace(i, counter.get(i)+1);
        int najpopularniejsza_wystepujaca_liczba = 0;
        String najpopularniejsza_wartosc = "";
        for(String i : counter.keySet())
            if(counter.get(i) > najpopularniejsza_wystepujaca_liczba)
                najpopularniejsza_wartosc = i;
        return najpopularniejsza_wartosc;
    }


    public static String [][] zadanie_4_a(String [][] decision_system, int [] indexes_of_numeric) {
        int number_of_objects = decision_system.length;
        int number_of_numeric_attributes = indexes_of_numeric.length;
        for(int i = 0; i < number_of_numeric_attributes; ++i) {
            int [] indexes_of_this_ten_pre_cent = generate_ten_pre_cent_of_missing_values(number_of_objects);
            for(int j : indexes_of_this_ten_pre_cent)
                decision_system[j][indexes_of_numeric[i]] = "?";
        }
        String [] array = new String[number_of_objects - ((int)(0.1*number_of_objects))];
        for(int i = 0; i < number_of_numeric_attributes; ++i) {
            int index = 0;
            for(int j = 0; j < number_of_objects; ++j)
                if(!decision_system[j][indexes_of_numeric[i]].equals("?")) {
                    array[index] = decision_system[j][indexes_of_numeric[i]];
                    ++index;
                }
            String najpopularniejsza_wartosc = najpopularniejsza(array);
            for(int j = 0; j < number_of_objects; ++j) {
                if(decision_system[j][indexes_of_numeric[i]].equals("?")) {
                    decision_system[j][indexes_of_numeric[i]] = najpopularniejsza_wartosc;
                }
            }
        }
        return decision_system;
    }


    public static double [][] zadanie_4_b(double [][] numeric_attributes, double a, double b) {
        double [] max = zadanie_3_c(numeric_attributes)[0];
        double [] min = zadanie_3_c(numeric_attributes)[1];
        int number_of_objects = numeric_attributes.length;
        int number_of_numeric_attributes = numeric_attributes[0].length;
        for(int i = 0; i < number_of_numeric_attributes; ++i)
            for(int j = 0; j < number_of_objects; ++j) {
                double a_i = numeric_attributes[j][i];
                a_i = (((a_i - min[i]) * (b - a)) / (max[i] - min[i])) + a;
                numeric_attributes[j][i] = a_i;
            }
        return numeric_attributes;
    }


    public static double [][] zadanie_4_c(double [][] numeric_attributes) {
        int number_of_objects = numeric_attributes.length;
        int number_of_numeric_attributes = numeric_attributes[0].length;
        double [] array = new double[number_of_objects];
        for(int i = 0; i < number_of_numeric_attributes; ++i) {
            for(int j = 0; j < number_of_objects; ++j)
                array[j] = numeric_attributes[j][i];
            double mean_a_i = mean(array);
            double variance_a_i = variance(array);
            for(int j = 0; j < number_of_objects; ++j)
                numeric_attributes[j][i] = (numeric_attributes[j][i] - mean_a_i) / variance_a_i;
        }
        return numeric_attributes;
    }


    public static String [][] zadanie_4_d(String [][] csv) {
        int index_of_geography = 4;
        int number_of_rows_in_csv = csv.length;
        int number_of_objects = number_of_rows_in_csv - 1;
        String [] array = new String[number_of_objects];
        for(int i = 1; i < number_of_rows_in_csv; ++i)
            array[i-1] = csv[i][index_of_geography];
        ArrayList<String> available_values = Arrays.stream(array).distinct().collect(toCollection(ArrayList::new));
        int number_of_columns_in_csv = csv[0].length;
        int number_of_columns_in_new_csv = number_of_columns_in_csv + available_values.size() - 2;
        String [][] new_csv = new String[number_of_rows_in_csv][number_of_columns_in_new_csv];
        for(int i = 0; i < index_of_geography; i++)
            for(int j = 0; j < number_of_rows_in_csv; ++j)
                new_csv[j][i] = csv[j][i];
        for(int i = index_of_geography+1; i < number_of_columns_in_csv; i++)
            for(int j = 0; j < number_of_rows_in_csv; ++j)
                new_csv[j][i+available_values.size()-2] = csv[j][i];
        for(int i = 1; i < available_values.size(); ++i) {
            new_csv[0][index_of_geography-1+i] = "Geography."+available_values.get(i);
            for(int j = 1; j < number_of_rows_in_csv; ++j)
                if(csv[j][index_of_geography].equals(available_values.get(i)))
                    new_csv[j][index_of_geography-1+i] = "1";
                else
                    new_csv[j][index_of_geography-1+i] = "0";
        }
        return new_csv;
    }
}




