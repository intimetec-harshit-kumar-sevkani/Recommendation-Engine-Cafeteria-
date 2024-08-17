package org.example.util;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {
    private static StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static double getAverageRating(List<String> comments) {
        int totalSentiment = 0;
        int count = 0;

        for (String comment : comments) {
            Annotation annotation = new Annotation(comment);
            pipeline.annotate(annotation);
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {
                String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                totalSentiment += sentimentToScore(sentiment);
                count++;
            }
        }

        double averageSentiment = (double) totalSentiment / count;
        return averageSentiment;
    }

    private static int sentimentToScore(String sentiment) {
        switch (sentiment.toLowerCase()) {
            case "very positive":
                return 4;
            case "positive":
                return 3;
            case "neutral":
                return 2;
            case "negative":
                return 1;
            case "very negative":
                return 0;
            default:
                return 2;
        }
    }

}

