package greencity.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeocodingService {
    private final String API_KEY;

    public GeocodingService() {
        API_KEY = System.getenv("GEOCODING_API_KEY");
    }

    /**
     * Method for getting address by coordinates of the place
     *
     * @param latitude  - latitude of the place
     * @param longitude - longitude of the place
     * @return {@link HashMap} instance with formatted address, country, region, city, street in English and Ukrainian, house number.
     * @author Maksym Fartushok
     */
    public Map<String, String> getAddress(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        Map<String, String> addresses = new HashMap<>();

        processAddressComponents(latLng, "uk", addresses);
        processAddressComponents(latLng, "en", addresses);

        addresses.put("formattedAddressEn", getFormattedAddress(addresses, "en"));
        addresses.put("formattedAddressUa", getFormattedAddress(addresses, "uk"));
        return addresses;
    }

    private void processAddressComponents(LatLng latLng, String language, Map<String, String> addresses) {
        GeocodingResult[] results;

        try (GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build()) {
            results = GeocodingApi.reverseGeocode(context, latLng)
                    .language(language)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        for (int i = results.length - 1; i >= 0; i--) {
            for (AddressComponent addressComponent : results[i].addressComponents) {
                for (AddressComponentType type : addressComponent.types) {
                    processAddressComponentType(type, addressComponent, addresses, language);
                }
            }
        }
    }

    private void processAddressComponentType(AddressComponentType type, AddressComponent addressComponent, Map<String, String> addresses, String language) {
        switch (type) {
            case LOCALITY:
                putIfNotExists(language.equals("en") ? "cityEn" : "cityUa", addressComponent, addresses);
                break;
            case COUNTRY:
                putIfNotExists(language.equals("en") ? "countryEn" : "countryUa", addressComponent, addresses);
                break;
            case STREET_NUMBER:
                putIfNotExists("houseNumber", addressComponent, addresses);
            case ADMINISTRATIVE_AREA_LEVEL_1:
                putIfNotExists(language.equals("en") ? "regionEn" : "regionUa", addressComponent, addresses);
                break;
            case ROUTE:
                putIfNotExists(language.equals("en") ? "streetEn" : "streetUa", addressComponent, addresses);
                break;
        }
    }

    private void putIfNotExists(String key, AddressComponent addressComponent, Map<String, String> addresses) {
        if (!addresses.containsKey(key)) {
            addresses.put(key, addressComponent.longName);
        }
    }

    private String getFormattedAddress(Map<String, String> addresses, String language) {
        String formattedAddress;
        if (language.equals("uk")) {
            formattedAddress = addresses.get("houseNumber") + ", "
                    + addresses.get("streetUa") + ", "
                    + addresses.get("cityUa") + ", "
                    + addresses.get("regionUa") + ", "
                    + addresses.get("countryUa");
        } else {
            formattedAddress = addresses.get("houseNumber") + ", "
                    + addresses.get("streetEn") + ", "
                    + addresses.get("cityEn") + ", "
                    + addresses.get("regionEn") + ", "
                    + addresses.get("countryEn");
        }
        return formattedAddress;
    }
}





