package com.example.notesappfragment.UI.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.notesappfragment.R
import com.example.notesappfragment.databinding.FragmentPerviewBinding
import com.example.notesappfragment.entities.Notes
import java.text.SimpleDateFormat
import java.util.*


class PreviewFragment : Fragment() {

    private lateinit var binding: FragmentPerviewBinding
    private lateinit var notes: Notes

    companion object {
        private const val SAVED_NOTES = "notes"
        fun newInstance(data: Notes) = PreviewFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(SAVED_NOTES, data)
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPerviewBinding.inflate(layoutInflater, container, false)
        val bundle: Bundle = requireArguments()
        notes = bundle.getParcelable(SAVED_NOTES)!!
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }
        binding.textDateTime.text =
            SimpleDateFormat("dd-MM-yy/HH:mm a", Locale.getDefault()).format(
                Date()
            )
        binding.etTitle.setText(notes.title)
        binding.etsubtitle.setText(notes.subtitle)
        binding.etdescrption.setText(notes.noteText)
        binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(notes.imagePath))
        binding.imageNote.visibility = View.VISIBLE
        if (notes.weblink != "") {
            binding.textwebUrl.setText(notes.weblink)
            binding.textwebUrl.visibility = View.VISIBLE
        }

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.preview_menu, menu)

            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.edit -> {
                        binding.apply {
                            this.etdescrption.requestFocus()
                            this.etsubtitle.requestFocus()
                            this.textwebUrl.requestFocus()
                        }

                        true
                    }
                    android.R.id.home -> {
                        requireActivity().onBackPressed()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}