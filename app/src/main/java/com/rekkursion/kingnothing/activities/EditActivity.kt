package com.rekkursion.kingnothing.activities

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.rekkursion.kingnothing.ColorPickerDialog
import com.rekkursion.kingnothing.R
import com.rekkursion.kingnothing.factories.ImageProcessFactory
import com.rekkursion.kingnothing.singletons.ImageProcessManager
import kotlinx.android.synthetic.main.activity_edit.*
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.view.Gravity
import android.view.OrientationEventListener
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import java.util.*


class EditActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var mImgbtnRotate: ImageButton
    private lateinit var mPumRotate: PopupMenu

    private lateinit var mImgbtnColorize: ImageButton

    private lateinit var mImgvMainBitmap: ImageView

    private lateinit var mBtnUndo: Button
    private lateinit var mBtnRedo: Button

    private val mOnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.rotate_menu_item_turn_left_90 -> {
                ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.turnLeft90Degrees(ImageProcessManager.getCurrentBitmap()))
                mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
            }

            R.id.rotate_menu_item_turn_right_90 -> {
                ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.turnRight90Degrees(ImageProcessManager.getCurrentBitmap()))
                mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
            }

            R.id.rotate_menu_item_turn_180_degrees -> {
                ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.turn180Degrees(ImageProcessManager.getCurrentBitmap()))
                mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
            }

            R.id.rotate_menu_item_turn_any_degrees -> {
                val txtvShowDegree = TextView(this)
                txtvShowDegree.text = "000 degrees"
                txtvShowDegree.typeface = Typeface.DEFAULT_BOLD
                txtvShowDegree.setPadding(20, 0, 0, 0)

                val skbDegree = SeekBar(this)
                skbDegree.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                skbDegree.max = 360
                skbDegree.progress = 0
                skbDegree.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isUser: Boolean) {
                        txtvShowDegree.text = String.format(Locale.CANADA, "%03d degrees", progress)
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                })

                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL
                layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                layout.setPadding(20)
                layout.gravity = Gravity.CENTER
                layout.addView(txtvShowDegree)
                layout.addView(skbDegree)

                AlertDialog.Builder(this)
                    .setTitle("Range: [0, 360] degrees")
                    .setView(layout)
                    .setPositiveButton("OK") { _, _ ->
                        ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.turnAnyDegrees(ImageProcessManager.getCurrentBitmap(), skbDegree.progress.toFloat()))
                        mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show()
            }

            R.id.rotate_menu_item_vertical_flip -> {
                ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.verticalFlip(ImageProcessManager.getCurrentBitmap()))
                mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
            }

            R.id.rotate_menu_item_horizontal_flip -> {
                ImageProcessManager.addNewProcessedBitmap(ImageProcessFactory.horizontalFlip(ImageProcessManager.getCurrentBitmap()))
                mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
            }

            else -> {
                Log.e("pop-up", "wtf")
            }
        }
        true
    }

    private val mOnUndoRedoButtonsClickListener = View.OnClickListener { view ->
        when (view?.id) {
            R.id.btn_undo -> ImageProcessManager.undo()
            R.id.btn_redo -> ImageProcessManager.redo()
        }
        mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())
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
        mImgvMainBitmap.setImageBitmap(ImageProcessManager.getCurrentBitmap())

        mBtnUndo = findViewById(R.id.btn_undo)
        mBtnUndo.setOnClickListener(mOnUndoRedoButtonsClickListener)
        mBtnRedo = findViewById(R.id.btn_redo)
        mBtnRedo.setOnClickListener(mOnUndoRedoButtonsClickListener)
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
                        val original = ImageProcessManager.getCurrentBitmap()
                        val processed =
                            if (original == null) null
                            else ImageProcessFactory.colorCover(original, Color.argb(a, r, g, b))

                        ImageProcessManager.addNewProcessedBitmap(processed)
                        imgv_main_bitmap.setImageBitmap(processed)
                    }
                })
                colorPickerDialog.show()
            }
        }
    }
}
