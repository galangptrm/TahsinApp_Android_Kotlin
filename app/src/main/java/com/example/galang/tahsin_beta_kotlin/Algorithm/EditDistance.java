package com.example.galang.tahsin_beta_kotlin.Algorithm;
import java.util.ArrayList;

public class EditDistance {

    public ArrayList textList = new ArrayList();

    /**
     * Uses recursion to find minimum edits
     */
    public int recEditDistance(char[]  str1, char str2[], int len1,int len2){

        if(len1 == str1.length){
            return str2.length - len2;
        }
        if(len2 == str2.length){
            return str1.length - len1;
        }
        return min(recEditDistance(str1, str2, len1 + 1, len2 + 1) + str1[len1] == str2[len2] ? 0 : 1, recEditDistance(str1, str2, len1, len2 + 1) + 1, recEditDistance(str1, str2, len1 + 1, len2) + 1);
    }

    /**
     * Uses bottom up DP to find the edit distance
     */
    public int dynamicEditDistance(char[] str1, char[] str2){
        int temp[][] = new int[str1.length+1][str2.length+1];

        for(int i=0; i < temp[0].length; i++){
            temp[0][i] = i;
        }

        for(int i=0; i < temp.length; i++){
            temp[i][0] = i;
        }

        for(int i=1;i <=str1.length; i++){
            for(int j=1; j <= str2.length; j++){
                if(str1[i-1] == str2[j-1]){
                    temp[i][j] = temp[i-1][j-1];
                }else{
                    temp[i][j] = 1 + min(temp[i-1][j-1], temp[i-1][j], temp[i][j-1]);
                }
            }
        }
        printActualEdits(temp, str1, str2);
        return temp[str1.length][str2.length];

    }

    /**
     * Prints the actual edits which needs to be done.
     */
    public void printActualEdits(int T[][], char[] str1, char[] str2) {
        int i = T.length - 1;
        int j = T[0].length - 1;

        while(true) {
            if (i==0||j==0){
                break;
            }
            if (str1[i-1] == str2[j-1]) {
                //move diagonally to upper left
                i = i-1;
                j = j-1;
            } else if (T[i][j] == T[i-1][j-1] + 1){
                System.out.println("Edit " + str2[j-1] +"("+j+") in string2 to " + str1[i-1]);
                textList.add("\"Edit \" + str2[j-1] +\"(\"+j+\") in string2 to \" + str1[i-1]");
                i = i-1;
                j = j-1;
            } else if (T[i][j] == T[i-1][j] + 1) {
                System.out.println("Add in string2 "+str1[i-1]);
                textList.add("Add in string2 "+str1[i-1]);
                i = i-1;
            } else if (T[i][j] == T[i][j-1] + 1){
                System.out.println("Delete in string2 " + str2[j-1]);
                textList.add("Delete in string2 " + str2[j-1]);
                j = j -1;
            }
            else{
                throw new IllegalArgumentException("Some wrong with given data");
            }
        }

        while (true){
            if (j>0){
                System.out.println("Delete in string2 " + str2[j-1]);
                textList.add("Delete in string2 " + str2[j-1]);
                j = j-1;
            } else if(i>0) {
                System.out.println("Add in string2 "+str1[i-1]);
                textList.add("Add in string2 "+str1[i-1]);
                i=i-1;
            } else{
                break;
            }
        }

    }

    private int min(int a,int b, int c){
        int l = Math.min(a, b);
        return Math.min(l, c);
    }

    public static void main(String args[]){
        String str1 = "abbc";
        String str2 = "acbc";
        EditDistance editDistance = new EditDistance();
        int result = editDistance.dynamicEditDistance(str1.toCharArray(), str2.toCharArray());
        System.out.print(result);
    }

    public int execute(String stringPrimer, String stringSekunder){
        String str1 = stringPrimer;
        String str2 = stringSekunder;
        EditDistance editDistance = new EditDistance();
        int result = editDistance.dynamicEditDistance(str1.toCharArray(), str2.toCharArray());
        return result;
    }

}
