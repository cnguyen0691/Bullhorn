package com.example.bullhorn;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;


@Controller
public class HomeController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @RequestMapping("/")
    public String listMessage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addMessage(Model model){
        model.addAttribute("message", new Message());
            return "addMessage";

    }
    @PostMapping("/process")
    public String processMessage (@Valid @ModelAttribute("message") Message message,
                                  BindingResult result,
                                  @RequestParam("file") MultipartFile file) {
        System.out.println("object =" + message);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e);
            }
            return "addMessage";
        }
        //if there is a picture path and file is empty then save message
        if (message.getImage() != null && file.isEmpty()) {
            messageRepository.save(message);
            return "redirect:/";
        }
        if( file.isEmpty()){
            return "addMessage";
        }
        Map uploadResult;
        try {
            uploadResult = cloudinaryConfig.upload(
                    file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/addMessage";
        }
        String url = uploadResult.get("url").toString();
        message.setImage(url);
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "addMessage";
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

    @RequestMapping("/about")
    public String getAbout(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "about";
    }

}
