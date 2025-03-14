package Practice.BookMyShow;

import java.util.*;

public class BookMyShowSystem {
    public static void main(String[] args) {
        BookMyShow bookMyShow = new BookMyShow();
        bookMyShow.createBooking(City.Bangalore, "BAAHUBALI");
        bookMyShow.createBooking(City.Bangalore, "BAAHUBALI");
    }
}

class BookMyShow {
    MovieController movieController;
    TheatreController theatreController;

    public BookMyShow() {
        movieController = new MovieController();
        theatreController = new TheatreController();
        createMovies();
        createTheatre();

    }

    public boolean createBooking(City city, String movieName) {
        //1. search movie by my location
        List<Movie> movies = movieController.getMoviesByCity(city);

        //2. select the movie which you want to see. i want to see Baahubali
        Movie interestedMovie = null;
        for (Movie movie : movies) {

            if ((movie.getName()).equals(movieName)) {
                interestedMovie = movie;
            }
        }

        //3. get all show of this movie in Bangalore location
        Map<Theatre, List<Show>> showsTheatreWise = theatreController.getAllShows(interestedMovie, city);

        //4. select the particular show user is interested in
        Map.Entry<Theatre,List<Show>> entry = showsTheatreWise.entrySet().iterator().next();
        List<Show> runningShows = entry.getValue();
        Show interestedShow = runningShows.get(0);

        //5. select the seat
        int seatNumber = 30;
        List<Integer> bookedSeats = interestedShow.getBookedSeats();
        if(!bookedSeats.contains(seatNumber)){
            bookedSeats.add(seatNumber);
            //startPayment
            Booking booking = new Booking();
            List<Seat> myBookedSeats = new ArrayList<>();
            for(Seat screenSeat : interestedShow.getScreen().getSeats()) {
                if(screenSeat.getId() == seatNumber) {
                    myBookedSeats.add(screenSeat);
                }
            }
            booking.setSeats(myBookedSeats);
            booking.setShow(interestedShow);
        } else {
            //throw exception
            System.out.println("seat already booked, try again");
            return false;
        }

        System.out.println("BOOKING SUCCESSFUL");
        return true;
    }

    private void createTheatre() {

        Movie avengerMovie = movieController.getMovieByName("AVENGERS");
        Movie baahubali = movieController.getMovieByName("BAAHUBALI");

        Theatre inoxTheatre = new Theatre();
        inoxTheatre.setId(1);
        inoxTheatre.setScreens(createScreen());
        inoxTheatre.setCity(City.Bangalore);
        List<Show> inoxShows = new ArrayList<>();
        Show inoxMorningShow = createShows(1, inoxTheatre.getScreens().get(0), avengerMovie, 8);
        Show inoxEveningShow = createShows(2, inoxTheatre.getScreens().get(0), baahubali, 16);
        inoxShows.add(inoxMorningShow);
        inoxShows.add(inoxEveningShow);
        inoxTheatre.setShows(inoxShows);


        Theatre pvrTheatre = new Theatre();
        pvrTheatre.setId(2);
        pvrTheatre.setScreens(createScreen());
        pvrTheatre.setCity(City.Delhi);
        List<Show> pvrShows = new ArrayList<>();
        Show pvrMorningShow = createShows(3, pvrTheatre.getScreens().get(0), avengerMovie, 13);
        Show pvrEveningShow = createShows(4, pvrTheatre.getScreens().get(0), baahubali, 20);
        pvrShows.add(pvrMorningShow);
        pvrShows.add(pvrEveningShow);
        pvrTheatre.setShows(pvrShows);

        theatreController.addTheatre(inoxTheatre, City.Bangalore);
        theatreController.addTheatre(pvrTheatre, City.Delhi);

    }


    private List<Screen> createScreen() {
        List<Screen> screens = new ArrayList<>();
        Screen screen1 = new Screen();
        screen1.setId(1);
        screen1.setSeats(createSeats());
        screens.add(screen1);
        return screens;
    }

    private Show createShows(int showId, Screen screen, Movie movie, int showStartTime) {
        Show show = new Show();
        show.setId(showId);
        show.setScreen(screen);
        show.setMovie(movie);
        return show;
    }

