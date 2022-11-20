package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        File file = new File(args[0]);
        StringBuilder str = new StringBuilder();
        int numSentences;
        String text;
        ArrayList<String> sentences;
        try (Scanner scanner = new Scanner(file)) {
            // could possibly use next instead to get each word and count syllables for each
            // or do it in while loop in countMatches <- confirmed works
            System.out.println("The text is:");
            while (scanner.hasNext()) {
                String string = scanner.nextLine();
                System.out.println(string);
                str.append(string);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + file);
        }

        text = str.toString();
        sentences = intoSentences(text);
        numSentences = sentences.size();

        System.out.println();
        System.out.println("Words: " + ReadabilityScores.getWords(text));
        System.out.println("Sentences: " + numSentences);
        System.out.println("Characters: " + ReadabilityScores.getCharacters(text));
        System.out.println("Syllables: " + ReadabilityScores.getSyllables(text));
        System.out.println("Polysyllables: " + ReadabilityScores.getPolysyllables(sentences));
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        double[] scoreAge;
        double age;
        try (Scanner sysin = new Scanner(System.in)) {
            switch (sysin.nextLine()) {
                case "ARI" -> {
                    scoreAge = ReadabilityScores.automatedReadabilityIndex(text, numSentences);
                    //age = scoreAge[1];
                    System.out.printf("%nAutomated Readability Index: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                }
                case "FK" -> {
                    scoreAge = ReadabilityScores.fleshKincaid(text, numSentences);
                    //age = scoreAge[1];
                    System.out.printf("%nFlesch–Kincaid readability tests: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                }
                case "SMOG" -> {
                    scoreAge = ReadabilityScores.smogIndex(sentences, numSentences);
                    //age = scoreAge[1];
                    System.out.printf("%nSimple Measure of Gobbledygook: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                }
                case "CL" -> {
                    scoreAge = ReadabilityScores.colemanLiau(text, numSentences);
                    //age = scoreAge[1];
                    System.out.printf("%nColeman–Liau index: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                }
                case "all" -> {
                    scoreAge = ReadabilityScores.automatedReadabilityIndex(text, numSentences);
                    age = scoreAge[1];
                    System.out.printf("%nAutomated Readability Index: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                    scoreAge = ReadabilityScores.fleshKincaid(text, numSentences);
                    age += scoreAge[1];
                    System.out.printf("%nFlesch–Kincaid readability tests: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                    scoreAge = ReadabilityScores.smogIndex(sentences, numSentences);
                    age += scoreAge[1];
                    System.out.printf("%nSimple Measure of Gobbledygook: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                    scoreAge = ReadabilityScores.colemanLiau(text, numSentences);
                    age += scoreAge[1];
                    System.out.printf("%nColeman–Liau index: %.2f (about %d-year-olds).", scoreAge[0], (int) scoreAge[1]);
                    System.out.printf("%n%nThis text should be understood in average by %.2f-year-olds.", age / 4);
                }
                default -> System.out.println("invalid input");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input" + e);
        }

    }
    //No need for an ArrayList with each sentence broken up into its own index at this stage
    public static ArrayList<String> intoSentences(String text) {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);
        int start = iterator.first();
        ArrayList<String> sentences = new ArrayList<>();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            sentences.add(text.substring(start,end));
        }
        return sentences;
    }
}
