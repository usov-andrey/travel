package org.humanhelper.travel.place.createdb;

import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.type.region.Island;

/**
 * @author Андрей
 * @since 14.10.15
 */
public class JavaPlaceRouteLoader {

    public void load() {

        Country id = new Country().id(CountryBuilder.ID.getCode());
        Island.build("Pulau Derawan").location(2.284665, 118.243298);


		 /*

		priceMap.addRound(
				build().sourceBus("Dumaguete").targetBus("Bayawan").price(110),
				build().sourceSea("Maya").targetSea("Malapascua Island").price(80),
				build().sourceBus("Cebu City", "North Bus Terminal").targetBus("Maya").price(170).transport("Ceres Bus"),
				build().sourceAir("USU").targetRegion("Coron").price(150),
				build().sourceBus("Cebu City", "South Bus Terminal").targetRegion("Moalboal").price(116).transport("Ceres Liner")
		);


		"id": "ID",
    "regions": [
      {
        "name": "Pulau Derawan",
        "kind": "island",
        "location": {
          "type": "manual",
          "value": "(2.284665, 118.243298)"
        }
      }

		{
        "name": "Dumaguete",
        "kind": "region",
        "childs": [
          {
            "name": "Ceres Bus Terminal",
            "kind": "bus",
            "location": {
              "name": "dumaguete ceres bus terminal",
              "type": "osm"
            }
          }
        ]
      }         ]    */

    }
}
