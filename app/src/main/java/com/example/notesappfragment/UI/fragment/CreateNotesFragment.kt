package com.example.notesappfragment.UI.fragment

import android.Manifest
import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notesappfragment.R
import com.example.notesappfragment.UI.FragmentListener
import com.example.notesappfragment.databinding.FragmentCreateNotesBinding
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


 class CreateNotesFragment private constructor() : Fragment(), View.OnClickListener {


    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 3

        fun newInstance(attach:Boolean,voice:Boolean,webLink:Boolean)=CreateNotesFragment().apply {
            isAttached=attach
            isvoice=voice
            isweblink=webLink

        }
    }
     private lateinit var fragmentListener: FragmentListener
     private  var isAttached=false
     private  var isvoice=false
     private  var isweblink=false
    lateinit var binding: FragmentCreateNotesBinding
    private var selectedImagePath: String = ""
    private lateinit var bottomsheet: BottomSheetDialog
    private lateinit var dailogBottomSheetDialog: BottomSheetDialog
    private var selectedNoteColor: String = "#FF822E"
    private var urlLink: String = ""

    val viewModel: NotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNotesBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)


        }

        binding.voice.setOnClickListener {


        }




        setHasOptionsMenu(true)
        return binding.root

    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         if(isAttached)
         {
             attach_image()
         }
         else if(isvoice)
         {
             voiceToText()
         }
         else if(isweblink)
         {
             showDailogOfUrl()
         }
     }


     fun voiceToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
    }


    private fun createNotes() {
        val title = binding.inputNoteTitle.text.toString()
        val subtitle = binding.inputNoteSubtitle.text.toString()
        val notetext = binding.inputNote.text.toString()

        val timeAndDate = SimpleDateFormat("dd MMMM yyyy HH:mm a", Locale.getDefault()).format(
            Date()
        )
        val notes = Notes(title, subtitle, timeAndDate, notetext)
        viewModel.addNotes(notes)
        notes.imagePath = selectedImagePath
        notes.color = selectedNoteColor
        notes.weblink = urlLink
        Toast.makeText(requireContext(), "Note is added Successfully", Toast.LENGTH_SHORT).show()
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction().apply {

            replace(R.id.fragmentContainerView, homeFragment).addToBackStack(null).commit()
        }


    }
    fun attach_image()
    {
        bottomsheet = BottomSheetDialog(requireContext())
        bottomsheet.setContentView(R.layout.bottomsheetdailog)
        bottomsheet.show()
        bottomsheet.findViewById<LinearLayout>(R.id.photoLinear)?.setOnClickListener(this)
        bottomsheet.findViewById<LinearLayout>(R.id.CameraLinear)?.setOnClickListener(this)
        bottomsheet.findViewById<LinearLayout>(R.id.Url)?.setOnClickListener(this)
        bottomsheet.findViewById<TextView>(R.id.cancel)?.setOnClickListener(this)
        bottomsheet.findViewById<LinearLayout>(R.id.Files)?.setOnClickListener(this)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> {
                createNotes()

            }
            R.id.menu_attach -> {
               attach_image()
            }
            R.id.menu_color -> {
                bottomsheet = BottomSheetDialog(requireContext())
                bottomsheet.setContentView(R.layout.pick_color)
                bottomsheet.show()

                val imagecolor1 = bottomsheet.findViewById<ImageView>(R.id.imagecolor1)!!
                val imagecolor2 = bottomsheet.findViewById<ImageView>(R.id.imagecolor2)!!
                val imagecolor3 = bottomsheet.findViewById<ImageView>(R.id.imagecolor3)!!
                val imagecolor4 = bottomsheet.findViewById<ImageView>(R.id.imagecolor4)!!
                val imagecolor5 = bottomsheet.findViewById<ImageView>(R.id.imagecolor5)!!
                val imagecolor6 = bottomsheet.findViewById<ImageView>(R.id.imagecolor6)!!
                val imagecolor7 = bottomsheet.findViewById<ImageView>(R.id.imagecolor7)!!
                val imagecolor8 = bottomsheet.findViewById<ImageView>(R.id.imagecolor8)!!
                val imagecolor9 = bottomsheet.findViewById<ImageView>(R.id.imagecolor9)!!
                val imagecolor10 = bottomsheet.findViewById<ImageView>(R.id.imagecolor10)!!

                imagecolor1.setOnClickListener {
                    selectedNoteColor = "#1F1F1F"
                    imagecolor1.setImageResource(R.drawable.ic_done)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }

                imagecolor2.setOnClickListener {
                    selectedNoteColor = "#9fbcf4"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(R.drawable.ic_done)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor3.setOnClickListener {
                    selectedNoteColor = "#ffba59"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(R.drawable.ic_done)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor4.setOnClickListener {
                    selectedNoteColor = "#9fbcf4"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(R.drawable.ic_done)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor5.setOnClickListener {
                    selectedNoteColor = "#ff99e5"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(R.drawable.ic_done)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor6.setOnClickListener {
                    selectedNoteColor = "#ffe065"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(R.drawable.ic_done)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor7.setOnClickListener {
                    selectedNoteColor = "#c2a6ff"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(R.drawable.ic_done)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor8.setOnClickListener {
                    selectedNoteColor = "#bcf493"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(R.drawable.ic_done)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor9.setOnClickListener {
                    selectedNoteColor = "#87f4b5"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(R.drawable.ic_done)
                    imagecolor10.setImageResource(0)
                    setSubtitleIndicatorColor()
                }
                imagecolor10.setOnClickListener {
                    selectedNoteColor = "#8ce2ff"
                    imagecolor1.setImageResource(0)
                    imagecolor2.setImageResource(0)
                    imagecolor3.setImageResource(0)
                    imagecolor4.setImageResource(0)
                    imagecolor5.setImageResource(0)
                    imagecolor6.setImageResource(0)
                    imagecolor7.setImageResource(0)
                    imagecolor8.setImageResource(0)
                    imagecolor9.setImageResource(0)
                    imagecolor10.setImageResource(R.drawable.ic_done)
                    setSubtitleIndicatorColor()
                }


            }


        }
        return super.onOptionsItemSelected(item)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun attachImageFiles() {
        if (checkAndRequestGalleryPermissions()) {
            takePictureFromGallery()
        }
    }

    private fun attachImageFromCamera() {
        if (checkAndRequestCameraPermissions()) {
            takePictureFromCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera()
        } else if (requestCode == 30 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromGallery()
        } else Toast.makeText(requireActivity(), "Permission not Granted", Toast.LENGTH_SHORT)
            .show()
    }


    private fun checkAndRequestCameraPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val galleryPermission: Int =
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            if (galleryPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA), 20
                )
                return false

            }
        }
        return true
    }


    private fun checkAndRequestGalleryPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val galleryPermission = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (galleryPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    30

                )
                return false
            }
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                val selectedImageUri: Uri? = data?.data
                binding.imageNote.setImageURI(selectedImageUri)
                binding.imageNote.visibility = View.VISIBLE
                selectedImagePath = selectedImageUri?.let { getPathFromUri(it) }.toString()
                bottomsheet.dismiss()

            }
            2 -> {
                val bundle = data?.extras
                val bitmapImage = bundle!!["data"] as Bitmap?
                binding.imageNote.setImageBitmap(bitmapImage)
                context?.cacheDir
                binding.imageNote.visibility = View.VISIBLE
                selectedImagePath = bitmapImage?.let { getImagePath(it) }!!
                bottomsheet.dismiss()

            }
            3 -> {
                if (resultCode === RESULT_OK && attr.data != null) {
                    val result: ArrayList<String> = data?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )!!
                    binding.inputNote.setText(
                        Objects.requireNonNull(result)[0]
                    )
                }

            }
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.photoLinear -> attachImageFiles()
            R.id.CameraLinear -> attachImageFromCamera()
            R.id.cancel -> bottomsheet.dismiss()
            R.id.Url -> showDailogOfUrl()
            R.id.btncancel -> dailogBottomSheetDialog.dismiss()
            R.id.btnSubmit -> validateUrl()
            R.id.Files -> voiceToText()
        }


    }

    private fun validateUrl() {
        val eturl = dailogBottomSheetDialog.findViewById<EditText>(R.id.etUrl)!!
        val url = eturl.text.toString()
        if (isValidURl(url)) {
            urlLink = url
            dailogBottomSheetDialog.dismiss()
            binding.textwebUrl.text = urlLink
            binding.layoutWebUrl.visibility = View.VISIBLE
            if(!isweblink)
            {
                bottomsheet.dismiss()
            }

        } else {
            eturl.error = "Pls Enter the Valid URL"
            eturl.setTextColor(Color.RED)
        }


    }

     fun showDailogOfUrl() {
        dailogBottomSheetDialog = BottomSheetDialog(requireContext())
        dailogBottomSheetDialog.setContentView(R.layout.add_url)


        dailogBottomSheetDialog.show()
        val eturl = dailogBottomSheetDialog.findViewById<EditText>(R.id.etUrl)!!
        dailogBottomSheetDialog.findViewById<TextView>(R.id.btncancel)?.setOnClickListener(this)
        dailogBottomSheetDialog.findViewById<TextView>(R.id.btnSubmit)?.setOnClickListener(this)
        eturl.addTextChangedListener {
            eturl.setTextColor(Color.BLACK)
        }

    }


    private fun takePictureFromGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 1)
    }

    private fun takePictureFromCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(requireActivity().packageManager) != null) {

            startActivityForResult(takePicture, 2)
        }
    }


    private fun getImagePath(inImage: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                inImage,
                "Title",
                null
            )
        return path
    }

    private fun getPathFromUri(contentUri: Uri): String {
        val filePath: String
        val cursor: Cursor? =
            requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path.toString()
        } else {
            cursor.moveToFirst()
            val index: Int = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun setSubtitleIndicatorColor() {
        binding.viewSubtitleIndictor.setBackgroundColor(Color.parseColor(selectedNoteColor))
    }


    fun isValidURl(url: String?): Boolean {

        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            false
        }
    }



}
