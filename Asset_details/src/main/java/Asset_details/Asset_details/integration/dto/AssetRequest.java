package Asset_details.Asset_details.integration.dto;

import java.util.List;

public class AssetRequest {
	private Filter filter;
    private List<String> fields;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
