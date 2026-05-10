package services;

import models.Vehicle;
import java.time.LocalDate;
import java.util.*;

public class FilterService {
    private VehicleService vehicleService;

    public FilterService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public static class FilterCriteria {
        public String field;
        public String value;

        public FilterCriteria(String field, String value) {
            this.field = field;
            this.value = value;
        }
    }

    public enum InspectionStatus {
        VALID("valid"),
        EXPIRED("expired");

        private final String value;

        InspectionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public List<Vehicle> filterVehicles(List<FilterCriteria> criteria) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<Vehicle> result = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            if (matchesAllCriteria(vehicle, criteria)) {
                result.add(vehicle);
            }
        }

        return result;
    }

    private boolean matchesAllCriteria(Vehicle vehicle, List<FilterCriteria> criteria) {
        for (FilterCriteria criterion : criteria) {
            if (!matchesCriterion(vehicle, criterion)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesCriterion(Vehicle vehicle, FilterCriteria criterion) {
        if (criterion.value == null || criterion.value.isEmpty()) {
            return true;
        }

        String lowerValue = criterion.value.toLowerCase();

        String fieldKey = criterion.field == null ? "" : criterion.field.toLowerCase().trim();

        // Accept several possible field names (English UI, Lithuanian, and some
        // synonyms)
        switch (fieldKey) {
            case "number":
            case "license":
            case "licensenumber":
            case "license no.":
            case "numeris":
                return safeContains(vehicle.getLicenseNumber(), lowerValue);
            case "make":
            case "brand":
            case "marke":
                return safeContains(vehicle.getMake(), lowerValue);
            case "color":
            case "colour":
            case "spalva":
                return safeContains(vehicle.getColor(), lowerValue);
            case "fuel":
            case "fueltype":
            case "kuras":
                return safeContains(vehicle.getFuelType(), lowerValue);
            case "category":
            case "kategorija":
                return safeContains(vehicle.getCategory(), lowerValue);
            case "inspection":
            case "inspection status":
            case "apziura":
                return matchesInspectionStatus(vehicle, lowerValue);
            default:
                // Unknown field: try to match against license, make, color, fuel and category
                // as a fallback
                return safeContains(vehicle.getLicenseNumber(), lowerValue)
                        || safeContains(vehicle.getMake(), lowerValue)
                        || safeContains(vehicle.getColor(), lowerValue)
                        || safeContains(vehicle.getFuelType(), lowerValue)
                        || safeContains(vehicle.getCategory(), lowerValue);
        }
    }

    // helper: safe null-check + case-insensitive contains
    private boolean safeContains(String haystack, String needleLower) {
        if (haystack == null || haystack.isEmpty())
            return false;
        return haystack.toLowerCase().contains(needleLower);
    }

    private boolean matchesInspectionStatus(Vehicle vehicle, String statusValue) {
        LocalDate now = LocalDate.now();

        if (statusValue.equals(InspectionStatus.VALID.getValue())) {
            if (vehicle.getInspectionExpiryDate() == null) {
                return true;
            }
            return vehicle.getInspectionExpiryDate().isAfter(now);
        } else if (statusValue.equals(InspectionStatus.EXPIRED.getValue())) {
            if (vehicle.getInspectionExpiryDate() == null) {
                return false;
            }
            LocalDate expiry = vehicle.getInspectionExpiryDate();
            LocalDate regDate = vehicle.getRegistrationDate();

            if (expiry.isBefore(now)) {
                if (regDate != null && expiry.isAfter(regDate)) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    public List<Vehicle> filterValidInspection() {
        List<FilterCriteria> criteria = new ArrayList<>();
        criteria.add(new FilterCriteria("apziura", InspectionStatus.VALID.getValue()));
        return filterVehicles(criteria);
    }

    public List<Vehicle> filterExpiredInspection() {
        List<FilterCriteria> criteria = new ArrayList<>();
        criteria.add(new FilterCriteria("apziura", InspectionStatus.EXPIRED.getValue()));
        return filterVehicles(criteria);
    }

    public List<Vehicle> searchVehicles(String field, String value) {
        List<FilterCriteria> criteria = new ArrayList<>();
        criteria.add(new FilterCriteria(field, value));
        return filterVehicles(criteria);
    }
}
