package ${basePackage}.rest;

import ${basePackage}.rest.dto.DailyWeather;
import ${basePackage}.rest.dto.type.WeatherCondition;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.Collator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Example JAX-RS rest web service
 */
@RequestScoped
@Path("/example")
public class ExampleEndpoint {

    /**
     * List system properties and return them as JSON object
     */
    @GET
    @Path("/properties")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getSystemProperties() {

        Map<String, String> result = new LinkedHashMap<>();

        Properties systemProperties = System.getProperties();
        List<String> keys = new LinkedList<>(systemProperties.stringPropertyNames());
        Collections.sort(keys, Collator.getInstance());

        for (String key : keys) {
            String value = systemProperties.getProperty(key);
            result.put(key, value);
        }

        return result;
    }

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

            WeatherCondition[] conditions = WeatherCondition.values();
            WeatherCondition randomCondition = conditions[(int) (Math.random() * conditions.length)];
            double randomTemperature = randomCondition.getTempMin() + Math.random() * (randomCondition.getTempMax() - randomCondition.getTempMin());

            DailyWeather dailyWeather = new DailyWeather();
            dailyWeather.setDate(today.plus(i, ChronoUnit.DAYS));
            dailyWeather.setWeatherCondition(randomCondition);
            dailyWeather.setTemperatureCelsius((int) (10 * (randomTemperature) / 10d));
            forecast.add(dailyWeather);
        }

        return forecast;
    }
}
