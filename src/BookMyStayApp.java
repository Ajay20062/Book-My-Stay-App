import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**

 * =====================================================
 * MAIN CLASS - BookMyStayApp
 * =====================================================
 * Combines:
 * UC1 - Application Startup
 * UC2 - Basic Room Types (Abstraction & Inheritance)
 * UC3 - Centralized Room Inventory (HashMap)
 * UC4 - Room Search & Availability Check
 * UC5 - Booking Request Queue (FIFO)
 * Demonstrates abstraction, inheritance, polymorphism,
 * centralized inventory management, read-only search,
 * and fair booking request handling using Queue.
 *
 * @author  T R Ajay Dharrsan
 * @version 5.0
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
ROOM INVENTORY (UC3)
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

    public void displayInventory() {

        System.out.println("------------- CURRENT ROOM INVENTORY -------------");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available : " + entry.getValue());
        }

        System.out.println("--------------------------------------------------");
    }
}

/*-------------------------------------------------------
ROOM SEARCH SERVICE (UC4)
-------------------------------------------------------*/
class RoomSearchService {

    public void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        System.out.println("=================================================");
        System.out.println("          AVAILABLE ROOMS FOR BOOKING            ");
        System.out.println("=================================================");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            if (available > 0) {

                room.displayRoomDetails();
                System.out.println("Available Rooms : " + available);
                System.out.println("---------------------------------------");
            }
        }
    }

}

/*-------------------------------------------------------
RESERVATION CLASS (UC5)
-------------------------------------------------------*/
class Reservation {
    private final String guestName;
    private final String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void displayReservation() {
        System.out.println("Guest : " + guestName + " | Requested Room : " + roomType);
    }
}

/*-------------------------------------------------------
BOOKING REQUEST QUEUE (UC5)
-------------------------------------------------------*/
class BookingRequestQueue {
    private final Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {

        queue.add(reservation);

        System.out.println("Booking Request Added:");
        reservation.displayReservation();
        System.out.println();
    }

    public void displayQueue() {

        System.out.println("=================================================");
        System.out.println("          CURRENT BOOKING REQUEST QUEUE          ");
        System.out.println("=================================================");

        for (Reservation r : queue) {
            r.displayReservation();
        }
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
        System.out.println("Version : 5.0");
        System.out.println("Status  : Application Started Successfully");
        System.out.println();

        /* Room Objects */
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleRoom, suite};

        /* Inventory */
        RoomInventory inventory = new RoomInventory();

        /* UC4: Search Service */
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(rooms, inventory);

        /* UC5: Booking Queue */
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Ajay", "Single Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite Room"));

        bookingQueue.displayQueue();

        System.out.println();
        inventory.displayInventory();

        System.out.println();
        System.out.println("=================================================");
        System.out.println("               APPLICATION TERMINATED            ");
        System.out.println("=================================================");
    }
}
