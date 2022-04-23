package com.su.mediabox.v2.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.su.mediabox.databinding.ActivityPluginInstallerBinding
import com.su.mediabox.plugin.PluginManager.launchPlugin
import com.su.mediabox.util.*
import com.su.mediabox.v2.viewmodel.PluginInstallerViewModel
import com.su.mediabox.view.adapter.type.dynamicGrid
import com.su.mediabox.view.adapter.type.initTypeList
import com.su.mediabox.view.adapter.type.submitList

class PluginInstallerActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<PluginInstallerViewModel>()
    private lateinit var mBinding: ActivityPluginInstallerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPluginInstallerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            viewModel.pluginInstallState.observe(this@PluginInstallerActivity) {
                when (it) {
                    is PluginInstallerViewModel.PluginInstallState.LOADING -> {
                        pluginInstallDownload.gone()
                        pluginInstallInstall.gone()
                    }
                    is PluginInstallerViewModel.PluginInstallState.PREVIEW -> {
                        pluginInstallDownload.visible()
                        pluginInstallInstall.gone()
                    }
                    is PluginInstallerViewModel.PluginInstallState.READY -> {
                        pluginInstallDownload.gone()
                        pluginInstallInstall.visible()
                        pluginInstallInfoList.submitList(it.installInfo)
                    }
                    is PluginInstallerViewModel.PluginInstallState.SUCCESS -> {
                        "插件安装成功".showToast()
                        pluginInstallDownload.gone()
                        pluginInstallInstall.gone()
                        pluginInstallLaunch.apply {
                            setOnClickListener { _ ->
                                this@PluginInstallerActivity.apply {
                                    launchPlugin(it.pluginInfo)
                                }
                                finish()
                            }
                            visible()
                        }
                    }
                    is PluginInstallerViewModel.PluginInstallState.ERROR -> {
                        pluginInstallDownload.gone()
                        pluginInstallInstall.gone()
                        pluginInstallInfoList.submitList(it.errorInfo)
                    }
                }
            }

            setViewsOnClickListener(pluginInstallInstall, pluginInstallCancel)

            pluginInstallInfoList.dynamicGrid().initTypeList { }
        }

        load(intent)
    }

    private fun load(intent: Intent?) {
        requestManageExternalStorage {
            onGranted {
                viewModel.load(intent)
            }
            onDenied {
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        load(intent)
    }

    override fun onClick(v: View?) {
        mBinding.apply {
            when (v) {
                pluginInstallInstall -> viewModel.install()
                pluginInstallCancel -> finish()
            }
        }
    }

    //override fun onBackPressed() {}
}