package com.example.demo;

import com.linkDatabase.Counties;
import com.repositories.CountiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CountiesController {
    @Autowired
    private CountiesRepository countiesRepository;

    @GetMapping("/counties")
    public String listCounties(Model model) {
        // Obține lista de orașe din baza de date
        List<Counties> counties = countiesRepository.findAll();
        // Adaugă lista de orașe în model, sub numele de "counties"
        model.addAttribute("counties", counties);
        // Returnează numele template-ului (register în acest exemplu, fișierul se numește counties.html în folderul templates)
        return "counties";
    }
}
