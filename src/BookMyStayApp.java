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
 * UC10 - Booking Cancellation & Inventory Rollback
 * @author  T R Ajay Dharrsan
 * @version 10.0
 */

/*------------------------------------------------
CUSTOM EXCEPTION (UC9)
------------------------------------------------*/
class InvalidBookingException extends Exception{
    public InvalidBookingException(String msg){
        super(msg);
    }
}

/*------------------------------------------------
ROOM ABSTRACT CLASS (UC2)
------------------------------------------------*/
abstract class Room{

    private final String roomType;
    private final int beds;
    private final int size;
    private final double price;

    public Room(String type,int beds,int size,double price){
        this.roomType=type;
        this.beds=beds;
        this.size=size;
        this.price=price;
    }

    public String getRoomType(){
        return roomType;
    }

    public void displayRoomDetails(){
        System.out.println(roomType+" | Beds:"+beds+" | Size:"+size+"sqft | ₹"+price);
    }
}

class SingleRoom extends Room{
    public SingleRoom(){ super("Single Room",1,200,2000); }
}

class DoubleRoom extends Room{
    public DoubleRoom(){ super("Double Room",2,350,3500); }
}

class SuiteRoom extends Room{
    public SuiteRoom(){ super("Suite Room",3,600,7000); }
}

/*------------------------------------------------
ROOM INVENTORY (UC3)
------------------------------------------------*/
class RoomInventory{

    private final Map<String,Integer> inventory=new HashMap<>();

    public RoomInventory(){
        inventory.put("Single Room",5);
        inventory.put("Double Room",3);
        inventory.put("Suite Room",2);
    }

    public boolean isValidRoom(String type){
        return inventory.containsKey(type);
    }

    public int getAvailability(String type){
        return inventory.getOrDefault(type,0);
    }

    public void decrementRoom(String type) throws InvalidBookingException{
        if(!isValidRoom(type))
            throw new InvalidBookingException("Invalid room type");

        if(getAvailability(type)<=0)
            throw new InvalidBookingException("No rooms available");

        inventory.put(type,getAvailability(type)-1);
    }

    public void incrementRoom(String type){
        inventory.put(type,getAvailability(type)+1);
    }

    public void displayInventory(){
        System.out.println("\nROOM INVENTORY");
        for(Map.Entry<String,Integer> e:inventory.entrySet())
            System.out.println(e.getKey()+" : "+e.getValue());
    }
}

/*------------------------------------------------
RESERVATION CLASS
------------------------------------------------*/
record Reservation(String guestName, String roomType, String roomId) {

}

/*------------------------------------------------
BOOKING REQUEST QUEUE (UC5)
------------------------------------------------*/
class BookingRequestQueue{

    private final Queue<Reservation> queue=new LinkedList<>();

    public void addRequest(Reservation r){
        queue.add(r);
    }

    public Reservation nextRequest(){
        return queue.poll();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}

/*------------------------------------------------
BOOKING HISTORY (UC8)
------------------------------------------------*/
class BookingHistory{

    private final List<Reservation> history=new ArrayList<>();

    public void addReservation(Reservation r){
        history.add(r);
    }

    public void removeReservation(Reservation r){
        history.remove(r);
    }

    public Reservation findReservation(String id){
        for(Reservation r:history)
            if(r.roomId().equals(id))
                return r;
        return null;
    }

    public List<Reservation> getHistory(){
        return history;
    }
}

/*------------------------------------------------
BOOKING REPORT SERVICE (UC8)
------------------------------------------------*/
class BookingReportService{

    public void generateReport(List<Reservation> list){

        System.out.println("\nBOOKING REPORT");

        Map<String,Integer> summary=new HashMap<>();

        for(Reservation r:list){

            System.out.println(
                    r.guestName()+" | "+
                            r.roomType()+" | "+
                            r.roomId()
            );

            summary.put(
                    r.roomType(),
                    summary.getOrDefault(r.roomType(),0)+1
            );
        }

        System.out.println("\nSUMMARY");

        for(Map.Entry<String,Integer> e:summary.entrySet())
            System.out.println(e.getKey()+" booked : "+e.getValue());
    }
}

/*------------------------------------------------
ADD ON SERVICE (UC7)
------------------------------------------------*/
class AddOnService{

