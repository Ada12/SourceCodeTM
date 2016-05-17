package controller;

import dao.CalculateMWE;
import dao.GetClassTopic;
import dao.GetTopicWeight;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangchen on 16-4-16.
 */
@Controller
public class MainController {
    @RequestMapping(value = "class/{topicNum}/{classId}", method = RequestMethod.GET)
    public String getMain() {
        return "class";
    }

    @RequestMapping(value = "topic/{topicNum}", method = RequestMethod.GET)
    public String getTopics() {
        return "allTopics";
    }

    @RequestMapping(value = "square", method = RequestMethod.GET)
    public String getSquare() {
        return "Square";
    }

    @RequestMapping(value = "rtc", method = RequestMethod.GET)
    public String getRtcs() {
        return "RTCs";
    }

    @RequestMapping(value = "catalina/{topicNum}/{classId}", method = RequestMethod.GET)
    public String getCatalina() {
        return "catalina";
    }

    @RequestMapping(value = "topicWeight", method = RequestMethod.GET)
    public String getWeight(){
        return "TopicWeight";
    }

    @RequestMapping(value = "topicWeight/{topicType}/{topicNum}", method = RequestMethod.GET)
    public String getTopicWeight(){
        return "TopicWeight";
    }

    @RequestMapping(value = "topicDetail/{topicId}", method = RequestMethod.GET)
    public String getTopicDetail(){
        return "TopicDetail";
    }

    @RequestMapping(value = "classTopic/{topicType}/{topicNum}", method = RequestMethod.GET)
    public String getClassTopic(){
        return "ClassTopic";
    }

    @RequestMapping(value = "stackMWE/{topicType}/{topicNum}/{moduleName}", method = RequestMethod.GET)
    public String getStackMWE() {
        return "StackMWE";
    }

    @RequestMapping(value = "json/higherMWE/{topicType}/{topicNum}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getHigherMWE(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String folder;
        String filePath;
        if("0".equalsIgnoreCase(topicType)){
            // 0 is full
            folder = "tomcat";
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnTomcat/tomcat" + topicNum + "/";
        }else if ("1".equalsIgnoreCase(topicType)){
            //1 is part
            folder = "catalina";
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnCatalina/catalina" + topicNum + "/";
        }else{
            folder = "tribes";
            // must change
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnTribes/tribes" + topicNum + "/";
        }
        File[] filePaths = new File(filePath).listFiles();
        List<Map<String, Object>> listMwe = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < filePaths.length; i ++){
            CalculateMWE calculateMWE = new CalculateMWE();
            double[] weRate = calculateMWE.getMWE(filePaths[i].getAbsolutePath(), Integer.parseInt(topicNum));
            double[] originRate = new double[weRate.length];
            for(int m = 0; m < weRate.length; m ++){
                originRate[m] = weRate[m];
            }
            int flag;
            double value;
            for (int j = 0; j < weRate.length; j++) {
                flag = j;
                value = weRate[j];
                for(int k = j; k < weRate.length; k++){
                    if(weRate[k] > value){
                        value = weRate[k];
                        flag = k;
                    }
                }
                double temp = weRate[j];
                weRate[j] = weRate[flag];
                weRate[flag] = temp;
            }
            //array index is the index of the sorted rates
            int[] index = new int[weRate.length];
            for(int n = 0; n < weRate.length; n ++){
                for(int p = 0; p < originRate.length; p ++){
                    if(weRate[n] == originRate[p]){
                        index[n] = p;
                    }
                }
            }
            String[] module = filePaths[i].getAbsolutePath().split("\\/|\\.");
            Map<String, Object> mwe = new HashMap<String, Object>();
            mwe.put("module", module[7]);
            int end = 0;
            for(int q = 0; q < 4; q++){
                if(weRate[q]/weRate[q+1] < 10){
                    end = q;
                }else {
                    break;
                }
            }
            String wer = "";
            for(int o = 0; o < end+1; o ++){
                wer = wer + index[o] + ",";
            }
            mwe.put("topic", wer);
//            mwe.put("rates", weRate);
            listMwe.add(mwe);
        }
        result.put("result", listMwe);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/stackMWE/{topicType}/{topicNum}/{moduleName}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getJsonStackMWE(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum, @PathVariable("moduleName") String moduleName) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String folder;
        String filePath;
        if("0".equalsIgnoreCase(topicType)){
            // 0 is full
            folder = "tomcat";
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnTomcat/tomcat" + topicNum + "/" + moduleName + ".txt";
        }else if ("1".equalsIgnoreCase(topicType)){
            //1 is part
            folder = "catalina";
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnCatalina/catalina" + topicNum + "/" + moduleName + ".txt";
        }else{
            folder = "tribes";
            // must change
            filePath = "/home/yangchen/ycdoc/topicModel/basedOnTribes/tribes" + topicNum + "/" + moduleName + ".txt";
        }
        Map<String, Map<String, String>> classTopic = GetClassTopic.getClassTopic(folder, topicNum);
        Map<String, List<String>> moduleNames = GetClassTopic.getClassNames(Integer.parseInt(topicType));

//        List<String> modules = GetClassTopic.getModule();
//        Map<String, String> modulesId = new HashMap<String, String>();
//        for(int i = 0; i < modules.size(); i ++){
//            modulesId.put(String.valueOf(i), modules.get(i));
//        }

