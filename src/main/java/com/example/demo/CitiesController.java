package com.example.demo;

import com.linkDatabase.Cities;
import com.repositories.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CitiesController {

    @Autowired
    private CitiesRepository citiesRepository;

    @GetMapping("/cities")
    public String listCities(Model model) {
        // Obține lista de orașe din baza de date
        List<Cities> cities = citiesRepository.findAll();
        // Adaugă lista de orașe în model, sub numele de "cities"
        model.addAttribute("cities", cities);
        // Returnează numele template-ului (register în acest exemplu, fișierul se numește cities.html în folderul templates)
        return "cities";
    }
}
