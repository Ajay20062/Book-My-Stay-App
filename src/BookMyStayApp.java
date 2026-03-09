/**

 * =====================================================
 * MAIN CLASS - BookMyStayApp
 * =====================================================
 * Combines:
 * UC1 - Application Startup
 * UC2 - Basic Room Types & Static Availability
 *
 * Demonstrates abstraction, inheritance, polymorphism
 * and static availability representation.
 *
 * @author  T R Ajay Dharrsan
 * @version 2.0
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

    public void displayRoomDetails() {
        System.out.println("Room Type   : " + roomType);
        System.out.println("Beds        : " + beds);
        System.out.println("Room Size   : " + size + " sq.ft");
        System.out.println("Price/Night : ₹" + price);
    }
}

/*-------------------------------------------------------
SINGLE ROOM
-------------------------------------------------------*/
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 2000);
    }
}

/*-------------------------------------------------------
DOUBLE ROOM
-------------------------------------------------------*/
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 3500);
    }
}

/*-------------------------------------------------------
SUITE ROOM
-------------------------------------------------------*/
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 7000);
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
        System.out.println("                      Version  : 2.0                   ");
        System.out.println("      Status   : Application Started Successfully      ");
        System.out.println("   Message : Welcome to the Book My Stay Application!  ");
        System.out.println();
        System.out.println("=======================================================");
        System.out.println("                 ROOM AVAILABILITY STATUS              ");
        System.out.println("=======================================================");
        System.out.println();

        /* Room Object Creation (Polymorphism) */
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        /* Static Availability Variables */
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        single.displayRoomDetails();
        System.out.println("Available   : " + singleAvailable);
        System.out.println("---------------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available   : " + doubleAvailable);
        System.out.println("---------------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available   : " + suiteAvailable);
        System.out.println("---------------------------------------");

        System.out.println();
        System.out.println("=======================================================");
        System.out.println("               APPLICATION TERMINATED                  ");
        System.out.println("=======================================================");
    }

}
