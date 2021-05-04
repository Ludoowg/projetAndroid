package com.example.todoandroidludovic.userinfo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todoandroidludovic.BuildConfig
import com.example.todoandroidludovic.R
import com.example.todoandroidludovic.network.models.User
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        viewModel.user.observe(this) { user ->
          firstname.setText(user.firstname)
            name.setText(user.lastname)
            email.setText(user.email)
            profile_image_view.load(user.avatar){
                transformations(CircleCropTransformation())
            }
        }
        validate.setOnClickListener {
            if (name.text.isNotEmpty() && firstname.text.isNotEmpty() && email.text.isNotEmpty() ) {
                var user:User=viewModel.user.value!!
                user.email=email.text.toString()
                user.firstname=firstname.text.toString()
                user.lastname=name.text.toString()
                viewModel.updateUser(user)
                setResult(RESULT_OK, intent)
                finish()
            }
            else {
                if(name.text.isEmpty()){
                    name.error = "Lastname must be not empty"
                }
                if(firstname.text.isEmpty()){
                    firstname.error = "Firstname must be not empty"
                }
                if(email.text.isEmpty()){
                    email.error = "Email must be not empty"
                }
        }
        }
        take_picture_button.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        upload_image_button.setOnClickListener {
            pickInGallery.launch("image/*")
        }
    }

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            handleImage(it)

        }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID +".fileprovider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)
        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // register
    private val takePictureOld = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }
    // use
    private fun openCamera() = takePicture.launch(photoUri)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openCamera()
            else showExplanationDialog()
        }
    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }
    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🤩")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }
    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )

    private  fun handleImage(uri:Uri){
        lifecycleScope.launch {
            viewModel.updateAvatar(convert(uri))
        }
    }
}