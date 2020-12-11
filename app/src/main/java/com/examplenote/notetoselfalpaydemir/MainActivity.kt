package com.examplenote.notetoselfalpaydemir

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DefaultItemAnimator




class MainActivity : AppCompatActivity() {

    //private var


    private var mSerializer: JSONSerializer? = null
    private var noteList: ArrayList<Note>? = null  //private val noteList  ArrayList<Note>


    private var recyclerView: RecyclerView? = null
    private var adapter: NoteAdapter? = null



    private var showDividers: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val dialog = DialogNewNote()
            dialog.show(supportFragmentManager, "")
        }

        mSerializer = JSONSerializer("NoteToSelf.json",
                applicationContext)

        try {
            noteList = mSerializer!!.load()
        } catch (e: Exception) {
            noteList = ArrayList()
            Log.e("Error loading notes: ", "", e)
        }

        recyclerView =
                findViewById<View>(R.id.recyclerView) as RecyclerView

        adapter = NoteAdapter(this, this.noteList!!)
        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        // Add a neat dividing line between items



        recyclerView!!.adapter = adapter // set the adapter
    }

    fun createNewNote(n: Note) {

        // tempNote = n
        noteList!!.add(n)
        adapter!!.notifyDataSetChanged()

    }

    fun showNote(noteToShow: Int) {
        val dialog = DialogShowNote()
        dialog.sendNoteSelected(noteList!![noteToShow])
        dialog.show(supportFragmentManager, "")
    }

    private fun saveNotes() {
        try {
            mSerializer!!.save(this.noteList!!)

        } catch (e: Exception) {
            Log.e("Error Saving Notes", "", e)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here.




        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this,
                        SettingsActivity::class.java)

                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()

        saveNotes()
    }


    override fun onResume() {
        super.onResume()

        val prefs =
                getSharedPreferences(
                        "Note to self",
                        Context.MODE_PRIVATE)

        showDividers = prefs.getBoolean(
                "dividers", true)

        // Add a neat dividing line
        if (showDividers)
            recyclerView!!.addItemDecoration(
                DividerItemDecoration(
                        this, LinearLayoutManager.VERTICAL))
        else { // check some dividers

            if (recyclerView!!.itemDecorationCount > 0)
                recyclerView!!.removeItemDecorationAt(0)
        }

    }

}