    //creating 100 seats
    private List<Seat> createSeats() {
        //creating 100 seats
        List<Seat> seats = new ArrayList<>();

        //1 to 40 : SILVER
        for (int i = 0; i < 40; i++) {
            Seat seat = new Seat();
            seat.setId(i);
            seat.setSeatCategory(SeatCategory.SILVER);
            seats.add(seat);
        }

        //41 to 70 : SILVER
        for (int i = 40; i < 70; i++) {
            Seat seat = new Seat();
            seat.setId(i);
            seat.setSeatCategory(SeatCategory.GOLD);
            seats.add(seat);
        }

        //1 to 40 : SILVER
        for (int i = 70; i < 100; i++) {
            Seat seat = new Seat();
            seat.setId(i);
            seat.setSeatCategory(SeatCategory.PLATINUM);
            seats.add(seat);
        }

        return seats;
    }

    private void createMovies() {

        //create Movies1
        Movie avengers = new Movie();
        avengers.setId(1);
        avengers.setName("AVENGERS");
        avengers.setDurationInMinutes(128);

        //create Movies2
        Movie baahubali = new Movie();
        baahubali.setId(2);
        baahubali.setName("BAAHUBALI");
        baahubali.setDurationInMinutes(180);


        //add movies against the cities
        movieController.addMovie(avengers, City.Bangalore);
        movieController.addMovie(avengers, City.Delhi);
        movieController.addMovie(baahubali, City.Bangalore);
        movieController.addMovie(baahubali, City.Delhi);
    }

}

class TheatreController {
    private Map<City, List<Theatre>> cityListMap;
    private List<Theatre> theatres;

    public TheatreController() {
        cityListMap = new HashMap<>();
        theatres = new ArrayList<>();
    }

    public void addTheatre(Theatre theatre, City city) {
        theatres.add(theatre);
        cityListMap.putIfAbsent(city, new ArrayList<>());
        cityListMap.get(city).add(theatre);
    }

    public Map<Theatre, List<Show>> getAllShows(Movie movie, City city) {
        Map<Theatre, List<Show>> shows = new HashMap<>();

        for(Theatre theatre: cityListMap.get(city)) {
            for(Show show: theatre.getShows()) {
                if(show.getId() == movie.getId()) {
                    shows.putIfAbsent(theatre, new ArrayList<>());
                    shows.get(theatre).add(show);
                }
            }
        }

        return shows;
    }
}

class MovieController {
    private Map<City, List<Movie>> cityListMap;
    private List<Movie> movies;

    public MovieController() {
        cityListMap = new HashMap<>();
        movies = new ArrayList<>();
    }

    public void addMovie(Movie movie, City city) {
        movies.add(movie);
        cityListMap.putIfAbsent(city, new ArrayList<>());
        cityListMap.get(city).add(movie);
    }

    public Movie getMovieByName(String movieName) {
        for(Movie movie: movies) {
            if(movie.getName().equals(movieName)) return movie;
        }
        return null;
    }

    public List<Movie> getMoviesByCity(City city) {
        return cityListMap.get(city);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        cityListMap.forEach((city, movieList) -> {
            if(!movieList.isEmpty()) movieList.remove(movie);
        });
    }
}

class Booking {
    private int id;
    private List<Seat> seats;
    private Show show;
    Payment payment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}

class Seat {
    private int id;
    private int row;
    SeatCategory seatCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public SeatCategory getSeatCategory() {
        return seatCategory;
    }

    public void setSeatCategory(SeatCategory seatCategory) {
        this.seatCategory = seatCategory;
    }
}

class Movie {
    private int id;
    private String name;
    private int durationInMinutes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}

class Theatre {
    private int id;
    private String address;
    private City city;
    private List<Screen> screens;
    private List<Show> shows;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}

class Screen {
    private int id;
    List<Seat> seats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}

class Show {
    private int id;
    private Movie movie;
    private Screen screen;
    private List<Integer> bookedSeats;

    public Show() {
        this.bookedSeats = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public List<Integer> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<Integer> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}

class Payment {
    private int id;
    private String paymentMode;
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

enum SeatCategory {
    SILVER,
    GOLD,
    PLATINUM;
}

enum City {
    Bangalore,
    Delhi;
}


