package services;

import models.Vehicle;
import persistence.VehicleRepository;
import persistence.FileVehicleRepository;
import util.ValidationUtil;
import java.time.LocalDate;
import java.util.*;

public class VehicleService {
    private VehicleRepository vehicleRepository;

    public VehicleService() {
        this.vehicleRepository = new FileVehicleRepository();
    }

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public boolean registerVehicle(String licenseNumber, String make, String color,
                                   String fuelType, String category) {
        if (!ValidationUtil.isValidLicensePlate(licenseNumber) ||
            !ValidationUtil.isValidMake(make) ||
            !ValidationUtil.isValidColor(color) ||
            !ValidationUtil.isValidCategory(category)) {
            return false;
        }

        List<Vehicle> vehicles = vehicleRepository.loadAll();
        LocalDate now = LocalDate.now();
        int inspectionInterval = ValidationUtil.getInspectionInterval(category);
        LocalDate inspectionExpiry = now.plusYears(inspectionInterval);

        int existingIndex = vehicleRepository.findIndexByLicenseNumber(vehicles, licenseNumber);
        Vehicle newVehicle = new Vehicle(licenseNumber, make, color, fuelType, category,
                                        now, inspectionExpiry);

        if (existingIndex >= 0) {
            vehicles.set(existingIndex, newVehicle);
        } else {
            vehicles.add(newVehicle);
        }

        return vehicleRepository.saveAll(vehicles);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.loadAll();
    }

    public List<Vehicle> getVehicles(int limit) {
        List<Vehicle> all = vehicleRepository.loadAll();
        if (limit <= 0 || limit >= all.size()) {
            return all;
        }
        return all.subList(0, limit);
    }

    public Vehicle getVehicleByLicense(String licenseNumber) {
        return vehicleRepository.findByLicenseNumber(licenseNumber);
    }

    public boolean updateInspectionDate(String licenseNumber, LocalDate newDate) {
        List<Vehicle> vehicles = vehicleRepository.loadAll();
        int index = vehicleRepository.findIndexByLicenseNumber(vehicles, licenseNumber);

        if (index >= 0) {
            vehicles.get(index).setInspectionExpiryDate(newDate);
            return vehicleRepository.saveAll(vehicles);
        }

        return false;
    }

    public boolean updateVehicleDetails(String licenseNumber, String make, String color,
                                       String fuelType, String category) {
        List<Vehicle> vehicles = vehicleRepository.loadAll();
        int index = vehicleRepository.findIndexByLicenseNumber(vehicles, licenseNumber);

        if (index >= 0) {
            Vehicle vehicle = vehicles.get(index);
            if (make != null && !make.isEmpty()) {
                vehicle.setMake(make);
            }
            if (color != null && !color.isEmpty()) {
                vehicle.setColor(color);
            }
            if (fuelType != null && !fuelType.isEmpty()) {
                vehicle.setFuelType(fuelType);
            }
            if (category != null && !category.isEmpty()) {
                vehicle.setCategory(category);
            }

            return vehicleRepository.saveAll(vehicles);
        }

        return false;
    }
}
