package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.ExternalJarAdapter;
import com.tfl.billing.GetCardReadersData;
import com.tfl.billing.Journey;
import com.tfl.billing.TravelTracker;
import com.tfl.external.Customer;
import com.tfl.underground.Station;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class OysterCardForTesting {

    private OysterCard myCard;
    private String nameOfCustomer;
    private Customer customer;
    private OysterCardReader startStationOysterReader;
    private OysterCardReader endStationOysterReader;
    private ExternalJarAdapter externalJarAdapter;
    private GetCardReadersData getCardReadersData;
    private Station start;
    private Station end;


    /*
     Creating an oyster card and card readers, connecting them with the purpose of using in testing.
    */

    public OysterCardForTesting(String nameOfCustomer){

        this.nameOfCustomer = nameOfCustomer;
        externalJarAdapter = new ExternalJarAdapter();
        start = Station.PADDINGTON;
        end = Station.BAKER_STREET;
    }

    public void createCustomerAndConnectCardReaders() {

        externalJarAdapter.addCustomer(nameOfCustomer);
        myCard = externalJarAdapter.getOysterCard(nameOfCustomer);
        customer = externalJarAdapter.getCustomer(nameOfCustomer);

        startStationOysterReader = externalJarAdapter.getCardReader(start);
        endStationOysterReader = externalJarAdapter.getCardReader(end);

        getCardReadersData = new GetCardReadersData();
        getCardReadersData.connect(startStationOysterReader,endStationOysterReader);
    }

    public Customer getCustomer() {return customer;}

    public OysterCard getMyCard() {return myCard;}

    public List<Customer> getCustomersInDatabase() {return externalJarAdapter.getCustomersInDatabase();}

    public OysterCardReader getStartStationOysterReader() {return startStationOysterReader;}

    public OysterCardReader getEndStationOysterReader() {return endStationOysterReader;}

    public GetCardReadersData getCardReadersData() {return getCardReadersData;}

    public void printChargingForThisDay(Customer customer, List<Journey> journeys, BigDecimal cost) {externalJarAdapter.charge(customer,journeys,cost);}
}
