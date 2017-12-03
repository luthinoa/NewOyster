package com.tfl.billing;

public class CharacteristicsFactory {

    public Characteristics getCharacteristic(String characteristic) {

        if (characteristic.equals("isLong")) {

            return new IsLong();
        }

        if (characteristic.equals("isPeak")) {
            return new IsPeak();
        }

        return null;
    }
}
