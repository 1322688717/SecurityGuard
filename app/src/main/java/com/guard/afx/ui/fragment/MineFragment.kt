package com.guard.afx.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.guard.afx.R
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentMineBinding
import com.guard.afx.viewmodel.MineFragmentViewModel


class MineFragment : BaseFragment<MineFragmentViewModel,FragmentMineBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        onclick()
    }

    private fun onclick() {
        mViewBind.apply {
            ccAboutOur.setOnClickListener{
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val alterDiaglog = AlertDialog.Builder(requireActivity(), R.style.MyDialog)
        var view : View = LayoutInflater.from(requireActivity()).inflate(R.layout.default_dialog, null, false)
        alterDiaglog.setView(view) //加载进去
        val dialog = alterDiaglog.create()

        var confirm : TextView = view.findViewById(R.id.confirm)
        confirm.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}