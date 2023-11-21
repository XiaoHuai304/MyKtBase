package com.zgh.baseLibrary

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.zgh.baseLibrary.databinding.BaselibraryLoadingLayoutBinding

abstract class BaseVmActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    protected open lateinit var mViewModel: VM
    protected open lateinit var mBinding: VB
    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = getViewBinding()
        setContentView(mBinding.root)
        initialize()

        initViewModel()


        observer()
        initView()
        initData()
        setListener()

    }

    private fun toLogin(login: AppCompatActivity) {
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        ActivityUtils.finishOtherActivities(login::class.java)
    }

    private fun initialize() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    private fun showLoadIng() {
        loadingDialog ?: initDialog().show()
    }

    private fun hitLoading() {
        loadingDialog?.dismiss()
    }

    private fun initDialog(): AlertDialog {
        val dialogBinding: BaselibraryLoadingLayoutBinding =
            BaselibraryLoadingLayoutBinding.inflate(
                LayoutInflater.from(this),
                null,
                false
            )
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setView(dialogBinding.root)
        dialogBuilder.setCancelable(false)
        loadingDialog = dialogBuilder.create()
        return loadingDialog as AlertDialog
    }

    open fun setListener() {
    }

    open fun initData() {

    }

    open fun initView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hitLoading()
        ActivityUtils.finishActivity(this)
    }


    /**
     * 订阅退出登录逻辑
     */
    open fun observer() {
        mViewModel.showLoading.observe(this) {
            if (it)
                showLoadIng()
            else
                hitLoading()
        }
    }


    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    abstract fun viewModelClass(): Class<VM>
    abstract fun getViewBinding(): VB
}
