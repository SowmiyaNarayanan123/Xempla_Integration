package Asset_details.Asset_details.dto;
import java.util.List;
public class BuildingAssetsRequest {
	    private String building;
	    private List<Asset> assets;

	    public String getBuilding() {
	        return building;
	    }
	    public void setBuilding(String building) {
	        this.building = building;
	    }

	    public List<Asset> getAssets() {
	        return assets;
	    }
	    public void setAssets(List<Asset> assets) {
	        this.assets = assets;
	    }
	}


