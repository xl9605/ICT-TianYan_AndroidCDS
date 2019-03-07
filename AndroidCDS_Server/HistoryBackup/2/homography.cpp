#include <opencv2/opencv.hpp>
#include "opencv2/xfeatures2d.hpp"
#include "opencv2/stitching.hpp"
#include <algorithm>
#include <iostream>
#include <string>
#include<time.h>
using namespace cv;
using namespace std;


Mat warp_crops(String path)
{
        clock_t start = clock();
        int width = 640 * 2;
	int height = 800 * 2;

        Mat im_1 = imread("example.jpg");
        Mat im_2 = imread(path);
        
        clock_t finish1 = clock();
        double consumeTime1 = (double)(finish1-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"图片读取时间："<< consumeTime1 << endl;

        resize(im_1, im_1, Size(width,height));	
	resize(im_2, im_2, Size(width,height));
        
        clock_t finish2 = clock();
        double consumeTime2 = (double)(finish2-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"图片放大或缩小时间："<< consumeTime2 << endl;

	cv::Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();


	// Step 1: Detect the keypoints:
	std::vector<KeyPoint> keypoints_1, keypoints_2;
	f2d->detect( im_1, keypoints_1 );
	f2d->detect( im_2, keypoints_2 );
        clock_t finish5 = clock();
        double consumeTime5 = (double)(finish5-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"找出特征值："<< consumeTime5 << endl;

	// Step 2: Calculate descriptors (feature vectors)
	Mat descriptors_1, descriptors_2;
	f2d->compute( im_1, keypoints_1, descriptors_1 );
	f2d->compute( im_2, keypoints_2, descriptors_2 );

        clock_t finish4 = clock();
        double consumeTime4 = (double)(finish4-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"计算特征值："<< consumeTime4 << endl;

	// Step 3: Matching descriptor vectors using BFMatcher :
	BFMatcher matcher;
	std::vector< DMatch > matches;
	matcher.match( descriptors_1, descriptors_2, matches );

	// Keep best matches only to have a nice drawing.
	// We sort distance between descriptor matches
	Mat index;
	int nbMatch = int(matches.size());
	Mat tab(nbMatch, 1, CV_32F);
	for (int i = 0; i < nbMatch; i++)
		tab.at<float>(i, 0) = matches[i].distance;
	sortIdx(tab, index, SORT_EVERY_COLUMN + SORT_ASCENDING);
	vector<DMatch> bestMatches;

	for (int i = 0; i < 200; i++)
		bestMatches.push_back(matches[index.at < int > (i, 0)]);

      

	// 1st image is the destination image and the 2nd image is the src image
	std::vector<Point2f> dst_pts;                   //1st
	std::vector<Point2f> source_pts;                //2nd

	for (vector<DMatch>::iterator it = bestMatches.begin(); it != bestMatches.end(); ++it) {
		//cout << it->queryIdx << "\t" <<  it->trainIdx << "\t"  <<  it->distance << "\n";
		//-- Get the keypoints from the good matches
		dst_pts.push_back( keypoints_1[ it->queryIdx ].pt );
		source_pts.push_back( keypoints_2[ it->trainIdx ].pt );
	}


	/* Mat img_matches;
	 drawMatches( im_1, keypoints_1, im_2, keypoints_2,
	           bestMatches, img_matches, Scalar::all(-1), Scalar::all(-1),
	           vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS );*/
	
	 //-- Show detected matches
	// imwrite( "Good_Matches.jpg", img_matches );



	Mat H = findHomography( source_pts, dst_pts, CV_RANSAC );
	//cout << H << endl;

	Mat wim_2;
	warpPerspective(im_2, wim_2, H, im_1.size());
        

        Mat wim_3(im_1.size(),CV_8UC3);
        Mat wim_4(im_1.size(),CV_8UC3);
    
  
        for (int i = 0; i < im_1.cols; i++)
		for (int j = 0; j < im_1.rows; j++) {
			Vec3b color_im1 = im_1.at<Vec3b>(Point(i, j));
			Vec3b color_im2 = wim_2.at<Vec3b>(Point(i, j));
			if (norm(color_im1) != 0)
			{  
                           wim_3.at<Vec3b>(Point(i, j)) = color_im2;
                           wim_4.at<Vec3b>(Point(i, j)) = color_im2;
                        }

		}
     //   addWeighted(wim_3,0.5,im_1,0.5,0.0,wim_3);
       // imwrite("./Image/result7_btm.png", wim_3);
//	waitKey(0);
       
        addWeighted(wim_3,1,im_1,0,0.0,wim_3);
        addWeighted(wim_3,0.5,im_1,0.5,0.0,wim_4);
	//waitKey(0);
        imwrite("./otherImages/result.jpg", wim_3);
        imwrite("./otherImages/result_btm.jpg", wim_4);
        
        clock_t finish3 = clock();
        double consumeTime3 = (double)(finish3-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"其他时间："<< consumeTime3 << endl;
     return wim_3;
}


void getMat(Mat& im,int i,int x,int y,int cols,int rows)
{

    std::string path="./Images/"+std::to_string(i)+".png";
    Rect rect(x,y,cols,rows);
    Mat image=im(rect);
    imwrite(path, image);
}


void getData(Mat& im)
{
        clock_t start = clock();
	getMat(im,1,105,148,476,72);       //1.单位
	getMat(im,2,725,148,458,72);       //2.编号
        getMat(im,3,166,233,136,53);       //3.发令人
        getMat(im,4,399,233,182,53);       //4.受令人
        getMat(im,5,684,233,499,53);       //5.发令时间
        getMat(im,6,207,286,374,56);       //6.操作开始时间
        getMat(im,7,730,286,453,56);       //7.操作结束时间
        getMat(im,8,58,342,85,54);         //8.监护下操作
        getMat(im,9,495,342,82,54);        //9.单人操作
        getMat(im,10,852,342,81,54);       //10.检修人员操作
        getMat(im,11,58,396,1125,123);     //11.操作任务
        getMat(im,12,134,574,950,51);      //12.1-操作项目
        getMat(im,13,1084,574,99,51);      //13.1-是否完成
        getMat(im,14,134,625,950,50);      //14.2-操作项目
        getMat(im,15,1084,625,99,50);      //15.2-是否完成
        getMat(im,16,134,675,950,53);      //16.3-操作项目
        getMat(im,17,1084,675,99,53);      //17.3-是否完成
        getMat(im,18,134,728,950,54);      //18.4-操作项目
        getMat(im,19,1084,728,99,54);      //19.4-是否完成
        getMat(im,20,134,782,950,54);      //20.5-操作项目
        getMat(im,21,1084,782,99,54);      //21.5-是否完成
        getMat(im,22,134,836,950,52);      //22.6-操作项目
        getMat(im,23,1084,836,99,52);      //23.6-是否完成
        getMat(im,24,134,888,950,50);      //24.7-操作项目
        getMat(im,25,1084,888,99,50);      //25.7-是否完成
        getMat(im,26,134,938,950,54);      //26.8-操作项目
        getMat(im,27,1084,938,99,54);      //27.8-是否完成
        getMat(im,28,134,992,950,50);      //28.9-操作项目
        getMat(im,29,1084,992,99,50);      //29.9-是否完成
        getMat(im,30,134,1042,950,56);     //30.10-操作项目
        getMat(im,31,1084,1042,99,56);     //31.10-是否完成
        getMat(im,32,134,1098,950,50);     //32.11-操作项目
        getMat(im,33,1084,1098,99,50);     //33.11-是否完成
        getMat(im,34,134,1148,950,55);     //34.12-操作项目
        getMat(im,35,1084,1148,99,55);     //35.12-是否完成
        getMat(im,36,134,1203,950,50);     //36.13-操作项目
        getMat(im,37,1084,1203,99,50);     //37.13-是否完成
        getMat(im,38,134,1253,950,56);     //38.14-操作项目
        getMat(im,39,1084,1253,99,56);     //39.14-是否完成
        getMat(im,40,134,1309,950,55);     //40.15-操作项目
        getMat(im,41,1084,1309,99,55);     //41.15-是否完成
        getMat(im,42,129,1364,1054,48);    //42.备注
        getMat(im,43,146,1412,188,52);     //43.操作人
        getMat(im,44,421,1412,267,52);     //44.监护人
        getMat(im,45,896,1412,287,52);     //45.值班负责人（值长）
        clock_t finish = clock();
        double consumeTime = (double)(finish-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"裁减用时："<< consumeTime << endl;
     }




int main(int argc,char **argv)
{
        clock_t start = clock();
        String a=argv[1]; 
        //cout << a << endl;
	Mat im_3=warp_crops(a);
        getData(im_3);
        clock_t finish = clock();
        double consumeTime = (double)(finish-start)/CLOCKS_PER_SEC;//注意转换为double的位置
        cout <<"总共用时："<< consumeTime << endl;
}
