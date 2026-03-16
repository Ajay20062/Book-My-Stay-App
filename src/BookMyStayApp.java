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
 *
 * @author  T R Ajay Dharrsan
 * @version 7.0
 */

/*-------------------------------------------------------
ABSTRACT CLASS : Room
-------------------------------------------------------*/
abstract class Room {

    private final String roomType;
    private final int beds;
    private final int size;
    private final double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type    : " + roomType);
        System.out.println("Beds         : " + beds);
        System.out.println("Room Size    : " + size + " sq.ft");
        System.out.println("Price/Night  : ₹" + price);
    }
}

/*-------------------------------------------------------
ROOM TYPES
-------------------------------------------------------*/
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 2000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 3500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 7000);
    }
}

/*-------------------------------------------------------
ROOM INVENTORY
-------------------------------------------------------*/
class RoomInventory {

    private final Map<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {

        int count = inventory.get(roomType);

        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    public void displayInventory() {

        System.out.println("------------- CURRENT ROOM INVENTORY -------------");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available : " + entry.getValue());
        }

        System.out.println("--------------------------------------------------");
    }
}

/*-------------------------------------------------------
RESERVATION CLASS
-------------------------------------------------------*/
record Reservation(String guestName, String roomType) {

}

/*-------------------------------------------------------
BOOKING REQUEST QUEUE
-------------------------------------------------------*/
class BookingRequestQueue {

    private final Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.add(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/*-------------------------------------------------------
ROOM ALLOCATION SERVICE (UC6)
-------------------------------------------------------*/
class RoomAllocationService {

    private final Map<String, Set<String>> allocatedRooms;
    private final Set<String> usedRoomIds;

    public RoomAllocationService() {

        allocatedRooms = new HashMap<>();
        usedRoomIds = new HashSet<>();

        allocatedRooms.put("Single Room", new HashSet<>());
        allocatedRooms.put("Double Room", new HashSet<>());
        allocatedRooms.put("Suite Room", new HashSet<>());
    }

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("=================================================");
        System.out.println("        PROCESSING BOOKING REQUESTS (FIFO)       ");
        System.out.println("=================================================");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();

            String roomType = request.roomType();
            String guest = request.guestName();

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                allocatedRooms.get(roomType).add(roomId);
                usedRoomIds.add(roomId);

                inventory.decrementRoom(roomType);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest  : " + guest);
                System.out.println("Room   : " + roomType);
                System.out.println("RoomID : " + roomId);
                System.out.println("---------------------------------------");

            } else {

                System.out.println("Reservation Failed (No Rooms Available)");
                System.out.println("Guest : " + guest);
                System.out.println("Requested : " + roomType);
                System.out.println("---------------------------------------");
            }
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0,2).toUpperCase();
        String roomId;

        do {
            roomId = prefix + (int)(Math.random() * 1000);
        }
        while (usedRoomIds.contains(roomId));

        return roomId;
    }
}

/*-------------------------------------------------------
ADD ON SERVICE CLASS (UC7)
-------------------------------------------------------*/
record AddOnService(String serviceName, double cost) {

}

/*-------------------------------------------------------
ADD ON SERVICE MANAGER (UC7)
-------------------------------------------------------*/
class AddOnServiceManager {

    private final Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.cost();
            }
        }

        return total;
    }

    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId);

        for (AddOnService s : services) {
            System.out.println(s.serviceName() + " : ₹" + s.cost());
        }

        System.out.println("Total Add-On Cost : ₹" + calculateTotalCost(reservationId));
    }
}

/*-------------------------------------------------------
MAIN APPLICATION
-------------------------------------------------------*/
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println("                BOOK MY STAY APP                 ");
        System.out.println("=================================================");
        System.out.println("                 Version : 7.0                   ");
        System.out.println("         Application Started Successfully!       ");
        System.out.println("=================================================");
        System.out.println();
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Ajay", "Single Room"));
        queue.addRequest(new Reservation("Rahul", "Double Room"));
        queue.addRequest(new Reservation("Priya", "Suite Room"));

        RoomAllocationService allocationService = new RoomAllocationService();

        allocationService.processBookings(queue, inventory);

        inventory.displayInventory();

        /* UC7 Add-On Services */
        System.out.println("\n========== ADD ON SERVICES ==========");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        String reservationId = "RES101";

        serviceManager.addService(reservationId, new AddOnService("Breakfast", 500));
        serviceManager.addService(reservationId, new AddOnService("Airport Pickup", 1200));
        serviceManager.addService(reservationId, new AddOnService("Spa", 2000));

        serviceManager.displayServices(reservationId);
    }
}
