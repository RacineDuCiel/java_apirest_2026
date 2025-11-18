package com.javaapirestgosse.service;

import com.javaapirestgosse.dto.GeocodeResponse;
import com.javaapirestgosse.model.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AddressValidationService {

    private final RestClient restClient;
    
    @Value("${geocoding.api.url:https://data.geopf.fr}")
    private String apiBaseUrl;
    
    @Value("${geocoding.api.path:/geocodage/search}")
    private String apiPath;

    public AddressValidationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public boolean validateAddress(Address address) {
        try {
            String query = String.format("%s %s %s",
                    address.getStreet(),
                    address.getPostalCode(),
                    address.getCity()
            );

            GeocodeResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host(apiBaseUrl.replace("https://", "").replace("http://", ""))
                            .path(apiPath)
                            .queryParam("q", query)
                            .queryParam("limit", 5)
                            .queryParam("autocomplete", 0)
                            .build())
                    .retrieve()
                    .body(GeocodeResponse.class);

            if (response == null || response.getFeatures() == null) {
                return false;
            }

            // Utilisation de Stream pour trouver le meilleur score de correspondance
            return response.getFeatures().stream()
                    .map(feature -> feature.getProperties().getScore())
                    .filter(score -> score != null)
                    .max(Double::compare)
                    .map(maxScore -> maxScore > 0.5)
                    .orElse(false);

        } catch (Exception e) {
            return false;
        }
    }
}
