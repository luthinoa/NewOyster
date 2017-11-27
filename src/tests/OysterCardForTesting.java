package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.billing.GetCardReadersData;
import com.tfl.billing.TravelTracker;
import com.tfl.underground.Station;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class OysterCardForTesting {

    private OysterCard myCard;
    private OysterCardReader startStationOysterReader;
    private OysterCardReader endStationOysterReader;
    private ExternalJarAdapter externalJarAdapter;
    private GetCardReadersData getCardReadersData;
    private Station start;
    private Station end;
    private String cardID;


     /*
     Creating an oyster card and card readers, connecting them with the purpose of using in testing.
    */

    public OysterCardForTesting(){

        start = Station.PADDINGTON;
        end = Station.BAKER_STREET;
        cardID = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
    }

    public void connectCardReaders() {

        externalJarAdapter = new ExternalJarAdapter();
        myCard = externalJarAdapter.getOysterCard(cardID);

        startStationOysterReader = externalJarAdapter.getCardReader(start);
        endStationOysterReader = externalJarAdapter.getCardReader(end);

        getCardReadersData = new GetCardReadersData();
        getCardReadersData.connect(startStationOysterReader,endStationOysterReader);
    }

    public OysterCard getMyCard() {return myCard;}

    public OysterCardReader getStartStationOysterReader() {return startStationOysterReader;}

    public OysterCardReader getEndStationOysterReader() {return endStationOysterReader;}

    public GetCardReadersData getCardReadersData() {return getCardReadersData;}
}
