package com.example.notesappfragment.UI.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.notesappfragment.R
import com.example.notesappfragment.databinding.FragmentEditNotesFragmentBinding
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

 class EditNotesFragmentFragment private constructor(): Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditNotesFragmentBinding
private lateinit var notes:Notes
    private val viewModel: NotesViewModel by viewModels()

private lateinit var bottomSheetDialog: BottomSheetDialog
companion object
{ private const val SAVED_NOTES="notes"
    fun newInstance( data:Notes)=EditNotesFragmentFragment().apply {
        val bundle= Bundle()
        bundle.putParcelable(SAVED_NOTES,data)
        arguments=bundle
    }
}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditNotesFragmentBinding.inflate(layoutInflater, container, false)
        val bundle: Bundle = requireArguments()
        notes = bundle.getParcelable(SAVED_NOTES)!!
        (requireActivity() as AppCompatActivity ).supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }
        binding.textDateTime.text =
            SimpleDateFormat("dd-MM-yyyy /HH:mm a", Locale.getDefault()).format(
                Date()
            )
        binding.etTitle.setText(notes.title)
        binding.etsubtitle.setText(notes.subtitle)
        binding.etdescrption.setText(notes.noteText)
        binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(notes.imagePath))
        binding.imageNote.visibility = View.VISIBLE
        if (notes.weblink != "") {
            binding.textwebUrl.text = notes.weblink
            binding.textwebUrl.visibility = View.VISIBLE
        }
        view?.findViewById<Toolbar>(R.id.my_toolbar)?.hasExpandedActionView()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun updateNotes(id: Int) {
        val title = binding.etTitle.text.toString()
        val subTitle = binding.etsubtitle.text.toString()
        val des = binding.etdescrption.text.toString()
        val date = binding.textDateTime.text.toString()


        val notes = Notes(title, subTitle, date, des, id = id)
        viewModel.updateNote(notes)
        Toast.makeText(requireContext(), "Notes is Updated ", Toast.LENGTH_SHORT).show()
        navigateHomeFragment()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> {
                updateNotes(notes.id)
            }
            R.id.menu_delete -> {
               bottomSheetDialog = BottomSheetDialog(requireContext())
               bottomSheetDialog.setContentView(R.layout.dailog_delete)
                bottomSheetDialog.findViewById<TextView>(R.id.yes)?.setOnClickListener(this)
              bottomSheetDialog.findViewById<TextView>(R.id.No)?.setOnClickListener {
                    bottomSheetDialog.dismiss()

                }

                bottomSheetDialog.show()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete, menu)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.yes -> {
                viewModel.deleteNote(notes.id)
                bottomSheetDialog.dismiss()
                navigateHomeFragment()


            }


        }
    }

    private fun navigateHomeFragment() {
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, homeFragment).addToBackStack(null).commit()

        }
    }
}