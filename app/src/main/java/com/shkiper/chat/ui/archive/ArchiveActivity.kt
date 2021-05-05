package com.shkiper.chat.ui.archive

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shkiper.chat.R
import com.shkiper.chat.ui.adapters.ArchiveAdapter
import com.shkiper.chat.ui.adapters.ArchiveItemTouchHelperCallback
import com.shkiper.chat.ui.chat.ChatActivity
import com.shkiper.chat.ui.main.MainActivity
import com.shkiper.chat.viewmodels.ArchiveViewModel
import kotlinx.android.synthetic.main.activity_archive.*

class ArchiveActivity : AppCompatActivity() {

    private lateinit var archiveAdapter: ArchiveAdapter
    private val viewModel: ArchiveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_archive)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if(item?.itemId == android.R.id.home){
            finish()
            true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun initViews(){

        archiveAdapter = ArchiveAdapter{
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(MainActivity.CHAT_ID, it.id)
            startActivity(intent)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ArchiveItemTouchHelperCallback(archiveAdapter){
            val id = it.id
            viewModel.restoreFromArchive(it.id)
            val snackBar: Snackbar = Snackbar.make(rv_archive_list, "${it.title} был восстановлен", Snackbar.LENGTH_LONG)
            snackBar.setAction("Undo"){
                viewModel.addToArchive(id)
            }
            snackBar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_archive_list)

        with(rv_archive_list){
            adapter = archiveAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }


    private fun initViewModel() {
        viewModel.getChats().observe(this, Observer { archiveAdapter.updateData(it) })
    }

}
