package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("activePage", "about");
        return "about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("activePage", "services");
        return "services";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("activePage", "blog");
        return "blog";
    }

    @GetMapping("/contact_greeting")
    public String contact(Model model) {
        model.addAttribute("activePage", "contact");
        return "contact";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("activePage", "login");
        return "login";
    }

    @GetMapping("/map")
    public String map(Model model) {
        model.addAttribute("activePage", "map");
        return "map_test";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("activePage", "news");
        return "news";}

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("activePage", "privacy");
        return "privacy";}

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("activePage", "terms");
        return "terms";}

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("activePage", "profile");
        return "profile";}

    @GetMapping("/friends")
    public String friends(Model model) {
        model.addAttribute("activePage", "friends");
        return "friends";}

    @GetMapping("/profile_edit")
    public String profile_edit(Model model) {
        model.addAttribute("activePage", "profile_edit");
        return "profile_edit";}

    @GetMapping("/profile_photos")
    public String profile_photos(Model model) {
        model.addAttribute("activePage", "profile_photos");
        return "profile_photos";}

    @GetMapping("/profile_about")
    public String profile_about(Model model) {
        model.addAttribute("activePage", "profile_about");
        return "profile_about";}

    @GetMapping("/resetpassword")
    public String resetpassword(Model model) {
        model.addAttribute("activePage", "resetpassword");
        return "resetpassword";}

    @GetMapping("/mailresetpassword")
    public String mailresetpassword(Model model) {
        model.addAttribute("activePage", "mailresetpassword");
        return "mailresetpassword";}
}
