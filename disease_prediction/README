# Chronic Disease Prediction

This project is focused on predicting chronic illness diagnoses using data analysis and machine learning techniques. The dataset contains various features related to health, demographic, and lifestyle factors, which are used to predict whether individuals are diagnosed with a chronic condition.

---

## Task

The objective is to classify members into one of the following categories:

1. **Yes**: The member has been diagnosed with a chronic condition.
2. **Yes, but only during pregnancy**: The member has been diagnosed with a chronic condition, but it was reported only during pregnancy.
3. **No**: The member has not been diagnosed with a chronic condition.

---

## Key Steps in the Project

### 1. Data Manipulation
- The data was initially loaded and inspected to understand its structure.
- Various missing values were handled, and ambiguous data points were removed for cleaner analysis.

### 2. Feature Standardization
- Numerical features such as height and weight were converted to consistent units (e.g., centimeters, kilograms).
- Outliers were removed to maintain the integrity of the dataset.

### 3. Exploratory Data Analysis (EDA)
- The distribution of chronic conditions across features like age and exercise status was explored.
- A correlation matrix was generated to identify highly correlated features, and unnecessary features were removed.

### 4. Model Selection
- **XGBoost** was selected as the primary model due to its ability to handle classification problems effectively.
- SMOTE (Synthetic Minority Over-sampling Technique) was applied to balance the dataset and improve model performance.

### 5. Model Optimization
- Hyperparameter tuning was performed using **GridSearchCV** with K-Fold cross-validation to prevent overfitting and find the best parameters for the XGBoost model.
- The final model achieved an accuracy of **92%** after optimization.

### 6. Test Set Predictions
- Predictions were made on the test set, and the model's performance was evaluated using confusion matrices and classification reports.

---

## Insights and Results

### 1. Feature Importances
- The most important features impacting chronic illness were identified as:
  - **General Health**
  - **Sex**
  - **BMI Category**
  - **Age**
- These features were critical in predicting chronic conditions and provided key insights into factors associated with chronic illness.

### 2. Impact of Exercise
- A significant trend was observed where individuals who exercise regularly had a lower proportion of chronic illness diagnoses compared to those who exercised less or were inactive.
- **Regular exercise** plays a pivotal role in reducing the risk of chronic illness.

### 3. BMI and Chronic Conditions
- Higher BMI categories (Overweight, Obese) were strongly associated with an increased proportion of chronic illness diagnoses.

### 4. Age and Chronic Conditions
- As expected, the incidence of chronic illness increased with age, especially in older age groups.

---

## Final Model Performance

- **Accuracy**: 92%
- **Best Model**: XGBoost with the following parameters:
  - Learning Rate: 0.2
  - Max Depth: 5
  - Number of Estimators: 100
- **Feature Importances**: 
  - General Health, Sex, and BMI Category were among the top features driving the predictions.

---

## Visualizations

1. **Proportion of Chronic Conditions Across General Health Categories**
   - A clear link between reported general health and chronic illness diagnoses was observed.

2. **Prevalence of Chronic Conditions by Age Group**
   - The proportion of chronic illness diagnoses increased with age.

3. **Chronic Conditions and Exercise**
   - Individuals who exercised regularly were less likely to be diagnosed with chronic conditions compared to those who exercised less or not at all.

4. **Chronic Conditions Across BMI Categories**
   - A higher BMI was associated with a higher proportion of chronic illness diagnoses.

---

## Appendix

- **References**:
  - Park, D.J., et al. *Development of machine learning model for diagnostic disease prediction based on laboratory tests*. Sci Rep 11, 7567 (2021).
  - Lee, C., et al. *Chronic disease prediction using the common data model*. Artificial Intelligence in Medicine, 1(1), e41030 (2024).
  - Chawla, N.V., et al. *SMOTE: Synthetic Minority Over-sampling Technique*. Journal of Artificial Intelligence Research, 16, 321-357 (2002). 

---

This project highlights the strong correlation between general health, BMI, and exercise habits in predicting chronic illness, providing valuable insights for healthcare analysis and preventive measures.
