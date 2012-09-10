package org.exoplatform.setup.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 * @author Hela
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatasourcesDto {
    private List<String> data;

    DatasourcesDto() {}

    public DatasourcesDto(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

}
