package com.example.demo.service;

import com.example.demo.dto.DemoDto;
import com.example.demo.model.Demo;
import com.example.demo.repository.DemoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    
    private DemoRepository demoRepository;
    private ModelMapper modelMapper;
    private final static double EARTH_RADIUS = 6371; // radius in kilometers

    public DemoService(DemoRepository demoRepository){
        this.demoRepository = demoRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setFieldMatchingEnabled(true).setAmbiguityIgnored(true);
        
    }

    public DemoDto processLocation(String postcode){
        Demo demo = demoRepository.findByPostcode(postcode);
        
        DemoDto demoDto = modelMapper.map(demo, DemoDto.class);

        demoDto.setLatitudeDegree(convertLatitudeToDegrees(demo.getLatitude()));
        demoDto.setLongitudeDegree(convertLongitudeToDegrees(demo.getLongitude()));
        return demoDto;
    }

    public String convertLatitudeToDegrees(double decimalDegrees) {
        String result;

        // Handle negative values for southern hemisphere
        String direction = (decimalDegrees < 0) ? "S" : "N";

        decimalDegrees = Math.abs(decimalDegrees);

        double degrees = Math.floor(decimalDegrees);
        double minutes = Math.floor((decimalDegrees - degrees) * 60.0);
        double seconds = (decimalDegrees - degrees - (minutes / 60.0)) * 3600.0;

        result = String.valueOf(degrees) + "° " + String.valueOf(minutes) + "' " + String.valueOf(seconds) + '"' + ' ' + String.valueOf(direction);

        return result;
    }

    public String convertLongitudeToDegrees(double decimalDegrees) {
        String result;

        // Handle negative values for western hemisphere
        String direction = (decimalDegrees < 0) ? "W" : "E";

        decimalDegrees = Math.abs(decimalDegrees);

        double degrees = Math.floor(decimalDegrees);
        double minutes = Math.floor((decimalDegrees - degrees) * 60.0);
        double seconds = (decimalDegrees - degrees - (minutes / 60.0)) * 3600.0;

        result = String.valueOf(degrees) + "° " + String.valueOf(minutes) + "' " + String.valueOf(seconds) + '"' + ' ' + String.valueOf(direction);


        return result;
    }

    public double calculateDistanceBetweenTwoLocations(String postcode1, String postcode2) {
        Demo demo1 = demoRepository.findByPostcode(postcode1);
        Demo demo2 = demoRepository.findByPostcode(postcode2);

        double result = calculateDistance(demo1.getLatitude(), demo1.getLongitude(), demo2.getLatitude(), demo2.getLongitude());
        
        return result;
    }

    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    double calculateDistance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));
    
        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);
    
        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
        return EARTH_RADIUS * c;
    }

    public void updatePostcode(DemoDto demoDto) {
        Demo demo = demoRepository.findById(demoDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Id: " + demoDto.getId()));
        modelMapper.map(demoDto, demo); // To preserve the destination unrelated field
        demoRepository.save(demo);
    }
}
