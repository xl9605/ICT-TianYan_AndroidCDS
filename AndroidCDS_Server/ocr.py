import subprocess
import os
main = "./homography"
try:
    import Image
except ImportError:
    from PIL import Image
import pytesseract

import cv2

#img = cv2.imread(r'3.jpg')
img = cv2.imread(r'/home/humanmotion/2_jian.png')
#img = cv2.imread(r'test.png')

# w = img.shape[1]
# h = img.shape[2]

# persent = w/1000

# cv2.resize(img, (int(w//persent), int(h//persent)), interpolation=cv2.INTER_CUBIC)

# gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
print(pytesseract.image_to_string(img, lang='chi_sim'))
