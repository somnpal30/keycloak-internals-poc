package com.sample.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Category {
    private String labelKey;
    private String icon;
    private int order;
    private List<Menu> menu;
}
