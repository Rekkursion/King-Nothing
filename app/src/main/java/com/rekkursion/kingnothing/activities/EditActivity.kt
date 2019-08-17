package com.rekkursion.kingnothing.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.rekkursion.kingnothing.ColorPickerDialog
import com.rekkursion.kingnothing.R



class EditActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mImgbtnRotate: ImageButton
    private lateinit var mPumRotate: PopupMenu

    private lateinit var mImgbtnColorize: ImageButton

    private lateinit var mImgvMainBitmap: ImageView

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
    }

    private fun initViews() {
        mImgbtnRotate = findViewById(R.id.imgbtn_rotate)
        mPumRotate = setPopupMenu(mImgbtnRotate)
        mImgbtnRotate.setOnClickListener(this)

        mImgbtnColorize = findViewById(R.id.imgbtn_colorize)
        mImgbtnColorize.setOnClickListener(this)

        mImgvMainBitmap = findViewById(R.id.imgv_main_bitmap)
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

    override fun onClick(view: View?) {
        when (view?.id) {
            // rotate the bitmap (4 choices at popup-menu)
            R.id.imgbtn_rotate -> mPumRotate.show()

            // colorize the bitmap
            R.id.imgbtn_colorize -> {
                val colorPickerDialog = ColorPickerDialog(this)
                colorPickerDialog.setOnColorPickingCancelClickListener("Cancel", null)
                colorPickerDialog.setOnColorPickingSelectClickListener("Select", object: ColorPickerDialog.OnColorPickingSelectClickListener {
                    override fun onSelectClick(a: Int, r: Int, g: Int, b: Int) {
                        

//                        Toast.makeText(this@EditActivity, "($a, $r, $g, $b)", Toast.LENGTH_SHORT).show()
                        colorPickerDialog.dismiss()
                    }
                })
                colorPickerDialog.show()
            }
        }
    }
}
