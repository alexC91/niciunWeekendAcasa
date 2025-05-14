package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class RegistrationController {
    // Token storage - in production, use a database
    private static final Map<String, String> tokenToEmailMap = new ConcurrentHashMap<>();

    // Server configuration - explicitly set to your application's port
    private static final String SERVER_PORT = "9090"; // Your application's port

    public String token;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register1")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String confirmPassword,
            Model model,
            HttpServletRequest request
    ) {
        try {
            // Manual validation
            if (firstName == null || firstName.isEmpty()) {
                model.addAttribute("error", "Prenumele este obligatoriu");
                return "signup";
            }

            if (lastName == null || lastName.isEmpty()) {
                model.addAttribute("error", "Numele este obligatoriu");
                return "signup";
            }

            if (email == null || email.isEmpty()) {
                model.addAttribute("error", "Email-ul este obligatoriu");
                return "signup";
            } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                model.addAttribute("error", "Formatul email-ului este invalid");
                return "signup";
            }

            if (password == null || password.isEmpty()) {
                model.addAttribute("error", "Parola este obligatorie");
                return "signup";
            } else if (password.length() < 6) {
                model.addAttribute("error", "Parola trebuie să aibă minim 6 caractere");
                return "signup";
            }

            // Only validate confirmPassword if it's provided (for backward compatibility)
            if (confirmPassword != null && !password.equals(confirmPassword)) {
                model.addAttribute("error", "Parolele nu corespund");
                return "signup";
            }

            // Check if email exists - safely handle Optional
            Users existingUser = null;
            try {
                existingUser = userRepository.findByEmail(email).orElse(null);
            } catch (Exception e) {
                System.out.println("Error checking existing user: " + e.getMessage());
            }

            if (existingUser != null) {
                model.addAttribute("error", "Emailul este deja folosit");
                return "signup";
            }

            // Create new user
            Users newUser = new Users();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setCreatedAt(new Date());
            newUser.setIsActivated(true); // Set to true by default for now
            newUser.setIsDisabled(false);
            newUser.setPassword(passwordEncoder.encode(password));

            userRepository.save(newUser);

            // Generate verification token
            String verificationToken = UUID.randomUUID().toString();
            tokenToEmailMap.put(verificationToken, newUser.getEmail());

            // Debug: Print all tokens in the map
            System.out.println("Current tokens in map: " + tokenToEmailMap);

            // Get server information from the request
            String serverName = request.getServerName();
            String scheme = request.getScheme();

            // Build the verification link using the correct port (9090)
            String verificationLink = scheme + "://" + serverName + ":" + SERVER_PORT + "/verify?token=" + verificationToken;

            // Send email using the Email class - wrapped in try-catch to prevent failures
            try {
                String subject = "Activează-ți contul niciunWeekendAcasa";
                String body = "Salut " + newUser.getFirstName() + ",\n\n" +
                        "Pentru a-ți activa contul, accesează link-ul de mai jos:\n\n" +
                        verificationLink + "\n\nMulțumim!";

                Email.sendEmail(newUser.getEmail(), subject, body);
                System.out.println("Verification email sent to: " + newUser.getEmail());
                System.out.println("Verification link: " + verificationLink); // Log the link for debugging
            } catch (Exception e) {
                System.out.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
                // Continue even if email fails - we'll set the account as activated
            }

            model.addAttribute("message", "Contul a fost creat cu succes. Te poți loga acum.");
            model.addAttribute("success", true);
            return "signup";

        } catch (Exception ex) {
            // Log the full exception for debugging
            ex.printStackTrace();

            // Add a generic error message
            model.addAttribute("error", "A apărut o eroare la înregistrare: " + ex.getMessage());
            return "signup";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        try {
            System.out.println("Verifying token: " + token); // Log the token for debugging
            System.out.println("Available tokens: " + tokenToEmailMap); // Debug: print all tokens

            String email = tokenToEmailMap.get(token); // Get without removing first
            if (email == null) {
                model.addAttribute("message", "Link invalid sau expirat.");
                return "verify_result";
            }

            Users user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                model.addAttribute("message", "Utilizatorul nu a fost găsit.");
                return "verify_result";
            }

            // Activate the account
            user.setIsActivated(true);
            userRepository.save(user);

            // Now remove the token after successful activation
            tokenToEmailMap.remove(token);

            System.out.println("Account activated for: " + email);
            model.addAttribute("message", "Cont activat! Poți să te loghezi.");
            return "verify_result";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Eroare la activarea contului: " + e.getMessage());
            return "verify_result";
        }
    }

    @GetMapping("/login1")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpServletResponse response,
                            HttpSession session) {
        try {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if password matches
            if (passwordEncoder.matches(password, user.getPassword())) {
                // User is authenticated - generate token regardless of activation status
                token = jwtUtil.generateToken(email);

                // Add token to response header
                response.addHeader("Authorization", "Bearer " + token);

                // Add token as cookie
                Cookie jwtCookie = new Cookie("jwt_token", token);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(3600); // 1 hour
                jwtCookie.setHttpOnly(true);
                response.addCookie(jwtCookie);

                // Set authentication in SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Check if account is activated and set a session attribute if not
                if (!user.getIsActivated()) {
                    session.setAttribute("accountNotActivated", true);
                    session.setAttribute("userEmail", email);
                }

                return "redirect:/";
            } else {
                // Authentication failed - wrong password
                return "redirect:/login?error=invalid_credentials";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=" + e.getMessage();
        }
    }

    // Add a method to resend verification email
    @GetMapping("/resend-verification")
    public String resendVerification(@RequestParam("email") String email, Model model, HttpServletRequest request) {
        try {
            Users user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                model.addAttribute("message", "Email-ul nu a fost găsit în sistem.");
                return "verify_result";
            }

            if (user.getIsActivated()) {
                model.addAttribute("message", "Contul este deja activat. Te poți loga.");
                return "verify_result";
            }

            // Generate new verification token
            String verificationToken = UUID.randomUUID().toString();
            tokenToEmailMap.put(verificationToken, user.getEmail());

            // Get server information from the request
            String serverName = request.getServerName();
            String scheme = request.getScheme();

            // Build the verification link using the correct port (9090)
            String verificationLink = scheme + "://" + serverName + ":" + SERVER_PORT + "/verify?token=" + verificationToken;

            try {
                // Send email
                String subject = "Activează-ți contul niciunWeekendAcasa";
                String body = "Salut " + user.getFirstName() + ",\n\n" +
                        "Pentru a-ți activa contul, accesează link-ul de mai jos:\n\n" +
                        verificationLink + "\n\nMulțumim!";

                Email.sendEmail(user.getEmail(), subject, body);
                System.out.println("Resent verification link: " + verificationLink); // Log the link for debugging
            } catch (Exception e) {
                System.out.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
                // Continue even if email fails
            }

            model.addAttribute("message", "Un nou email de verificare a fost trimis. Verifică căsuța de email.");
            return "verify_result";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Eroare la trimiterea email-ului: " + e.getMessage());
            return "verify_result";
        }
    }

    // Manual activation endpoint for testing
    @GetMapping("/manual-activate")
    public String manualActivate(@RequestParam("email") String email, Model model) {
        try {
            Users user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                model.addAttribute("message", "Email-ul nu a fost găsit în sistem.");
                return "verify_result";
            }

            if (user.getIsActivated()) {
                model.addAttribute("message", "Contul este deja activat.");
                return "verify_result";
            }

            // Activate the account
            user.setIsActivated(true);
            userRepository.save(user);

            model.addAttribute("message", "Cont activat manual! Poți să te loghezi acum.");
            return "verify_result";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Eroare la activarea contului: " + e.getMessage());
            return "verify_result";
        }
    }
}
