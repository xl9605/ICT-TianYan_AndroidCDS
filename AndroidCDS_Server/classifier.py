import pandas as pd
import numpy as np
from sklearn.neural_network import MLPClassifier

clf = MLPClassifier(hidden_layer_sizes=(100,),
                    activation='logistic', solver='adam',
                    learning_rate_init=0.0001, max_iter=2000)

iris = pd.read_csv("train.csv", header=None)
# print (iris.head())
X = iris.iloc[:, 0:120]
y = iris.iloc[:, -1:]

clf.fit(X, y)


# # print (clf.score())

from sklearn.externals import joblib
joblib.dump(clf, "train_model.m")

# pre_y = clf.predict(X[3100:3208])
# print(pre_y)
# print(y[5:10])
# import numpy as np
# test = np.array([[5.1,2.9,1.8,3.6]])
# #对test进行预测
# test_y = clf.predict(test)
# print(test_y)

# from sklearn.externals import joblib
# clf = joblib.load("train_model.m")

# from sklearn.metrics import accuracy_score
# y_pred = clf.predict(X)
# print (accuracy_score(y_true=y, y_pred=y_pred))