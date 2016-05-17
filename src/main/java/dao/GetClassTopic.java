package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchen on 16-5-6.
 */
public class GetClassTopic {

    private static List<String> filePaths;

    public static Map<String, Map<String, String>> getClassTopic(String folder, String topicNum) throws IOException {
        String filePath = "/usr/local/mallet-2.0.7/" + folder + "/" + folder + topicNum + "/composition.txt";
        File file  = new File(filePath);
        Map<String, Map<String, String>> classTopic = new HashMap<String, Map<String, String>>();
        if(file.exists()) {
            InputStream is = new FileInputStream(filePath);
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            line = reader.readLine();
            while (line != null){
                String[] content = line.split("\t");
                Map<String, String> state = new HashMap<String, String>();
                for(int i = 2; i < content.length; i=i+2){
                    if(i <= Integer.parseInt(topicNum) * 2/3){
                        state.put(content[i], "1");
                    }else if((i <= Integer.parseInt(topicNum) * 4/3)&&(i >Integer.parseInt(topicNum) * 2/3)){
                        state.put(content[i], "2");
                    }else{
                        state.put(content[i], "3");
                    }
                }
                String[] name = content[1].split("\\/");
                classTopic.put(name[6].substring(0, name[6].length()-4) ,state);
                line = reader.readLine();
            }
        }
        return classTopic;
    }

    public static Map<String, List<String>> getClassNames(int flag) throws IOException {
        Map<String, List<String>> moduleNames = new HashMap<String, List<String>>();
        if(flag == 2){
            File[] files = new File("/home/yangchen/ycdoc/topicModel/tribestri/").listFiles();
            List<String> paths = new ArrayList<String>();
            GetClassTopic.setFilePaths(paths);
            showFiles(files);
            for(int i = 0; i < filePaths.size(); i ++){
                String filePath = filePaths.get(i);
                // get module names notice the url if change sth
                String moduleName = filePaths.get(i).split("\\/|\\.")[6];
                List<String> names = new ArrayList<String>();
                File file  = new File(filePath);
                if(file.exists()) {
                    InputStream is = new FileInputStream(filePath);
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    line = reader.readLine();
                    while (line != null) {
                        String[] content = line.split("\\/");
                        names.add(content[6].substring(0, content[6].length() - 4));
                        line = reader.readLine();
                    }
                }
                moduleNames.put(moduleName, names);
            }
        }else{
            File[] files = new File("/home/yangchen/ycdoc/topicModel/tomcatcata/").listFiles();
            List<String> paths = new ArrayList<String>();
            GetClassTopic.setFilePaths(paths);
            showFiles(files);
            for(int i = 0; i < filePaths.size(); i ++){
                String filePath = filePaths.get(i);
                // get module names notice the url if change sth
                String moduleName = filePaths.get(i).split("\\/|\\.")[6];
                List<String> names = new ArrayList<String>();
                File file  = new File(filePath);
                if(file.exists()) {
                    InputStream is = new FileInputStream(filePath);
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    line = reader.readLine();
                    while (line != null) {
                        String[] content = line.split("\\/");
                        names.add(content[6].substring(0, content[6].length()-4));
                        line = reader.readLine();
                    }
                }
                moduleNames.put(moduleName, names);
            }
        }
        return moduleNames;
    }

    public static List<String> getModule(int flag){
        List<String> modules = new ArrayList<String>();
        if(flag == 2){
            File[] files = new File("/home/yangchen/ycdoc/topicModel/tomcatcata/").listFiles();
            List<String> paths = new ArrayList<String>();
            GetClassTopic.setFilePaths(paths);
            showFiles(files);
            for(int i = 0; i < filePaths.size(); i ++) {
                String module = filePaths.get(i).split("\\/|\\.")[6];
                modules.add(module);
            }
        }else{
            File[] files = new File("/home/yangchen/ycdoc/topicModel/tomcatcata/").listFiles();
            List<String> paths = new ArrayList<String>();
            GetClassTopic.setFilePaths(paths);
            showFiles(files);
            for(int i = 0; i < filePaths.size(); i ++) {
                String module = filePaths.get(i).split("\\/|\\.")[6];
                modules.add(module);
            }
        }
        return modules;
    }

    private static void showFiles(File[] files) {
        for(File file : files){
            if(file.isDirectory()){
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles());
            }else{
                System.out.println(file.getAbsolutePath());
                filePaths.add(file.getAbsolutePath());
            }
        }
    }

    public static List<String> getFilePaths() {
        return filePaths;
    }

    public static void setFilePaths(List<String> filePaths) {
        GetClassTopic.filePaths = filePaths;
    }

}
