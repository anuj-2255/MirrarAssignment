package com.happytaxidriver.view.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.testassignment.utils.Loading
import com.example.testassignment.utils.TranslucentBarManager
import com.example.testassignment.utils.showToast
import com.example.testassignment.view.base.BaseViewModel
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>>(clazz: KClass<V>) :
    AppCompatActivity() {

    private val loading: Loading by inject()
    lateinit var binding: T
    private val mViewModel: V by viewModel(clazz = clazz)

    private var searchJob: Job? = null

    //for app update
    //private var appUpdateManager: AppUpdateManager? = null
    private val MY_REQUEST_CODE = 986

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */

    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */

    abstract val viewModel: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TranslucentBarManager(this).transparent(this, false)
        performDataBinding()


    }


    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(bindingVariable, mViewModel)
        binding.executePendingBindings()
    }
/*
    fun moveToFragment(fragment: Fragment, containerId: Int = R.id.container) {
        try {
            replaceFragment(fragment, containerId, true)
        } catch (ex: Exception) {

        }
    }

    fun moveToFragmentAfterAdding(fragment: Fragment, containerId: Int = R.id.container) {
        try {
            addFragment(fragment, containerId, true)
        } catch (ex: Exception) {

        }

    }*/

    fun toast(msg: String) {
        showToast(msg)
    }

/*    fun moveToFragmentWithClear(fragment: Fragment, containerId: Int = R.id.container) {
        try {
            replaceFragment(fragment, containerId, addToStack = true, clearBackStack = true)
        } catch (ex: Exception) {
        }
    }

    fun moveToFragmentWithClear2(fragment: Fragment, containerId: Int = R.id.container) {
        try {
            replaceFragment(fragment, containerId, addToStack = false, clearBackStack = true)
        } catch (ex: Exception) {
        }
    }*/

    fun showLoader() {
        try {
            loading.hide(this)
            loading.show(this)
        } catch (e: Exception) {
            Log.e("<<showLoaderFunctionException", e.message.toString())
        }

    }

    fun hideLoader() {
        try {
            loading.hide(this)
        } catch (e: Exception) {
            Log.e("<<hideLoaderFunctionException", e.message.toString())
        }
    }

    //this function allow onActivity result in fragment
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }


    }


}