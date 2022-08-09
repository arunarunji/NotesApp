package com.example.notesappfragment.UI.adaptorimport


import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappfragment.R
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.viewModel.NotesViewModel


class Notesadaptor(
    val context: FragmentActivity,
    val viewLifecycleOwner: LifecycleOwner,
    var notes: ArrayList<Notes>,
    val notesSelectedLisenter: (Notes) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    var isEnable = false
    var isSelectAll = false
    var selectList: ArrayList<Notes> = ArrayList()
    private lateinit var viewModel: NotesViewModel
    private val differentCallback = object : DiffUtil.ItemCallback<Notes>() {
        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }


    fun filtering(newFilteredList: ArrayList<Notes>) {
        notes = newFilteredList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        viewModel = ViewModelProvider(context)[NotesViewModel::class.java]
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
val pos=position
        val data: Notes = notes[pos]
        holder.setNote(data)
        holder.itemView.setOnClickListener {

            if (isEnable) {
                clickItem(holder)
            } else {
                notesSelectedLisenter(data)
            }

        }

        holder.itemView.setOnLongClickListener {
            if (!isEnable) {
                val callback = object : ActionMode.Callback {
                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        val menuInflater: MenuInflater = mode!!.menuInflater
                        menuInflater.inflate(R.menu.muliptle_delete, menu)
                        return true
                    }

                    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        isEnable = true
                        clickItem(holder)
                        viewModel.getNotes().observe(viewLifecycleOwner) {
                            mode!!.title = "${notes[pos].title} is selected"
                        }
                        return true
                    }

                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        when (item!!.itemId) {
                            R.id.menu_md_delete -> {
                                for (note in selectList) {

                                    notes.remove(note)
                                }
                                if (notes.size == 0) {
                                    // when array list is empty
                                    // visible text view
//                                    tvEmpty.setVisibility(View.VISIBLE);
                                }
                                mode!!.finish()
                            }
                            R.id.menu_md_selectAll -> {
                                if (selectList.size == notes.size) {
                                    isSelectAll = false
                                    selectList.clear()

                                } else {
                                    isSelectAll = true
                                    selectList.clear()
                                    selectList.addAll(notes)
                                }
                                notifyDataSetChanged()
                            }
                        }
                        return true

                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {
                        isEnable = false
                        isSelectAll = false
                        selectList.clear()
                        notifyDataSetChanged()
                    }

                }
                context.startActionMode(callback)
            } else {
                clickItem(holder);
            }
            return@setOnLongClickListener true
        }
        if (isSelectAll) {
            holder.itemView.findViewById<ImageView>(R.id.checkbox).visibility = View.VISIBLE
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.findViewById<ImageView>(R.id.checkbox).visibility = View.GONE
            holder.itemView.findViewById<LinearLayout>(R.id.layoutNote).setBackgroundColor(R.drawable.background_note)
        }

    }

    private fun clickItem(holder: ViewHolder) {
        val note = notes[holder.adapterPosition]
        if (holder.itemView.findViewById<ImageView>(R.id.checkbox).visibility == View.GONE) {
            holder.itemView.findViewById<ImageView>(R.id.checkbox).visibility = View.VISIBLE
            holder.itemView.findViewById<LinearLayout>(R.id.layoutNote).setBackgroundColor(Color.LTGRAY)
            selectList.add(note)
        } else {
            holder.itemView.findViewById<ImageView>(R.id.checkbox).visibility = View.GONE
            holder.itemView.findViewById<LinearLayout>(R.id.layoutNote).setBackgroundColor(R.drawable.background_note)
            selectList.remove(note)
        }

    }


    override fun getItemCount(): Int {
        return notes.size
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val texttitle = itemView.findViewById<TextView>(R.id.texttitle)
    val textsubtitle = itemView.findViewById<TextView>(R.id.textsubtitle)
    val textdateTime = itemView.findViewById<TextView>(R.id.textDateTime)
    val linearlayout = itemView.findViewById<LinearLayout>(R.id.layoutNote)
    val roundImage = itemView.findViewById<ImageView>(R.id.imageNoteround)

    fun setNote(note: Notes) {
        texttitle.text = note.title
        if (note.subtitle.trim().isEmpty())
            textsubtitle.visibility = View.GONE
        else {
            textsubtitle.text = note.subtitle
        }
        textdateTime.text = note.datetime
//        val gradientDrawable = linearlayout.background as GradientDrawable
        if (note.color.isNotEmpty())
           linearlayout.setBackgroundColor(Color.parseColor(note.color))
        else
            linearlayout.setBackgroundColor(Color.parseColor("#FF822E"))
        if (note.imagePath != "") {
            roundImage.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
            roundImage.visibility = View.VISIBLE
        }

    }

}
