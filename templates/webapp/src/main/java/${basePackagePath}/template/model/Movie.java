package ${basePackage}.template.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Movie data object
 *
 * @author ${user}
 * @since ${date}
 */
public class Movie {

    private String title;

    private int year;

    private List<String> genres = new LinkedList<>();

    private String synopsis;

    private List<Actor> actors = new LinkedList<>();

    public static Movie example() {

        Movie movie = new Movie();
        movie.setTitle("Blade Runner");
        movie.setYear(1982);
        movie.setGenres(Arrays.asList("Sci-Fi", "Thriller"));
        movie.setSynopsis("A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.");
        movie.getActors().add(new Actor("Harrison", "Ford", LocalDate.of(1942, 7, 13)));
        movie.getActors().add(new Actor("Rutger", "Hauer", LocalDate.of(1944, 1, 23)));
        movie.getActors().add(new Actor("Sean", "Young", LocalDate.of(1959, 11, 20)));
        return movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        title = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}