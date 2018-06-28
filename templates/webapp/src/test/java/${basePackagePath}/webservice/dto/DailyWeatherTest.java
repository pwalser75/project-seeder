package ${basePackage}.webservice.dto;

import org.junit.Assert;
import org.junit.Test;
import ${basePackage}.util.JsonUtil;
import ${basePackage}.webservice.dto.type.WeatherCondition;

import java.time.LocalDate;

/**
 * Tests for the DailyWeather DTO.
 *
 * @author ${user}
 * @since ${date}
 */
public class DailyWeatherTest {

    private final static double EPSILON = 1e-10;

    @Test
    public void testSerialized() {

        DailyWeather dailyWeather = new DailyWeather();
        dailyWeather.setDate(LocalDate.now());
        dailyWeather.setTemperatureCelsius(22.5);
        dailyWeather.setWeatherCondition(WeatherCondition.PARTIALLY_CLOUDY);

        String json = JsonUtil.stringify(dailyWeather);
        System.out.println(json);

        DailyWeather parser = JsonUtil.parse(DailyWeather.class, json);
        verifyParsed(dailyWeather, parser);
    }

    private void verifyParsed(DailyWeather original, DailyWeather parsed) {
        Assert.assertEquals(original.getDate(), parsed.getDate());
        Assert.assertEquals(original.getTemperatureCelsius(), parsed.getTemperatureCelsius(), EPSILON);
        Assert.assertEquals(original.getWeatherCondition(), parsed.getWeatherCondition());
    }
}
