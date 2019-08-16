package com.rekkursion.kingnothing.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.rekkursion.kingnothing.ColorPickerDialog
import com.rekkursion.kingnothing.R



class EditActivity : AppCompatActivity() {
    private lateinit var mImgvRotate: ImageView
    private lateinit var mPmnImgvRotate: PopupMenu

    private lateinit var mImgvColorize: ImageView

    private val mOnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.rotate_menu_item_turn_left_90 -> {
                Log.e("pop-up", "turn left 90")
            }

            R.id.rotate_menu_item_turn_right_90 -> {
                Log.e("pop-up", "turn right 90")
            }

            R.id.rotate_menu_item_vertical_flip -> {
                Log.e("pop-up", "vertical flip")
            }

            R.id.rotate_menu_item_horizontal_flip -> {
                Log.e("pop-up", "horizontal flip")
            }

            else -> {
                Log.e("pop-up", "wtf")
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        initViews()

        val colorPickerDialog = ColorPickerDialog(this)
        colorPickerDialog.setOnColorPickingCancelClickListener("Cancel", object: ColorPickerDialog.OnColorPickingCancelClickListener {
            override fun onCancelClick() {
                Toast.makeText(this@EditActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                colorPickerDialog.dismiss()
            }
        })
        colorPickerDialog.setOnColorPickingSelectClickListener("Select", object: ColorPickerDialog.OnColorPickingSelectClickListener {
            override fun onSelectClick() {
                Toast.makeText(this@EditActivity, "Selected", Toast.LENGTH_SHORT).show()
                colorPickerDialog.dismiss()
            }
        })
        colorPickerDialog.show()
    }

    private fun initViews() {
        mImgvRotate = findViewById(R.id.imgv_rotate)
        mPmnImgvRotate = setPopupMenu(mImgvRotate)
        mImgvRotate.setOnClickListener { mPmnImgvRotate.show() }

        mImgvColorize = findViewById(R.id.imgv_colorize)
    }

    private fun setPopupMenu(view: View): PopupMenu {
        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.rotate_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener)
        popupMenu.setOnDismissListener {
            // 控件消失时的事件
        }

        return popupMenu
    }
}
