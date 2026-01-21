import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

// 1. The Physical Entity
class MeetingRoom {
    String id;
    int capacity;
    String name;
    // Each room has its own lock. This allows User A to book Room 1 
    // while User B books Room 2 without blocking each other.
    ReentrantLock lock; 
    
    TreeMap<LocalDateTime, LocalDateTime> bookedIntervals; 

    public MeetingRoom(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.bookedIntervals = new TreeMap<>();
    }
}

// 2. The Reservation Receipt
class Meeting {
    String id;
    String roomId;
    String organizerId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<String> attendees;
    
    // Constructor and Getters...
}

// 3. The Actor
class User {
    String id;
    String email;
    // Getters...
}

class BookingScheduler {
    // In-memory storage of all rooms
    private final Map<String, MeetingRoom> roomDirectory;
    // Map to quickly find meetings by User ID (for requirement #4)
    private final Map<String, List<Meeting>> userHistory;

    public BookingScheduler(List<MeetingRoom> rooms) {
        this.roomDirectory = new ConcurrentHashMap<>();
        this.userHistory = new ConcurrentHashMap<>();
        for (MeetingRoom r : rooms) {
            roomDirectory.put(r.id, r);
        }
    }

    public List<MeetingRoom> searchRooms(int minCapacity, LocalDateTime start, LocalDateTime end) {
        List<MeetingRoom> availableRooms = new ArrayList<>();
        
        for (MeetingRoom room : roomDirectory.values()) {
            if (room.capacity >= minCapacity) {
                if (isAvailable(room, start, end)) {
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }

    public Meeting bookRoom(String userId, String roomId, LocalDateTime start, LocalDateTime end) throws Exception {
        MeetingRoom room = roomDirectory.get(roomId);
        if (room == null) throw new IllegalArgumentException("Invalid Room ID");

        // CRITICAL SECTION START
        // We lock ONLY this specific room. Other rooms can still be booked.
        room.lock.lock(); 
        try {
            // 1. Double-Check Availability (The "Check-Then-Act" pattern)
            if (!isAvailable(room, start, end)) {
                throw new IllegalStateException("Room " + room.name + " is already booked for this time.");
            }

            // 2. Persist the Booking
            // Add to the interval map
            room.bookedIntervals.put(start, end);

            // Create Meeting Object
            Meeting meeting = new Meeting();
            meeting.id = UUID.randomUUID().toString();
            meeting.roomId = roomId;
            meeting.organizerId = userId;
            meeting.startTime = start;
            meeting.endTime = end;

            // Update User History
            userHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(meeting);
            
            System.out.println("SUCCESS: Room " + room.name + " booked by " + userId);
            return meeting;

        } finally {
            room.lock.unlock(); 
            // CRITICAL SECTION END
        }
    }

    // Helper: Logic to check if a slot overlaps with existing bookings
    private boolean isAvailable(MeetingRoom room, LocalDateTime start, LocalDateTime end) {
        Map.Entry<LocalDateTime, LocalDateTime> prev = room.bookedIntervals.floorEntry(start);
        
        // Check the entry that starts immediately after our requested start
        Map.Entry<LocalDateTime, LocalDateTime> next = room.bookedIntervals.ceilingEntry(start);

        // Conflict Rule 1: Previous meeting ends AFTER we want to start
        if (prev != null && prev.getValue().isAfter(start)) {
            return false;
        }

        // Conflict Rule 2: Next meeting starts BEFORE we want to finish
        if (next != null && next.getKey().isBefore(end)) {
            return false;
        }

        return true;
    }
}

public class MeetingSystemDemo {
    public static void main(String[] args) {
        // Setup: Create 2 Rooms
        List<MeetingRoom> rooms = new ArrayList<>();
        rooms.add(new MeetingRoom("R1", "BoardRoom", 10)); // Capacity 10
        rooms.add(new MeetingRoom("R2", "PhoneBooth", 1)); // Capacity 1

        BookingScheduler scheduler = new BookingScheduler(rooms);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        // Simulation: 2 Users try to book the SAME room at the SAME time
        Runnable userTask = () -> {
            try {
                String threadName = Thread.currentThread().getName();
                scheduler.bookRoom(threadName, "R1", now, oneHourLater);
            } catch (Exception e) {
                System.out.println("FAILURE (" + Thread.currentThread().getName() + "): " + e.getMessage());
            }
        };

        Thread t1 = new Thread(userTask, "User-A");
        Thread t2 = new Thread(userTask, "User-B");

        t1.start();
        t2.start();
    }
}