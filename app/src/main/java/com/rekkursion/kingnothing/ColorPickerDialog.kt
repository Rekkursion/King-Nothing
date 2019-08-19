package com.rekkursion.kingnothing

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import java.util.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.core.content.ContextCompat.getSystemService




class ColorPickerDialog: Dialog {
    private var mContext: Context

    private var mOnColorPickingCancelClickListener: OnColorPickingCancelClickListener? = null
    private var mOnColorPickingSelectClickListener: OnColorPickingSelectClickListener? = null

    private var mCancelButtonString: String = "Cancel"
    private var mSelectButtonString: String = "Select"

    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSelect: Button

    private lateinit var mSkbPickRed: SeekBar
    private lateinit var mSkbPickGreen: SeekBar
    private lateinit var mSkbPickBlue: SeekBar
    private lateinit var mSkbPickAlpha: SeekBar

    private lateinit var mBtnShowPickedRedValue: Button
    private lateinit var mBtnShowPickedGreenValue: Button
    private lateinit var mBtnShowPickedBlueValue: Button
    private lateinit var mBtnShowPickedAlphaValue: Button

    private lateinit var mBtnOrUsingHexCode: Button

    private lateinit var mImgvPreviewPickedColor: ImageView

    // constructor
    constructor(context: Context): super(context, R.style.ColorPickerDialogTheme) {
        mContext = context
    }

