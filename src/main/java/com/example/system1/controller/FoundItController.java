package com.example.system1.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.system1.Service.ImageStorageService;
import com.example.system1.Service.GeminiService;
import com.example.system1.Service.EmailService;
import com.example.system1.Service.QrCodeService;
import com.example.system1.model.ClaimRequest;
import com.example.system1.model.Item;
import com.example.system1.model.Message;
import com.example.system1.model.User; 
import com.example.system1.repository.ClaimRequestRepository;
import com.example.system1.repository.ItemRepository;
import com.example.system1.repository.MessageRepository;
import com.example.system1.repository.UserRepository;

@Controller
public class FoundItController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ClaimRequestRepository claimRepository;
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private UserRepository userRepository;

    // --- UPDATED STUDENT DASHBOARD ROUTE ---
    @GetMapping("/student")
    public String viewDashboard(HttpSession session, Model model, 
                                @RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(name = "category", required = false) String category) {
        
        // 1. Secure the route
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/"; 
        }

        // 2. Pass the user to the HTML template to prevent the Error 500 crash
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("user", loggedInUser);

        // 3. Search logic
        if ((keyword != null && !keyword.isEmpty()) || (category != null && !category.isEmpty() && !category.equals("All Categories"))) {
            model.addAttribute("items", itemRepository.searchItems(keyword, category));
        } else {
            model.addAttribute("items", itemRepository.findAll());
        }
        
        model.addAttribute("newItem", new Item());
        return "Index";
    }

    // 1. My Items (Claims & Tickets) Route
    @GetMapping("/my-items")
    public String viewMyItems(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/"; // Kick out if not logged in

        model.addAttribute("myClaims", claimRepository.findByStudentId(loggedInUser.getStudentId()));
        return "my-items";
    }

    // 2. My Profile Route
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/"; // Kick out if not logged in

        // Generate QR code if missing
        if (loggedInUser.getQrCodeFilename() == null || loggedInUser.getQrCodeFilename().isEmpty()) {
            String qrUrl = "http://localhost:8081/qr/" + loggedInUser.getStudentId();
            String qrFilename = qrCodeService.generateQrCode(qrUrl);
            if (qrFilename != null) {
                loggedInUser.setQrCodeFilename(qrFilename);
                userRepository.save(loggedInUser);
                session.setAttribute("loggedInUser", loggedInUser);
            }
        }

        model.addAttribute("user", loggedInUser);
        return "profile";
    }

    @GetMapping("/contact")
    public String viewContactPage(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("allMessages", messageRepository.findAll());
        model.addAttribute("myClaims", claimRepository.findAll());
        return "contact";
    }

    @PostMapping("/send-message")
    public String sendMessage(@ModelAttribute Message message) {
        messageRepository.save(message);
        return "redirect:/contact?success";
    }

    @GetMapping("/admin")
    public String viewAdminPanel(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/";
        }
        try {
            model.addAttribute("pendingClaims", claimRepository.findAll());
            model.addAttribute("adminMessages", messageRepository.findAll());
            
            java.util.List<Item> allItems = itemRepository.findAll();
            model.addAttribute("allItems", allItems); 
            
            // --- Analytics Calculation for Dashboard ---
            long availableCount = allItems.stream().filter(i -> "AVAILABLE".equals(i.getStatus())).count();
            long claimingCount = allItems.stream().filter(i -> "CLAIMING".equals(i.getStatus())).count();
            long releasedCount = allItems.stream().filter(i -> "RELEASED".equals(i.getStatus())).count();
            
            long electronicsCount = allItems.stream().filter(i -> "Electronics".equals(i.getCategory())).count();
            long walletsCount = allItems.stream().filter(i -> "Wallets & Cards".equals(i.getCategory())).count();
            long bagsCount = allItems.stream().filter(i -> "Bags & Accessories".equals(i.getCategory())).count();
            long documentsCount = allItems.stream().filter(i -> "Documents".equals(i.getCategory())).count();
            long othersCount = allItems.stream().filter(i -> i.getCategory() == null || "Others".equals(i.getCategory())).count();

            model.addAttribute("statusData", java.util.Arrays.asList(availableCount, claimingCount, releasedCount));
            model.addAttribute("categoryData", java.util.Arrays.asList(electronicsCount, walletsCount, bagsCount, documentsCount, othersCount));

        } catch (Exception e) {
            model.addAttribute("pendingClaims", new java.util.ArrayList<>());
            model.addAttribute("adminMessages", new java.util.ArrayList<>());
            model.addAttribute("allItems", new java.util.ArrayList<>());
            model.addAttribute("statusData", java.util.Arrays.asList(0, 0, 0));
            model.addAttribute("categoryData", java.util.Arrays.asList(0, 0, 0, 0, 0));
        }
        return "admin";
    }

    @PostMapping("/admin/verify/{claimId}")
    public String verifyClaim(@PathVariable Long claimId, @RequestParam String action, HttpSession session) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/";

        ClaimRequest claim = claimRepository.findById(claimId).orElse(null);
        if (claim == null) return "redirect:/admin?error=notfound";

        if ("approve".equals(action)) {
            claim.setStatus("APPROVED");
            claimRepository.save(claim);
            
            if (claim.getStudent() != null && claim.getStudent().getEmail() != null) {
                emailService.sendEmail(claim.getStudent().getEmail(), "Claim Approved", "Your claim for item '" + claim.getItem().getName() + "' has been approved! You can now pick it up.");
            }
            return "redirect:/admin?qrgen=success";
            
        } else if ("reject".equals(action)) {
            Item item = claim.getItem();
            if (item != null) {
                item.setStatus("AVAILABLE");
                itemRepository.save(item);
            }
            claim.setStatus("REJECTED");
            claimRepository.save(claim);
            
            if (claim.getStudent() != null && claim.getStudent().getEmail() != null) {
                emailService.sendEmail(claim.getStudent().getEmail(), "Claim Rejected", "Your claim for item '" + (item != null ? item.getName() : "Unknown") + "' was rejected. Reason: Incorrect verification answer or invalid proof.");
            }
            return "redirect:/admin?rejected=success";
        }
        return "redirect:/admin";
    }
    
    @PostMapping("/admin/finalize/{claimId}")
    public String finalizeClaim(@PathVariable Long claimId, HttpSession session) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/";

        ClaimRequest claim = claimRepository.findById(claimId).orElse(null);
        if (claim == null) return "redirect:/admin?error=notfound";

        Item item = claim.getItem();

        claim.setStatus("RETURNED");
        if (item != null) {
            item.setStatus("RETURNED");
            itemRepository.save(item);
        }
        claimRepository.save(claim);
        
        return "redirect:/admin?released=success";
    }

    @PostMapping("/admin/item/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId, HttpSession session) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/";

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            for (ClaimRequest claim : claimRepository.findAll()) {
                if (claim.getItem() != null && claim.getItem().getId().equals(itemId)) {
                    claimRepository.delete(claim);
                }
            }
            itemRepository.delete(item);
        }
        
        return "redirect:/admin?deleted=success";
    }

    @PostMapping("/report")
    public String reportLostItem(@ModelAttribute Item newItem, @RequestParam("imageFile") MultipartFile file) {
        // Save placeholder first so UI responds instantly
        newItem.setImageFilename("https://via.placeholder.com/300x200?text=Processing+Image...");
        itemRepository.save(newItem);
        Long itemId = newItem.getId();
        
        if (file != null && !file.isEmpty()) {
            try {
                byte[] fileBytes = file.getBytes();
                String mimeType = file.getContentType();
                String originalFilename = file.getOriginalFilename();
                
                java.util.concurrent.CompletableFuture.runAsync(() -> {
                    try {
                        String filename = imageStorageService.saveImageBytes(fileBytes, originalFilename);
                        String aiTags = geminiService.analyzeImage(fileBytes, mimeType);
                        
                        Item itemToUpdate = itemRepository.findById(itemId).orElse(null);
                        if (itemToUpdate != null) {
                            itemToUpdate.setImageFilename(filename);
                            itemToUpdate.setAiTags(aiTags);
                            itemRepository.save(itemToUpdate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {}
        }
        
        return "redirect:/student";
    }

    @PostMapping("/claim/{itemId}")
    public String submitClaim(@PathVariable Long itemId,
                            @RequestParam String ownershipDetails,
                            @RequestParam(value = "verificationAnswer", required = false) String verificationAnswer,
                            @RequestParam(value = "proofFile", required = false) MultipartFile proofFile,
                            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/";

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) return "redirect:/student?error=notfound";
        
        int attempts = claimRepository.countByStudentIdAndItem_Id(loggedInUser.getStudentId(), itemId);
        if (attempts >= 2) {
            return "redirect:/student?error=blocked";
        }

        Boolean isCorrect = null;
        if (item.getVerificationQuestion() != null && !item.getVerificationQuestion().isEmpty()) {
            if (verificationAnswer == null || !verificationAnswer.trim().equalsIgnoreCase(item.getVerificationAnswer().trim())) {
                isCorrect = false;
            } else {
                isCorrect = true;
            }
        }
        
        ClaimRequest claim = new ClaimRequest();
        claim.setItem(item);
        claim.setStudent(loggedInUser);
        claim.setOwnershipDetails(ownershipDetails);
        claim.setStudentId(loggedInUser.getStudentId());
        claim.setIsVerificationCorrect(isCorrect);
        claim.setProvidedAnswer(verificationAnswer);
        claim.setStatus("PENDING");
        claim.setClaimToken("QR-" + System.currentTimeMillis() + "-" + loggedInUser.getStudentId());
        
        // Save immediately with placeholder
        claim.setProofImage("https://via.placeholder.com/300x200?text=Processing+Proof...");
        claimRepository.save(claim);
        Long savedClaimId = claim.getId();

        item.setStatus("CLAIMING");
        itemRepository.save(item);
        
        if (proofFile != null && !proofFile.isEmpty()) {
            try {
                byte[] proofBytes = proofFile.getBytes();
                String originalFilename = proofFile.getOriginalFilename();
                
                java.util.concurrent.CompletableFuture.runAsync(() -> {
                    try {
                        String filename = imageStorageService.saveImageBytes(proofBytes, originalFilename);
                        ClaimRequest claimToUpdate = claimRepository.findById(savedClaimId).orElse(null);
                        if (claimToUpdate != null) {
                            claimToUpdate.setProofImage(filename);
                            claimRepository.save(claimToUpdate);
                        }
                    } catch (Exception e) {}
                });
            } catch (Exception e) {}
        }
        
        if (item.getReporter() != null && item.getReporter().getEmail() != null) {
            emailService.sendEmail(item.getReporter().getEmail(), "Someone Claimed Your Item", "A user has claimed the item you reported: '" + item.getName() + "'. The admin will review their claim.");
        }
        
        return "redirect:/student";
    }

    @PostMapping("/admin/reply/{messageId}")
    public String replyToMessage(@PathVariable Long messageId, @RequestParam String adminReply, HttpSession session) {
        if (session.getAttribute("isAdmin") == null) return "redirect:/";

        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setAdminReply(adminReply);
            messageRepository.save(message);
        }

        return "redirect:/admin";
    }

    @GetMapping("/qr/{studentId}")
    public String scanQrCode(@PathVariable String studentId, Model model) {
        User user = userRepository.findByStudentId(studentId);
        if (user == null) {
            return "redirect:/?error=userNotFound";
        }
        
        // Notify the user via email that someone scanned their tag
        if (user.getEmail() != null) {
            emailService.sendEmail(user.getEmail(), "Your Return Tag was Scanned!", "Someone just scanned your QR code Return Tag! They will be reaching out to you soon.");
        }
        
        model.addAttribute("owner", user);
        // We reuse the contact page to allow them to send a message to the owner
        model.addAttribute("message", new Message());
        return "redirect:/contact?foundItemOwner=" + studentId;
    }
}