package com.sample.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Menu {
    private String labelKey;
    private String icon;
    private int order;
    private String viewUrl;
}