        List<String> mns = moduleNames.get(moduleName);
        String highStr = "";
        String centerStr = "";
        String lowStr = "";
        if(!mns.equals(null)){
            Map<Integer, Double> high = new HashMap<Integer, Double>();
            Map<Integer, Double> center = new HashMap<Integer, Double>();
            Map<Integer, Double> low = new HashMap<Integer, Double>();
            for(int m = 0; m < Integer.parseInt(topicNum); m ++){
                high.put(m, 0.0);
                center.put(m, 0.0);
                low.put(m, 0.0);
            }
            for(int j = 0; j < mns.size(); j ++){
                Map<String, String> topicLevel = classTopic.get(mns.get(j));
                for(int k = 0; k < topicLevel.size(); k ++){
                    if("1".equalsIgnoreCase(topicLevel.get(String.valueOf(k)))){
                        double highBefore = high.get(k);
                        high.put(k, highBefore + 1);
                    }else if("2".equalsIgnoreCase(topicLevel.get(String.valueOf(k)))){
                        double centerBeore = center.get(k);
                        center.put(k, centerBeore + 1);
                    }else{
                        double lowBefore = low.get(k);
                        low.put(k, lowBefore + 1);
                    }
                }
            }
            for(int n = 0; n < Integer.parseInt(topicNum); n ++){
                highStr = highStr + high.get(Integer.valueOf(n))/mns.size() + "-";
                centerStr = centerStr + center.get(Integer.valueOf(n))/mns.size() + "-";
                lowStr = lowStr + low.get(Integer.valueOf(n))/mns.size() + "-";
            }
        }

        Map<String, String> we = new HashMap<String, String>();
        CalculateMWE calculateMWE = new CalculateMWE();
        double[] weRate = calculateMWE.getMWE(filePath, Integer.parseInt(topicNum));
        String wer = "";
        for (int i = 0; i < weRate.length; i++) {
            wer = wer + weRate[i] + ",";
        }

