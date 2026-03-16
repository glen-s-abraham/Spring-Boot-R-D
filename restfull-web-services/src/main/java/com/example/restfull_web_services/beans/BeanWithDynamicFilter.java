package com.example.restfull_web_services.beans;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Demonstrates DYNAMIC filtering via @JsonView.
 *
 * Jackson @JsonView lets you declare a set of named "views" (just marker
 * interfaces) and tag each field with which view(s) it belongs to.
 * The controller then annotates each endpoint with @JsonView to pick the view —
 * that's where the "dynamic" per-endpoint choice happens at runtime.
 *
 * View hierarchy (each view also includes its parent):
 * Basic → id, field1
 * Normal → id, field1, field2 (extends Basic)
 * Full → id, field1, field2, field3 (extends Normal)
 */
@Data
@AllArgsConstructor
public class BeanWithDynamicFilter {

    /** Available views, nested for hierarchical inclusion. */
    public interface Views {
        interface Basic {
        }

        interface Normal extends Basic {
        }

        interface Full extends Normal {
        }
    }

    // Present in ALL views (Basic, Normal, Full)
    @JsonView(Views.Basic.class)
    private Integer id;

    // Present in ALL views (Basic, Normal, Full)
    @JsonView(Views.Basic.class)
    private String field1;

    // Present in Normal and Full only
    @JsonView(Views.Normal.class)
    private String field2;

    // Present in Full only
    @JsonView(Views.Full.class)
    private String field3;
}
