package br.org.edu.ifrn.LojaCarro.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeuController {
    int count = 0;
    @RequestMapping("teste")
    public String teste() throws Exception {
        count++;
        if(count < 10) {
            return "bom dia";
        }else{
            throw new Exception("Excedeu o limite");
        }
    }

}
