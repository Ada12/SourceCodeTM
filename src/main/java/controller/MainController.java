package controller;

import dao.CalculateMWE;
import dao.GetClassTopic;
import dao.GetTopicWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangchen on 16-4-16.
 */
@Controller
public class MainController {
    @Autowired
    ServletContext context;

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
//        String abPath = System.getProperty("user.dir");
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
        List<Map<String, String>> listMwe = new ArrayList<Map<String, String>>();
        List<Integer> highList = new ArrayList<Integer>();
        List<Integer> lowList = new ArrayList<Integer>();
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
            Map<String, String> mwe = new HashMap<String, String>();
            mwe.put("module", module[7]);

            int end = 0;
            for(int q = 0; q < weRate.length-1; q++){
//                if(weRate[q]/weRate[q+1] < 10){
                if(weRate[q]> weRate[0]/10){
                    end = q;
                }else {
                    break;
                }
            }
            String weh = "";
            for(int o = 0; o < end+1; o ++){
                weh = weh + index[o] + ",";
                highList.add(index[o]);
            }
            mwe.put("begin", weh);

            int begin = 0;
            for(int r = weRate.length-1; r > 0; r--){
                if(weRate[r-1]/weRate[r] < 10){
//                if(weRate[r-1] < weRate[weRate.length-1]*10){
                    begin = r;
                }else {
                    break;
                }
            }
            String wel = "";
            for(int o = index.length-1; o > index.length-begin-2; o --){
                wel = wel + index[o] + ",";
                lowList.add(index[o]);
            }
            mwe.put("end", wel);

//            mwe.put("rates", weRate);
            listMwe.add(mwe);
        }

        Map<Integer, String> color = new HashMap<Integer, String>();
//        color.put(1, "#FFFFCC");
//        color.put(2, "#FFFF99");
//        color.put(3, "#FFFF66");
//        color.put(4, "#FFFF33");
//        color.put(5, "#FFFF00");
//        color.put(6, "#FFCC00");
//        color.put(7, "#FF9900");
//        color.put(8, "#FF6600");
//        color.put(9, "#FF3300");
//        color.put(10, "#FF0000");

