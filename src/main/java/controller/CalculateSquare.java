package controller;

import dao.CalculateMWE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchen on 16-4-19.
 */
public class CalculateSquare {
    public static void main(String[] args){
        CalculateMWE calculateMWE = new CalculateMWE();
        List<Double> squ = new ArrayList<Double>();
        for(int i =10; i<101;i =i+10){
            try {
                for(int n = 0; n <i;n++){
                    String filePath = "/home/yangchen/tomcatTempData/temp" + i + "/" + n + ".txt";
                    double[] weRate = calculateMWE.getMWE(filePath, i);
                    double avg = 0.0;
                    for(int j = 0; j<weRate.length; j++){
                        avg = avg + weRate[j];
                    }
                    avg = avg/i;
                    double square = 0.0;
                    for(int k = 0; k < weRate.length; k++){
                        square = square + Math.pow(weRate[k]-avg, 2);
                    }
                    squ.add(square/i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int m =0;m<squ.size();m++){
            System.out.println(squ.get(m));
        }

    }
}
