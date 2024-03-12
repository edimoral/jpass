package jpass.util;

public final class PasswordStrengthEstimator {

    // Adjust weights for character types
    private static final double LOWERCASE_WEIGHT = 0.7; // Minor adjustment
    private static final double UPPERCASE_WEIGHT = 0.7; // Minor adjustment
    private static final double NUMBER_WEIGHT = 0.5;    // Lower emphasis on numbers
    private static final double SYMBOL_WEIGHT = 2.0;    // Higher emphasis on symbols


    private PasswordStrengthEstimator() {

    }

    /**
     * Estimates the base strength of the password using a logarithmic approach.
     *
     * @param password The password string to be evaluated.
     * @return The base strength score of the password.
     */
    private static double calculateBaseStrength(String password) {
        double strength = 0.0;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                strength += Math.log(LOWERCASE_WEIGHT + 1);
            } else if (Character.isUpperCase(c)) {
                strength += Math.log(UPPERCASE_WEIGHT + 1);
            } else if (Character.isDigit(c)) {
                strength += Math.log(NUMBER_WEIGHT + 1);
            } else {
                strength += Math.log(SYMBOL_WEIGHT + 1);
            }
        }
        return strength;
    }

    /**
     * Applies diversity bonuses to the strength score based on character variety.
     *
     * @param password The password string to be evaluated.
     * @return The total bonus for character diversity.
     */
    private static double calculateDiversityBonus(String password) {
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasNumber = false;
        boolean hasSymbol = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSymbol = true;
            }
        }

        double diversityBonus = 0;
        if (hasLower) {
            diversityBonus += 2;
        }
        if (hasUpper) {
            diversityBonus += 2;
        }
        if (hasNumber) {
            diversityBonus += 2;
        }
        if (hasSymbol) {
            diversityBonus += 4;
        }

        return diversityBonus;
    }

    /**
     * Scales the combined strength and diversity bonus into a 0-100 range smoothly.
     *
     * @param password The password string to be evaluated.
     * @return A score between 0 to 100, indicating the password's strength.
     */
    public static int calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        double baseStrength = calculateBaseStrength(password);
        double diversityBonus = calculateDiversityBonus(password);
        double totalStrength = baseStrength + diversityBonus;

        // Define a theoretical maximum strength for normalization purposes
        // This could be based on an "ideal" password length and composition
        double maxStrength = calculateBaseStrength("Example!2D") + calculateDiversityBonus("Example!2D");
        maxStrength *= Math.sqrt(12); // Adjust this based on an "ideal" password length

        // Scale the score to the 0-100 range based on the calculated max strength
        double score = (totalStrength / maxStrength) * 100;
        score = Math.min(score, 100); // Ensure the score does not exceed 100

        return (int) score;
    }

    // Calculate the classification and brute-force time based on entropy
    public static String getLabelBasedOnPasswordStrength(int passwordStrength) {
        String classification = getClassification(passwordStrength);
        return String.format("Password Strength: %s", classification);
    }

    private static String getClassification(int score) {
        String classification;
        if (score < 20) {
            classification = "Very Weak";
        } else if (score < 40) {
            classification = "Weak";
        } else if (score < 60) {
            classification = "Marginal";
        } else if (score < 80) {
            classification = "Strong";
        } else if (score < 90) {
            classification = "Very Strong";
        } else {
            classification = "Overkill";
        }
        return classification;
    }
}
