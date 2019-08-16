package com.rekkursion.kingnothing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.rekkursion.kingnothing.models.CapturedImageItemModel
import android.widget.Toast
import android.graphics.BitmapFactory
import com.rekkursion.kingnothing.R
import java.io.FileNotFoundException
import java.io.IOException


class CapturedImageItemAdapter: BaseAdapter {
    private val mContext: Context
    private val mCapturedImageItemList: ArrayList<CapturedImageItemModel>

    constructor(context: Context, capturedImageItemList: ArrayList<CapturedImageItemModel>) {
        mContext = context
        mCapturedImageItemList = capturedImageItemList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val grid: View
        val layoutInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (convertView == null) {
            grid = layoutInflater.inflate(R.layout.layout_captured_image_item, null)

            val imgv: ImageView = grid.findViewById(R.id.imgv_captured_image)
            val imageFilename: String = mCapturedImageItemList[position].filename

            try {
                val fis = mContext.openFileInput(imageFilename)
                val imageBitmap = BitmapFactory.decodeStream(fis)
                imgv.setImageBitmap(imageBitmap)
                fis.close()
            } catch (e: FileNotFoundException) {
                Toast.makeText(mContext, "File not found. It might be renamed or removed.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e:NullPointerException) {
                e.printStackTrace()
            }
        }
        else
            grid = convertView

        return grid
    }

    override fun getItem(position: Int): Any {
        return mCapturedImageItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return mCapturedImageItemList[position].id
    }

    override fun getCount(): Int {
        return mCapturedImageItemList.size
    }
}