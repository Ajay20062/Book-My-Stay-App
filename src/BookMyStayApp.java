import java.util.*;

/**
 * =====================================================
 * MAIN CLASS - BookMyStayApp
 * =====================================================
 * Combines:
 * UC1 - Application Startup
 * UC2 - Room Domain Model
 * UC3 - Centralized Inventory
 * UC4 - Room Search
 * UC5 - Booking Request Queue
 * UC6 - Reservation Confirmation & Room Allocation
 * UC7 - Add-On Service Selection
 * UC8 - Booking History & Reporting
 * UC9 - Error Handling & Validation
 *
 * @author  T R Ajay Dharrsan
 * @version 9.0
 *</p>
 * @author T R Ajay Dharrsan
 * @version 0.0
 */

/*-------------------------------------------------------
CUSTOM EXCEPTION (UC9)
-------------------------------------------------------*/
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message){
        super(message);
    }
}

/*-------------------------------------------------------
ABSTRACT CLASS : Room
-------------------------------------------------------*/
abstract class Room {

    private final String roomType;
    private final int beds;
    private final int size;
    private final double price;

    public Room(String roomType,int beds,int size,double price){
        this.roomType=roomType;
        this.beds=beds;
        this.size=size;
        this.price=price;
    }

    public String getRoomType(){
        return roomType;
    }

    public void displayRoomDetails(){
        System.out.println("Room Type : "+roomType);
        System.out.println("Beds : "+beds);
        System.out.println("Room Size : "+size+" sq.ft");
        System.out.println("Price/Night : ₹"+price);
    }
}

/*-------------------------------------------------------
ROOM TYPES
-------------------------------------------------------*/
class SingleRoom extends Room{
    public SingleRoom(){
        super("Single Room",1,200,2000);
    }
}

class DoubleRoom extends Room{
    public DoubleRoom(){
        super("Double Room",2,350,3500);
    }
}

class SuiteRoom extends Room{
    public SuiteRoom(){
        super("Suite Room",3,600,7000);
    }
}

/*-------------------------------------------------------
ROOM INVENTORY
-------------------------------------------------------*/
class RoomInventory{

    private final Map<String,Integer> inventory;

    public RoomInventory(){

        inventory=new HashMap<>();

        inventory.put("Single Room",5);
        inventory.put("Double Room",3);
        inventory.put("Suite Room",2);
    }

    public int getAvailability(String roomType){
        return inventory.getOrDefault(roomType,-1);
    }

    public boolean isValidRoomType(String roomType){
        return inventory.containsKey(roomType);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException{

        int count=getAvailability(roomType);

        if(count<0)
            throw new InvalidBookingException("Invalid Room Type: "+roomType);

        if(count==0)
            throw new InvalidBookingException("No rooms available for "+roomType);

        inventory.put(roomType,count-1);
    }

    public void displayInventory(){

        System.out.println("\n------------- CURRENT ROOM INVENTORY -------------");

        for(Map.Entry<String,Integer> entry:inventory.entrySet())
            System.out.println(entry.getKey()+" Available : "+entry.getValue());

        System.out.println("--------------------------------------------------");
    }
}

/*-------------------------------------------------------
RESERVATION CLASS
-------------------------------------------------------*/
class Reservation{

    private final String guestName;
    private final String roomType;

    public Reservation(String guestName,String roomType){
        this.guestName=guestName;
        this.roomType=roomType;
    }

    public String getGuestName(){
        return guestName;
    }

    public String getRoomType(){
        return roomType;
    }
}

/*-------------------------------------------------------
BOOKING HISTORY (UC8)
-------------------------------------------------------*/
class BookingHistory{

    private final List<Reservation> history=new ArrayList<>();

    public void addReservation(Reservation r){
        history.add(r);
    }

    public List<Reservation> getHistory(){
        return history;
    }
}

/*-------------------------------------------------------
BOOKING REPORT SERVICE
-------------------------------------------------------*/
class BookingReportService{

    public void generateReport(List<Reservation> reservations){

        System.out.println("\n=========== BOOKING HISTORY REPORT ===========");

        Map<String,Integer> summary=new HashMap<>();

        for(Reservation r:reservations){

            System.out.println("Guest : "+r.getGuestName()+" | Room : "+r.getRoomType());

            summary.put(
                    r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(),0)+1
            );
        }

        System.out.println("\n--------- BOOKING SUMMARY ---------");

        for(Map.Entry<String,Integer> entry:summary.entrySet())
            System.out.println(entry.getKey()+" Booked : "+entry.getValue());

        System.out.println("-----------------------------------");
    }
}

/*-------------------------------------------------------
BOOKING REQUEST QUEUE
-------------------------------------------------------*/
class BookingRequestQueue{

    private final Queue<Reservation> queue;

    public BookingRequestQueue(){
        queue=new LinkedList<>();
    }

    public void addRequest(Reservation reservation){
        queue.add(reservation);
    }

    public Reservation getNextRequest(){
        return queue.poll();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}

/*-------------------------------------------------------
BOOKING VALIDATOR (UC9)
-------------------------------------------------------*/
class BookingValidator{

