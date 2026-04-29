package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ValidationUtil {
    private static final String INSPECTION_INTERVALS_FILE = "data/inspection_intervals.txt";

    public static boolean isValidLicensePlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            return false;
        }

        // Check if plate contains at least one digit
        for (char c : plate.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidMake(String make) {
        return make != null && !make.trim().isEmpty();
    }

    public static boolean isValidColor(String color) {
        return color != null && !color.trim().isEmpty();
    }

    public static boolean isValidFuelType(int fuelChoice) {
        return fuelChoice >= 1 && fuelChoice <= 4;
    }

    public static boolean isValidCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(INSPECTION_INTERVALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 1 && parts[0].equals(category)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    public static int getInspectionInterval(String category) {
        try (BufferedReader reader = new BufferedReader(new FileReader(INSPECTION_INTERVALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2 && parts[0].equals(category)) {
                    try {
                        return Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        return 1; // Default to 1 year
                    }
                }
            }
        } catch (IOException e) {
            // File not found or read error
        }

        return 1; // Default to 1 year
    }

    public static String[] getFuelTypes() {
        return new String[]{"Benzinas", "Dyzelinas", "Elektra", "Benzinas/Dujos"};
    }
}
