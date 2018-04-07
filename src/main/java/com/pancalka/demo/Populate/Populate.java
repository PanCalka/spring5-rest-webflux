package com.pancalka.demo.Populate;

import com.pancalka.demo.domain.Category;
import com.pancalka.demo.domain.Vendor;
import com.pancalka.demo.repository.CategoryRepository;
import com.pancalka.demo.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Populate implements CommandLineRunner {

private final CategoryRepository categoryRepository;
private final VendorRepository vendorRepository;

    public Populate(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0){
            loadingCategoryData();
            loadingVendorData();
        }
    }

    private void loadingCategoryData() {
        categoryRepository.save(Category.builder().description("Meat").build()).block();
        categoryRepository.save(Category.builder().description("Fish").build()).block();
        categoryRepository.save(Category.builder().description("Vegan").build()).block();
        log.info("Category loaded " + categoryRepository.count().block());
    }

    private void loadingVendorData() {
        vendorRepository.save(Vendor.builder().firstName("Tom").lastName("Jerry").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Bob").lastName("Cold").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Paul").lastName("Angry").build()).block();
        log.info("Vendor loaded " + vendorRepository.count().block());
    }
}
