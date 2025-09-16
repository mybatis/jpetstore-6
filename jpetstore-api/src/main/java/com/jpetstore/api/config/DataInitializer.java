package com.jpetstore.api.config;

import com.jpetstore.api.entity.*;
import com.jpetstore.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    @Transactional
    ApplicationRunner initDatabase(
            SupplierRepository supplierRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            ItemRepository itemRepository,
            InventoryRepository inventoryRepository,
            AccountRepository accountRepository,
            SignOnRepository signOnRepository,
            ProfileRepository profileRepository,
            BannerDataRepository bannerDataRepository) {
        
        return (ApplicationArguments args) -> {
            log.info("Initializing database with sample data...");
            
            // Check if data already exists
            if (categoryRepository.count() > 0 || itemRepository.count() > 0) {
                log.info("Database already contains data, skipping initialization");
                return;
            }
            
            // Initialize Suppliers
            Supplier supplier1 = new Supplier();
            supplier1.setSupplierId(1);
            supplier1.setName("XYZ Pets");
            supplier1.setStatus("AC");
            supplier1.setAddress1("600 Avon Way");
            supplier1.setAddress2("");
            supplier1.setCity("Los Angeles");
            supplier1.setState("CA");
            supplier1.setZip("94024");
            supplier1.setPhone("212-947-0797");
            
            Supplier supplier2 = new Supplier();
            supplier2.setSupplierId(2);
            supplier2.setName("ABC Pets");
            supplier2.setStatus("AC");
            supplier2.setAddress1("700 Abalone Way");
            supplier2.setAddress2("");
            supplier2.setCity("San Francisco");
            supplier2.setState("CA");
            supplier2.setZip("94024");
            supplier2.setPhone("415-947-0797");
            
            supplierRepository.saveAll(Arrays.asList(supplier1, supplier2));
            log.info("Suppliers initialized");
            
            // Initialize Categories
            Category fishCategory = new Category();
            fishCategory.setCategoryId("FISH");
            fishCategory.setName("Fish");
            fishCategory.setDescription("Fresh and salt water fish");
            
            Category dogsCategory = new Category();
            dogsCategory.setCategoryId("DOGS");
            dogsCategory.setName("Dogs");
            dogsCategory.setDescription("Various dog breeds");
            
            Category reptilesCategory = new Category();
            reptilesCategory.setCategoryId("REPTILES");
            reptilesCategory.setName("Reptiles");
            reptilesCategory.setDescription("Lizards and snakes");
            
            Category catsCategory = new Category();
            catsCategory.setCategoryId("CATS");
            catsCategory.setName("Cats");
            catsCategory.setDescription("Various cat breeds");
            
            Category birdsCategory = new Category();
            birdsCategory.setCategoryId("BIRDS");
            birdsCategory.setName("Birds");
            birdsCategory.setDescription("Exotic birds");
            
            categoryRepository.saveAll(Arrays.asList(
                fishCategory, dogsCategory, reptilesCategory, catsCategory, birdsCategory
            ));
            log.info("Categories initialized");
            
            // Initialize Products
            Product angelfish = createProduct("FI-SW-01", fishCategory, "Angelfish", "Salt Water fish from Australia");
            Product tigerShark = createProduct("FI-SW-02", fishCategory, "Tiger Shark", "Salt Water fish from Australia");
            Product koi = createProduct("FI-FW-01", fishCategory, "Koi", "Fresh Water fish from Japan");
            Product goldfish = createProduct("FI-FW-02", fishCategory, "Goldfish", "Fresh Water fish from China");
            Product bulldog = createProduct("K9-BD-01", dogsCategory, "Bulldog", "Friendly dog from England");
            Product poodle = createProduct("K9-PO-02", dogsCategory, "Poodle", "Cute dog from France");
            Product dalmation = createProduct("K9-DL-01", dogsCategory, "Dalmation", "Great dog for a Fire Station");
            Product goldenRetriever = createProduct("K9-RT-01", dogsCategory, "Golden Retriever", "Great family dog");
            Product labradorRetriever = createProduct("K9-RT-02", dogsCategory, "Labrador Retriever", "Great hunting dog");
            Product chihuahua = createProduct("K9-CW-01", dogsCategory, "Chihuahua", "Great companion dog");
            Product rattlesnake = createProduct("RP-SN-01", reptilesCategory, "Rattlesnake", "Doubles as a watch dog");
            Product iguana = createProduct("RP-LI-02", reptilesCategory, "Iguana", "Friendly green friend");
            Product manx = createProduct("FL-DSH-01", catsCategory, "Manx", "Great for reducing mouse populations");
            Product persian = createProduct("FL-DLH-02", catsCategory, "Persian", "Friendly house cat, doubles as a princess");
            Product amazonParrot = createProduct("AV-CB-01", birdsCategory, "Amazon Parrot", "Great companion for up to 75 years");
            Product finch = createProduct("AV-SB-02", birdsCategory, "Finch", "Great stress reliever");
            
            List<Product> products = Arrays.asList(
                angelfish, tigerShark, koi, goldfish,
                bulldog, poodle, dalmation, goldenRetriever, labradorRetriever, chihuahua,
                rattlesnake, iguana, manx, persian, amazonParrot, finch
            );
            productRepository.saveAll(products);
            log.info("Products initialized");
            
            // Initialize Items
            List<Item> items = Arrays.asList(
                createItem("EST-1", angelfish, new BigDecimal("16.50"), new BigDecimal("10.00"), supplier1, "P", "Large"),
                createItem("EST-2", angelfish, new BigDecimal("16.50"), new BigDecimal("10.00"), supplier1, "P", "Small"),
                createItem("EST-3", tigerShark, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Toothless"),
                createItem("EST-4", koi, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotted"),
                createItem("EST-5", koi, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotless"),
                createItem("EST-6", bulldog, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Male Adult"),
                createItem("EST-7", bulldog, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Female Puppy"),
                createItem("EST-8", poodle, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Male Puppy"),
                createItem("EST-9", dalmation, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotless Male Puppy"),
                createItem("EST-10", dalmation, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotted Adult Female"),
                createItem("EST-11", rattlesnake, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Venomless"),
                createItem("EST-12", rattlesnake, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Rattleless"),
                createItem("EST-13", iguana, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Green Adult"),
                createItem("EST-14", manx, new BigDecimal("58.50"), new BigDecimal("12.00"), supplier1, "P", "Tailless"),
                createItem("EST-15", manx, new BigDecimal("23.50"), new BigDecimal("12.00"), supplier1, "P", "With tail"),
                createItem("EST-16", persian, new BigDecimal("93.50"), new BigDecimal("12.00"), supplier1, "P", "Adult Female"),
                createItem("EST-17", persian, new BigDecimal("93.50"), new BigDecimal("12.00"), supplier1, "P", "Adult Male"),
                createItem("EST-18", amazonParrot, new BigDecimal("193.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
                createItem("EST-19", finch, new BigDecimal("15.50"), new BigDecimal("2.00"), supplier1, "P", "Adult Male"),
                createItem("EST-20", goldfish, new BigDecimal("5.50"), new BigDecimal("2.00"), supplier1, "P", "Adult Male"),
                createItem("EST-21", goldfish, new BigDecimal("5.29"), new BigDecimal("1.00"), supplier1, "P", "Adult Female"),
                // Missing items from original data
                createItem("EST-22", labradorRetriever, new BigDecimal("135.50"), new BigDecimal("100.00"), supplier1, "P", "Adult Male"),
                createItem("EST-23", labradorRetriever, new BigDecimal("145.49"), new BigDecimal("100.00"), supplier1, "P", "Adult Female"),
                createItem("EST-24", labradorRetriever, new BigDecimal("255.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
                createItem("EST-25", labradorRetriever, new BigDecimal("325.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female"),
                createItem("EST-26", chihuahua, new BigDecimal("125.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
                createItem("EST-27", chihuahua, new BigDecimal("155.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female"),
                createItem("EST-28", goldenRetriever, new BigDecimal("155.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female")
            );
            itemRepository.saveAll(items);
            log.info("Items initialized");
            
            // Initialize Inventory
            for (Item item : items) {
                Inventory inventory = new Inventory();
                inventory.setItemId(item.getItemId());
                inventory.setQuantity(10000);
                inventoryRepository.save(inventory);
            }
            log.info("Inventory initialized");
            
            // Initialize SignOn accounts
            SignOn signOn1 = new SignOn();
            signOn1.setUsername("j2ee");
            signOn1.setPassword("j2ee");
            
            SignOn signOn2 = new SignOn();
            signOn2.setUsername("ACID");
            signOn2.setPassword("ACID");
            
            signOnRepository.saveAll(Arrays.asList(signOn1, signOn2));
            log.info("SignOn accounts initialized");
            
            // Initialize Accounts
            Account account1 = new Account();
            account1.setUsername("j2ee");
            account1.setEmail("yourname@yourdomain.com");
            account1.setFirstName("ABC");
            account1.setLastName("XYX");
            account1.setStatus("OK");
            account1.setAddress1("901 San Antonio Road");
            account1.setAddress2("MS UCUP02-206");
            account1.setCity("Palo Alto");
            account1.setState("CA");
            account1.setZip("94303");
            account1.setCountry("USA");
            account1.setPhone("555-555-5555");
            
            Account account2 = new Account();
            account2.setUsername("ACID");
            account2.setEmail("acid@yourdomain.com");
            account2.setFirstName("ABC");
            account2.setLastName("XYX");
            account2.setStatus("OK");
            account2.setAddress1("901 San Antonio Road");
            account2.setAddress2("MS UCUP02-206");
            account2.setCity("Palo Alto");
            account2.setState("CA");
            account2.setZip("94303");
            account2.setCountry("USA");
            account2.setPhone("555-555-5555");
            
            accountRepository.saveAll(Arrays.asList(account1, account2));
            log.info("Accounts initialized");
            
            // Initialize Profiles
            Profile profile1 = new Profile();
            profile1.setUserId("j2ee");
            profile1.setLanguagePreference("english");
            profile1.setFavoriteCategory("DOGS");
            profile1.setListOption(true);
            profile1.setBannerOption(true);
            
            Profile profile2 = new Profile();
            profile2.setUserId("ACID");
            profile2.setLanguagePreference("english");
            profile2.setFavoriteCategory("CATS");
            profile2.setListOption(true);
            profile2.setBannerOption(true);
            
            profileRepository.saveAll(Arrays.asList(profile1, profile2));
            log.info("Profiles initialized");
            
            // Initialize Banner Data
            List<BannerData> bannerData = Arrays.asList(
                new BannerData("FISH", "<image src=\"../images/banner_fish.gif\">"),
                new BannerData("CATS", "<image src=\"../images/banner_cats.gif\">"),
                new BannerData("DOGS", "<image src=\"../images/banner_dogs.gif\">"),
                new BannerData("REPTILES", "<image src=\"../images/banner_reptiles.gif\">"),
                new BannerData("BIRDS", "<image src=\"../images/banner_birds.gif\">")
            );
            bannerDataRepository.saveAll(bannerData);
            log.info("Banner data initialized");
            
            log.info("Database initialization completed successfully!");
        };
    }
    
    private Product createProduct(String id, Category category, String name, String description) {
        Product product = new Product();
        product.setProductId(id);
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        return product;
    }
    
    private Item createItem(String itemId, Product product, BigDecimal listPrice, 
                           BigDecimal unitCost, Supplier supplier, String status, String attr1) {
        Item item = new Item();
        item.setItemId(itemId);
        item.setProduct(product);
        item.setListPrice(listPrice);
        item.setUnitCost(unitCost);
        item.setSupplier(supplier);
        item.setStatus(status);
        item.setAttribute1(attr1);
        return item;
    }
}
