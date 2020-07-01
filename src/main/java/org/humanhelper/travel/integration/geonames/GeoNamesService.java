package org.humanhelper.travel.integration.geonames;

/**
 * Например, по запросу через
 * https://addlink.rome2rio.com/api/addlink/GeocodeLocationGeonames
 * {"lat":-8.786193056546608,"lng":121.7999486053943,"version":"201510070301"}
 * мы получаем
 * { "geonames": { "entry": [ {"countryCode": "ID", "distance": "2.8993", "elevation": "1693", "feature": "mountain", "lang": "en", "lat": "-8.7606", "lng": "121.805", "rank": "87", "summary": "Kelimutu is a volcano, close to the small town of Moni in central Flores island in Indonesia. The volcano is around 50km to the east of Ende, Indonesia, the capital of Ende regency in East Nusa Tenggara province.  (...)", "thumbnailImg": "http:\/\/www.geonames.org\/img\/wikipedia\/114000\/thumb-113278-100.jpg", "title": "Kelimutu", "wikipediaUrl": "http:\/\/en.wikipedia.org\/wiki\/Kelimutu" }, {"countryCode": "ID", "distance": "3.7074", "elevation": "1063", "feature": null, "lang": "en", "lat": "-8.7917", "lng": "121.7667", "rank": "32", "summary": "Sukaria is a 8 km wide caldera to the northeast of Mount Iya on Flores, Indonesia. The southern caldera wall is irregular and a small fumarolic area is found in the western flanks which contains several vents and eject geyser-like water column (...)", "thumbnailImg": null, "title": "Mount Sukaria", "wikipediaUrl": "http:\/\/en.wikipedia.org\/wiki\/Mount_Sukaria" }, {"countryCode": "ID", "distance": "5.8141", "elevation": "873", "feature": "landmark", "lang": "en", "lat": "-8.7583", "lng": "121.8447", "rank": "70", "summary": "Kelimutu National Park is located on the island of Flores, Indonesia. It consists of a region with hills and mountains, with Mount Kelibara (1,731 m) as its highest peak. Mount Kelimutu, which has the three coloured lakes, is also located in this national park (...)", "thumbnailImg": null, "title": "Kelimutu National Park", "wikipediaUrl": "http:\/\/en.wikipedia.org\/wiki\/Kelimutu_National_Park" }, {"countryCode": "ID", "distance": "7.9433", "elevation": "1091", "feature": null, "lang": "en", "lat": "-8.7167", "lng": "121.7833", "rank": "17", "summary": "Ndete Napu is a fumarole field in the center of Flores island, Indonesia. It is located along the Lowomelo river valley and contains mudpots and high-pressure water fountains. The field is listed as an active volcano based on its thermal activity.  (...)", "thumbnailImg": null, "title": "Ndete Napu", "wikipediaUrl": "http:\/\/en.wikipedia.org\/wiki\/Ndete_Napu" } ] }}
 *
 * @author Андрей
 * @since 09.10.15
 */
public class GeoNamesService {
}
