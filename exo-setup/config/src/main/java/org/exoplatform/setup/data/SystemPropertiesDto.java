package org.exoplatform.setup.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 *
 * @author Hela
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemPropertiesDto {
    private Map<String,String> data;

    SystemPropertiesDto() {}

    public SystemPropertiesDto(Map<String,String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

}
