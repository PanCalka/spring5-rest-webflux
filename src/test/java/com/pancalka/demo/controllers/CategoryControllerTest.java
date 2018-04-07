package com.pancalka.demo.controllers;

import com.pancalka.demo.domain.Category;
import com.pancalka.demo.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
}