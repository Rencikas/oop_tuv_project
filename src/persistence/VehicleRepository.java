package persistence;

import models.Vehicle;
import java.util.List;

public interface VehicleRepository {
    List<Vehicle> loadAll();
    boolean saveAll(List<Vehicle> vehicles);
    Vehicle findByLicenseNumber(String licenseNumber);
    int findIndexByLicenseNumber(List<Vehicle> vehicles, String licenseNumber);
}
