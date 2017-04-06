package ${basePackage}.api.converter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * JAXB adapter for {@link LocalDateTime}
 */
public class LocalDateTimeXMLAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public String marshal(LocalDateTime value) throws Exception {
        return value == null ? null : value.toString();
    }

    @Override
    public LocalDateTime unmarshal(String value) throws Exception {
        return value == null ? null : LocalDateTime.parse(value);
    }
}
