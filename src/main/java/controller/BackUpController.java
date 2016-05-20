package controller;

import dao.CalculateMWE;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchen on 16-5-18.
 */
@Controller
public class BackUpController {

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



}
