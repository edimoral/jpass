package jpass.util;

public final class PasswordStrengthEstimator {

    private static final double LOWERCASE_WEIGHT = 1.0;
    private static final double UPPERCASE_WEIGHT = 1.2;
    private static final double NUMBER_WEIGHT = 1.3;
    private static final double SYMBOL_WEIGHT = 1.5;

    private PasswordStrengthEstimator() {

    }

    /**
     * Estimates the base strength of the password.
     *
     * @param password The password string to be evaluated.
     * @return The base strength score of the password.
     */
    private static double calculateBaseStrength(String password) {
        double strength = 0.0;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                strength += LOWERCASE_WEIGHT;
            } else if (Character.isUpperCase(c)) {
                strength += UPPERCASE_WEIGHT;
            } else if (Character.isDigit(c)) {
                strength += NUMBER_WEIGHT;
            } else {
                strength += SYMBOL_WEIGHT;
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
    private static int calculateDiversityBonus(String password) {
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

        int diversityBonus = 0;
        if (hasLower) {
            diversityBonus += 5;
        }
        if (hasUpper) {
            diversityBonus += 5;
        }
        if (hasNumber) {
            diversityBonus += 5;
        }
        if (hasSymbol) {
            diversityBonus += 5;
        }

        return diversityBonus;
    }

    /**
     * Estimates password strength with a more detailed calculation.
     *
     * @param password The password string to be evaluated.
     * @return A score between 0 to 100, indicating the password's strength.
     */
    public static int calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        double baseStrength = calculateBaseStrength(password);
        int diversityBonus = calculateDiversityBonus(password);

        // Scale and adjust the final score to fit into the 0-100 range
        int length = password.length();
        return Math.min(100, (int) ((baseStrength + diversityBonus) * length / 8));
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