        Map<String, String> detail = new HashMap<String, String>();
        detail.put("high", highStr);
        detail.put("center", centerStr);
        detail.put("low", lowStr);
        detail.put("rates", wer);
        result.put("result", detail);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/classTopic/{topicType}/{topicNum}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getJsonClassTopic(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String folder;
        if("0".equalsIgnoreCase(topicType)){
            // 0 is full
            folder = "tomcat";
        }else if("1".equalsIgnoreCase(topicType)){
            //0 is part
            folder = "catalina";
        }else{
            folder = "tribes";
        }
        Map<String, Map<String, String>> classTopic = GetClassTopic.getClassTopic(folder, topicNum);
        Map<String, List<String>> moduleNames = GetClassTopic.getClassNames(Integer.parseInt(topicType));
        List<String> modules = GetClassTopic.getModule(Integer.parseInt(topicType));
        List<Map<String, Object>> detail = new ArrayList<Map<String, Object>>();
        for(int k = 0; k < modules.size(); k++){
            List<String> moduleName = moduleNames.get(modules.get(k));
            List<Map<String, String>> lc = new ArrayList<Map<String, String>>();
            for(int i = 0; i < moduleName.size(); i ++){
                Map<String, String> state = classTopic.get(moduleName.get(i));
                String c = "";
                for(int j = 0; j < Integer.parseInt(topicNum); j ++){
                    c = c + state.get(String.valueOf(j)) + "-";
                }
                String[] m = moduleName.get(i).split("\\.");
                String module = m[m.length-1];
                Map<String, String> content = new HashMap<String, String>();
                content.put("name", module);
                content.put("content", c);
                lc.add(content);
            }
            Map<String, Object> dm = new HashMap<String, Object>();
            dm.put("module", modules.get(k));
            dm.put("classes", lc);
            detail.add(dm);
        }
        result.put("result", detail);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/topicWeight/{topicType}/{topicNum}/{topicId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getJsonTopicSimple(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum, @PathVariable("topicId") String topicId) throws IOException, SAXException, ParserConfigurationException {
        String folder;
        if("0".equalsIgnoreCase(topicType)){
            // 0 is full
            folder = "tomcat";
        }else if("1".equalsIgnoreCase(topicType)){
            //0 is part
            folder = "catalina";
        }else{
            folder = "tribes";
        }
        List<List> xml = GetTopicWeight.getNames("/usr/local/mallet-2.0.7/"+ folder + "/" + folder + topicNum +"/topic-phrases.xml");
        Map<String, Object> topicSimple = new HashMap<String, Object>();
        for(int i = 0; i < xml.get(0).size(); i ++){
            if(i == Integer.parseInt(topicId)){
                topicSimple.put("title", xml.get(2).get(i));
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", topicSimple);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/topicWeight/{topicType}/{topicNum}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getJsonTopicWeight(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum) throws IOException, SAXException, ParserConfigurationException {
        List<Map<String, Object>> topicWeight = new ArrayList<Map<String, Object>>();
        String folder;
        if("0".equalsIgnoreCase(topicType)){
            // 0 is full
            folder = "tomcat";
        }else if("1".equalsIgnoreCase(topicType)){
            //0 is part
            folder = "catalina";
        }else{
            folder = "tribes";
        }

        List<List> xml = GetTopicWeight.getNames("/usr/local/mallet-2.0.7/"+ folder + "/" + folder + topicNum +"/topic-phrases.xml");

        for(int i = 0; i < xml.get(0).size(); i ++){
            Map<String, Object> weight = new HashMap<String, Object>();
            weight.put("name", xml.get(0).get(i));
            weight.put("weight", xml.get(1).get(i));
            weight.put("title", xml.get(2).get(i));
            topicWeight.add(weight);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("children", topicWeight);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);

    }

    @RequestMapping(value = "json/catalina/{topicNum}/{classId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getCatalinaMWE(@PathVariable("topicNum") String topicNum, @PathVariable("classId") String classId) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> we = new HashMap<String, String>();
        Map<String, String> keyNames = new HashMap<String, String>();
        keyNames.put("0", "0");
        keyNames.put("1", "ant");
        keyNames.put("2", "authenticator");
        keyNames.put("3", "comet");
        keyNames.put("4", "connector");
        keyNames.put("5", "core");
        keyNames.put("6", "deploy");
        keyNames.put("7", "filters");
        keyNames.put("8", "ha");
        keyNames.put("9", "loader");
        keyNames.put("10", "manager");
        keyNames.put("11", "mbeans");
        keyNames.put("12", "realm");
        keyNames.put("13", "security");
        keyNames.put("14", "servlets");
        keyNames.put("15", "session");
        keyNames.put("16", "ssi");
        keyNames.put("17", "startup");
        keyNames.put("18", "tribes");
        keyNames.put("19", "users");
        keyNames.put("20", "util");
        keyNames.put("21", "valves");
        keyNames.put("22", "websocket");
        String className = keyNames.get(classId);
        // origin -> cataModuleResult
        String filePath = "/home/yangchen/ycdoc/cataModuleNewResult/topic" + topicNum + "/" + className + ".txt";
        CalculateMWE calculateMWE = new CalculateMWE();
        double[] weRate = calculateMWE.getMWE(filePath, Integer.parseInt(topicNum));
        String wer = "";
        for (int i = 0; i < weRate.length; i++) {
            wer = wer + weRate[i] + ",";
        }
        wer.substring(0, wer.length()-1);
        we.put("classId", className);
        we.put("rates", wer);
        result.put("Code", 0);
        result.put("Msg", "Success");
        result.put("result", we);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/class/{topicNum}/{classId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getMWE(@PathVariable("topicNum") String topicNum, @PathVariable("classId") String classId) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> we = new HashMap<String, String>();
        String filePath = "/home/yangchen/tomcatTempData/temp" + topicNum + "/" + classId + ".txt";
        CalculateMWE calculateMWE = new CalculateMWE();
        double[] weRate = calculateMWE.getMWE(filePath, Integer.parseInt(topicNum));
        String wer = "";
        for (int i = 0; i < weRate.length; i++) {
            wer = wer + weRate[i] + ",";
        }
        wer.substring(0, wer.length()-1);
        we.put("classId", classId);
        we.put("rates", wer);
        result.put("Code", 0);
        result.put("Msg", "Success");
        result.put("result", we);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/topic/{topicNum}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getTopicRate(@PathVariable("topicNum") String topicNum) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> we = new HashMap<String, String>();
        String mwe = "";
        for(int i=0; i < Integer.parseInt(topicNum); i++){
            String filePath = "/home/yangchen/tomcatTempData/temp"+ topicNum +"/" + i + ".txt";
            CalculateMWE calculateMWE = new CalculateMWE();
            double[] weRate = calculateMWE.getMWE(filePath, Integer.parseInt(topicNum));
            double flag = weRate[0];
            for(int j=1; j < weRate.length; j++){
                if(weRate[j] > flag){
                    flag = weRate[j];
                }
            }
            mwe = mwe + String.valueOf(flag) + ",";
        }
        we.put("classId", topicNum);
        we.put("rates", mwe);
        result.put("Code", 0);
        result.put("Msg", "Success");
        result.put("result", we);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "json/square", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getSquareRate() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> we = new HashMap<String, String>();
        String mwe = "";
        for(int n = 10; n < 101; n=n+10){
            double[] max = new  double[n];
            for(int i=0; i < n; i++){
                String filePath = "/home/yangchen/tomcatTempData/temp"+ n +"/" + i + ".txt";
                CalculateMWE calculateMWE = new CalculateMWE();
                double[] weRate = calculateMWE.getMWE(filePath, n);
                double flag = weRate[0];
                for(int j=1; j < weRate.length; j++){
                    if(weRate[j] > flag){
                        flag = weRate[j];
                    }
                }
                //calculate max
                max[i] = flag;
            }
            double avg = 0.0;
            for(int j = 0; j<n; j++){
                avg = avg + max[j];
            }
            double navg = avg/n;
            double square = 0.0;
            for(int k = 0; k < n; k++){
                square = square + Math.pow(max[k]-navg, 2);
            }
            mwe = mwe + square/n + ",";
        }
        we.put("rates", mwe);
        result.put("Code", 0);
        result.put("Msg", "Success");
        result.put("result", we);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "json/rtc", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getRtcRate() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> we = new HashMap<String, String>();
        String mwe = "0.1,0.05,0.033,0.025,0.02,0.017,0.0143,0.0125,0.011,0.01";
        we.put("rates", mwe);
        result.put("Code", 0);
        result.put("Msg", "Success");
        result.put("result", we);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

}
