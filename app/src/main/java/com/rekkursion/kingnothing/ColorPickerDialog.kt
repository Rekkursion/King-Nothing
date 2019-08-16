package com.rekkursion.kingnothing

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import java.util.*


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

    private lateinit var mTxtvShowPickedRedValue: TextView
    private lateinit var mTxtvShowPickedGreenValue: TextView
    private lateinit var mTxtvShowPickedBlueValue: TextView

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

        mTxtvShowPickedRedValue = findViewById(R.id.txtv_color_picker_dialog_show_red_value)
        mTxtvShowPickedGreenValue = findViewById(R.id.txtv_color_picker_dialog_show_green_value)
        mTxtvShowPickedBlueValue = findViewById(R.id.txtv_color_picker_dialog_show_blue_value)

        mImgvPreviewPickedColor = findViewById(R.id.imgv_color_picker_dialog_preview_color)
    }

    private fun initData() {
        mSkbPickRed.progress = 0
        mSkbPickGreen.progress = 0
        mSkbPickBlue.progress = 0

        mSkbPickRed.max = 255
        mSkbPickGreen.max = 255
        mSkbPickBlue.max = 255

        mTxtvShowPickedRedValue.text = "000"
        mTxtvShowPickedGreenValue.text = "000"
        mTxtvShowPickedBlueValue.text = "000"

        mImgvPreviewPickedColor.setBackgroundColor(Color.BLACK)
    }

    private fun initEvents() {
        // btn-cancel click
        mBtnCancel.setOnClickListener {
            if (mOnColorPickingCancelClickListener != null)
                mOnColorPickingCancelClickListener!!.onCancelClick()
        }

        // btn-select click
        mBtnSelect.setOnClickListener {
            if (mOnColorPickingSelectClickListener != null)
                mOnColorPickingSelectClickListener!!.onSelectClick()
        }

        // rgb value seek-bar change
        val onSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isUser: Boolean) {
                when (seekBar?.id) {
                    R.id.skb_color_picker_dialog_pick_red -> {
                        mTxtvShowPickedRedValue.text = String.format(Locale.CANADA, "%03d", progress)
                    }

                    R.id.skb_color_picker_dialog_pick_green -> {
                        mTxtvShowPickedGreenValue.text = String.format(Locale.CANADA, "%03d", progress)
                    }

                    R.id.skb_color_picker_dialog_pick_blue -> {
                        mTxtvShowPickedBlueValue.text = String.format(Locale.CANADA, "%03d", progress)
                    }
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
        }
        mSkbPickRed.setOnSeekBarChangeListener(onSeekBarChangeListener)
        mSkbPickGreen.setOnSeekBarChangeListener(onSeekBarChangeListener)
        mSkbPickBlue.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }

    // set on-cancel-listener
    fun setOnColorPickingCancelClickListener(cancelBtnStr: String, onColorPickingCancelClickListener: OnColorPickingCancelClickListener) {
        mCancelButtonString = cancelBtnStr
        mOnColorPickingCancelClickListener = onColorPickingCancelClickListener
    }

    // set on-select-listener
    fun setOnColorPickingSelectClickListener(selectBtnStr: String, onColorPickingSelectClickListener: OnColorPickingSelectClickListener) {
        mSelectButtonString = selectBtnStr
        mOnColorPickingSelectClickListener = onColorPickingSelectClickListener
    }

    // interface for cancel button
    interface OnColorPickingCancelClickListener {
        fun onCancelClick()
    }

    // interface for select button
    interface OnColorPickingSelectClickListener {
        fun onSelectClick()
    }
}