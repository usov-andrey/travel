package org.humanhelper.travel.transport.air;

import org.humanhelper.travel.transport.TransportType;

/**
 * @author Андрей
 * @since 19.12.14
 */
public class Airplane extends TransportType {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
