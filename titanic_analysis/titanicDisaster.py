# -*- coding: utf-8 -*-
"""
Kaggle competition - Titanic Disaster
@author: Tristin Glunt
"""
import sys
sys.path.insert(0, '../../tf_nn') #to use neural network, must have tf_nnet.py from MachineLearningAlgs repo

import tf_nnet as nnet
import math
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
import pandas as pd

tf.reset_default_graph();

def import_data():
    dataset = pd.read_csv('train.csv')
    X = dataset.iloc[:, [2,3,4,5,6,7,9]].values
    y = dataset.iloc[:, 1].values

    testSet = pd.read_csv('test.csv')
    testData = testSet.iloc[:, [1,2,3,4,5,6,8]].values
    return X, y, testData

from sklearn.preprocessing import Imputer
def replace_missing_data_mean(X, testData):
    imputer = Imputer(missing_values = 'NaN', strategy = 'mean', axis=0)
    imputer = imputer.fit(X[:, [3]])
    X[:, [3]] = imputer.transform(X[:,[3]]) #replace missing data by mean of column

    imputerTest = imputer.fit(testData[:, [3, 6]])
    testData[:, [3, 6]] = imputerTest.transform(testData[:, [3, 6]])

    return X, testData

#replace names with their titles, 0 for Mr, 1 for Miss, 2 for Mrs, 3 for master
def replace_titles(X, testData):
    for i in range(0, X.shape[0]):
        if("Mr" in X[i, 1]):
            X[i, 1] = 0
        elif("Miss" in X[i, 1]):
            X[i, 1] = 1
        elif("Mrs" in X[i, 1]):
            X[i, 1] = 2
        else:
            X[i, 1] = 3

    for i in range(0, testData.shape[0]):
        if("Mr" in testData[i, 1]):
            testData[i, 1] = 0
        elif("Miss" in testData[i, 1]):
            testData[i, 1] = 1
        elif("Mrs" in testData[i, 1]):
            testData[i, 1] = 2
        else:
            testData[i, 1] = 3

    return X, testData

#replace sibling and parents column with alone = 0, not alone = 1
def find_families(X, testData):
    for i in range(0, X.shape[0]):
        if(X[i, 4] + X[i, 5] > 0):
            X[i, 4] = 1
        else:
            X[i, 4] = 0

    for i in range(0, testData.shape[0]):
        if(testData[i, 4] + testData[i, 5] > 0):
            testData[i, 4] = 1
        else:
            testData[i, 4] = 0

    X = X[:, [0, 1, 2, 3, 4, 6]]                    #get rid of extra column
    testData = testData[:, [0, 1, 2, 3, 4, 6]]

    return X, testData

X, y, testData = import_data()
X, testData = replace_missing_data_mean(X, testData)
X, testData = replace_titles(X, testData)
X, testData = find_families(X, testData)

#categorize values that aren't numeric
from sklearn.preprocessing import OneHotEncoder, LabelEncoder
labelEncoder = LabelEncoder()
X[:, 2] = labelEncoder.fit_transform(X[:, 2])

oneHotEncoder = OneHotEncoder(categorical_features = [0, 1, 2])
X = oneHotEncoder.fit_transform(X).toarray() #update matrix X

labelEncoder = LabelEncoder()
testData[:, 2] = labelEncoder.fit_transform(testData[:, 2])

oneHotEncoder = OneHotEncoder(categorical_features = [0, 1, 2])
testData = oneHotEncoder.fit_transform(testData).toarray()

#feature scaling - scale the data to be numerically closer so one var doesn't dominate
from sklearn.preprocessing import StandardScaler
sc_X = StandardScaler()
X[:, [8, 9]] = sc_X.fit_transform(X[:, [8, 9]]) #we fit x_train to the object first so our test uses the same
testData[:, [8, 9]] = sc_X.fit_transform(testData[:, [8, 9]])             #scale as X_train has

#splitting the dataset into the training set and test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.2, random_state = 0)

#transpose data to fit learning models
y_train = np.transpose(y_train.reshape(-1,1))
y_test = np.transpose(y_test.reshape(-1,1))

X_train = np.transpose(X_train)
X_test = np.transpose(X_test)
testData = np.transpose(testData)

""" feed model to tensorflow neural network """

layer_dims = [11, 32, 16, 8, 4, 1]              #define nnet_dimensions of neural network, first item is number of inputs
                                                #last item is the number of outputs

parameters = nnet.model(X_train, y_train, X_test, y_test, layer_dims, minibatch_size = 2)
predictions = nnet.predictSurvivors(X_test, parameters, len(layer_dims))

""" Dtermine accuracy of neural network """
rounded_predictions = []
for i in range(0, X_test.shape[1]):
    if(predictions[0, i] > 0.49):
        rounded_predictions.append(1)
    else:
        rounded_predictions.append(0)

accuracy = 0
for i in range(0, y_test.shape[1]):
    if(y_test[0, i] == rounded_predictions[i]):
        accuracy += 1

accuracy = accuracy / y_test.shape[1]
print("X_test Accuracy: " + str(accuracy))

#save data straight to excel file
#my_submission=pd.DataFrame({'PassengerId': testSet.PassengerId, 'Survived': rounded_predictions})
#my_submission.to_csv('submission.csv', index=False)

""" Use Sklearn library to get a good comparison to the neural network performance """
from sklearn.ensemble import RandomForestClassifier
rfc = RandomForestClassifier(n_estimators = 300, random_state = 0)
rfc.fit(np.transpose(X_train), np.ravel(np.transpose(y_train)))

rfc_prediction = rfc.predict(np.transpose(X_test))
sklearn_accuracy = 0
for i in range(0, y_test.shape[1]):
    if(y_test[0, i] == rfc_prediction[i]):
        sklearn_accuracy += 1
sklearn_accuracy = sklearn_accuracy / y_test.shape[1]

print("RandomForestClassifier accuracy: " + str(sklearn_accuracy))
rfc.score(np.transpose(X_train), np.ravel(np.transpose(y_train)))
