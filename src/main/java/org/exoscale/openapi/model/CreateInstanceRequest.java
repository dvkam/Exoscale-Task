package org.exoscale.openapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public class CreateInstanceRequest {
    @NonNull
    @JsonProperty
    private final InstanceTemplate template;

    @NonNull
    @JsonProperty("instance-type")
    private final InstanceType instanceType;

    @NonNull
    @JsonProperty("disk-size")
    private final Integer diskSize;
    @Builder
    public static class InstanceTemplate {
        @JsonProperty
        private String description;
        @JsonProperty("ssh-key-enabled")
        private boolean sshKeyEnabled;
        @JsonProperty
        private String name;
        @JsonProperty("default-user")
        private String defaultUser;
        @JsonProperty
        private int size;
        @JsonProperty("password-enabled")
        private boolean passwordEnabled;
        private String checksum;
        @JsonProperty("boot-mode")
        private String bootMode;
        @JsonProperty
        private List<String> zones;
        @JsonProperty
        private String url;
    }

    @Builder
    public static class InstanceType {
        @JsonProperty
        private String id;
    }
}
