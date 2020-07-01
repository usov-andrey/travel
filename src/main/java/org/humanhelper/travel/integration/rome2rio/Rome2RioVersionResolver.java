package org.humanhelper.travel.integration.rome2rio;

import org.humanhelper.service.http.Http;
import org.springframework.stereotype.Service;

/**
 * <script type="text/javascript" src="//static.r2r.io/Static/StaticContent.2015011504.js" defer=""></script>
 *
 * @author Андрей
 * @since 17.01.15
 */
@Service
public class Rome2RioVersionResolver {

    private static final String URL = "http://www.rome2rio.com/";

    private String version;

    public String getVersion() {
        if (version == null) {
            version = Http.get(URL).responseBetween("StaticContent.Desktop.", ".css");
        }
        return version;
    }
}
