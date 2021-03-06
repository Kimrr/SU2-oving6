package services;

import jersey.repackaged.com.google.common.collect.HashBasedTable;
import jersey.repackaged.com.google.common.collect.HashMultimap;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author nilstes
 */
@Path("/thepath/")

public class AService {

    static ArrayList<Dish> appetizer= new ArrayList<Dish>();
    static ArrayList<Dish> maincourse= new ArrayList<Dish>();
    static ArrayList<Dish> dessert= new ArrayList<Dish>();
    static ArrayList<Dish> drink= new ArrayList<Dish>();
    static ArrayList<Account> myAccount = new ArrayList<Account>();

    private static MultiHashMap registration = new MultiHashMap();

    private final static int NUM_TABLES = 10;
    //static HashMap<LocalDate, HashMap<Integer, HashMap<Integer, Order>>>registration= new HashMap<LocalDate, HashMap<Integer, HashMap<Integer, Order>>>();

//    private static synchronized boolean addRegistration(LocalDate date, int table_nr, int slot_nr, Order order){
////        HashMap< Integer, Order> timeTest = new HashMap<Integer, Order>();
////        timeTest.put(slot_nr, order);
////
////        HashMap <Integer, HashMap<Integer, Order> > tableTest = new HashMap<Integer, HashMap<Integer, Order>>();
////        tableTest.put(table_nr, timeTest);
//
//        if(!registration.containsKey(date))
//            ;//registration.put(date, null);
//        if(!registration.get(date).containsKey(tableTest))
//            ;//registration.get(date).put(table_nr, null);
//        if(!registration.get(date).get(tableTest).containsKey(timeTest)){
//            //registration.get(date).get(tableTest).put(slot_nr, order);
//            return true;
//        }
//        else{
//            return false;
//        }
//
//
//    }

    static {
        Order myOrder = new Order("Kim R", 4, "Fish soup", "Halibut", "creme brulee", "");
        String test = "test";
//        //HashMap <Integer, String> timeTest = new HashMap <Integer, String> (7, 5);
//        //HashMultimap <Integer, Order> timeTest = new HashMultiMap <Integer, Order> (7, myOrder);
//        HashMap< Integer, Order> timeTest = new HashMap<Integer, Order>();
//        HashMap <Integer, HashMap<Integer, Order> > tableTest = new HashMap<Integer, HashMap<Integer, Order>>();
//        timeTest.put(7, myOrder);
//        tableTest.put(12, timeTest);
//        registration.put(LocalDate.of(2017, Month.AUGUST, 25) , tableTest);

        boolean a = registration.put("2017-9-25", 1, 1, new Order("kundenavn", 4, "Bread", "Burger", "creme brulee", ""));
        boolean b = registration.put("2017-9-26", 1, 2, new Order("kundenavn", 4, "Bread", "Burger", "creme brulee", ""));
        boolean c = registration.put("2017-9-26", 2, 2, new Order("kundenavn", 4, "Bread", "Burger", "creme brulee", ""));

        //System.out.println(a + " " + b + " " + c);

        appetizer.add(new Dish("Fish soup", 100));
        appetizer.add(new Dish("Bread", 50));
        maincourse.add(new Dish("Burger", 200));
        maincourse.add(new Dish("Hallibut", 300));
        dessert.add(new Dish("creme brulee", 150));
        dessert.add(new Dish("applecake and ice", 100));
        drink.add(new Dish("fanta", 50));
        drink.add(new Dish("cola", 50));
        //registrering.put("25092017", 12, myOrder);

        myAccount.add(new Account("Urke", "111", "2018", "1", "123", 10000));
        myAccount.add(new Account("Fauske", "222", "2018", "1", "123", 50));


    }




    @GET
    @Path("/dishes/appetizer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppetizer(){
        return Response.ok(appetizer).build();
    }

    @GET
    @Path("/dishes/maincourse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMainCourse(){
        return Response.ok(maincourse).build();
    }

    @GET
    @Path("/dishes/dessert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDessert(){
        return Response.ok(dessert).build();
    }

    @GET
    @Path("/dishes/drink")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrink(){
        return Response.ok(drink).build();
    }

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders(){
        return Response.ok(registration.getMap()).build();
    }

    @GET
    @Path("/orders/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersByDate(@PathParam("date") String date){
        return Response.ok(registration.getOrdersByDate(date)).build();
    }

    @POST
    @Path("/singleOrder/{orderDate}/{bordnr}/{slotnr}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putOrder(@PathParam("orderDate") String orderDate, @PathParam("bordnr") int bordnr, @PathParam("slotnr") int slotnr, Order order){
        boolean success = registration.put(orderDate, bordnr, slotnr, order);
        return Response.ok(success).build();
    }

    @GET
    @Path("/checkAvailable/{orderDate}/{bordnr}/{slotnr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailable(@PathParam("orderDate") String orderDate, @PathParam("bordnr") int bordnr, @PathParam("slotnr") int slotnr){
        boolean isFree = registration.isFree(orderDate, bordnr, slotnr);
        return Response.ok(isFree).build();
    }


    private static String testString = "Hei. Skiv noe og se hva som skjer.";

    private static int ACCOUNT_STATUS_OK = 0;
    private static int ACCOUNT_STATUS_NOTFOUND = 1;
    private static int ACCOUNT_STATUS_NOFUNDS = 2;

    @POST
    @Path("/account/check/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkBalance(@PathParam("amount") int amount, Account account){

        for(Account theaccount : myAccount){
            if(theaccount.equals(account)){
                if(theaccount.getBalance() >= amount)
                    return Response.ok(ACCOUNT_STATUS_OK).build();
                else
                    return Response.ok(ACCOUNT_STATUS_NOFUNDS).build();
            }
        }

        return Response.ok(ACCOUNT_STATUS_NOTFOUND).build();
    }

    @POST
    @Path("/account/pay/{balance}")
    @Consumes(MediaType.APPLICATION_JSON)
    // Trekker et bestemt beløp (balance) penger fra kontoen (account)
    public Response pay(@PathParam("balance") int balance, Account account){
        return Response.ok().build();
    }


    // return value -1 means free table was not found.
    @GET
    @Path("/orders/order/findTable/{date}/{slotnr}")
    @Produces(MediaType.APPLICATION_JSON)
    public int findTable(@PathParam("date") String date, @PathParam("slotnr") int slotnr){
        HashMap<Integer, HashMap<Integer, Order>>  ordersByDate = registration.getOrdersByDate(date);
        if(ordersByDate == null)
            return 0;

        HashMap<Integer, Order> ordersBySlot = ordersByDate.get(slotnr);
        if(ordersBySlot == null)
            return 0;

        for(int i = 0; i < NUM_TABLES; i++){
            if(ordersBySlot.get(i) == null)
                return i;
        }

        return -1;

    }

}


