package dao;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchen on 16-4-16.
 */
public class CalculateMWE {

    public double[] getMWE(String filePath,int topicNum) throws IOException {
        int count = 0;
        File file  = new File(filePath);
        if(file.exists()){
            InputStream is = new FileInputStream(filePath);
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            double[] topicRate = new double[topicNum];
            for(int d = 0; d < topicRate.length; d ++){
                topicRate[d] = 0.0;
            }
            while (line != null){
                count = count + 1;
                String[] rates = line.split("\t");
                for(int i = 0; i < topicRate.length; i ++){
                    topicRate[i] = topicRate[i] + Double.valueOf(rates[i*2+3]);
                }
                line = reader.readLine();
            }
            reader.close();
            is.close();
            double[] occupancy = new double[topicNum];
            for(int j = 0; j < occupancy.length; j ++){
                occupancy[j] = topicRate[j]/count;
            }
//        for(int o = 0; o < occupancy.length; o ++){
//            System.out.println(occupancy[o]);
//        }
            InputStream isn = new FileInputStream(filePath);
            BufferedReader readern = new BufferedReader(new InputStreamReader(isn));
            String newline;
            newline = readern.readLine();
            double[] distribution = new double[topicNum];
            for(int q = 0; q < distribution.length; q ++){
                distribution[q] = 0.0;
            }
            while (newline != null){
                String[] rate = newline.split("\t");
                for(int k = 0; k < distribution.length; k ++) {
                    double qt = Double.valueOf(rate[k*2+3])/topicRate[k];
//                System.out.println(qt);
                    distribution[k] = distribution[k] + (-qt) *(Math.log(qt))/(Math.log(count));
                }
                newline = readern.readLine();
            }
//        for(int m = 0; m < distribution.length; m ++){
//            System.out.println(distribution[m]);
//        }
            readern.close();
            isn.close();
            double[] MWE = new double[topicNum];
            for(int we = 0; we < MWE.length; we ++){
                MWE[we] = occupancy[we]*occupancy[we];
            }
            return MWE;
        }
        else{
            double[] fail = new double[topicNum];
            for(int f = 0; f < fail.length; f ++){
                fail[f] = 0.0;
            }
            return fail;
        }

    }
}
