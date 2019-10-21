package com.myparser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Java Bean Car
 */
public class Car {

    private static final List<Car> cars = new ArrayList<Car>() {
        private static final long serialVersionUID = 475392500803564565L;
        {
            add(new CarBuilder().setBrand("Smart").setEcoFriendly(false).setFuelType("Diesel").setNumberOfModels(1)
                    .build());
            add(new CarBuilder().setBrand("Mercedes-Benz").setEcoFriendly(true).setFuelType("Hybrid").setNumberOfModels(3)
                    .build());
            add(new CarBuilder().setBrand("Mercedes-AMG").setEcoFriendly(true).setFuelType("Electric")
                    .setNumberOfModels(0).build());
        }
    };

    private String brand;
    private boolean ecoFriendly;
    private String fuelType;
    private List<String> models;

    private Car(final CarBuilder builder) {
        this.brand = builder.brand;
        this.ecoFriendly = builder.ecoFriendly;
        this.fuelType = builder.fuelType;
        this.models = new ArrayList<>();

        for (int it = 0; it < builder.numberOfModels; it++) {
            models.add("Model " + it);
        }
    }

    public static Car lookup(final String id) {
        if (id != null && !id.isEmpty()) {
            final int carId = Integer.valueOf(id);
            if (carId > 0 && carId <= cars.size()) {
                return cars.get(carId - 1);
            }
        }

        return new CarBuilder().setBrand("Empty Brand").setEcoFriendly(false).setFuelType("Empty fuelType").setNumberOfModels(0)
                .build();
    }

    public String getBrand() {
        return brand;
    }

    public boolean isEcoFriendly() {
        return ecoFriendly;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public List<String> getModels() {
        return models;
    }

    @Override
    public String toString() {
        return "Car [brand=" + brand + ", ecoFriendly=" + ecoFriendly + ", fuelType=" + fuelType + ", models=" + models + "]";
    }

    /**
     * Static Car Builder.
     */
    static final class CarBuilder {

        private String brand;
        private boolean ecoFriendly;
        private String fuelType;
        private int numberOfModels;

        CarBuilder setBrand(final String brand) {
            this.brand = brand;
            return this;
        }

        CarBuilder setEcoFriendly(final boolean ecoFriendly) {
            this.ecoFriendly = ecoFriendly;
            return this;
        }

        CarBuilder setFuelType(final String fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        CarBuilder setNumberOfModels(final int numberOfModels) {
            this.numberOfModels = numberOfModels;
            return this;
        }

        Car build() {
            return new Car(this);
        }
    }
}
