__author__ = 'jhughes'
import cv2
import numpy as np
import getopt
import sys
import random
from matplotlib import pyplot as plt


#
# Read in an image file, errors out if we can't find the file
#
def readImage(filename):
    img = cv2.imread(filename, 0)
    if img is None:
        print('Invalid image:' + filename)
        return None
    else:
        print('Image successfully read...')
        return img

#
# Main parses argument list and runs the functions
#
def main():
    estimation_thresh = 0.60
    MIN_MATCH_COUNT = 10


    img1name ="example.jpg"
    img2name ="OUTPUT.jpg"

    #query image
    img1 = readImage("./Images/example.jpg")
    #train image
    img2 = readImage("./Images/OUTPUT3.jpg")

    #find features and keypoints
    # Initiate SIFT detector
    sift = cv2.xfeatures2d.SIFT_create()
    # find the keypoints and descriptors with SIFT
    kp1, des1 = sift.detectAndCompute(img1,None)
    kp2, des2 = sift.detectAndCompute(img2,None)
    
    #img1 = cv2.drawKeypoints(img1,kp1,img1,color=(0,255,0))
    #img2 = cv2.drawKeypoints(img2,kp2,img2,color=(0,255,0))
  

    #cv2.imwrite('sift_keypoints1.png', img1)
    #cv2.imwrite('sift_keypoints2.png', img2)    


    FLANN_INDEX_KDTREE = 0
    index_params = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
    search_params = dict(checks = 50)
    flann = cv2.FlannBasedMatcher(index_params, search_params)
    matches = flann.knnMatch(des1,des2,k=2)
    # store all the good matches as per Lowe's ratio test.
    good = []
    for m,n in matches:
    	if m.distance < 0.7*n.distance:
            good.append(m)


    if len(good)>MIN_MATCH_COUNT:
        src_pts = np.float32([ kp1[m.queryIdx].pt for m in good ]).reshape(-1,1,2)
        dst_pts = np.float32([ kp2[m.trainIdx].pt for m in good ]).reshape(-1,1,2)

        M, mask = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC,5.0)
        matchesMask = mask.ravel().tolist()

        h,w = img1.shape
        pts = np.float32([ [0,0],[0,h-1],[w-1,h-1],[w-1,0] ]).reshape(-1,1,2)
        dst = cv2.perspectiveTransform(pts,M)

        img2 = cv2.polylines(img2,[np.int32(dst)],True,255,3, cv2.LINE_AA)

    else:
        print "Not enough matches are found - %d/%d" % (len(good),MIN_MATCH_COUNT)
        matchesMask = None
 
    draw_params = dict(matchColor = (0,255,0), # draw matches in green color
                   singlePointColor = None,
                   matchesMask = matchesMask, # draw only inliers
                   flags = 2)

    img3 = cv2.drawMatches(img1,kp1,img2,kp2,good,None,**draw_params)
    cv2.imwrite('result.png', img3)
    plt.imshow(img3, 'gray')
    plt.show()  

if __name__ == "__main__":
    main()
