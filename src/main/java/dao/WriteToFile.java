package dao;

import java.io.*;

/**
 * Created by yangchen on 16-4-16.
 */
public class WriteToFile {

    //write string to file
    public static void contentToTextFile(String filePath, String content) {
        String str = new String(); // original content
        String s1 = new String();// update content
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print(f.getName() + " exits");
            } else {
                System.out.print(f.getName() + " not exits");
                f.createNewFile();// create one
            }
            BufferedReader input = new BufferedReader(new FileReader(f));

            while ((str = input.readLine()) != null) {
                s1 += str + "\n";
            }
            System.out.println(s1);
            input.close();
            s1 += content;

            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
