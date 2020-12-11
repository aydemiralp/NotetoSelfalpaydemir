package com.examplenote.notetoselfalpaydemir


import android.content.Context

import org.json.JSONArray
import org.json.JSONException

import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import org.json.JSONTokener

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.ArrayList

class JSONSerializer(
        private val filename: String,
        private val context: Context) {
    //rest of the code for the class go here

    @Throws(IOException::class, JSONException::class)
    fun save(notes: List<Note>) {

        // Make array in JSON format
        val jArray = JSONArray()


        for (n in notes)
            jArray.put(n.convertToJSON())


        var writer: Writer? = null
        try {
            val out = context.openFileOutput(filename,
                    Context.MODE_PRIVATE)

            writer = OutputStreamWriter(out)
            writer.write(jArray.toString())
        } finally {
            if (writer != null) {

                writer.close()
            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun load(): ArrayList<Note> {
        val noteList = ArrayList<Note>()
        var reader: BufferedReader? = null

        try {

            val `in` = context.openFileInput(filename)
            reader = BufferedReader(InputStreamReader(`in`))
            val jsonString = StringBuilder()

            for (line in reader.readLine()) {
                jsonString.append(line)
            }

            val jArray = JSONTokener(jsonString.toString()).
                    nextValue() as JSONArray

            for (i in 0 until jArray.length()) {
                noteList.add(Note(jArray.getJSONObject(i)))
            }
        } catch (e: FileNotFoundException) {
            // we will ignore this one

        } finally {// always run
            reader!!.close()
        }

        return noteList
    }


}// End class