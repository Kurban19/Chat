package com.shkiper.chat.ui.archive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shkiper.chat.App
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_archive.toolbar
import com.shkiper.chat.R
import com.shkiper.chat.ui.adapters.ArchiveAdapter
import com.shkiper.chat.ui.adapters.ArchiveItemTouchHelperCallback
import com.shkiper.chat.viewmodels.ArchiveViewModel
import javax.inject.Inject

class ArchiveActivity : AppCompatActivity() {

    private lateinit var archiveAdapter: ArchiveAdapter
    @Inject
    lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        (applicationContext as App).appComponent.inject(this)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
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
            Snackbar.make(rv_archive_list,"Click on ${it.title}", Snackbar.LENGTH_LONG).show()
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
