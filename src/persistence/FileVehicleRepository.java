package persistence;

import models.Vehicle;
import java.io.*;
import java.util.*;

public class FileVehicleRepository implements VehicleRepository {
    private static final String VEHICLES_FILE = "data/cars.txt";

    @Override
    public List<Vehicle> loadAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        File file = new File(VEHICLES_FILE);

        if (!file.exists()) {
            return vehicles;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle vehicle = Vehicle.fromCsvString(line);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }

        return vehicles;
    }

    @Override
    public boolean saveAll(List<Vehicle> vehicles) {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.toCsvString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Vehicle findByLicenseNumber(String licenseNumber) {
        for (Vehicle vehicle : loadAll()) {
            if (vehicle.getLicenseNumber().equalsIgnoreCase(licenseNumber)) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public int findIndexByLicenseNumber(List<Vehicle> vehicles, String licenseNumber) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getLicenseNumber().equalsIgnoreCase(licenseNumber)) {
                return i;
            }
        }
        return -1;
    }
}
