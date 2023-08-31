package com.happytaxidriver.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.testassignment.view.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>>(clazz: KClass<V>) : Fragment() {

    lateinit var parentActivity: BaseActivity<*, *>
    lateinit var mRootView: View
    lateinit var binding: T
    val mViewModel: V by viewModel(clazz = clazz)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.parentActivity = context
        }
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root
        return mRootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

         try {
             binding.unbind()
         }
         catch (ex: Exception )
         {

         }






    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(bindingVariable, mViewModel)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.executePendingBindings()


    }


/*

    fun moveToChildFragment(fragment: Fragment, containerId: Int? = R.id.container) {
        replaceChildFragment(fragment, containerId!!, addToStack = true, clearBackStack = false)
    }

    fun moveToChildFragmentWithClear(fragment: Fragment, containerId: Int? = R.id.container) {
        replaceChildFragment(fragment, containerId!!, addToStack = false, clearBackStack = true)
    }
*/


}