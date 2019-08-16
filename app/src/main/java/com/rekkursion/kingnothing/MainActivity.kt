package com.rekkursion.kingnothing

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.provider.MediaStore
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import java.util.*
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import java.io.File


class MainActivity: AppCompatActivity(), View.OnClickListener {
    private val REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE: Int = 4731
    private val REQ_CODE_PERMISSION_TO_USE_CAMERA: Int = 5371
    private val REQ_CODE_GET_IMAGE_FROM_EXTERNAL_STORAGE: Int = 8620
    private val REQ_CODE_USE_CAMERA: Int = 5275

    private var mCapturedImageUri: Uri? = null

    private lateinit var mFabShowAddImageByManyWays: FloatingActionButton
    private lateinit var mFabAddNewImageByLocalFile: FloatingActionButton
    private lateinit var mFabAddNewImageByCamera: FloatingActionButton
    private lateinit var mGdvCapturedImages: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // function: delete the file by the designated path
        fun deleteFile(filePath: String): Boolean {
            val fileToDelete = File(filePath)
            if (fileToDelete.exists())
                return fileToDelete.delete()
            return false
        }

        val capturedImageBitmap: Bitmap
        when (requestCode) {
            // back from getting image from external storage
            REQ_CODE_GET_IMAGE_FROM_EXTERNAL_STORAGE -> {
                val imgUri: Uri? = convertUri(data?.data)
                val imgFilename: String? = imgUri?.lastPathSegment

                // null check
                if (imgFilename == null)
                    return

                // decode the bitmap by the file path
                capturedImageBitmap = BitmapFactory.decodeFile(imgUri.path)

                // go to edit-activity
                goToEditActivity(capturedImageBitmap)
            }

            // back from camera
            REQ_CODE_USE_CAMERA -> {
                if (resultCode == Activity.RESULT_OK && mCapturedImageUri != null) {
                    val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = contentResolver.query(mCapturedImageUri!!, projection, null, null, null)

                    // null check
                    if (cursor == null) {
                        Toast.makeText(this, "Error happened when taking the photo.", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()

                    // get the file path of the captured image
                    val capturedImageFilePath = cursor.getString(columnIndexData)
                    cursor.close()

                    // decode the bitmap by the file path
                    capturedImageBitmap = BitmapFactory.decodeFile(capturedImageFilePath) as Bitmap

                    // after decoded, delete the captured photo
                    deleteFile(capturedImageFilePath)

                    // set linear-layout for putting image-view at alert-dialog
                    val llyCapturedImage = LinearLayout(this)
                    llyCapturedImage.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    llyCapturedImage.gravity = Gravity.CENTER

                    // set layout-params for image-view too scale down the too-large-image
                    val widthPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250.0F, resources.displayMetrics)
                    val heightPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250.0F, resources.displayMetrics)

                    // set the image-view
                    val imgvShowImageFromCamera = ImageView(this)
                    imgvShowImageFromCamera.layoutParams = LinearLayout.LayoutParams(widthPX.toInt(), heightPX.toInt())
                    imgvShowImageFromCamera.setImageBitmap(capturedImageBitmap)

                    // add the image-view to linear-layout
                    llyCapturedImage.addView(imgvShowImageFromCamera)

                    // preview the photo to let user decide if it wanna take the captured image
                    AlertDialog.Builder(this)
                        .setTitle(R.string.str_preview_photo_title)
                        .setMessage(R.string.str_preview_photo_message)
                        .setView(llyCapturedImage)
                        .setCancelable(false)

                        // yes, edit it
                        .setPositiveButton(R.string.str_user_check_yes) { _, _ ->
                            // go to edit-activity
                            goToEditActivity(capturedImageBitmap)
                        }

                        // no, do nothing
                        .setNegativeButton(R.string.str_user_check_no, null)

                        // recapture, activate the camera again
                        .setNeutralButton(R.string.str_user_check_recapture) { _, _ ->
                            getImageFromCamera()
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // check the permissions of read-external-storage & write-external-storage
            REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    getImageFromExternalStorage()
                else
                    Toast.makeText(this, "Cannot access files without permissions.", Toast.LENGTH_SHORT).show()
            }

            // check the permissions of camera & write-external-storage
            REQ_CODE_PERMISSION_TO_USE_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    getImageFromCamera()
                else
                    Toast.makeText(this, "Cannot use the camera without permissions.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        mFabShowAddImageByManyWays = findViewById(R.id.fab_show_add_image_by_many_ways)
        mFabAddNewImageByLocalFile = findViewById(R.id.fab_add_new_image_by_local_file)
        mFabAddNewImageByCamera = findViewById(R.id.fab_add_new_image_by_camera)
        mGdvCapturedImages = findViewById(R.id.gdv_captured_images)

        // on-click-listener for (floating-action-) buttons
        mFabShowAddImageByManyWays.setOnClickListener(this)
        mFabAddNewImageByLocalFile.setOnClickListener(this)
        mFabAddNewImageByCamera.setOnClickListener(this)
    }

    private fun getImageFromExternalStorage() {
        val intentGetImage = Intent(Intent.ACTION_GET_CONTENT)
        intentGetImage.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intentGetImage, "Get image"),
            REQ_CODE_GET_IMAGE_FROM_EXTERNAL_STORAGE
        )
    }

    private fun getImageFromCamera() {
        val fileName = "${UUID.randomUUID()}.jpg"
        val values = ContentValues()
        val intentUseCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        values.put(MediaStore.Images.Media.TITLE, fileName)
        mCapturedImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        intentUseCamera.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri)
        startActivityForResult(intentUseCamera, REQ_CODE_USE_CAMERA)
    }

