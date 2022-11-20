package readability;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadabilityScores {
    private static final String wordRegex = "\\b\\w+,*\\w*\\b";
    private static final String charRegex = "[^ ]";
    private static final String syllableRegex = "\\b[^aeyuioAEYUIO\\s]+[aeyuioAEYUIO]\\b|[aeyuioAEYUIO]*[eE](?!\\b)[aeyuioAEYUIO]*|[ayuioAYUIO]+|\\d+[,.]\\d*";

    public static double[] automatedReadabilityIndex(String str, int numSentences) {
        long numWords = getWords(str);
        long numChars = getCharacters(str);
        double score = (4.71 * (numChars / (double) numWords) + 0.5 * (numWords / (double) numSentences) - 21.43);
        int age = (int) (Math.ceil(score)) + 5;
        return new double[] {score, age};
        //return String.format("%.2f (about %d-year-olds).", score, age);
    }

    public static double[] fleshKincaid(String str, int numSentences) {
        long numWords = getWords(str);
        long numSyllables = getSyllables(str);
        double score = (0.39 * (numWords / (double) numSentences) + 11.8 * (numSyllables / (double) numWords) - 15.59);
        int age = (int) (Math.ceil(score)) + 5;
        return new double[] {score, age};
        //return String.format("%.2f (about %d-year-olds).", score, age);
    }

    public static double[] smogIndex(ArrayList<String> str, int numSentences) {
        long numPolysyllables = getPolysyllables(str);
        double score = (1.043 * Math.sqrt(numPolysyllables * (30 / (double) numSentences)) + 3.1291);
        int age = (int) (Math.ceil(score)) + 5;
        return new double[] {score, age};
        //return String.format("%.2f (about %d-year-olds).", score, age);
    }

    public static double[] colemanLiau(String str, int numSentences) {
        long numWords = getWords(str);
        long numChars = getCharacters(str);
        double L = (numChars / (double) numWords) * 100;
        double S = (numSentences / (double) numWords) * 100;
        double score = 0.0588 * L - 0.296 * S - 15.8;
        int age = (int) (Math.ceil(score)) + 6;
        return new double[] {score, age};
        //return String.format("%.2f (about %d-year-olds).", score, age);
    }

    public static long getWords(String str) {
        return countMatches(str, wordRegex);
    }
    public static long getCharacters(String str) {
        return countMatches(str, charRegex);
    }
    public static long getSyllables(String str) {
        return countMatches(str, syllableRegex);
    }

    public static long getPolysyllables(ArrayList<String> strings) {
        return countPolySyllables(strings);
    }
    private static long countMatches(String sentences, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sentences);
        return matcher.results().count();
    }
    private static long countPolySyllables(ArrayList<String> sentences) {
        Pattern pattern = Pattern.compile(wordRegex);
        Pattern pattern1 = Pattern.compile(syllableRegex);
        Matcher[] matcher = new Matcher[sentences.size()];
        long count = 0;
        for (int i = 0; i < sentences.size(); i++) {
            matcher[i] = pattern.matcher(sentences.get(i));
            int k = 0;
            while (matcher[i].find(k)) {
                k = matcher[i].start() + 1;
                Matcher matcherWord = pattern1.matcher(matcher[i].group());
                if (matcherWord.results().count() > 2) {
                    count++;
                }
                //System.out.println("Test: " + matcher[i].group()); //matcher[i].group() to get the current word
            }
        }
        return count;
    }
}
