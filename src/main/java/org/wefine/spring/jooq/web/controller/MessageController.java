package org.wefine.spring.jooq.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wefine.spring.jooq.web.dto.Message;
import org.wefine.spring.jooq.web.repo.MessageRepository;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MessageController {

    @Resource
    private MessageRepository messageRepository;

    @GetMapping
    public String list() {
//        Iterable<Message> messages = this.messageRepository.findAll();
//        return new ModelAndView("messages/list", "messages", messages);
        return "welcome";
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Message message) {
        return new ModelAndView("messages/view", "message", message);
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute Message message) {
        return "messages/form";
    }

    @PostMapping
    public ModelAndView create(@Valid Message message, BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("messages/form", "formErrors", result.getAllErrors());
        }
        message = this.messageRepository.save(message);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new message");
        return new ModelAndView("redirect:/{message.id}", "message.id", message.getId());
    }

    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        this.messageRepository.deleteMessage(id);
        Iterable<Message> messages = this.messageRepository.findAll();
        return new ModelAndView("messages/list", "messages", messages);
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Message message) {
        return new ModelAndView("messages/form", "message", message);
    }

}
