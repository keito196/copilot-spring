public class Refactoring {
    // Create a long string by concatenating numbers from 0 to 10000 in a loop
    
    /**
     * INEFFICIENT: Using string concatenation in a loop
     * This creates a new String object for each concatenation
     * Time Complexity: O(n²) due to string immutability
     */
    public static String inefficientStringConcatenation() {
        String result = "";
        for (int i = 0; i <= 10000; i++) {
            result += i;  // Creates a new String object each iteration
        }
        return result;
    }

    /**
     * EFFICIENT: Using StringBuilder for string concatenation
     * This is the refactored version - much faster and more memory efficient
     * Time Complexity: O(n)
     */
    public static String efficientStringConcatenation() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= 10000; i++) {
            result.append(i);
        }
        return result.toString();
    }

    /**
     * Demonstrates the performance difference between the two approaches
     */
    public static void main(String[] args) {
        // Test inefficient approach
        long startTime = System.currentTimeMillis();
        String inefficientResult = inefficientStringConcatenation();
        long inefficientTime = System.currentTimeMillis() - startTime;
        System.out.println("Inefficient approach time: " + inefficientTime + " ms");
        System.out.println("Result length: " + inefficientResult.length());

        // Test efficient approach
        startTime = System.currentTimeMillis();
        String efficientResult = efficientStringConcatenation();
        long efficientTime = System.currentTimeMillis() - startTime;
        System.out.println("Efficient approach time: " + efficientTime + " ms");
        System.out.println("Result length: " + efficientResult.length());

        // Display performance improvement
        System.out.println("\nPerformance improvement: " + ((float) inefficientTime / efficientTime) + "x faster");
    }
}
