package com.example.demo.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DemoDto;
import com.example.demo.service.DemoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/demo")
@Validated
@Slf4j
public class DemoController {
    private DemoService demoService;


    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping(value = "/getTwoLocationDetails", params = { "postcode1", "postcode2"})
    public ResponseEntity<Object> getTwoLocationDetails(
            @RequestParam("postcode1") @NotNull @Size(min = 5, max = 7) String postcode1, @RequestParam("postcode2") @NotNull @Size(min = 5, max = 7) String postcode2) {

        DemoDto demo1 = demoService.processLocation(postcode1);
        DemoDto demo2 = demoService.processLocation(postcode2);
        

        List<DemoDto> records = new ArrayList<DemoDto>();
        records.add(demo1);
        records.add(demo2);
            log.info("Postcode 1: " , demo1 );
            log.info("Postcode 2: ",demo2);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping(value = "/getDistanceBetweenTwoLocations", params = { "postcode1", "postcode2"})
    public ResponseEntity<Object> getDistanceBetweenTwoLocations(
            @RequestParam("postcode1") @NotNull @Size(min = 5, max = 7) String postcode1, @RequestParam("postcode2") @NotNull @Size(min = 5, max = 7) String postcode2) {

        double records = demoService.calculateDistanceBetweenTwoLocations(postcode1, postcode2);
        
                log.info("Postcode 1: " , postcode1 );
                log.info("Postcode 2: ",postcode2);
                log.info("Distance in km: ", records);
        return new ResponseEntity<>(String.valueOf(records) + " km", HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updatePostcode(@PathVariable int id,
            @Valid @NonNull @RequestBody DemoDto demoDto) {
        demoDto.setId(id);
        demoService.updatePostcode(demoDto);

        log.info("Postcode update: " , demoDto );
        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }
}


