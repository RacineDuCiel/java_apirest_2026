package com.example.demo.service;

import com.example.demo.dto.GeocodeResponse;
import com.example.demo.model.Address;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AddressValidationService {

    private final RestClient restClient;

    public AddressValidationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://data.geopf.fr")
                .build();
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
                            .path("/geocodage/search")
                            .queryParam("q", query)
                            .queryParam("limit", 1)
                            .queryParam("autocomplete", 0)
                            .build())
                    .retrieve()
                    .body(GeocodeResponse.class);

            return response != null
                    && response.getFeatures() != null
                    && !response.getFeatures().isEmpty()
                    && response.getFeatures().get(0).getProperties().getScore() > 0.5;

        } catch (Exception e) {
            System.err.println("Erreur lors de la validation de l'adresse: " + e.getMessage());
            return false;
        }
    }
}