    private fun goToEditActivity(beEdittedBitmap: Bitmap?) {

    }

    private fun popUpOrCloseDownNewImageWaysFloatingActionButtons(shouldPopUp: Boolean) {
        val popUpAnimDuration = 260L
        val translationXAnimator: ObjectAnimator
        val translationYAnimator: ObjectAnimator
        val scaleXSmallAnimator: ObjectAnimator
        val scaleYSmallAnimator: ObjectAnimator

        // close down
        if (!shouldPopUp) {
            translationXAnimator = ObjectAnimator.ofFloat(mFabAddNewImageByLocalFile, "translationX", -200.0F, 0.0F)
            translationXAnimator.duration = popUpAnimDuration
            translationXAnimator.interpolator = DecelerateInterpolator(1.2F) as TimeInterpolator
            translationXAnimator.start()

            translationYAnimator = ObjectAnimator.ofFloat(mFabAddNewImageByCamera, "translationY", -200.0F, 0.0F)
            translationYAnimator.duration = popUpAnimDuration
            translationYAnimator.interpolator = DecelerateInterpolator(1.2F)
            translationYAnimator.start()

            scaleXSmallAnimator = ObjectAnimator.ofFloat(mFabShowAddImageByManyWays, "scaleX", 0.6F, 1.0F)
            scaleXSmallAnimator.duration = popUpAnimDuration
            scaleXSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
            scaleXSmallAnimator.start()

            scaleYSmallAnimator = ObjectAnimator.ofFloat(mFabShowAddImageByManyWays, "scaleY", 0.6F, 1.0F)
            scaleYSmallAnimator.duration = popUpAnimDuration
            scaleYSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
            scaleYSmallAnimator.start()

            mFabAddNewImageByLocalFile.hide()
            mFabAddNewImageByCamera.hide()
        }

        // pop up
        else {
            translationXAnimator = ObjectAnimator.ofFloat(mFabAddNewImageByLocalFile, "translationX", 0.0F, -200.0F)
            translationXAnimator.duration = popUpAnimDuration
            translationXAnimator.interpolator = DecelerateInterpolator(1.2F)
            translationXAnimator.start()

            translationYAnimator = ObjectAnimator.ofFloat(mFabAddNewImageByCamera, "translationY", 0.0F, -200.0F)
            translationYAnimator.duration = popUpAnimDuration
            translationYAnimator.interpolator = DecelerateInterpolator(1.2F)
            translationYAnimator.start()

            scaleXSmallAnimator = ObjectAnimator.ofFloat(mFabShowAddImageByManyWays, "scaleX", 1.0F, 0.6F)
            scaleXSmallAnimator.duration = popUpAnimDuration
            scaleXSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
            scaleXSmallAnimator.start()

            scaleYSmallAnimator = ObjectAnimator.ofFloat(mFabShowAddImageByManyWays, "scaleY", 1.0F, 0.6F)
            scaleYSmallAnimator.duration = popUpAnimDuration
            scaleYSmallAnimator.interpolator = DecelerateInterpolator(1.2F)
            scaleYSmallAnimator.start()

            mFabAddNewImageByLocalFile.show()
            mFabAddNewImageByCamera.show()
        }
    }

    // convert uri from content uri to file uri
//    @Throws(NullPointerException::class)
    private fun convertUri(uri: Uri?): Uri? {
        if (uri == null)
            return null

        val newUri: Uri
        if (uri.toString().substring(0, 7) == "content") {
            val colName = arrayOf(MediaStore.MediaColumns.DATA)
            val cursor = contentResolver.query(uri, colName, null, null, null)

            if (cursor == null)
                return null

            cursor!!.moveToFirst()
            newUri = Uri.parse("file://" + cursor.getString(0))

            cursor.close()
        } else
            newUri = uri

        return newUri
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // to show the ways of getting image
            R.id.fab_show_add_image_by_many_ways -> {
                //pop up the get-image-floating-action-buttons
                popUpOrCloseDownNewImageWaysFloatingActionButtons(!mFabAddNewImageByLocalFile.isShown)
            }

            // get image by local file
            R.id.fab_add_new_image_by_local_file -> {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    getImageFromExternalStorage()
                else
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQ_CODE_PERMISSION_TO_READ_EXTERNAL_STORAGE
                    )

                // close down the pop-upped floating-action-buttons
                if (mFabAddNewImageByLocalFile.isShown)
                    popUpOrCloseDownNewImageWaysFloatingActionButtons(false)
            }

            // get image by camera
            R.id.fab_add_new_image_by_camera -> {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    getImageFromCamera()
                else
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQ_CODE_PERMISSION_TO_USE_CAMERA
                    )

                // close down the pop-upped floating-action-buttons
                if (mFabAddNewImageByLocalFile.isShown)
                    popUpOrCloseDownNewImageWaysFloatingActionButtons(false)
            }
        }

        return
    }
}
