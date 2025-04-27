package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.service.PaypalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service("paypalService")
public class PaypalServiceImpl implements PaypalService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.api.url}")
    private String paypalApiUrl;

    public String createOrder(Double amount, String currency, String description){
        String accessToken = this.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> orderRequest = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "amount", Map.of(
                                "currency_code", currency,
                                "value", amount.toString()
                        ),
                        "description", description
                )),
                "application_context", Map.of(
                        "return_url", "http://localhost:4200/paypal-success",
                        "cancel_url", "http://localhost:4200/paypal-cancel"
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                this.paypalApiUrl + "/v2/checkout/orders",
                entity,
                Map.class
        );

        List<Map<String, String>> links = (List<Map<String, String>>) response.getBody().get("links");
        return links.stream()
                .filter(link -> "approve".equals(link.get("rel")))
                .findFirst()
                .map(link -> link.get("href"))
                .orElseThrow(() -> new RuntimeException("Approval link not found"));
    }

    public Map<String, Object> captureOrder(String orderId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                this.paypalApiUrl + "/v2/checkout/orders/" + orderId + "/capture",
                entity,
                Map.class
        );

        return response.getBody();
    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                this.paypalApiUrl + "/v1/oauth2/token",
                entity,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }
}
