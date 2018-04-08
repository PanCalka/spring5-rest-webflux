package com.pancalka.demo.controllers;

import com.pancalka.demo.domain.Vendor;
import com.pancalka.demo.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

public class VendorControllerTest {

    VendorController vendorController;
    VendorRepository vendorRepository;
    WebTestClient webTestClient;


    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        given(vendorRepository.findAll()).willReturn(
                Flux.just(Vendor.builder().firstName("Test").build(),
                        Vendor.builder().firstName("Test2").build()));

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void listById() {
        given(vendorRepository.findById("id")).willReturn(
                Mono.just(Vendor.builder().firstName("Test").build()));

        webTestClient.get().uri("/api/v1/vendors/id")
                .exchange()
                .expectBodyList(Vendor.class);
    }
}