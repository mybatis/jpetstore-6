package org.mybatis.jpetstore.service;

import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;
import weka.core.*;

import java.util.*;

/**
 * 데이터마이닝 기반 반려동물 추천 서비스
 * J48 의사결정 트리 알고리즘을 사용하여 사용자 속성에 맞는 동물을 추천합니다.
 */
@Service
public class PetRecommendationService {

    private J48 classifier;
    private Instances trainingData;
    private boolean isModelTrained = false;

    public PetRecommendationService() {
        initializeClassifier();
    }

    /**
     * J48 분류기 초기화 및 훈련 데이터 생성
     */
    private void initializeClassifier() {
        try {
            // 속성 정의
            ArrayList<Attribute> attributes = new ArrayList<>();

            // 1. 하루 재택 시간 (숫자형)
            attributes.add(new Attribute("homeHours"));

            // 2. 주거 형태
            ArrayList<String> housingValues = new ArrayList<>();
            housingValues.add("원룸");
            housingValues.add("아파트");
            housingValues.add("단독주택");
            attributes.add(new Attribute("housing", housingValues));

            // 3. 월 예산
            ArrayList<String> budgetValues = new ArrayList<>();
            budgetValues.add("10만원미만");
            budgetValues.add("10-30만원");
            budgetValues.add("30만원이상");
            attributes.add(new Attribute("budget", budgetValues));

            // 4. 활동성
            ArrayList<String> activityValues = new ArrayList<>();
            activityValues.add("활발함");
            activityValues.add("보통");
            activityValues.add("조용함");
            attributes.add(new Attribute("activityLevel", activityValues));

            // 5. 알레르기 여부
            ArrayList<String> allergyValues = new ArrayList<>();
            allergyValues.add("있음");
            allergyValues.add("없음");
            attributes.add(new Attribute("allergy", allergyValues));

            // 6. 반려동물 경험
            ArrayList<String> experienceValues = new ArrayList<>();
            experienceValues.add("있음");
            experienceValues.add("없음");
            attributes.add(new Attribute("experience", experienceValues));

            // 7. 클래스 (추천 동물)
            ArrayList<String> classValues = new ArrayList<>();
            classValues.add("FISH");
            classValues.add("DOGS");
            classValues.add("CATS");
            classValues.add("BIRDS");
            classValues.add("REPTILES");
            attributes.add(new Attribute("petType", classValues));

            // 훈련 데이터셋 생성
            trainingData = new Instances("PetRecommendation", attributes, 0);
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

            // 샘플 훈련 데이터 추가
            addTrainingData();

            // J48 분류기 훈련
            classifier = new J48();
            classifier.setOptions(new String[]{"-C", "0.25", "-M", "2"});
            classifier.buildClassifier(trainingData);

            isModelTrained = true;

        } catch (Exception e) {
            e.printStackTrace();
            isModelTrained = false;
        }
    }

    /**
     * 훈련 데이터 추가
     */
    private void addTrainingData() {
        // 물고기 추천 케이스
        addInstance(2, "원룸", "10만원미만", "조용함", "있음", "없음", "FISH");
        addInstance(3, "원룸", "10만원미만", "보통", "있음", "없음", "FISH");
        addInstance(4, "아파트", "10-30만원", "조용함", "있음", "없음", "FISH");

        // 강아지 추천 케이스
        addInstance(8, "단독주택", "30만원이상", "활발함", "없음", "있음", "DOGS");
        addInstance(10, "아파트", "30만원이상", "활발함", "없음", "있음", "DOGS");
        addInstance(6, "단독주택", "10-30만원", "보통", "없음", "있음", "DOGS");
        addInstance(7, "아파트", "30만원이상", "보통", "없음", "없음", "DOGS");

        // 고양이 추천 케이스
        addInstance(4, "원룸", "10-30만원", "조용함", "없음", "없음", "CATS");
        addInstance(3, "아파트", "10-30만원", "보통", "없음", "없음", "CATS");
        addInstance(5, "아파트", "10-30만원", "조용함", "없음", "있음", "CATS");
        addInstance(6, "단독주택", "30만원이상", "조용함", "없음", "있음", "CATS");

        // 새 추천 케이스
        addInstance(5, "아파트", "10-30만원", "보통", "없음", "없음", "BIRDS");
        addInstance(6, "원룸", "10-30만원", "활발함", "없음", "있음", "BIRDS");
        addInstance(4, "아파트", "10만원미만", "보통", "있음", "없음", "BIRDS");

        // 파충류 추천 케이스
        addInstance(2, "원룸", "10-30만원", "조용함", "있음", "있음", "REPTILES");
        addInstance(3, "아파트", "10-30만원", "조용함", "있음", "있음", "REPTILES");
        addInstance(4, "단독주택", "30만원이상", "조용함", "없음", "있음", "REPTILES");
    }

    /**
     * 인스턴스 추가 헬퍼 메서드
     */
    private void addInstance(double homeHours, String housing, String budget,
                             String activity, String allergy, String experience, String petType) {
        Instance inst = new DenseInstance(7);
        inst.setDataset(trainingData);

        inst.setValue(0, homeHours);
        inst.setValue(1, housing);
        inst.setValue(2, budget);
        inst.setValue(3, activity);
        inst.setValue(4, allergy);
        inst.setValue(5, experience);
        inst.setValue(6, petType);

        trainingData.add(inst);
    }

