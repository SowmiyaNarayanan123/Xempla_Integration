package Asset_details.Asset_details.integration.dto;

import java.util.List;
import java.util.Map;

public class AssetListRequestDTO {
	private Map<String, Object> filter;
	private List<String>fields;
	
	
	public Map<String, Object> getFilter() {
		return filter;
	}
	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
}
