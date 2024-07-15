package org.example.DTO;

public class RecommendedDTO {
    public String MealType;
    public int NumberOfItems;

    public RecommendedDTO() {
    }

    public RecommendedDTO(String mealType, int numberOfItems) {
        MealType = mealType;
        NumberOfItems = numberOfItems;
    }

    public String getMealType() {
        return MealType;
    }

    public void setMealType(String mealType) {
        MealType = mealType;
    }

    public int getNumberOfItems() {
        return NumberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        NumberOfItems = numberOfItems;
    }
}

