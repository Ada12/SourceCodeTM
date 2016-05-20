package controller;

/**
 * Created by yangchen on 16-5-13.
 */
public class Test {
    public static void main(String[] args) {
//        for (int i = 5; i < 101; i++) {
//            System.out.println("bin/mallet train-topics --input catalina.mallet  --num-topics "+ i +" --num-top-words 10 --num-iterations 10000 --optimize-burn-in 1000 --optimize-interval 100 --output-state catalina/catalina"+i+"/topic-state.gz --output-topic-keys catalina/catalina"+i+"/keys.txt --output-doc-topics catalina/catalina"+i+"/composition.txt --xml-topic-phrase-report catalina/catalina"+i+"/topic-phrases.xml --word-topic-counts-file catalina/catalina"+i+"/word-top.txt");
//        System.out.prntln("cp /usr/local/mallet-2.0.7/tribes/tribes"+i+"/composition.txt /home/yangchen/ycdoc/topicModel/tribesComposite/");
//        System.out.println("mv /home/yangchen/ycdoc/topicModel/tribesComposite/composition.txt /home/yangchen/ycdoc/topicModel/tribesComposite/composition"+i+".txt");

//        System.out.println("cp /usr/local/mallet-2.0.7/tribes/tribes"+i+"/topic-phrases.xml /home/yangchen/ycdoc/topicModel/tribesXml/");
//        System.out.println("mv /home/yangchen/ycdoc/topicModel/tribesXml/topic-phrases.xml /home/yangchen/ycdoc/topicModel/tribesXml/topic-phrases" + i + ".xml");
//        }

        String abPath = System.getProperty("user.dir");
//        String filePath = "/usr/local/mallet-2.0.7/" + folder + "/" + folder + topicNum + "/composition.txt";
        String filePath = abPath + "/topicModel/" + "tomcat" + "Xml/topic-phrases"+ "50" +".xml";
        System.out.print(filePath);
    }
}
