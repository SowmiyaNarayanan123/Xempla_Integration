package com.nectarit.assets.integration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nectarit.assets.integration.service.AssetService;
@Component
	public class AssetRunner implements CommandLineRunner {

	    private final AssetService assetService;

	    @Autowired
	    public AssetRunner(AssetService assetService) {
	        this.assetService = assetService;
	    }

	    @Override
	    public void run(String... args) throws Exception {
	        assetService.fetchAssets();
	        System.out.println("Assets fetched successfully.");
	    }
	}


