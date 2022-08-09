package com.example.notesappfragment.UI.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesappfragment.R
import com.example.notesappfragment.UI.adaptorimport.Notesadaptor
import com.example.notesappfragment.databinding.FragmentHomeBinding
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.helper.IndentKeys.CURRENT_MODE
import com.example.notesappfragment.helper.IndentKeys.LIST_VIEW
import com.example.notesappfragment.helper.IndentKeys.SHARE_PREFERENCE_OF_CURRENT_DATA
import com.example.notesappfragment.helper.IndentKeys.STAGGERED_GRID
import com.example.notesappfragment.lisenter.NoteListener
import com.example.notesappfragment.viewModel.NotesViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(),NoteListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: Notesadaptor
    private lateinit var createNotesFragment: CreateNotesFragment
    private lateinit var bottomAppBar: BottomAppBar
    private var currentMode: Int = LIST_VIEW
    private val viewModel: NotesViewModel by viewModels()
    var oldMyNotes = arrayListOf<Notes>()
    private lateinit var rootView: ConstraintLayout
    private lateinit var menuObj: Menu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        disableNavigateUp()
        viewModel.getNotes().observe(viewLifecycleOwner) {
            oldMyNotes = it as ArrayList<Notes>
            displayNotes(it)


        }

        binding.floatingActionAddNotesBtn.setOnClickListener {
            navigateFragment(attach = false, voice = false, webLink = false)
        }
        invalidateOptionsMenu(requireActivity())
        rootView = binding.root
        return rootView
    }


    private fun navigateFragment(attach: Boolean, voice: Boolean, webLink: Boolean) {
        createNotesFragment = CreateNotesFragment.newInstance(attach, voice, webLink)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, createNotesFragment).addToBackStack(null)
                .commit()

        }
    }


    private fun setActionOnViews() {
        bottomAppBar.setOnMenuItemClickListener()
        {
            when (it.itemId) {
                R.id.menu_image -> {
                    navigateFragment(attach = true, voice = false, webLink = false)
                    true
                }
                R.id.menu_voice -> {
                    navigateFragment(attach = false, voice = true, webLink = false)

                    true
                }
                R.id.menu_web_link -> {
                    navigateFragment(attach = false, voice = false, webLink = true)
                    true
                }
                else -> false
            }
        }
    }

    private fun notesFiltering(p0: String) {
        val newFilteredList = arrayListOf<Notes>()

        for (i in oldMyNotes) {
            if (i.title.contains(p0) || i.subtitle.contains(p0)) {
                newFilteredList.add(i)

            }
            adapter.filtering(newFilteredList)
        }

    }

    private fun displayNotes(list: ArrayList<Notes>) {

        if (currentMode == LIST_VIEW) {
            setAdaptor(null, LinearLayoutManager(requireContext()), true, list)
            activity

        } else if (currentMode == STAGGERED_GRID) {
            setAdaptor(
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                null,
                false,
                list
            )
        }
    }


    private fun setAdaptor(
        staggeredGridLayoutManager: StaggeredGridLayoutManager?,
        linearLayoutManager: LinearLayoutManager?,
        isLinear: Boolean,
        list: ArrayList<Notes>
    ) {
        if (isLinear) {
            binding.recyclerView.layoutManager =
                linearLayoutManager
        } else {
            binding.recyclerView.layoutManager = staggeredGridLayoutManager
        }
        adapter = Notesadaptor(requireActivity(), viewLifecycleOwner, list, notesSelectedLisenter,this)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val fab: FloatingActionButton = binding.floatingActionAddNotesBtn
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10 && fab.isShown) {
                    Log.i("arun", "$dy")
                    fab.hide()
                }

                if (dy < -10 && !fab.isShown) {
                    Log.i("arun", "$dy")
                    fab.show()
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    fab.show()
                }
            }

        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedNotes: Notes =
                    oldMyNotes[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                Log.i("arun", "$deletedNotes  $position")

                oldMyNotes.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)


                Snackbar.make(rootView, "Note   ${deletedNotes.title} Deleted ", Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("UNDO")
                        {
                            Log.i("arun", " snack bar $deletedNotes  $position")
                            oldMyNotes.add(position, deletedNotes)
                            viewModel.addNotes(deletedNotes)
                            adapter.notifyItemInserted(position)
                        }
                        show()
                    }
                viewModel.deleteNote(deletedNotes.id)

            }
        }
        ).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = adapter


        requireActivity().actionBar

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferences = requireActivity().getSharedPreferences(
            SHARE_PREFERENCE_OF_CURRENT_DATA,
            Context.MODE_PRIVATE
        )
        currentMode = preferences.getInt(CURRENT_MODE, LIST_VIEW)
        bottomAppBar = requireView().findViewById(R.id.main_bottom_app_bar)
        setActionOnViews()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
             menu.clear()
                menuInflater.inflate(R.menu.search, menu)
                menuObj = menu
                val item = menu.findItem(R.id.menu_Search)
                val searchView = item.actionView as SearchView
                searchView.queryHint = "Enter the Notes Here ...."
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        notesFiltering(p0!!)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.menu_List -> {
                        currentMode = if (currentMode == LIST_VIEW) {
                            STAGGERED_GRID

                        } else {
                            LIST_VIEW

                        }
                        displayNotes(oldMyNotes)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        preferences.edit().apply {
            putInt(CURRENT_MODE, currentMode)
            apply()
        }
        super.onDestroyView()

    }

    private val notesSelectedLisenter: (Notes) -> Unit = {
        parentFragmentManager.beginTransaction().apply {

            replace(
                R.id.fragmentContainerView,
                PreviewFragment.newInstance(it)
            ).addToBackStack(null)
                .commit()
        }
    }


    private fun disableNavigateUp() {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)

        }
    }

    override fun onNoteClicked(view: View?, note: Notes, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onNoteLongClicked(view: View?, note: Notes, position: Int) {

    }


}
