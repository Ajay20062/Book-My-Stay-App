import java.util.HashMap;
import java.util.Map;

/**
 * =====================================================
 * MAIN CLASS - BookMyStayApp
 * =====================================================
 * Combines:
 * UC1 - Application Startup
 * UC2 - Basic Room Types & Static Availability
 * UC3 - Centralized Room Inventory Management
 * UC4 - Room Search & Availability Check
 *
 * Demonstrates abstraction, inheritance, polymorphism,
 * centralized inventory management, and read-only search.
 *
 * @author  T R Ajay Dharrsan
 * @version 4.0
 */

/*-------------------------------------------------------
ABSTRACT CLASS : UC2 Room
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

    private Map<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
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

        System.out.println("=======================================================");
        System.out.println("            AVAILABLE ROOMS FOR BOOKING                ");
        System.out.println("=======================================================");

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
MAIN APPLICATION
-------------------------------------------------------*/
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("                    BOOK MY STAY APP                   ");
        System.out.println("=======================================================");
        System.out.println();
        System.out.println("                    Version  : 4.0                     ");
        System.out.println("       Status   : Application Started Successfully     ");
        System.out.println("   Message  : Welcome to the Book My Stay Application! ");
        System.out.println();
        System.out.println("=======================================================");

        /* Room Objects */
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleRoom, suite};

        /* Inventory */
        RoomInventory inventory = new RoomInventory();

        /* UC4 Search Service */
        RoomSearchService searchService = new RoomSearchService();

        /* Guest searches available rooms */
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println();
        inventory.displayInventory();

        System.out.println();
        System.out.println("=======================================================");
        System.out.println("               APPLICATION TERMINATED                  ");
        System.out.println("=======================================================");
    }
}
