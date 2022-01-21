package com.example.providertest

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.contentValuesOf
import com.example.providertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var bookId: String? = null
    private val tag: String = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addData.setOnClickListener {
            val uri = Uri.parse("content://com.example.databasetest.provider/book")
            val values = contentValuesOf("name" to "A Clash of Kings",
                "author" to "George Martin", "pages" to 1040, "price" to 22.85)
            val newUri = contentResolver.insert(uri, values)
            bookId = newUri?.pathSegments?.get(1)
        }

        binding.queryData.setOnClickListener {
            val uri = Uri.parse("content://com.example.databasetest.provider/book")
            contentResolver.query(uri, null, null, null, null)?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex("name"))
                    val author = getString(getColumnIndex("author"))
                    val pages = getInt(getColumnIndex("pages"))
                    val price = getInt(getColumnIndex("price"))
                    Log.d(tag, "book name is $name")
                    Log.d(tag, "book author is $author")
                    Log.d(tag, "book pages is $pages")
                    Log.d(tag, "book price is $price")
                }
                close()
            }
        }

        binding.updateData.setOnClickListener {
            bookId?.let {
                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
                val values = contentValuesOf("name" to "A Storm of Swords", "pages" to 1216, "price" to 24.05)
                contentResolver.update(uri, values, null, null)
            }
        }

        binding.deleteData.setOnClickListener {
            bookId?.let {
                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
                contentResolver.delete(uri, null, null)
            }
        }


    }
}