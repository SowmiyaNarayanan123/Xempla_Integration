package Asset_details.Asset_details.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Asset_details.Asset_details.integration.dto.BuildingAssetRequest;

@RestController
public class AssetController {
        @Autowired
		private ObjectMapper objectMapper;

		@PostMapping("/asset-details")
		public void getAssetDetails(@RequestBody BuildingAssetRequest buildingAssetRequest) throws JsonProcessingException {
			ObjectMapper ob = new ObjectMapper();
			ob.writeValueAsString(buildingAssetRequest);
			System.out.print("Success");
		}
	}


