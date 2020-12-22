package com.sample.controller;

import com.sample.model.Category;
import com.sample.model.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController("/v1")
public class WebController {

    @GetMapping("/time")
    public String time(){
        return new Date().toString();
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Category>> getMenu(){


        Menu m1 = Menu.builder().icon("icon1").labelKey("key1").order(1).viewUrl("/url1").build();
        Menu m2 = Menu.builder().icon("icon2").labelKey("key2").order(2).viewUrl("/url2").build();

        Category c1 = Category.builder().icon("icon00").labelKey("pkey1").order(1).menu(Arrays.asList(m1,m2)).build();
        Category c2 = Category.builder().icon("icon01").labelKey("pkey2").order(2).menu(Arrays.asList(m1,m2)).build();

        return ResponseEntity.ok(Arrays.asList(c1,c2));
    }
}