    String name;
    double cost;

    public AddOnService(String n,double c){
        name=n;
        cost=c;
    }
}

class AddOnServiceManager{

    private final Map<String,List<AddOnService>> services=new HashMap<>();

    public void addService(String resId,AddOnService s){

        services
                .computeIfAbsent(resId,k->new ArrayList<>())
                .add(s);
    }

    public void displayServices(String resId){

        List<AddOnService> list=services.get(resId);

        if(list==null){
            System.out.println("No Add-on services");
            return;
        }

        double total=0;

        for(AddOnService s:list){
            System.out.println(s.name+" ₹"+s.cost);
            total+=s.cost;
        }

        System.out.println("Total Add-on Cost : ₹"+total);
    }
}

/*------------------------------------------------
ROOM ALLOCATION SERVICE (UC6)
------------------------------------------------*/
class RoomAllocationService{

    private Set<String> usedIds=new HashSet<>();

    public void processBookings(
            BookingRequestQueue queue,
            RoomInventory inventory,
            BookingHistory history){

        while(!queue.isEmpty()){

            Reservation req=queue.nextRequest();

            try{

                inventory.decrementRoom(req.roomType());

                history.addReservation(req);

                System.out.println(
                        "Booking Confirmed : "+
                                req.guestName()+" | "+
                                req.roomType()+" | "+
                                req.roomId()
                );

            }
            catch(Exception e){

                System.out.println(
                        "Booking Failed for "+
                                req.guestName()+" : "+
                                e.getMessage()
                );
            }
        }
    }

    public Set<String> getUsedIds() {
        return usedIds;
    }

    public void setUsedIds(Set<String> usedIds) {
        this.usedIds = usedIds;
    }
}

/*------------------------------------------------
CANCELLATION SERVICE (UC10)
------------------------------------------------*/
class CancellationService{

    private final Stack<String> rollbackStack=new Stack<>();

    public void cancelBooking(
            String roomId,
            BookingHistory history,
            RoomInventory inventory
    ) throws InvalidBookingException{

        Reservation r=history.findReservation(roomId);

        if(r==null)
            throw new InvalidBookingException("Reservation not found");

        rollbackStack.push(roomId);

        inventory.incrementRoom(r.roomType());

        history.removeReservation(r);

        System.out.println("Booking Cancelled : "+roomId);
    }

    public void showRollback(){
        System.out.println("Rollback Stack : "+rollbackStack);
    }
}

/*------------------------------------------------
MAIN APPLICATION
------------------------------------------------*/
public class BookMyStayApp{

    public static void main(String[] args){

        System.out.println("=================================================");
        System.out.println("                BOOK MY STAY APP                 ");
        System.out.println("=================================================");
        System.out.println("                 Version : 10.0                  ");
        System.out.println("    Status : Application Started Successfully    ");
        System.out.println("=================================================");

        RoomInventory inventory=new RoomInventory();

        BookingRequestQueue queue=new BookingRequestQueue();

        BookingHistory history=new BookingHistory();

        queue.addRequest(new Reservation("Ajay","Single Room","SR101"));
        queue.addRequest(new Reservation("Rahul","Double Room","DR201"));
        queue.addRequest(new Reservation("Priya","Suite Room","SU301"));

        RoomAllocationService allocation=new RoomAllocationService();

        allocation.processBookings(queue,inventory,history);

        inventory.displayInventory();

        AddOnServiceManager addOn=new AddOnServiceManager();

        addOn.addService("SR101",new AddOnService("Breakfast",500));
        addOn.addService("SR101",new AddOnService("Spa",2000));

        System.out.println("\nAdd-on services for SR101");
        addOn.displayServices("SR101");

        BookingReportService report=new BookingReportService();
        report.generateReport(history.getHistory());

        CancellationService cancel=new CancellationService();

        try{
            cancel.cancelBooking("SR101",history,inventory);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        inventory.displayInventory();
        cancel.showRollback();
    }
}
