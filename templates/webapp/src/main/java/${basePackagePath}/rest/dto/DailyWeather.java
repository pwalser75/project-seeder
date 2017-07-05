package ${basePackage}.rest.dto;

import ${basePackage}.rest.dto.converter.LocalDateConverter;
import ${basePackage}.rest.dto.type.WeatherCondition;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Daily weather data
 */
@XmlRootElement(name = "daily-weather")
public class DailyWeather {

    private LocalDate date;
    private double temperatureCelsius;
    private WeatherCondition weatherCondition;

    @XmlJavaTypeAdapter(LocalDateConverter.class)
    @XmlElement(name = "date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @XmlElement(name = "temperature")
    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    @XmlElement(name = "condition")
    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
}