    /**
     * 사용자 속성 기반 반려동물 추천
     *
     * @param userAttributes 사용자 속성 맵
     * @return 추천 결과 리스트 (순위별)
     */
    public List<PetRecommendation> recommendPet(Map<String, Object> userAttributes) {
        List<PetRecommendation> recommendations = new ArrayList<>();

        if (!isModelTrained) {
            return getFallbackRecommendations(userAttributes);
        }

        try {
            // 사용자 입력을 인스턴스로 변환
            Instance userInstance = createUserInstance(userAttributes);

            // 분류 수행
            double[] distribution = classifier.distributionForInstance(userInstance);

            // 확률 순으로 정렬하여 추천 목록 생성
            for (int i = 0; i < distribution.length; i++) {
                String petType = trainingData.classAttribute().value(i);
                double probability = distribution[i] * 100;

                if (probability > 10) { // 10% 이상인 경우만 추천
                    PetRecommendation rec = new PetRecommendation();
                    rec.setPetType(petType);
                    rec.setConfidence(probability);
                    rec.setReason(generateReason(petType, userAttributes));
                    recommendations.add(rec);
                }
            }

            // 확률 내림차순 정렬
            recommendations.sort((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));

        } catch (Exception e) {
            e.printStackTrace();
            return getFallbackRecommendations(userAttributes);
        }

        return recommendations.isEmpty() ? getFallbackRecommendations(userAttributes) : recommendations;
    }

    /**
     * 사용자 속성을 Weka 인스턴스로 변환
     */
    private Instance createUserInstance(Map<String, Object> attrs) {
        Instance inst = new DenseInstance(7);
        inst.setDataset(trainingData);

        inst.setValue(0, Double.parseDouble(attrs.get("homeHours").toString()));
        inst.setValue(1, attrs.get("housing").toString());
        inst.setValue(2, attrs.get("budget").toString());
        inst.setValue(3, attrs.get("activityLevel").toString());
        inst.setValue(4, attrs.get("allergy").toString());
        inst.setValue(5, attrs.get("experience").toString());
        inst.setMissing(6); // 클래스는 예측할 값

        return inst;
    }

    /**
     * 추천 이유 생성
     */
    private String generateReason(String petType, Map<String, Object> attrs) {
        switch (petType) {
            case "FISH":
                return "조용하고 관리가 쉬워 바쁜 생활에 적합합니다.";
            case "DOGS":
                return "활동적이고 충분한 공간과 시간이 있어 강아지 양육에 적합합니다.";
            case "CATS":
                return "독립적이면서도 애교가 많아 당신의 라이프스타일에 잘 맞습니다.";
            case "BIRDS":
                return "작은 공간에서도 키울 수 있고 소리로 교감할 수 있습니다.";
            case "REPTILES":
                return "알레르기 걱정이 없고 조용하며 독특한 매력이 있습니다.";
            default:
                return "당신에게 적합한 반려동물입니다.";
        }
    }

    /**
     * 모델 실패 시 폴백 추천
     */
    private List<PetRecommendation> getFallbackRecommendations(Map<String, Object> attrs) {
        List<PetRecommendation> recommendations = new ArrayList<>();

        // 간단한 규칙 기반 추천
        double homeHours = Double.parseDouble(attrs.get("homeHours").toString());

        if (homeHours < 4) {
            recommendations.add(createRecommendation("FISH", 80, "재택 시간이 적어 물고기가 적합합니다."));
            recommendations.add(createRecommendation("CATS", 60, "고양이는 독립적이어서 혼자 있어도 괜찮습니다."));
        } else if (homeHours > 6) {
            recommendations.add(createRecommendation("DOGS", 85, "충분한 시간이 있어 강아지를 잘 돌볼 수 있습니다."));
            recommendations.add(createRecommendation("CATS", 70, "고양이도 좋은 선택입니다."));
        } else {
            recommendations.add(createRecommendation("CATS", 75, "균형잡힌 라이프스타일에 고양이가 적합합니다."));
            recommendations.add(createRecommendation("BIRDS", 60, "새도 좋은 반려동물입니다."));
        }

        return recommendations;
    }

    private PetRecommendation createRecommendation(String type, double conf, String reason) {
        PetRecommendation rec = new PetRecommendation();
        rec.setPetType(type);
        rec.setConfidence(conf);
        rec.setReason(reason);
        return rec;
    }

    /**
     * 추천 결과 클래스
     */
    public static class PetRecommendation {
        private String petType;
        private double confidence;
        private String reason;

        public String getPetType() {
            return petType;
        }

        public void setPetType(String petType) {
            this.petType = petType;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getPetTypeName() {
            switch (petType) {
                case "FISH": return "물고기";
                case "DOGS": return "강아지";
                case "CATS": return "고양이";
                case "BIRDS": return "새";
                case "REPTILES": return "파충류";
                default: return petType;
            }
        }
    }
}
