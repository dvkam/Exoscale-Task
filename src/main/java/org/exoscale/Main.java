package org.exoscale;

import org.exoscale.openapi.Client;
import org.exoscale.openapi.model.CreateInstanceRequest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InvalidKeyException, IOException, InterruptedException {

        Client client = new Client("MY-KEY", "MY-SECRET");

        var deployTargets = client.listDeployTargets();
        System.out.println(deployTargets.body());
        System.out.println(deployTargets.statusCode());

        var response = client.createInstance(CreateInstanceRequest.builder()
                .instanceType(CreateInstanceRequest.InstanceType.builder().id("71004023-bb72-4a97-b1e9-bc66dfce9470").build())
                .template(CreateInstanceRequest.InstanceTemplate.builder()
                        .name("Testing")
                        .description("David")
                        .defaultUser("David")
                        .bootMode("legacy")
                        .zones(List.of("at-vie-1"))
                        .size(1)
                        .build())
                .diskSize(10)
                .build());


        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
