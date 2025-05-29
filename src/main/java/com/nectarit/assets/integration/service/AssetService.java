package com.nectarit.assets.integration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nectarit.assets.integration.dto.LoginRequest;
import com.nectarit.assets.integration.dto.LoginResponse;
import com.nectarit.assets.integration.payloaddto.*;

@Service
public class AssetService {
	private static final Logger logger = LoggerFactory.getLogger(AssetService.class);
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${assets.login.url}")
	private String loginUrl;

	@Value("${assets.asset.url}")
	private String assetUrl;

	@Value("${assets.login.username}")
	private String username;

	@Value("${assets.login.password}")
	private String password;

	@Value("${assets.building.name:Park Heights}")
	private String buildingName;

	@Value("${assets.path.uuid}")
	private String pathUuid;
	@Value("${assets.other.api.url}")
	private String otherApiUrl;

	@Autowired
	public AssetService(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	public BuildingAssetRequest fetchAssets() {
		try {

			LoginRequest loginRequest = new LoginRequest(username, password);
			HttpHeaders loginHeaders = new HttpHeaders();
			loginHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<LoginRequest> loginEntity = new HttpEntity<>(loginRequest, loginHeaders);

			ResponseEntity<LoginResponse> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginEntity,
					LoginResponse.class);

			String accessToken = Optional.ofNullable(loginResponse.getBody()).map(LoginResponse::getAccessToken)
					.orElseThrow(() -> new RuntimeException("Failed to obtain access token."));

			HttpHeaders assetHeaders = new HttpHeaders();
			assetHeaders.setContentType(MediaType.APPLICATION_JSON);
			assetHeaders.setBearerAuth(accessToken);

			AssetListRequestDTO assetRequest = new AssetListRequestDTO();
			assetRequest.setDomains("buildingdemo");
			assetRequest.setOffset(1);
			assetRequest.setPageSize(12);
			assetRequest.setPath(List.of(pathUuid));
			assetRequest.setFields(List.of("displayName", "points", "dataTime"));

			HttpEntity<AssetListRequestDTO> assetEntity = new HttpEntity<>(assetRequest, assetHeaders);

			ResponseEntity<String> response = restTemplate.exchange(assetUrl, HttpMethod.POST, assetEntity,
					String.class);

			System.out.println(response);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("Asset API call failed with status: " + response.getStatusCode());
			}

			String responseBody = response.getBody();
			if (responseBody == null || responseBody.trim().isEmpty()) {
				throw new RuntimeException("Asset API returned empty response body");
			}

			logger.debug("Asset API raw response: {}", responseBody);

			JsonNode root = objectMapper.readTree(responseBody);
			JsonNode assetsNode = root.path("assets");

			if (!assetsNode.isArray()) {
				throw new RuntimeException("Expected 'assets' to be an array.");
			}

			List<Asset> assetsList = new ArrayList<>();

			for (JsonNode assetNode : assetsNode) {
				Asset asset = new Asset();

				String equipmentName = assetNode.hasNonNull("displayName") ? assetNode.path("displayName").asText()
						: assetNode.path("name").asText(null);
				asset.setEquipment(equipmentName);

				String dataTime = assetNode.path("dataTime").asText("");
				if (!dataTime.isEmpty()) {
					try {
						long epochMillis = Long.parseLong(dataTime);
						dataTime = java.time.Instant.ofEpochMilli(epochMillis).toString();
					} catch (NumberFormatException e) {
						logger.warn("Invalid dataTime format: {}", dataTime);
					}
				}

				List<Point> pointsList = new ArrayList<>();
				JsonNode pointsNode = assetNode.path("points");
				if (pointsNode.isArray()) {
					for (JsonNode pointNode : pointsNode) {
						Point point = new Point();

						point.setPoint(pointNode.path("pointName").asText(null));
						point.setTime(dataTime);

						JsonNode dataNode = pointNode.path("data");
						if (dataNode.isNumber()) {
							point.setValue(dataNode.numberValue());
						} else {
							point.setValue(dataNode.asText(null));
						}

						pointsList.add(point);
					}
				}

				asset.setPoints(pointsList);
				assetsList.add(asset);
			}

			BuildingAssetRequest buildingAssetsRequest = new BuildingAssetRequest();
			buildingAssetsRequest.setBuilding(buildingName);
			buildingAssetsRequest.setAssets(assetsList);
			System.out.println(assetsList);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<BuildingAssetRequest> requestEntity = new HttpEntity<>(buildingAssetsRequest, headers);

			ResponseEntity<BuildingAssetRequest> otherApiResponse = restTemplate.exchange(otherApiUrl, HttpMethod.POST,
					requestEntity, BuildingAssetRequest.class);

			if (!otherApiResponse.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException(
						"Other API call failed with HTTP status: " + otherApiResponse.getStatusCode());
			}

			BuildingAssetRequest responseData = otherApiResponse.getBody();
			if (responseData == null) {
				logger.info("Other API returned successful response but null body, returning original data");

				return buildingAssetsRequest;
			}

			return responseData;

		} catch (Exception e) {
			logger.error("Failed to process asset data", e);
			throw new RuntimeException("Failed during asset processing: " + e.getMessage(), e);
		}
	}
}