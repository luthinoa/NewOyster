package com.tfl.billing;

/**
 * Created by Noa Luthi on 19/11/2017.
 */

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Example {

    public static void main(String[] args) throws Exception {

        OysterCard myCard = new OysterCard("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        OysterCardReader paddingtonReader = OysterReaderLocator.atStation(Station.PADDINGTON);
        OysterCardReader bakerStreetReader = OysterReaderLocator.atStation(Station.BAKER_STREET);

        GetCardReadersData cardReadersData = new GetCardReadersData();
        cardReadersData.connect(paddingtonReader,bakerStreetReader);

        TravelTracker travelTracker = new TravelTracker(cardReadersData);

        paddingtonReader.touch(myCard);
        minutesPass(1);
        bakerStreetReader.touch(myCard);

        travelTracker.chargeAccounts();
    }

    private static void minutesPass(int n) throws InterruptedException {
        Thread.sleep(n * 60 * 1000);
    }
}