import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtils {
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+");

    /**
     * Reads a file line by line, counts word frequencies (case-insensitive, ignores punctuation)
     * @param filePath the path to the file to read
     * @return a Map containing word frequencies, or empty map if an error occurs
     */
    public static Map<String, Integer> countWordFrequency(String filePath) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Extract words using regex pattern (letters only)
                var matcher = WORD_PATTERN.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found at path: " + filePath);
        } catch (IOException e) {
            System.err.println("Error: An IO error occurred while reading the file: " + e.getMessage());
        }

        return wordFrequencyMap;
    }
}
