package ict.ac.humanmotion.uapplication.lpmsbs.hworkadapter;


import ict.ac.humanmotion.uapplication.lpmsbs.model.WorkListDetail;

/**
 * Created by hbh on 2017/4/20.
 * 父布局Item点击监听接口
 */

public interface ItemClickListener {
    /**
     * 展开子Item
     * @param bean
     */
    void onExpandChildren(WorkListDetail bean);

    /**
     * 隐藏子Item
     * @param bean
     */
    void onHideChildren(WorkListDetail bean);
}
