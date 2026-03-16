package com.example.restfull_web_services.controllers;

import com.example.restfull_web_services.beans.BeanWithDynamicFilter;
import com.example.restfull_web_services.beans.BeanWithStaticFilter;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * FilteringDemoController — demonstrates two Jackson filtering strategies
 * that are fully supported in Spring Boot 4 / Spring Framework 7.
 *
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║ STATIC FILTERING ║
 * ║ Fields are excluded unconditionally via annotations on the POJO.║
 * ║ • @JsonIgnore — excludes a single field ║
 * ║ • @JsonIgnoreProperties — excludes one or more at class level ║
 * ║ No controller code needed; Jackson handles it automatically. ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║ DYNAMIC FILTERING (@JsonView) ║
 * ║ Each POJO field is tagged with @JsonView(SomeView.class). ║
 * ║ Each endpoint picks a view via @JsonView — only fields that ║
 * ║ belong to that view (or a parent view) are serialized. ║
 * ║ ║
 * ║ This is the Spring Boot 4 / Spring Framework 7 replacement for ║
 * ║ the old MappingJacksonValue + @JsonFilter pattern, which is ║
 * ║ no longer supported. ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
@RestController
@RequestMapping("/filtering")
public class FilteringDemoController {

    // ─── Static Filtering ──────────────────────────────────────────────────────
    // @JsonIgnore and @JsonIgnoreProperties on BeanWithStaticFilter handle
    // everything automatically — no extra controller code required.

    /**
     * GET /filtering/static
     * Response: { id, username, email }
     * Excluded: password (@JsonIgnore), creditCardNumber (@JsonIgnoreProperties)
     */
    @GetMapping("/static")
    public BeanWithStaticFilter staticBean() {
        return new BeanWithStaticFilter(
                1, "john_doe", "john@example.com",
                "supersecret", // @JsonIgnore → never in response
                "4111-1111-1111-1111" // @JsonIgnoreProperties → never in response
        );
    }

    /**
     * GET /filtering/static/list
     * Response: list of { id, username, email } — same exclusions apply.
     */
    @GetMapping("/static/list")
    public List<BeanWithStaticFilter> staticBeanList() {
        return List.of(
                new BeanWithStaticFilter(1, "john_doe", "john@example.com", "pass1", "4111-1111-1111-1111"),
                new BeanWithStaticFilter(2, "jane_doe", "jane@example.com", "pass2", "5500-0000-0000-0004"),
                new BeanWithStaticFilter(3, "admin_usr", "admin@example.com", "pass3", "3714-496353-98431"));
    }

    // ─── Dynamic Filtering (@JsonView) ────────────────────────────────────────
    // BeanWithDynamicFilter tags each field with a view.
    // @JsonView on the endpoint picks which fields come through.
    //
    // View hierarchy:
    // Basic → id, field1
    // Normal → id, field1, field2 (extends Basic)
    // Full → id, field1, field2, field3 (extends Normal)

    /**
     * GET /filtering/dynamic/basic
     * Response: { id, field1 }
     */
    @JsonView(BeanWithDynamicFilter.Views.Basic.class)
    @GetMapping("/dynamic/basic")
    public BeanWithDynamicFilter dynamicBasic() {
        return new BeanWithDynamicFilter(1, "value1", "value2", "value3");
    }

    /**
     * GET /filtering/dynamic/normal
     * Response: { id, field1, field2 }
     */
    @JsonView(BeanWithDynamicFilter.Views.Normal.class)
    @GetMapping("/dynamic/normal")
    public BeanWithDynamicFilter dynamicNormal() {
        return new BeanWithDynamicFilter(1, "value1", "value2", "value3");
    }

    /**
     * GET /filtering/dynamic/full
     * Response: { id, field1, field2, field3 }
     */
    @JsonView(BeanWithDynamicFilter.Views.Full.class)
    @GetMapping("/dynamic/full")
    public BeanWithDynamicFilter dynamicFull() {
        return new BeanWithDynamicFilter(1, "value1", "value2", "value3");
    }

    /**
     * GET /filtering/dynamic/list
     * Response: list of { id, field1, field2 } — Normal view applied to a list.
     */
    @JsonView(BeanWithDynamicFilter.Views.Normal.class)
    @GetMapping("/dynamic/list")
    public List<BeanWithDynamicFilter> dynamicList() {
        return List.of(
                new BeanWithDynamicFilter(1, "value1_a", "value2_a", "value3_a"),
                new BeanWithDynamicFilter(2, "value1_b", "value2_b", "value3_b"));
    }
}
