package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vehicle {
    private String licenseNumber;
    private String make;
    private String color;
    private String fuelType;
    private String category;
    private LocalDate registrationDate;
    private LocalDate inspectionExpiryDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Vehicle(String licenseNumber, String make, String color, String fuelType,
                   String category, LocalDate registrationDate, LocalDate inspectionExpiryDate) {
        this.licenseNumber = licenseNumber;
        this.make = make;
        this.color = color;
        this.fuelType = fuelType;
        this.category = category;
        this.registrationDate = registrationDate;
        this.inspectionExpiryDate = inspectionExpiryDate;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getMake() {
        return make;
    }

    public String getColor() {
        return color;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getInspectionExpiryDate() {
        return inspectionExpiryDate;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInspectionExpiryDate(LocalDate inspectionExpiryDate) {
        this.inspectionExpiryDate = inspectionExpiryDate;
    }

    public boolean isInspectionValid() {
        if (inspectionExpiryDate == null) {
            return false;
        }
        return inspectionExpiryDate.isAfter(LocalDate.now());
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                licenseNumber,
                make,
                color,
                fuelType,
                category,
                registrationDate != null ? registrationDate.format(DATE_FORMATTER) : "-",
                inspectionExpiryDate != null ? inspectionExpiryDate.format(DATE_FORMATTER) : "-");
    }

    public static Vehicle fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 7) {
            return null;
        }

        LocalDate regDate = parseDate(parts[5]);
        LocalDate inspDate = parseDate(parts[6]);

        return new Vehicle(parts[0], parts[1], parts[2], parts[3], parts[4], regDate, inspDate);
    }

    private static LocalDate parseDate(String dateStr) {
        if (dateStr.equals("-") || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Nr: %s | Marke: %s | Spalva: %s | Kuras: %s | Kategorija: %s | " +
                "Registruota: %s | TA galioja iki: %s",
                licenseNumber, make, color, fuelType, category,
                registrationDate != null ? registrationDate : "-",
                inspectionExpiryDate != null ? inspectionExpiryDate : "-");
    }
}
