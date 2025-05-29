//package com.nectarit.assets.integration.controller;
//
//import com.nectarit.assets.integration.payloaddto.*;
//import com.nectarit.assets.integration.service.AssetService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/assets")
//public class AssetController {
//
//    private final AssetService assetService;
//
//    @Autowired
//    public AssetController(AssetService assetService) {
//        this.assetService = assetService;
//    }
//
//    @GetMapping("/fetch")
//    public BuildingAssetRequest fetchAssets() {
//        try {
//            
//            return assetService.fetchAssets();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to fetch assets: " + e.getMessage(), e);
//        }
//    }
//}
