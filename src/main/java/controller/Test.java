package controller;

/**
 * Created by yangchen on 16-5-13.
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 100; i < 101; i++) {
            System.out.println("bin/mallet train-topics --input catalina.mallet  --num-topics "+ i +" --num-top-words 10 --num-iterations 10000 --optimize-burn-in 1000 --optimize-interval 100 --output-state catalina/catalina"+i+"/topic-state.gz --output-topic-keys catalina/catalina"+i+"/keys.txt --output-doc-topics catalina/catalina"+i+"/composition.txt --xml-topic-phrase-report catalina/catalina"+i+"/topic-phrases.xml --word-topic-counts-file catalina/catalina"+i+"/word-top.txt");
        }
    }
}
