package com.pancalka.demo.controllers;

import com.pancalka.demo.domain.Category;
import com.pancalka.demo.domain.Vendor;
import com.pancalka.demo.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    public void shouldCreateVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> monoVendor = Mono.just(Vendor.builder().firstName("test").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(monoVendor,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void shouldUpdateVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> monoVendor = Mono.just(Vendor.builder().firstName("name").build());

        webTestClient.put()
                .uri("/api/v1/vendors/test")
                .body(monoVendor,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldPatchVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> monoVendor = Mono.just(Vendor.builder().firstName("name").lastName("lastName").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/test")
                .body(monoVendor,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}