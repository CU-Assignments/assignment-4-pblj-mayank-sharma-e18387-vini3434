import java.util.concurrent.locks.ReentrantLock;

class TicketBookingSystem {
    private int availableSeats;
    private final ReentrantLock lock = new ReentrantLock();

    public TicketBookingSystem(int seats) {
        this.availableSeats = seats;
    }

    public void bookTicket(String name, int seats) {
        lock.lock();
        try {
            if (availableSeats >= seats) {
                System.out.println(name + " successfully booked " + seats + " seat(s). Remaining: " + (availableSeats - seats));
                availableSeats -= seats;
            } else {
                System.out.println(name + " failed to book. Not enough seats available.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class BookingThread extends Thread {
    private TicketBookingSystem system;
    private String name;
    private int seats;

    public BookingThread(TicketBookingSystem system, String name, int seats, int priority) {
        super(name);
        this.system = system;
        this.name = name;
        this.seats = seats;
        setPriority(priority);
    }

    @Override
    public void run() {
        system.bookTicket(name, seats);
    }
}

public class Main {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem(10);

        BookingThread vip1 = new BookingThread(system, "VIP1", 3, Thread.MAX_PRIORITY);
        BookingThread vip2 = new BookingThread(system, "VIP2", 2, Thread.MAX_PRIORITY);
        BookingThread user1 = new BookingThread(system, "User1", 4, Thread.NORM_PRIORITY);
        BookingThread user2 = new BookingThread(system, "User2", 2, Thread.NORM_PRIORITY);

        vip1.start();
        vip2.start();
        user1.start();
        user2.start();
    }
}
