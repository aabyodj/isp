package by.aab.isp.web;

import org.springframework.data.domain.Sort;

public class Const {

    public static final String SCHEMA_REDIRECT = "redirect:";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final Sort DEFAULT_PROMOTIONS_SORT = Sort.by("activeSince", "activeUntil");
    public static final Sort DEFAULT_TARIFFS_SORT = Sort.by("name");
    public static final Sort DEFAULT_EMPLOYEES_SORT = Sort.by("email");

    private Const() {}
}
