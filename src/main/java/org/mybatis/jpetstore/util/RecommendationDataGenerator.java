/*
 *    Copyright 2010-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;
import org.mybatis.jpetstore.service.OpenAiRecommendationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RecommendationDataGenerator {

  private static final String SQL_FILE_PATH = "src/main/resources/database/jpetstore-hsqldb-recommendations.sql";

  public static void main(String[] args) {
    // Initialize Spring context to get access to services
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    AccountService accountService = context.getBean(AccountService.class);
    CatalogService catalogService = context.getBean(CatalogService.class);
    OpenAiRecommendationService openAiRecommendationService = context.getBean(OpenAiRecommendationService.class);

    // Define all survey options
    List<String> residenceEnvs = Arrays.asList("Indoor-focused (apartment, studio, etc.)",
        "Includes outdoor space (yard, balcony, etc.)", "Humid environment (near river/lake)", "Dry environment",
        "Can maintain special spaces (aquarium, terrarium, etc.)");
    List<String> carePeriods = Arrays.asList("5 years or less", "5-10 years", "10 years or more", "Doesn't matter");
    List<String> petColorPrefs = Arrays.asList("Light colors (white | ivory tones)", "Warm colors (brown | gold tones)",
        "Dark colors (black | gray tones)", "Mixed colors", "Doesn't matter");
    List<String> petSizePrefs = Arrays.asList("Small (suitable for studio/small apartment)",
        "Medium (regular home, some space available)", "Large (yard and spacious indoor area possible)");
    List<String> activityTimes = Arrays.asList("Day", "Night", "Irregular");
    List<String> dietManagements = Arrays.asList("Simple kibble-focused (carnivore, omnivore)",
        "Can provide diverse diet (vegetarian, mixed possible)",
        "Can manage special diets (insect-based, dried food, etc.)");

    // Retrieve all products from the catalog
    List<Product> allProducts = new ArrayList<>();
    List<Category> categories = catalogService.getCategoryList();
    for (Category category : categories) {
      allProducts.addAll(catalogService.getProductListByCategory(category.getCategoryId()));
    }

    try (var writer = Files.newBufferedWriter(Path.of(SQL_FILE_PATH))) {
      writer.write(
          "INSERT INTO SURVEY_RECOMMENDATIONS (survey_recommendation_id, residence_env, care_period, pet_color_pref, pet_size_pref, activity_time, diet_management, recommended_json_data) VALUES\n");

      long idCounter = 1;
      List<String> insertValues = new ArrayList<>();

      for (String residenceEnv : residenceEnvs) {
        for (String carePeriod : carePeriods) {
          for (String petColorPref : petColorPrefs) {
            for (String petSizePref : petSizePrefs) {
              for (String activityTime : activityTimes) {
                for (String dietManagement : dietManagements) {
                  Account dummyAccount = new Account();
                  dummyAccount.setResidenceEnv(residenceEnv);
                  dummyAccount.setCarePeriod(carePeriod);
                  dummyAccount.setPetColorPref(petColorPref);
                  dummyAccount.setPetSizePref(petSizePref);
                  dummyAccount.setActivityTime(activityTime);
                  dummyAccount.setDietManagement(dietManagement);

                  String recommendedJson = openAiRecommendationService.getRecommendation(dummyAccount, allProducts);

                  // Escape single quotes for SQL insertion
                  String escapedResidenceEnv = residenceEnv.replace("'", "''");
                  String escapedCarePeriod = carePeriod.replace("'", "''");
                  String escapedPetColorPref = petColorPref.replace("'", "''");
                  String escapedPetSizePref = petSizePref.replace("'", "''");
                  String escapedActivityTime = activityTime.replace("'", "''");
                  String escapedDietManagement = dietManagement.replace("'", "''");
                  String escapedRecommendedJson = recommendedJson.replace("'", "''");

                  insertValues.add(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')", idCounter++,
                      escapedResidenceEnv, escapedCarePeriod, escapedPetColorPref, escapedPetSizePref,
                      escapedActivityTime, escapedDietManagement, escapedRecommendedJson));
                  System.out.println("Generated recommendation for combination " + (idCounter - 1));
                  // Add a small delay to avoid hitting OpenAI rate limits during generation
                  try {
                    Thread.sleep(1000); // 1 second delay
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                  }
                }
              }
            }
          }
        }
      }

      writer.write(String.join(",\n", insertValues));
      writer.write(";\n");
      System.out.println("SQL INSERT statements generated successfully to " + SQL_FILE_PATH);
    } catch (IOException e) {
      System.err.println("Error writing SQL file: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error during recommendation generation: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
