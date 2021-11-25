package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class SuperTrumpf {

    String jsonString;
    ObjectMapper objectMapper = new ObjectMapper();
    ClassPathResource cpr = new ClassPathResource("../../data.json");

    @CrossOrigin
    @RequestMapping(
            value = "/api/animals",
            produces = "application/json",
            method = {RequestMethod.GET}
    )
    List<Animal> getAllAnimals() {
        List<Animal> animalsList = null;
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            jsonString = new String(bdata, StandardCharsets.UTF_8);
            animalsList = objectMapper.readValue(jsonString, new TypeReference<List<Animal>>() {
            });
        } catch (Exception e) {
            System.out.println("Exception" + e.getStackTrace());
        }
        return animalsList;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/api/animals/{id}",
            produces = "application/json",
            method = {RequestMethod.GET})
    public Animal getAnimalById(@PathVariable int id) {
        List<Animal> animalListFiltered = new ArrayList<>();
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            jsonString = new String(bdata, StandardCharsets.UTF_8);
            List<Animal> animalsList;
            animalsList = objectMapper.readValue(jsonString, new TypeReference<List<Animal>>() {
            });
            animalListFiltered = animalsList
                    .stream()
                    .filter(c -> c.getId() == id)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("Exception" + e.getStackTrace());
        }
        if (animalListFiltered.size() > 0) {
            return animalListFiltered.get(0);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @RequestMapping(
            value = "/api/animals",
            produces = "application/json",
            method = {RequestMethod.PUT})
    public Animal updateAnimal(@RequestBody Map<String, Object> animal) throws Exception {
        Animal animalEntity = null;
        try {
            int id = (int) animal.get("id");
            String name = (String) animal.get("name");
            String image = (String) animal.get("image");
            double size = (double) animal.get("size");
            int weight = (int) animal.get("weight");
            int age = (int) animal.get("age");
            int offspring = (int) animal.get("weight");
            int speed = (int) animal.get("speed");
            animalEntity = new Animal(id, name, image, size, weight, age, offspring, speed);
        } catch (Exception e) {
            System.out.println("Exception" + e.getStackTrace());
        }
        return animalEntity;
    }
}