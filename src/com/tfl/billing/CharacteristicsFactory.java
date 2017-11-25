package com.tfl.billing;

public class CharacteristicsFactory {

    /*
    This factory enables creating different objects to check specific characteristics of a journey.
     */

    public Characteristics getCharacteristic(String characteristic) {

        if(characteristic==null) {
            return null;
        }

        else if(characteristic=="IsPeak") {
            return new IsPeak();
        }

        else if(characteristic=="IsLong") {
            return new IsLong();
        }
        return null;
    }
}
