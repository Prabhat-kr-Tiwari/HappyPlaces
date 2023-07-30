package com.example.happyplaces


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.happyplaces.ApplicationClass.PlaceApplicationClass
import com.example.happyplaces.DAO.PlaceDao
import com.example.happyplaces.Model.PlaceEntity
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAddHappyPlaceBinding


    private var cal = Calendar.getInstance()

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_happy_place)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_happy_place)
//        val view = binding.root
//        setContentView(view)


        //getting the database instance
        val placeDao=(application as PlaceApplicationClass).db.placedao()
        // setSupportActionBar(binding.toolbar_add_place)
        val toolbar: Toolbar = binding.toolbarAddPlace
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddPlace.setNavigationOnClickListener {

            onBackPressed()
        }
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateView()

        }
        //handling the setOnclick listener as we have extended this class as onclick lisstener
        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)



        binding.btnSave.setOnClickListener { addRecord(placeDao) }

    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.et_date -> {
                DatePickerDialog(
                    this,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image->{
                val picturedialog=AlertDialog.Builder(this)
                picturedialog.setTitle("Select Action")
                val pictureDialogItems= arrayOf("Select From galary","Capture Photo from Camera")
                picturedialog.setItems(pictureDialogItems){

                    dialog ,which->
                    when(which){
                        0->{
                            choosePhotoFromGalary()
                            Toast.makeText(this, "Select From galary",
                                Toast.LENGTH_SHORT).show()
                        }
                        1->{
                            Toast.makeText(this, "Capture Photo from Camera",
                                Toast.LENGTH_SHORT).show()}
                    }

                }
                picturedialog.show()

            }
        }
    }

    private fun choosePhotoFromGalary() {



        if (allPermissionsGranted()) {
            //viewModel.startCamera(enableVideoBtn, enableImageBtn, autoCapture)
            Toast.makeText(this, "Read and write are granted", Toast.LENGTH_SHORT).show()
        } else {
            showRationalDialogPermission()
           /* ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )*/
        }

    }



    private fun showRationalDialogPermission() {
        AlertDialog.Builder(this).setMessage("You have turned of the permission")
            .setPositiveButton("Got tot setting"){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }


    // Register a permission request launcher
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Check if all permissions are granted
            val allPermissionsGranted = permissions.all { it.value }

            if (allPermissionsGranted) {
                Toast.makeText(
                    this@AddHappyPlaceActivity,
                    "Read and write are granted",
                    Toast.LENGTH_SHORT
                ).show()

                // TODO: Implement the logic to choose a photo from the gallery here
            } else {
                // Handle the case where one or more permissions are denied
                // You can display an error message or take appropriate action
                // based on your app's requirements.
            }
        }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {


        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
               // viewModel.startCamera(enableVideoBtn, enableImageBtn, autoCapture)
            } else {
               /* UtilityFunctions.showToast(
                    this,
                    "Permissions not granted by the user.",
                )*/
                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun updateView() {
        val myformat = "dd.mm.yyyy"
        val sdf = SimpleDateFormat(myformat, Locale.getDefault())
        binding.etDate.setText(sdf.format(cal.time).toString())

    }
    fun addRecord(placeDao: PlaceDao) {
        val title=binding.etTitle.text.toString()
        val description=binding.etDescription.text.toString()
        val date=binding.etDate.text.toString()
        val location=binding.etDate.text.toString()
        val image=binding.ivPlaceImage.setImageResource(R.drawable.img)
        val imageByteArray = convertDrawableToByteArray(R.drawable.img)


        if (title.isNotEmpty() && description.isNotEmpty()&&date.isNotEmpty()&&location.isNotEmpty()) {

            //launching coroutine scope so that it would run in background
            lifecycleScope.launch {
                placeDao.insert(PlaceEntity(title = title, description = description,date=date, location = location, image = imageByteArray))
                Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_SHORT).show()

                //after adding the data we need to clear the record
                binding?.etTitle?.text?.clear()
                binding?.etDescription?.text?.clear()
                binding?.etDate?.text?.clear()
                binding?.etDate?.text?.clear()

//                binding?.etName?.text?.clear()
//                binding?.etEmailId?.text?.clear()

            }

        } else {
            Toast.makeText(applicationContext, "Email or name cannot be blank", Toast.LENGTH_SHORT)
                .show()

        }


    }
    private fun convertDrawableToByteArray(drawableResId: Int): ByteArray? {
        val bitmap = BitmapFactory.decodeResource(resources, drawableResId)
        return bitmap?.let { bitmapToByteArray(it) }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}