    // constructor
    constructor(
        context: Context,
        onColorPickingCancelClickListener: OnColorPickingCancelClickListener,
        onColorPickingSelectClickListener: OnColorPickingSelectClickListener
    ): super(context, R.style.ColorPickerDialogTheme) {
        mContext = context
        mOnColorPickingCancelClickListener = onColorPickingCancelClickListener
        mOnColorPickingSelectClickListener = onColorPickingSelectClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_color_picker_dialog)

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false)

        init()
    }

    private fun init() {
        initViews()
        initData()
        initEvents()
    }

    private fun initViews() {
        mBtnCancel = findViewById(R.id.btn_color_picker_dialog_cancel)
        mBtnSelect = findViewById(R.id.btn_color_picker_dialog_select)

        mSkbPickRed = findViewById(R.id.skb_color_picker_dialog_pick_red)
        mSkbPickGreen = findViewById(R.id.skb_color_picker_dialog_pick_green)
        mSkbPickBlue = findViewById(R.id.skb_color_picker_dialog_pick_blue)
        mSkbPickAlpha = findViewById(R.id.skb_color_picker_dialog_pick_alpha)

        mSkbPickRed.progressDrawable = mContext.resources.getDrawable(R.drawable.drawable_seek_bar_pick_red)
        mSkbPickRed.thumb = mContext.resources.getDrawable(R.drawable.ic_block_black_24dp)
        mSkbPickGreen.progressDrawable = mContext.resources.getDrawable(R.drawable.drawable_seek_bar_pick_green)
        mSkbPickGreen.thumb = mContext.resources.getDrawable(R.drawable.ic_block_black_24dp)
        mSkbPickBlue.progressDrawable = mContext.resources.getDrawable(R.drawable.drawable_seek_bar_pick_blue)
        mSkbPickBlue.thumb = mContext.resources.getDrawable(R.drawable.ic_block_black_24dp)
        mSkbPickAlpha.progressDrawable = mContext.resources.getDrawable(R.drawable.drawable_seek_bar_pick_alpha)
        mSkbPickAlpha.thumb = mContext.resources.getDrawable(R.drawable.ic_block_black_24dp)

        mBtnShowPickedRedValue = findViewById(R.id.btn_color_picker_dialog_show_red_value)
        mBtnShowPickedGreenValue = findViewById(R.id.btn_color_picker_dialog_show_green_value)
        mBtnShowPickedBlueValue = findViewById(R.id.btn_color_picker_dialog_show_blue_value)
        mBtnShowPickedAlphaValue = findViewById(R.id.btn_color_picker_dialog_show_alpha_value)

        mBtnOrUsingHexCode = findViewById(R.id.btn_color_picker_dialog_or_using_hex_code)

        mImgvPreviewPickedColor = findViewById(R.id.imgv_color_picker_dialog_preview_color)
    }

    private fun initData() {
        mSkbPickRed.max = 255
        mSkbPickGreen.max = 255
        mSkbPickBlue.max = 255
        mSkbPickAlpha.max = 255

        mSkbPickRed.progress = 0
        mSkbPickGreen.progress = 0
        mSkbPickBlue.progress = 0
        mSkbPickAlpha.progress = 255

        mBtnShowPickedRedValue.text = "000"
        mBtnShowPickedGreenValue.text = "000"
        mBtnShowPickedBlueValue.text = "000"
        mBtnShowPickedAlphaValue.text = "255"

        setPreviewColorAtImageView()
    }

    private fun initEvents() {
        // btn-cancel click
        mBtnCancel.setOnClickListener {
            if (mOnColorPickingCancelClickListener != null)
                mOnColorPickingCancelClickListener!!.onCancelClick(
                    mSkbPickAlpha.progress,
                    mSkbPickRed.progress,
                    mSkbPickGreen.progress,
                    mSkbPickBlue.progress
                )
            this.dismiss()
        }

        // btn-select click
        mBtnSelect.setOnClickListener {
            if (mOnColorPickingSelectClickListener != null)
                mOnColorPickingSelectClickListener!!.onSelectClick(
                    mSkbPickAlpha.progress,
                    mSkbPickRed.progress,
                    mSkbPickGreen.progress,
                    mSkbPickBlue.progress
                )
            this.dismiss()
        }

        // rgb value seek-bar change
        val onSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isUser: Boolean) {
                // change the corresponding text-view to show the value
                when (seekBar?.id) {
                    R.id.skb_color_picker_dialog_pick_red ->
                        setPickedValueText(mBtnShowPickedRedValue, progress)

                    R.id.skb_color_picker_dialog_pick_green ->
                        setPickedValueText(mBtnShowPickedGreenValue, progress)

                    R.id.skb_color_picker_dialog_pick_blue ->
                        setPickedValueText(mBtnShowPickedBlueValue, progress)

                    R.id.skb_color_picker_dialog_pick_alpha ->
                        setPickedValueText(mBtnShowPickedAlphaValue, progress)
                }

                // set the color at image-view for previewing
                setPreviewColorAtImageView()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
        }
        mSkbPickRed.setOnSeekBarChangeListener(onSeekBarChangeListener)
        mSkbPickGreen.setOnSeekBarChangeListener(onSeekBarChangeListener)
        mSkbPickBlue.setOnSeekBarChangeListener(onSeekBarChangeListener)
        mSkbPickAlpha.setOnSeekBarChangeListener(onSeekBarChangeListener)

        // color value text-views click
        val onShowingColorValueButtonClickListener = { view: View? ->
            val colorStr: String = when (view?.id) {
                R.id.btn_color_picker_dialog_show_red_value -> "Red"
                R.id.btn_color_picker_dialog_show_green_value -> "Green"
                R.id.btn_color_picker_dialog_show_blue_value -> "Blue"
                R.id.btn_color_picker_dialog_show_alpha_value -> "Alpha"
                else -> "Joseph Joestar"
            }

            // create a new edit-text
            val edtInputColorValue = EditText(mContext)
            edtInputColorValue.hint = "Range: [0, 255]"
            edtInputColorValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            edtInputColorValue.transformationMethod = object: PasswordTransformationMethod() {
                override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
                    return source!!
                }
            }

            // build a new alert-dialog
            AlertDialog.Builder(mContext)
                .setTitle("$colorStr, current value: ${(view as TextView).text}")
                .setView(edtInputColorValue)
                .setPositiveButton("OK") { _, _ ->
                    if (edtInputColorValue.text.isEmpty())
                        return@setPositiveButton

                    val inputValue: Int = Integer.valueOf(edtInputColorValue.text.toString())
                    when (view?.id) {
                        R.id.btn_color_picker_dialog_show_red_value -> mSkbPickRed.progress = inputValue
                        R.id.btn_color_picker_dialog_show_green_value -> mSkbPickGreen.progress = inputValue
                        R.id.btn_color_picker_dialog_show_blue_value -> mSkbPickBlue.progress = inputValue
                        R.id.btn_color_picker_dialog_show_alpha_value -> mSkbPickAlpha.progress = inputValue
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }

        mBtnShowPickedRedValue.setOnClickListener(onShowingColorValueButtonClickListener)
        mBtnShowPickedGreenValue.setOnClickListener(onShowingColorValueButtonClickListener)
        mBtnShowPickedBlueValue.setOnClickListener(onShowingColorValueButtonClickListener)
        mBtnShowPickedAlphaValue.setOnClickListener(onShowingColorValueButtonClickListener)

        // or-using-hex-code button
        mBtnOrUsingHexCode.setOnClickListener {
            val currentHexStringRed = String.format("%02X", Integer.valueOf(mBtnShowPickedRedValue.text.toString()))
            val currentHexStringGreen = String.format("%02X", Integer.valueOf(mBtnShowPickedGreenValue.text.toString()))
            val currentHexStringBlue = String.format("%02X", Integer.valueOf(mBtnShowPickedBlueValue.text.toString()))
            val currentHexStringAlpha = String.format("%02X", Integer.valueOf(mBtnShowPickedAlphaValue.text.toString()))

            val currentHexCode = "#$currentHexStringAlpha$currentHexStringRed$currentHexStringGreen$currentHexStringBlue"

            // edit-text to let user enter a hex code
            val edtHexCode = EditText(mContext)
            edtHexCode.setText(currentHexCode)
            edtHexCode.setSelection(1, currentHexCode.length)

            // automatically show the keyboard
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

            // build a new alert-dialog
            AlertDialog.Builder(mContext)
                .setTitle("Enter a hex code by the order of ARGB or RGB")
                .setView(edtHexCode)
                .setPositiveButton("OK") { _, _ ->
                    // get the string of edit-text
                    val str = edtHexCode.text.toString().substring(edtHexCode.text.toString().lastIndexOf("#") + 1)

                    val redHexString: String
                    val greenHexString: String
                    val blueHexString: String
                    val alphaHexString: String

                    // ARGB: shortened format of hex code, e.g. #F3EA, equals to #FF33EEAA
                    if (str.length == 4) {
                        redHexString = str.substring(1, 2).repeat(2).toUpperCase()
                        greenHexString = str.substring(2, 3).repeat(2).toUpperCase()
                        blueHexString = str.substring(3, 4).repeat(2).toUpperCase()
                        alphaHexString = str.substring(0, 1).repeat(2).toUpperCase()
                    }
                    // ARGB: normal format of hex code
                    else if (str.length == 8) {
                        redHexString = str.substring(2, 4).toUpperCase()
                        greenHexString = str.substring(4, 6).toUpperCase()
                        blueHexString = str.substring(6, 8).toUpperCase()
                        alphaHexString = str.substring(0, 2).toUpperCase()
                    }
                    // RGB: shortened format
                    else if (str.length == 3) {
                        redHexString = str.substring(0, 1).repeat(2).toUpperCase()
                        greenHexString = str.substring(1, 2).repeat(2).toUpperCase()
                        blueHexString = str.substring(2, 3).repeat(2).toUpperCase()
                        alphaHexString = currentHexStringAlpha
                    }
                    // RGB: normal format
                    else if (str.length == 6) {
                        redHexString = str.substring(0, 2).toUpperCase()
                        greenHexString = str.substring(2, 4).toUpperCase()
                        blueHexString = str.substring(4, 6).toUpperCase()
                        alphaHexString = currentHexStringAlpha
                    }
                    // wrong format
                    else {
                        Toast.makeText(mContext, "Wrong hex code format.", Toast.LENGTH_SHORT).show()
                        imm?.hideSoftInputFromWindow(edtHexCode.windowToken, 0)
                        return@setPositiveButton
                    }

                    // invalid characters check
                    val regex = "[^0-9a-fA-F]".toRegex()
                    if (redHexString.contains(regex) || greenHexString.contains(regex) || blueHexString.contains(regex) || alphaHexString.contains(regex)) {
                        Toast.makeText(mContext, "Wrong hex code format.", Toast.LENGTH_SHORT).show()
                        imm?.hideSoftInputFromWindow(edtHexCode.windowToken, 0)
                        return@setPositiveButton
                    }

                    // convert hex codes into integer values
                    val redValue = redHexString.toInt(radix = 16)
                    val greenValue = greenHexString.toInt(radix = 16)
                    val blueValue = blueHexString.toInt(radix = 16)
                    val alphaValue = alphaHexString.toInt(radix = 16)

                    // assign them to seek-bars
                    mSkbPickRed.progress = redValue
                    mSkbPickGreen.progress = greenValue
                    mSkbPickBlue.progress = blueValue
                    mSkbPickAlpha.progress = alphaValue

                    // hide the keyboard
                    imm?.hideSoftInputFromWindow(edtHexCode.windowToken, 0)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    imm?.hideSoftInputFromWindow(edtHexCode.windowToken, 0)
                }
                .create()
                .show()
        }
    }

    private fun setPreviewColorAtImageView() {
        val currentColorInt: Int = Color.argb(
            mSkbPickAlpha.progress,
            mSkbPickRed.progress,
            mSkbPickGreen.progress,
            mSkbPickBlue.progress
        )
        mImgvPreviewPickedColor.setImageDrawable(ColorDrawable(currentColorInt))
    }

    private fun setPickedValueText(btn: Button?, value: Int) {
        btn?.text = String.format(Locale.CANADA, "%03d", value)
    }

    // set on-cancel-listener
    fun setOnColorPickingCancelClickListener(cancelBtnStr: String, onColorPickingCancelClickListener: OnColorPickingCancelClickListener?) {
        mCancelButtonString = cancelBtnStr
        mOnColorPickingCancelClickListener = onColorPickingCancelClickListener
    }

    // set on-select-listener
    fun setOnColorPickingSelectClickListener(selectBtnStr: String, onColorPickingSelectClickListener: OnColorPickingSelectClickListener?) {
        mSelectButtonString = selectBtnStr
        mOnColorPickingSelectClickListener = onColorPickingSelectClickListener
    }

    // interface for cancel button
    interface OnColorPickingCancelClickListener {
        fun onCancelClick(a: Int, r: Int, g: Int, b: Int)
    }

    // interface for select button
    interface OnColorPickingSelectClickListener {
        fun onSelectClick(a: Int, r: Int, g: Int, b: Int)
    }
}