package com.pancalka.demo.controllers;

import com.pancalka.demo.domain.Category;
import com.pancalka.demo.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryController categoryController;
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("TestCat_1").build(),
                        Category.builder().description("TestCat_2").build()));

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        given(categoryRepository.findById("test"))
                .willReturn(Mono.just(Category.builder().description("TestCat_1").build()));

        webTestClient.get()
                .uri("/api/v1/categories/test")
                .exchange()
                .expectBodyList(Category.class);
    }

    @Test
    public void shouldCreateCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> monoCategory = Mono.just(Category.builder().description("test").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(monoCategory,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void shouldUpdateCategory() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> monoCategory = Mono.just(Category.builder().description("someCat").build());

        webTestClient.put()
                .uri("/api/v1/categories/test")
                .body(monoCategory,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldPatchCategory() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> monoCategory = Mono.just(Category.builder().description("someCat").build());

        webTestClient.patch()
                .uri("/api/v1/categories/test")
                .body(monoCategory,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }


}