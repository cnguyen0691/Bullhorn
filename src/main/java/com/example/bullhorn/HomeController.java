package com.example.bullhorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Controller
public class HomeController {
    @Autowired
    MessageRepository messageRepository;

    @RequestMapping("/")
    public String listmessage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }
//    @RequestMapping(value = "{image}")
//    @ResponseBody
//    public byte[] getImage(@PathVariable(value = "image") String imageName) throws IOException {
//
//        File serverFile = new File("/home/user/uploads/" + imageName + ".jpg");
//
//        return Files.readAllBytes(serverFile.toPath());
//    }

    @GetMapping("/add")
    public String addmessage(Model model){
        model.addAttribute("message", new Message());
            return "message";

    }
    @PostMapping("/process")
    public String processmessage (@Valid Message message, BindingResult result){
        if(result.hasErrors()){
            return ("message");
        }
        messageRepository.save(message);
        return ("redirect:/");

    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "message";
    }

    @RequestMapping("/detail/{id}")
    public String detailMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id) {
        messageRepository.deleteById(id);
        return "redirect:/";
    }

}
