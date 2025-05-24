package Practice.QuickRevision;
import java.time.LocalDate;
import java.util.*;

public class HotelBookingSystem {
    public static void main(String[] args) {
        Room r1 = new Room(1, RoomType.Single, 1000);
        Room r2 = new Room(2, RoomType.Double, 2000);
        Room r3 = new Room(3, RoomType.Suit, 5000);
        Room r4 = new Room(4, RoomType.Single, 1000);
        Hotel h1 = new Hotel("xyz", "hotel1", List.of(r1, r2, r3, r4));

        BookingService bookingService = new BookingService(List.of(h1));
        Booking booking = bookingService.bookRoom(h1.id, RoomType.Single, LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 20));
        if(booking != null) {
            System.out.println(booking.id);
            bookingService.cancelBooking(booking.id);
        }
    }
}

class BookingService {
    List<Hotel> hotels;
    Map<UUID, Booking> allBookings;

    public BookingService(List<Hotel> hotels) {
        this.hotels = hotels;
        allBookings = new HashMap<>();
    }

    List<Room> searchRooms(UUID hotelId, RoomType roomType, LocalDate start, LocalDate end) {
        Hotel hotel = findHotelById(hotelId);
        if(hotel == null) {
            System.out.println("Hotel not Found");
            return null;
        }
        List<Room> availableRooms = new ArrayList<>();
        for(Room room: hotel.rooms) {
            if(room.roomType.equals(roomType) && room.isAvailable(start, end)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    Booking bookRoom(UUID hotelId, RoomType roomType, LocalDate start, LocalDate end) {
        List<Room> availableRooms = searchRooms(hotelId, roomType, start, end);
        if(availableRooms.isEmpty()) {
            System.out.println("No Rooms Available");
            return null;
        }
        Room room = availableRooms.getFirst();
        Booking booking = new Booking(room, start, end, BookingStatus.CONFIRMED);
        room.bookings.add(booking);
        allBookings.put(booking.id, booking);
        System.out.println("Booking Confirmed");
        return booking;
    }

    void cancelBooking(UUID bookingId) {
        Booking booking = allBookings.get(bookingId);
        if (booking != null && booking.bookingStatus == BookingStatus.CONFIRMED) {
            booking.bookingStatus = BookingStatus.CANCELLED;
            System.out.println("Booking Cancelled");
        }
    }

    Hotel findHotelById(UUID hotelId) {
        for(Hotel hotel: hotels) {
            if(hotel.id.equals(hotelId)) return hotel;
        }
        return null;
    }
}

class Hotel {
    UUID id;
    String address, name;
    List<Room> rooms;

    public Hotel(String address, String name, List<Room> rooms) {
        id = UUID.randomUUID();
        this.address = address;
        this.name = name;
        this.rooms = rooms;
    }
}

class Room {
    UUID id;
    int roomNumber;
    RoomType roomType;
    double price;
    List<Booking> bookings;

    public Room(int roomNumber, RoomType roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        id = UUID.randomUUID();
        bookings = new ArrayList<>();
    }

    boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        for(Booking booking: bookings) {
            if(booking.isOverlap(startDate, endDate) && booking.bookingStatus.equals(BookingStatus.CONFIRMED)) return false;
        }
        return true;
    }
}

class Booking {
    UUID id;
    Room room;
    LocalDate startDate, endDate;
    BookingStatus bookingStatus;

    public Booking(Room room, LocalDate startDate, LocalDate endDate, BookingStatus bookingStatus) {
        id = UUID.randomUUID();
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
    }

    boolean isOverlap(LocalDate start, LocalDate end) {
        return start.isBefore(endDate) || end.isAfter(startDate);
    }
}

enum BookingStatus { CONFIRMED, CANCELLED }

enum RoomType { Single, Double, Suit }