    public static void validate(Reservation r,RoomInventory inventory)
            throws InvalidBookingException{

        if(r.getGuestName()==null||r.getGuestName().isEmpty())
            throw new InvalidBookingException("Guest name cannot be empty");

        if(!inventory.isValidRoomType(r.getRoomType()))
            throw new InvalidBookingException("Invalid Room Type: "+r.getRoomType());

        if(inventory.getAvailability(r.getRoomType())<=0)
            throw new InvalidBookingException("Room not available: "+r.getRoomType());
    }
}

/*-------------------------------------------------------
ROOM ALLOCATION SERVICE
-------------------------------------------------------*/
class RoomAllocationService{

    private final Map<String,Set<String>> allocatedRooms=new HashMap<>();
    private final Set<String> usedRoomIds=new HashSet<>();

    public RoomAllocationService(){

        allocatedRooms.put("Single Room",new HashSet<>());
        allocatedRooms.put("Double Room",new HashSet<>());
        allocatedRooms.put("Suite Room",new HashSet<>());
    }

    public void processBookings(
            BookingRequestQueue queue,
            RoomInventory inventory,
            BookingHistory history){

        System.out.println("\n=========== PROCESSING BOOKINGS ===========");

        while(!queue.isEmpty()){

            Reservation request=queue.getNextRequest();

            try{

                BookingValidator.validate(request,inventory);

                String roomId=generateRoomId(request.getRoomType());

                allocatedRooms.get(request.getRoomType()).add(roomId);

                inventory.decrementRoom(request.getRoomType());

                System.out.println("Reservation Confirmed");
                System.out.println("Guest : "+request.getGuestName());
                System.out.println("Room : "+request.getRoomType());
                System.out.println("RoomID : "+roomId);
                System.out.println("--------------------------------");

                history.addReservation(request);

            }
            catch(InvalidBookingException e){

                System.out.println("Reservation Failed");
                System.out.println("Guest : "+request.getGuestName());
                System.out.println("Error : "+e.getMessage());
                System.out.println("--------------------------------");
            }
        }
    }

    private String generateRoomId(String roomType){

        String prefix=roomType.replace(" ","").substring(0,2).toUpperCase();
        String roomId;

        do{
            roomId=prefix+(int)(Math.random()*1000);
        }
        while(usedRoomIds.contains(roomId));

        usedRoomIds.add(roomId);

        return roomId;
    }
}

/*-------------------------------------------------------
ADD ON SERVICE (UC7)
-------------------------------------------------------*/
class AddOnService{

    private final String serviceName;
    private final double cost;

    public AddOnService(String serviceName,double cost){
        this.serviceName=serviceName;
        this.cost=cost;
    }

    public String getServiceName(){
        return serviceName;
    }

    public double getCost(){
        return cost;
    }
}

/*-------------------------------------------------------
ADD ON SERVICE MANAGER
-------------------------------------------------------*/
class AddOnServiceManager{

    private final Map<String,List<AddOnService>> services=new HashMap<>();

    public void addService(String reservationId,AddOnService service){

        services
                .computeIfAbsent(reservationId,k->new ArrayList<>())
                .add(service);
    }

    public void displayServices(String reservationId){

        List<AddOnService> list=services.get(reservationId);

        if(list==null){
            System.out.println("\nNo Add-On Services Selected");
            return;
        }

        double total=0;

        System.out.println("\nAdd-On Services for "+reservationId);

        for(AddOnService s:list){

            System.out.println(s.getServiceName()+" : ₹"+s.getCost());
            total+=s.getCost();
        }

        System.out.println("Total Add-On Cost : ₹"+total);
    }
}

/*-------------------------------------------------------
MAIN APPLICATION
-------------------------------------------------------*/
public class BookMyStayApp{

    public static void main(String[] args){

        System.out.println("=================================================");
        System.out.println("                BOOK MY STAY APP                 ");
        System.out.println("=================================================");
        System.out.println("                 Version : 9.0                   ");
        System.out.println("    Status : Application Started Successfully    ");
        System.out.println("=================================================");

        RoomInventory inventory=new RoomInventory();
        BookingRequestQueue queue=new BookingRequestQueue();
        BookingHistory history=new BookingHistory();

        queue.addRequest(new Reservation("Ajay","Single Room"));
        queue.addRequest(new Reservation("Rahul","Double Room"));
        queue.addRequest(new Reservation("Priya","Suite Room"));
        queue.addRequest(new Reservation("Arun","Invalid Room"));

        RoomAllocationService allocationService=new RoomAllocationService();

        allocationService.processBookings(queue,inventory,history);

        inventory.displayInventory();

        AddOnServiceManager serviceManager=new AddOnServiceManager();

        serviceManager.addService("RES101",new AddOnService("Breakfast",500));
        serviceManager.addService("RES101",new AddOnService("Airport Pickup",1200));
        serviceManager.addService("RES101",new AddOnService("Spa",2000));

        serviceManager.displayServices("RES101");

        BookingReportService reportService=new BookingReportService();

        reportService.generateReport(history.getHistory());
    }
    
}
