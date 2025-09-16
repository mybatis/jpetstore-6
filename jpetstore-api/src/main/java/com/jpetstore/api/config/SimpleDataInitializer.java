package com.jpetstore.api.config;

import com.jpetstore.api.entity.*;
import com.jpetstore.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimpleDataInitializer {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;

    @Bean
    ApplicationRunner initData() {
        return args -> {
            if (categoryRepository.count() > 0) {
                log.info("Data already exists, skipping initialization");
                return;
            }

            log.info("Initializing sample data...");
            initializeData();
            log.info("Sample data initialized successfully!");
        };
    }

    @Transactional
    public void initializeData() {
        // Create suppliers
        Supplier supplier1 = new Supplier();
        supplier1.setSupplierId(1);
        supplier1.setName("XYZ Pets");
        supplier1.setStatus("AC");
        supplier1.setAddress1("600 Avon Way");
        supplier1.setCity("Los Angeles");
        supplier1.setState("CA");
        supplier1.setZip("94024");
        supplier1.setPhone("212-947-0797");
        
        Supplier supplier2 = new Supplier();
        supplier2.setSupplierId(2);
        supplier2.setName("ABC Pets");
        supplier2.setStatus("AC");
        supplier2.setAddress1("700 Abalone Way");
        supplier2.setCity("San Francisco");
        supplier2.setState("CA");
        supplier2.setZip("94024");
        supplier2.setPhone("415-947-0797");
        
        supplierRepository.saveAll(Arrays.asList(supplier1, supplier2));

        // Create categories - exactly as in original database (all 5)
        Category fishCategory = new Category();
        fishCategory.setCategoryId("FISH");
        fishCategory.setName("Fish");
        fishCategory.setDescription("Fresh and salt water fish");
        
        Category dogsCategory = new Category();
        dogsCategory.setCategoryId("DOGS");
        dogsCategory.setName("Dogs");
        dogsCategory.setDescription("Various dog breeds");
        
        Category catsCategory = new Category();
        catsCategory.setCategoryId("CATS");
        catsCategory.setName("Cats");
        catsCategory.setDescription("Various cat breeds");
        
        Category reptilesCategory = new Category();
        reptilesCategory.setCategoryId("REPTILES");
        reptilesCategory.setName("Reptiles");
        reptilesCategory.setDescription("Reptiles");
        
        Category birdsCategory = new Category();
        birdsCategory.setCategoryId("BIRDS");
        birdsCategory.setName("Birds");
        birdsCategory.setDescription("Birds");
        
        categoryRepository.saveAll(Arrays.asList(fishCategory, dogsCategory, catsCategory, reptilesCategory, birdsCategory));

        // Create products - exactly as in original database (all 16)
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
        
        List<Product> products = Arrays.asList(angelfish, tigerShark, koi, goldfish, bulldog, poodle, dalmation, 
                                             goldenRetriever, labradorRetriever, chihuahua, rattlesnake, iguana, 
                                             manx, persian, amazonParrot, finch);
        productRepository.saveAll(products);

        // Create items - exactly as in original database (all 28 items with exact IDs and pricing)
        List<Item> items = Arrays.asList(
            // Fish items
            createItem("EST-1", angelfish, new BigDecimal("16.50"), new BigDecimal("10.00"), supplier1, "P", "Large"),
            createItem("EST-2", angelfish, new BigDecimal("16.50"), new BigDecimal("10.00"), supplier1, "P", "Small"),
            createItem("EST-3", tigerShark, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Toothless"),
            createItem("EST-4", koi, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotted"),
            createItem("EST-5", koi, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotless"),
            createItem("EST-20", goldfish, new BigDecimal("5.50"), new BigDecimal("2.00"), supplier1, "P", "Adult Male"),
            createItem("EST-21", goldfish, new BigDecimal("5.29"), new BigDecimal("1.00"), supplier1, "P", "Adult Female"),
            
            // Dog items
            createItem("EST-6", bulldog, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Male Adult"),
            createItem("EST-7", bulldog, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Female Puppy"),
            createItem("EST-8", poodle, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Male Puppy"),
            createItem("EST-9", dalmation, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotless Male Puppy"),
            createItem("EST-10", dalmation, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Spotted Adult Female"),
            createItem("EST-28", goldenRetriever, new BigDecimal("155.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female"),
            createItem("EST-22", labradorRetriever, new BigDecimal("135.50"), new BigDecimal("100.00"), supplier1, "P", "Adult Male"),
            createItem("EST-23", labradorRetriever, new BigDecimal("145.49"), new BigDecimal("100.00"), supplier1, "P", "Adult Female"),
            createItem("EST-24", labradorRetriever, new BigDecimal("255.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
            createItem("EST-25", labradorRetriever, new BigDecimal("325.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female"),
            createItem("EST-26", chihuahua, new BigDecimal("125.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
            createItem("EST-27", chihuahua, new BigDecimal("155.29"), new BigDecimal("90.00"), supplier1, "P", "Adult Female"),
            
            // Reptile items
            createItem("EST-11", rattlesnake, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Venomless"),
            createItem("EST-12", rattlesnake, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Rattleless"),
            createItem("EST-13", iguana, new BigDecimal("18.50"), new BigDecimal("12.00"), supplier1, "P", "Green Adult"),
            
            // Cat items
            createItem("EST-14", manx, new BigDecimal("58.50"), new BigDecimal("12.00"), supplier1, "P", "Tailless"),
            createItem("EST-15", manx, new BigDecimal("23.50"), new BigDecimal("12.00"), supplier1, "P", "With tail"),
            createItem("EST-16", persian, new BigDecimal("93.50"), new BigDecimal("12.00"), supplier1, "P", "Adult Female"),
            createItem("EST-17", persian, new BigDecimal("93.50"), new BigDecimal("12.00"), supplier1, "P", "Adult Male"),
            
            // Bird items
            createItem("EST-18", amazonParrot, new BigDecimal("193.50"), new BigDecimal("92.00"), supplier1, "P", "Adult Male"),
            createItem("EST-19", finch, new BigDecimal("15.50"), new BigDecimal("2.00"), supplier1, "P", "Adult Male")
        );
        itemRepository.saveAll(items);

        // Create inventory for all items - exactly as in original database
        // Save inventory items individually to avoid Hibernate relationship issues
        String[] itemIds = {"EST-1", "EST-2", "EST-3", "EST-4", "EST-5", "EST-6", "EST-7", "EST-8", "EST-9", "EST-10",
                           "EST-11", "EST-12", "EST-13", "EST-14", "EST-15", "EST-16", "EST-17", "EST-18", "EST-19", "EST-20",
                           "EST-21", "EST-22", "EST-23", "EST-24", "EST-25", "EST-26", "EST-27", "EST-28"};
        
        for (String itemId : itemIds) {
            Inventory inventory = new Inventory();
            inventory.setItemId(itemId);
            inventory.setQuantity(10000);
            inventoryRepository.save(inventory);
        }
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
    
    private Inventory createInventory(String itemId, int quantity) {
        Inventory inventory = new Inventory();
        inventory.setItemId(itemId);
        inventory.setQuantity(quantity);
        return inventory;
    }
}
