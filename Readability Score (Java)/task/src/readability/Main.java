package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        File file = new File(fileName);

        int words = 0;
        int sentences = 0;
        int characters = 0;
        int syllables = 0;
        int polysyllables = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                words += line.split("\\s+").length;
                sentences += line.split("[.!?]").length;
                characters += line.replaceAll("[\\s\\n\\t]", "").length();
                for (String word : line.split("\\s+")) {
                    int wordSyllables = countSyllables(word);
                    syllables += wordSyllables;
                    if (wordSyllables > 2) {
                        polysyllables++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        System.out.println();
        switch (choice) {
            case "ARI":
                printARI(characters, words, sentences);
                break;
            case "FK":
                printFK(words, sentences, syllables);
                break;
            case "SMOG":
                printSMOG(sentences, polysyllables);
                break;
            case "CL":
                printCL(characters, words, sentences);
                break;
            case "all":
                printARI(characters, words, sentences);
                printFK(words, sentences, syllables);
                printSMOG(sentences, polysyllables);
                printCL(characters, words, sentences);
                break;
        }
        System.out.println();
    }

    private static int countSyllables(String word) {
        int syllables = 0;
        word = word.toLowerCase();
        String vowels = "aeiouy";
        char[] letters = word.toCharArray();

        for (int i = 0; i < letters.length; i++) {
            if (vowels.indexOf(letters[i]) >= 0) {
                syllables++;
                if (i + 1 < letters.length && vowels.indexOf(letters[i + 1]) >= 0) {
                    i++;
                }
            }
        }

        if (word.endsWith("e")) {
            syllables--;
        }

        return Math.max(syllables, 1);
    }

    private static void printARI(int characters, int words, int sentences) {
        double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n", score, getAge((int) Math.round(score)));
    }

    private static void printFK(int words, int sentences, int syllables) {
        double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).\n", score, getAge((int) Math.round(score)));
    }

    private static void printSMOG(int sentences, int polysyllables) {
        double score = 1.043 * Math.sqrt(polysyllables * (30.0 / sentences)) + 3.1291;
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).\n", score, getAge((int) Math.round(score)));
    }

    private static void printCL(int characters, int words, int sentences) {
        double L = (double) characters / words * 100;
        double S = (double) sentences / words * 100;
        double score = 0.0588 * L - 0.296 * S - 15.8;
        System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).\n", score, getAge((int) Math.round(score)));
    }

    private static String getAge(int score) {
        switch (score) {
            case 1: return "5-6";
            case 2: return "6-7";
            case 3: return "7-8";
            case 4: return "8-9";
            case 5: return "9-10";
            case 6: return "10-11";
            case 7: return "11-12";
            case 8: return "12-13";
            case 9: return "13-14";
            case 10: return "14-15";
            case 11: return "15-16";
            case 12: return "16-17";
            case 13: return "17-18";
            case 14: return "18-22";
            default: return "Unknown";
        }
    }
}
