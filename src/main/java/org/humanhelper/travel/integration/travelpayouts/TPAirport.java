package org.humanhelper.travel.integration.travelpayouts;

/**
 * {"code":"MQP","name":"Kruger Mpumalanga International Airport","coordinates":{"lon":31.098131,"lat":-25.384947},"time_zone":"Africa/Johannesburg","name_translations":{"de":"Nelspruit","en":"Kruger Mpumalanga International Airport","tr":"International Airport","it":"Kruger Mpumalanga","fr":"Kruger Mpumalanga","es":"Kruger Mpumalanga","th":"สนามบินเนลสปรุต"},"country_code":"ZA","city_code":"NLP"}
 *
 * @author Андрей
 * @since 08.01.15
 */
public class TPAirport extends TPPlace {

    private String country_code;
    private String city_code;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    @Override
    public String toString() {
        return "TPAirport{" +
                "country_code='" + country_code + '\'' +
                ", city_code='" + city_code + '\'' +
                super.toString() +
                '}';
    }
}
