package ${basePackage}.webservice;

import ${basePackage}.webservice.dto.DailyWeather;
import ${basePackage}.webservice.dto.type.WeatherCondition;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Example JAX-RS webservice web service
 *
 * @author ${user}
 * @since ${date}
 */
@RequestScoped
@Path("/")
public class WeatherEndpoint {

    /**
     * Create weather forecast
     */
    @GET
    @Path("/weather")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DailyWeather> getWeatherForecast() {

        List<DailyWeather> forecast = new LinkedList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 6; i++) {
            DailyWeather dailyWeather = randomDailyWeather();
            dailyWeather.setDate(today.plus(i, ChronoUnit.DAYS));
            forecast.add(dailyWeather);
        }

        return forecast;
    }

    private DailyWeather randomDailyWeather() {

        DailyWeather dailyWeather = new DailyWeather();

        WeatherCondition[] conditions = WeatherCondition.values();
        WeatherCondition randomCondition = conditions[(int) (Math.random() * conditions.length)];
        dailyWeather.setWeatherCondition(randomCondition);

        double randomTemperature = randomCondition.getTempMin() + Math.random() * (randomCondition.getTempMax() - randomCondition.getTempMin());
        dailyWeather.setTemperatureCelsius((int) (10 * (randomTemperature) / 10d));

        return dailyWeather;

    }
}
