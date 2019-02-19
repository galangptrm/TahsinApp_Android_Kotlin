package com.example.galang.tahsin_beta_kotlin.Algorithm;
import java.util.ArrayList;

public class LevensteinDistance {

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
                //move diagonally to upper left
                //Ganti str2 dengan str1
                i = i-1;
                j = j-1;
            } else if (T[i][j] == T[i-1][j] + 1) {
                //move to upper
                //kekurangan huruf, tambahkan str1
                i = i-1;
            } else if (T[i][j] == T[i][j-1] + 1){
                //move to left
                //hapus huruf pada str2
                j = j -1;
            }
            else{
                throw new IllegalArgumentException("Some wrong with given data");
            }
        }

        while (true){
            if (j>0){
                //move to left
                //hapus huruf di akhir str2
                j = j-1;
            } else if(i>0) {
                //move to upper
                //tambahkan huruf di akhir str1
                i=i-1;
            } else{
                break;
            }
        }

    }

    private int min(int a,int b, int c){
        //cari nilai minimal dari atas, kiri & serong kiri atas
        int l = Math.min(a, b);
        return Math.min(l, c);
    }

}
