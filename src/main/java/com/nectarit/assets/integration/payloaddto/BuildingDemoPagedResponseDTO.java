package com.nectarit.assets.integration.payloaddto;

import java.time.LocalDateTime;

public class BuildingDemoPagedResponseDTO {
	 private String typeName;
	    private Double points;
	    private LocalDateTime dataTime;
	    
	    
	    public BuildingDemoPagedResponseDTO() {}
	    
	    
	    public BuildingDemoPagedResponseDTO(String typeName, Double points, LocalDateTime dataTime) {
	        this.typeName = typeName;
	        this.points = points;
	        this.dataTime = dataTime;
	    }
	    
	    
	    public String getTypeName() {
	        return typeName;
	    }
	    
	    public void setTypeName(String typeName) {
	        this.typeName = typeName;
	    }
	    
	    public Double getPoints() {
	        return points;
	    }
	    
	    public void setPoints(Double points) {
	        this.points = points;
	    }
	    
	    public LocalDateTime getDataTime() {
	        return dataTime;
	    }
	    
	    public void setDataTime(LocalDateTime dataTime) {
	        this.dataTime = dataTime;
	    }
}
