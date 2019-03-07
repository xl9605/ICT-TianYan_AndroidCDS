package ict.ac.humanmotion.uapplication.lpmsbs.ui

import android.support.v4.app.Fragment
import ict.ac.humanmotion.uapplication.lpmsbs.lpms.ImuStatus
import ict.ac.humanmotion.uapplication.lpmsbs.lpms.LpmsBData

abstract class MyFragment : Fragment() {

    abstract var myFragmentTag: Int

    interface MyFragmentListener {
        fun onUserInput(mode: Int, data: String)
    }

    abstract fun updateView(d: LpmsBData, s: ImuStatus)

//    abstract fun getMyFragmentTag():Int
}
