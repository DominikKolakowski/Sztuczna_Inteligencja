import java.lang.Math;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Zadanie 1");
        boolean [][] logicVars = generateLogicVar(3);
        String [] namesOfLogicVars = {"p", "q", "r"};
        // "¬", "∧", "∨", "⇒", "⇔", "(", ")"
        String S = "(p ∨ ¬q) ∧ (r ∨ ¬p)";
        drawFirstTask(logicVars, namesOfLogicVars, S);
    }

    public static Boolean plTrue(String S, HashMap<String, Integer> m) {
        if(S.indexOf("(") != -1) {
            while (S.indexOf("(") != -1) {
                Integer indexOfCloseBracket = S.indexOf(")");
                Integer indexOfOpenBracket = null;
                for (Integer i = indexOfCloseBracket; i >= 0 && indexOfOpenBracket == null; --i)
                    if (("" + S.charAt(i)).equals("(")) {
                        indexOfOpenBracket = i;
                    }
                String StringWhioutBracekts = S.substring(indexOfOpenBracket+1, indexOfCloseBracket);
                if(plTrueWithoutBrackets(StringWhioutBracekts, m))
                    S = S.substring(0, indexOfOpenBracket)+"1"+S.substring(indexOfCloseBracket+1);
                else
                    S = S.substring(0, indexOfOpenBracket)+"0"+S.substring(indexOfCloseBracket+1);
            }
            return plTrueWithoutBrackets(S, m);
        } else {
            return plTrueWithoutBrackets(S, m);
        }
    }

    public static Boolean plTrueWithoutBrackets(String S, HashMap<String, Integer> m) {
        String [] binaryOperators = {"∧", "∨", "⇒", "⇔"};
        S = S.replace(" ", "");
        for(String i : m.keySet()) {
            S = S.replace(i, m.get(i).toString());
        }
        while(S.indexOf("¬") != -1) {
            Integer index = S.indexOf("¬");
            String value = ""+S.charAt(index+1);
            if(value.equals("1"))
                S = S.substring(0,index)+"0"+S.substring(index+2);
            else
                S = S.substring(0,index)+"1"+S.substring(index+2);
        }
        for(String operator : binaryOperators) {
            while (S.indexOf(operator) != -1) {
                Integer index = S.indexOf(operator);
                String firstArgument = "" + S.charAt(index-1);
                String secondArgument = "" + S.charAt(index+1);
                if(operator.equals("∧")) {
                    if(firstArgument.equals("1") && secondArgument.equals("1"))
                        S = S.substring(0, index-1)+"1"+S.substring(index+2);
                    else
                        S = S.substring(0, index-1)+"0"+S.substring(index+2);;
                }
                if(operator.equals("∨")) {
                    if(firstArgument.equals("0") && secondArgument.equals("0"))
                        S = S.substring(0, index-1)+"0"+S.substring(index+2);
                    else
                        S = S.substring(0, index-1)+"1"+S.substring(index+2);;
                }
                if(operator.equals("⇒")) {
                    if(firstArgument.equals("1") && secondArgument.equals("0"))
                        S = S.substring(0, index-1)+"0"+S.substring(index+2);
                    else
                        S = S.substring(0, index-1)+"1"+S.substring(index+2);;
                }
                if(operator.equals("⇔")) {
                    if(firstArgument.equals(secondArgument))
                        S = S.substring(0, index-1)+"1"+S.substring(index+2);
                    else
                        S = S.substring(0, index-1)+"0"+S.substring(index+2);;
                }
            }
        }
        if(S.equals("1"))
            return true;
        else
            return false;
    }

    public static void drawFirstTask (boolean [][] logicVars, String [] namesOfVars, String S) {
        for(String i : namesOfVars)
            System.out.print(" "+i+" │");
        System.out.println(" "+S);
        for(int i = 0; i < logicVars.length; ++i) {
            HashMap<String, Integer> m = new HashMap<>();
            for(int j = 0; j < logicVars[0].length; ++j) {
                if(logicVars[i][j]) {
                    System.out.print(" 1 │");
                    m.put(namesOfVars[j], 1);
                }
                else {
                    System.out.print(" 0 │");
                    m.put(namesOfVars[j], 0);
                }
            }
            for(int j = 0; j < S.length()/2; ++j)
                System.out.print(" ");
            if(plTrue(S, m))
                System.out.println(1);
            else
                System.out.println(0);
        }
    }

    public static boolean [][] generateLogicVar(int numberOfVariables) {
        int numberOfPossibleAssignments = (int) Math.pow(2, numberOfVariables);
        boolean [][] array = new boolean[numberOfPossibleAssignments][numberOfVariables];

        for(int i = numberOfVariables-1; i >= 0; --i) {
            int helper = ((int) Math.pow(2, numberOfVariables-i));
            for(int j = 0; j < numberOfPossibleAssignments; ++j) {
                if((j % helper) < (helper/2))
                    array[j][i] = false;
                else
                    array[j][i] = true;
            }
        }
        return array;
    }
}
