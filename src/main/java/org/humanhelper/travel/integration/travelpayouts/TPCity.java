package org.humanhelper.travel.integration.travelpayouts;

/**
 * {
 * "code": "SCE",
 * "name": "State College",
 * "coordinates": {
 * "lon": -77.84823,
 * "lat": 40.85372
 * },
 * "time_zone": "America/New_York",
 * "name_translations": {
 * "de": "State College",
 * "en": "State College",
 * "zh-CN": "大学城",
 * "tr": "State College",
 * "ru": "Стейт Колледж",
 * "it": "State College",
 * "es": "State College",
 * "fr": "State College",
 * "th": "สเตทคอลเลจ"
 * },
 * "country_code": "US"
 * }
 *
 * @author Андрей
 * @since 08.01.15
 */
public class TPCity extends TPPlace {

    private String country_code;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