//        color.put(1, "#FFFFCC");
//        color.put(2, "#FFCCFF");
//        color.put(3, "#FFCCCC");
//        color.put(4, "#FFCC99");
//        color.put(5, "#FF99CC");
//        color.put(6, "#FF9999");
//        color.put(7, "#FF9966");
//        color.put(8, "#FF6699");
//        color.put(9, "#FF6666");
//        color.put(10, "#FF6633");
//        color.put(11, "#FF3333");
//        color.put(12, "#FF3300");

        color.put(1, "#FFCCFF");
        color.put(2, "#FF99FF");
        color.put(3, "#FF66FF");
        color.put(4, "#FF33FF");
        color.put(5, "#FF00FF");
        color.put(6, "#FF00CC");
        color.put(7, "#FF0099");
        color.put(8, "#FF0066");
        color.put(9, "#FF0033");
        color.put(10, "#FF0000");

        Map<Integer, String> lowColor = new HashMap<Integer, String>();
        lowColor.put(1, "#33FFFF");
        lowColor.put(2, "#33CCFF");
        lowColor.put(3, "#3399FF");
        lowColor.put(4, "#3366FF");
        lowColor.put(5, "#3333FF");
        lowColor.put(6, "#33FFCC");
        lowColor.put(7, "#33CCCC");
        lowColor.put(8, "#3399CC");
        lowColor.put(9, "#3366CC");
        lowColor.put(10, "#3333CC");
        lowColor.put(11, "#3300CC");
        lowColor.put(12, "#339999");
        lowColor.put(13, "#336699");
        lowColor.put(14, "#333399");
        lowColor.put(15, "#330099");
        lowColor.put(16, "#336666");
        lowColor.put(17, "#333366");
        lowColor.put(18, "#330066");
        lowColor.put(19, "#333333");
        lowColor.put(20, "#330033");
        lowColor.put(21, "#330000");
        lowColor.put(22, "#003300");
        lowColor.put(23, "#000033");
        lowColor.put(24, "#000000");

        Map<Integer, Integer> highKey = new HashMap<Integer, Integer>();
        Map<Integer, Integer> lowKey = new HashMap<Integer, Integer>();
        for(int i = 0; i < highList.size(); i ++){
            Integer h = highKey.get(highList.get(i));
            if(h != null){
                highKey.put(highList.get(i), h+1);
            }else{
                highKey.put(highList.get(i), 1);
            }
        }
        for (int j = 0; j < lowList.size(); j ++){
            Integer l = lowKey.get(lowList.get(j));
            if(l != null){
                lowKey.put(lowList.get(j), l+1);
            }else {
                lowKey.put(lowList.get(j), 1);
            }
        }
        List<Map<String, Object>> newListMwe = new ArrayList<Map<String, Object>>();
        for(int k = 0; k < listMwe.size(); k ++){
            Map<String, Object> nlm = new HashMap<String, Object>();
            String module = listMwe.get(k).get("module");
            String begin = listMwe.get(k).get("begin");
            String end = listMwe.get(k).get("end");
            List<Map<String, String>> newBegin = new ArrayList<Map<String, String>>();
            String[] b = begin.split("\\,");
            for(int i = 0; i < b.length; i ++){
                Map<String, String> nb = new HashMap<String, String>();
                nb.put("topic", b[i]);
                Integer countH = highKey.get(Integer.valueOf(b[i]));
                nb.put("color", color.get(countH));
                newBegin.add(nb);
            }
            List<Map<String, String>> newEnd = new ArrayList<Map<String, String>>();
            String[] e = end.split("\\,");
            for(int j = 0; j < e.length; j ++){
                Map<String, String> ne = new HashMap<String, String>();
                ne.put("topic", e[j]);
                Integer countE = lowKey.get(Integer.valueOf(e[j]));
                ne.put("color", lowColor.get(countE));
                newEnd.add(ne);
            }
            nlm.put("module", module);
            nlm.put("begin", newBegin);
            nlm.put("end", newEnd);
            newListMwe.add(nlm);
        }


        result.put("result", newListMwe);
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
//        String abPath = System.getProperty("user.dir");
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
//        String abPath = System.getProperty("user.dir");
//        String filePathXml = abPath + "/topicModel/" + folder + "Xml/topic-phrases"+ topicNum +".xml";
        String filePathXml = "/usr/local/mallet-2.0.7/" + folder + "/" + folder + topicNum + "/topic-phrases.xml";
        List<List> xml = GetTopicWeight.getNames(filePathXml);
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
    public ResponseEntity<Map<String, Object>> getJsonTopicWeight(@PathVariable("topicType") String topicType, @PathVariable("topicNum") String topicNum, HttpServletRequest request) throws IOException, SAXException, ParserConfigurationException {
//        String test = context.getRealPath("/");

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

//        String abPath = System.getProperty("user.dir");
//        String filePathXml = abPath + "/topicModel/" + folder + "Xml/topic-phrases"+ topicNum +".xml";
        String filePathXml = "/usr/local/mallet-2.0.7/" + folder + "/" + folder + topicNum + "/topic-phrases.xml";
        List<List> xml = GetTopicWeight.getNames(filePathXml);

        //wordsArray 1.1.1 shows the 1st topic, the 1st phrase, the 0 words(there is a blank before all the words)
        Map<String, List<String>> wordsArray = new HashMap<String, List<String>>();
        List<String> wordsList = new ArrayList<String>();
        int[] singlePhraseCount = new int[Integer.valueOf(topicNum)];
        for(int j = 0; j < xml.get(2).size(); j ++){
            String topicContent = xml.get(2).get(j).toString().trim();
            String[] phrase = topicContent.split("\\,");
            if(topicContent.charAt(topicContent.length()-1) == ','){
                singlePhraseCount[j] = phrase.length -1;
            }else{
                singlePhraseCount[j] = phrase.length;
            }
            for(int k = 0; k < phrase.length; k ++){
                if((!phrase[k].equals(" "))&&(!phrase[k].equals(""))){
                    String[] words = phrase[k].split(" ");
                    for(int m = 0; m < words.length; m ++){
                        if((!words[m].equals(" "))&&(!words[m].equals(""))){
                            String pos = j + "." + k + "." + m;
                            List<String> position = wordsArray.get(words[m]);
                            if(position != null){
                                position.add(pos);
                                wordsArray.put(words[m],position);
                            }else{
                                List<String> p = new ArrayList<String>();
                                p.add(pos);
                                wordsArray.put(words[m], p);
                            }
                            wordsList.add(words[m]);
                        }
                    }
                }
            }
        }
        int wordsLength = wordsList.size();
        List<String> wordsListUnique = GetClassTopic.removeDuplicate(wordsList);
        double all = 0.0;
        double[] single = new double[Integer.valueOf(topicNum)];
        for(int n = 0; n < Integer.valueOf(topicNum); n ++){
            single[n] = 0.0;
        }
        for(int a = 0; a < wordsListUnique.size(); a ++){
            List<String> simplePosition = wordsArray.get(wordsListUnique.get(a));
            Map<String, List<String>> wordsAllResult = new HashMap<String, List<String>>();
            List<String> keysAll = new ArrayList<String>();
            for(int b = 0; b < simplePosition.size(); b ++){
                String[] contentAll = simplePosition.get(b).split("\\.");
                List<String> topicIDPos = wordsAllResult.get(contentAll[0]);
                if(topicIDPos != null){
                    topicIDPos.add(simplePosition.get(b));
                    wordsAllResult.put(contentAll[0], topicIDPos);
                }else{
                    List<String> tip = new ArrayList<String>();
                    tip.add(simplePosition.get(b));
                    wordsAllResult.put(contentAll[0], tip);
                    keysAll.add(contentAll[0]);
                }
            }
            all = all + wordsAllResult.size() - 1;
            for(int c = 0; c < keysAll.size(); c++){
                List<String> tpPos = wordsAllResult.get(keysAll.get(c));
                Map<String, List<String>> wordsSimpleResult = new HashMap<String, List<String>>();
                if(tpPos.size() > 1) {
                    for (int d = 0; d < tpPos.size(); d++) {
                        String[] contentSimple = tpPos.get(d).split("\\.");
                        List<String> phraseIDPos = wordsSimpleResult.get(contentSimple[1]);
                        if (phraseIDPos != null) {
                            phraseIDPos.add(simplePosition.get(d));
                            wordsSimpleResult.put(contentSimple[1], phraseIDPos);
                        } else {
                            List<String> pip = new ArrayList<String>();
                            pip.add(simplePosition.get(d));
                            wordsSimpleResult.put(contentSimple[1], pip);
                        }
                    }
                }
                //if tpPos.size >1 and the result = 1, that is the words are in the same phrase, just calculate it as 1
                int mi = Integer.valueOf(keysAll.get(c));
                if(wordsSimpleResult.size() == 1){
                    single[mi] = single[mi] + 1;
                }else if(wordsSimpleResult.size() == 0){
                    single[mi] = single[mi] + wordsSimpleResult.size();
                }else{
                    single[mi] = single[mi] + wordsSimpleResult.size() - 1;
                }
            }
        }

        double zero = 0;
        for(int i = 0; i < xml.get(0).size(); i ++){
            Map<String, Object> weight = new HashMap<String, Object>();
            weight.put("name", xml.get(0).get(i));
            weight.put("weight", xml.get(1).get(i));
            weight.put("title", xml.get(2).get(i));
            double simpleR = single[i]/singlePhraseCount[i];
            String simpleRS = "";
            if(simpleR == 0.0){
                simpleRS = "0";
                zero = zero + 1;
            }else{
                simpleRS = String.format("%.3f",simpleR);
            }
            weight.put("simple", simpleRS);
            topicWeight.add(weight);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("children", topicWeight);
        result.put("allEva", all/wordsLength);
        result.put("zeroRate", zero/Integer.valueOf(topicNum));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);

    }

}
