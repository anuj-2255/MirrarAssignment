package com.example.testassignment.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
//import com.dropgrub.utils.SystemConfig
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class TranslucentBarManager {
    private var mToolbar: Toolbar? = null
    private var mRoot: ViewGroup? = null
    private var mCollapsingToolbarLayout: CollapsingToolbarLayout? = null

    //window flags
    private var mTranslucentStatusBar = false

    //a tag mark whether have view would show behind status bar or not
    private var mIsNeedOverStatusBar = false

    //mStatusBarView will put the place behind status bar and it to be a child of ContentFrameLayout(android:id/content)
    private var mStatusBarView: View? = null
    private var mSystemConfig: SystemConfig

    constructor(activity: Activity) {
        val win = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            val attrs = intArrayOf(
                R.attr.windowTranslucentStatus,
                R.attr.windowTranslucentNavigation
            )
            val a = activity.obtainStyledAttributes(attrs)
            mTranslucentStatusBar = try {
                a.getBoolean(0, false)
            } finally {
                a.recycle()
            }

            // check window flags
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (winParams.flags and bits != 0) {
                mTranslucentStatusBar = true
            }
        }
        mSystemConfig = SystemConfig(activity, mTranslucentStatusBar)
        mRoot = activity.window.decorView.findViewById<View>(R.id.content) as ViewGroup
        findToolbar(mRoot)
        findCollapsingToolbarLayout(mRoot)
    }

    constructor(fragment: Fragment) {
        val win = fragment.requireActivity().window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            val attrs = intArrayOf(
                R.attr.windowTranslucentStatus,
                R.attr.windowTranslucentNavigation
            )
            val a = fragment.requireActivity().obtainStyledAttributes(attrs)
            mTranslucentStatusBar = try {
                a.getBoolean(0, false)
            } finally {
                a.recycle()
            }

            // check window flags
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (winParams.flags and bits != 0) {
                mTranslucentStatusBar = true
            }
        }
        mSystemConfig = SystemConfig(fragment.requireActivity(), mTranslucentStatusBar)

        //remove view behind status bar when fragment may be has change,
        val decorViewGroup = fragment.requireActivity().window.decorView
            .findViewById<View>(R.id.content) as ViewGroup
        for (i in 0 until decorViewGroup.childCount) {
            val tag = decorViewGroup.getChildAt(i).tag
            if (tag != null && tag is String && tag.toString() == TAG_STATUS_BAR_VIEW) {
                decorViewGroup.removeViewAt(i)
                break
            }
        }
    }

    /**
     * tint toolbar and status to specified color
     *
     * @param activity activity
     * @param color    color to tint status bar and toolbar
     */
    private fun tintColor(activity: Activity?, @ColorRes color: Int) {
        if (!mIsNeedOverStatusBar) {
            val contentLayout =
                activity!!.window.decorView.findViewById<View>(R.id.content) as ViewGroup
            setupStatusBarView(activity, contentLayout)
            mStatusBarView!!.setBackgroundResource(color)
            mStatusBarView!!.visibility = View.VISIBLE
            mToolbar!!.setBackgroundResource(color)
        } else {
            mCollapsingToolbarLayout!!.setContentScrimResource(color)
            mCollapsingToolbarLayout!!.setStatusBarScrimResource(color)
        }
    }

    /**
     * make status bar translucent and tint toolbar and status to specified color
     * this method must call when onCreateView in fragment to take effect.
     *
     * @param fragment fragment
     * @param root     the root view of fragment
     * @param color    color to tint status bar and toolbar
     */
    fun translucent(fragment: Fragment, root: View?, @ColorRes color: Int) {
        mRoot = root as ViewGroup?
        findToolbar(mRoot)
        findCollapsingToolbarLayout(mRoot)
        translucent(fragment.activity, color)
        addStatusBarShade(fragment.activity)
    }

    /**
     * make status bar translucent and tint toolbar and status to specified color
     *
     * @param activity activity
     * @param color    toolbar and status bar would be tinted this color
     */
    @SuppressLint("ResourceType")
    fun translucent(activity: Activity?, @ColorRes color: Int) {
        if (!mTranslucentStatusBar || mToolbar == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setFitsSystemWindows(mRoot, true)
        }
        if (color <= 0) {
            val tv = TypedValue()
            activity!!.theme.resolveAttribute(R.attr.colorPrimary, tv, true)
            tintColor(activity, tv.resourceId)
        } else {
            tintColor(activity, color)
        }
    }

    /**
     * make status bar translucent and tint color,if you don't set any colors,toolbar would be tinted with @color/colorPrimary
     * and status bar would be tinted with @color/colorPrimaryDark
     *
     * @param activity acivity
     */
    fun translucent(activity: Activity?) {
        translucent(activity, -1)
    }

    /**
     * when set windowTranslucentStatus true, system will add translucent gray shade above of status bar,this is
     * standard style of material design，but some times，you may want to remove the shade, for example: if the
     * color of ActionBar is white and now show a gray shape above of white status bar is so ugly exactly！
     * make status bar transparent,if you don't set colors,toolbar would be tinted whit @color/colorPrimary
     * and status bar would be tinted with @color/colorPrimaryDark
     *
     * @param activity activity
     * @param color    color to tint status bar and toolbar
     */
    fun transparent(activity: Activity?, @ColorRes color: Int) {
        translucent(activity, color)
        removeStatusBarShade(activity)
    }

    fun transparent(activity: Activity, pIsDark: Boolean) {
        translucent(activity, -1)
        removeStatusBarShade(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val lFlags = activity.window.decorView.systemUiVisibility
            activity.window.decorView.systemUiVisibility =
                if (pIsDark) lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    /**
     * when set windowTranslucentStatus true, system will add translucent gray shade above of status bar,this is
     * standard style of material design，but some times，you may want to remove the shade, for example: if the
     * color of ActionBar is white and now show a gray shape above of white status bar is so ugly exactly！
     * make status bar transparent,if you don't set colors,toolbar would be tinted with @color/colorPrimary
     * and status bar would be tinted with @color/colorPrimaryDark
     *
     * @param activity activity
     */
    fun transparent(activity: Activity?) {
        translucent(activity, -1)
        removeStatusBarShade(activity)
    }

    /**
     * make status bar transparent and tint toolbar and status to specified color
     * this method must call when onCreateView in fragment to take effect.
     *
     * @param fragment fragment
     * @param root     the root view of fragment
     * @param color    color to tint status bar and toolbar
     */
    fun transparent(fragment: Fragment, root: View?, @ColorRes color: Int) {
        mRoot = root as ViewGroup?
        findToolbar(mRoot)
        findCollapsingToolbarLayout(mRoot)
        translucent(fragment.activity, color)
        removeStatusBarShade(fragment.activity)
    }

    /**
     * contrary to [TranslucentBarManager.transparent]  }
     *
     * @param activity
     */
    private fun removeStatusBarShade(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity!!.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * @param activity
     */
    private fun addStatusBarShade(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity!!.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * @param context            Context to create view
     * @param contentFrameLayout child of DecorView,get by decorView.findViewById(android.R.id.content)
     */
    private fun setupStatusBarView(context: Context?, contentFrameLayout: ViewGroup) {
        mStatusBarView = View(context)
        //set tag to mark view,Chances are mStatusBarView will be remove.
        mStatusBarView!!.tag = TAG_STATUS_BAR_VIEW
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            mSystemConfig.statusBarHeight
        )
        params.gravity = Gravity.TOP
        mStatusBarView!!.layoutParams = params
        mStatusBarView!!.visibility = View.GONE
        contentFrameLayout.addView(mStatusBarView)
    }

    /**
     * Recursive process view tree to setFitsSystemWindows（true or false）until find specify views
     *
     * @param view root view
     * @param b    setFitsSystemWindows（true or false）
     */
    private fun setFitsSystemWindows(view: View?, b: Boolean) {
        view!!.fitsSystemWindows = b
        if (view is ViewGroup) {
            val viewGroup = view
            if (viewGroup.childCount > 0) {
                val child = viewGroup.getChildAt(0)
                // if find CollapsingToolbarLayout then return,the deeper view setFitsSystemWindows is not work any morel!
                if (viewGroup is CollapsingToolbarLayout) {
                    child.fitsSystemWindows = b
                    return
                }
                // if find AppBarLayout or Toolbar then return,the deeper view setFitsSystemWindows(true) would get UI bugs!
                if (!mIsNeedOverStatusBar && (child is AppBarLayout || child is Toolbar)) {
                    return
                }
                setFitsSystemWindows(child, b)
            }
        }
    }

    /**
     * Recursive query to find toolbar in view tree
     *
     * @param view root view
     */
    private fun findToolbar(view: View?) {
        if (mToolbar != null) {
            return
        }
        if (view is Toolbar) {
            mToolbar = view
            return
        } else {
            if (view is ViewGroup) {
                val viewGroup = view
                for (i in 0 until viewGroup.childCount) {
                    val child = viewGroup.getChildAt(i)
                    findToolbar(child)
                }
            }
        }
    }

    /**
     * Recursive query to find collapsingToolbarLayout in view tree and judge if this layout need
     * to show view behind status bar(mIsNeedOverStatusBar).
     *
     * @param view root view
     */
    private fun findCollapsingToolbarLayout(view: View?) {
        if (view is ViewGroup) {
            val viewGroup = view
            if (viewGroup.childCount > 0) {
                val child = viewGroup.getChildAt(0)
                if (child is CollapsingToolbarLayout && viewGroup is AppBarLayout) {
                    if (child.getChildAt(0) !is Toolbar) {
                        mCollapsingToolbarLayout = child
                        mIsNeedOverStatusBar = true
                    }
                }
                findCollapsingToolbarLayout(child)
            }
        }
    }

    companion object {
        private const val TAG_STATUS_BAR_VIEW = "status_bar_view"
    }
}