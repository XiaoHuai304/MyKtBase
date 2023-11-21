package com.zgh.baseLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zgh.baseLibrary.databinding.BaselibraryLoadingLayoutBinding

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    protected open var binding: VB? = null
    protected open val mBinding get() = binding!!
    protected lateinit var mViewModel: VM
    private var lazyLoaded = false

    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initTabBar()

        initView()
        initData()
        setListener()
    }

    private fun initDialog(): AlertDialog {
        val dialogBinding: BaselibraryLoadingLayoutBinding =
            BaselibraryLoadingLayoutBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
        dialogBuilder.setView(dialogBinding.root)
        dialogBuilder.setCancelable(false)
        loadingDialog =  dialogBuilder.create()
        return loadingDialog as AlertDialog
    }


    open fun setListener() {
    }

    open fun initData() {
    }

    open fun initView() {
    }

    open fun observe() {
        mViewModel.showLoading.observe(viewLifecycleOwner) {

        }
    }

    open fun initTabBar() {

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    abstract fun viewModelClass(): Class<VM>
    abstract fun getViewBinding(): VB

    override fun onResume() {
        super.onResume()
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun lazyLoadData() {
    }
}
