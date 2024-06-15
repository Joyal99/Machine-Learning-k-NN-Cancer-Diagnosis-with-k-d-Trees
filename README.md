 # **Machine Learning Project for Data Structures and Algorithms**

## Overview 
This project implements a machine learning solution for cancer diagnosis as either malignant or benign using the k-nearest neighbors (k-NN) algorithm. using the k-nearest neighbors (k-NN) algorithm and k-d trees. 
It analyzes the [Breast Cancer Wisconsin dataset](https://www.kaggle.com/datasets/uciml/breast-cancer-wisconsin-data?resource=download), which has 569 instances of data, each with various attributes relevant to the diagnosis. The focus of this project is to optimize both the accuracy and efficiency of the k-NN algorithm using two tree data structures: KD-Tree and Ball Tree. The dataset is found in the "data.csv"

## Identifing the problem
Given a dataset with 569 instances, each representing a patient's cell information (either cancerous or non-cancerous), the objective is to design, implement, and evaluate a machine learning model that classifies the diagnosis accurately and efficiently. The k-NN algorithm, known for its simplicity and effectiveness, is used in conjunction with KD-Tree and Ball Tree Data structures to enhance Performance.

## Important Info regarding the project
### KD Tree
A binary tree that partitions data points by recursively splitting them along the dimension with the greatest variance.
Used to store the training subset and perform efficient nearest neighbor searches.

### Ball Tree
A hierarchical data structure that recursively divides data points into clusters (balls), optimizing for the k-NN search.
Each node represents a hypersphere containing points, with child nodes representing subclusters.

### K-Nearest Neighbors (k-NN) Algorithm:
Classifies a point based on the majority label of its k-nearest neighbors.
Implemented using both KD-Tree and Ball Tree for efficient neighbor searches.

## Evaluation
The model's performance is evaluated based on:
1. **Accuracy**: The percentage of correctly predicted diagnoses.
2. **Running Time**: The execution time for testing, not training.

## Results
- The results include figures showing how running time and testing accuracy change as a function of the number of training instances (N) for different values of k (1, 5, 7).
- A comparison between KD-Tree and Ball Tree implementations highlights their relative performance in terms of accuracy and efficiency.

## Usage Instructions
1. **Data Preparation**: Load the dataset and preprocess it into training and testing subsets.
2. **Model Training and Testing**: Train the model using KD-Tree and Ball Tree structures, then evaluate its accuracy and running time on the test set.
3. **Comparison**: Analyze and compare the results of KD-Tree and Ball Tree to determine the best approach for this classification problem.

## Conclusion
This project demonstrates the application of KD-Tree and Ball Tree structures to improve the efficiency of the k-NN algorithm in classifying breast cancer diagnoses. The results provide insights into the trade-offs between accuracy and computational efficiency, guiding the choice of data structure for similar machine learning tasks.

---


![ai-technology-brain-background-digital-transformation-concept_53876-125206](https://github.com/Joyal99/Machine-Learning-k-NN-Cancer-Diagnosis-with-k-d-Trees/assets/122915447/b963d334-d620-41f1-8fe4-9e5d4b72bf31)


A project completed with [@KevinMandiouba] (https://github.com/KevinMandiouba)
