package com.rekkursion.kingnothing

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.jar.Manifest

class MainActivity: AppCompatActivity(), View.OnClickListener {
    private val REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE: Int = 4731
    private val REQ_CODE_GET_IMAGE_FROM_EXTERNAL_STORAGE: Int = 8620

    private lateinit var fabShowAddImageByManyWays: FloatingActionButton
    private lateinit var fabAddNewImageByLocalFile: FloatingActionButton
    private lateinit var fabAddNewImageByCamera: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getImageFromExternalStorage()
                else
                    Toast.makeText(this, "Cannot get the image without permissions.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        fabShowAddImageByManyWays = findViewById(R.id.fab_show_add_image_by_many_ways)
        fabAddNewImageByLocalFile = findViewById(R.id.fab_add_new_image_by_local_file)
        fabAddNewImageByCamera = findViewById(R.id.fab_add_new_image_by_camera)

        fabShowAddImageByManyWays.setOnClickListener(this)
        fabAddNewImageByLocalFile.setOnClickListener(this)
        fabAddNewImageByCamera.setOnClickListener(this)
    }

    private fun getImageFromExternalStorage() {
        val intentGetImage = Intent(Intent.ACTION_GET_CONTENT)
        intentGetImage.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intentGetImage, "Get image"),
            REQ_CODE_GET_IMAGE_FROM_EXTERNAL_STORAGE
        )
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // to show the ways of getting image
            R.id.fab_show_add_image_by_many_ways -> {
                val popUpAnimDuration = 260L
                val translationXAnimator: ObjectAnimator
                val translationYAnimator: ObjectAnimator
                val scaleXSmallAnimator: ObjectAnimator
                val scaleYSmallAnimator: ObjectAnimator

                if (fabAddNewImageByLocalFile.isShown) {
                    translationXAnimator = ObjectAnimator.ofFloat(fabAddNewImageByLocalFile, "translationX", -200.0F, 0.0F)
                    translationXAnimator.duration = popUpAnimDuration
                    translationXAnimator.interpolator = DecelerateInterpolator(1.2F)
                    translationXAnimator.start()

                    translationYAnimator = ObjectAnimator.ofFloat(fabAddNewImageByCamera, "translationY", -200.0F, 0.0F)
                    translationYAnimator.duration = popUpAnimDuration
                    translationYAnimator.interpolator = DecelerateInterpolator(1.2F)
                    translationYAnimator.start()

                    scaleXSmallAnimator = ObjectAnimator.ofFloat(fabShowAddImageByManyWays, "scaleX", 0.6F, 1.0F)
                    scaleXSmallAnimator.duration = popUpAnimDuration
                    scaleXSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
                    scaleXSmallAnimator.start()

                    scaleYSmallAnimator = ObjectAnimator.ofFloat(fabShowAddImageByManyWays, "scaleY", 0.6F, 1.0F)
                    scaleYSmallAnimator.duration = popUpAnimDuration
                    scaleYSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
                    scaleYSmallAnimator.start()

                    fabAddNewImageByLocalFile.hide()
                    fabAddNewImageByCamera.hide()
                }
                else {
                    translationXAnimator = ObjectAnimator.ofFloat(fabAddNewImageByLocalFile, "translationX", 0.0F, -200.0F)
                    translationXAnimator.duration = popUpAnimDuration
                    translationXAnimator.interpolator = DecelerateInterpolator(1.2F)
                    translationXAnimator.start()

                    translationYAnimator = ObjectAnimator.ofFloat(fabAddNewImageByCamera, "translationY", 0.0F, -200.0F)
                    translationYAnimator.duration = popUpAnimDuration
                    translationYAnimator.interpolator = DecelerateInterpolator(1.2F)
                    translationYAnimator.start()

                    scaleXSmallAnimator = ObjectAnimator.ofFloat(fabShowAddImageByManyWays, "scaleX", 1.0F, 0.6F)
                    scaleXSmallAnimator.duration = popUpAnimDuration
                    scaleXSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
                    scaleXSmallAnimator.start()

                    scaleYSmallAnimator = ObjectAnimator.ofFloat(fabShowAddImageByManyWays, "scaleY", 1.0F, 0.6F)
                    scaleYSmallAnimator.duration = popUpAnimDuration
                    scaleYSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
                    scaleYSmallAnimator.start()

                    fabAddNewImageByLocalFile.show()
                    fabAddNewImageByCamera.show()
                }
            }

            // get image by local file
            R.id.fab_add_new_image_by_local_file -> {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    getImageFromExternalStorage()
                else
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE
                    )
            }
        }

        return
    }
}
