package com.envyglit.chat.presentation.archive

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.envyglit.chat.R
import com.envyglit.chat.databinding.ActivityArchiveBinding
import com.envyglit.chat.util.extensions.showToast
import com.envyglit.chat.presentation.adapters.ChatAdapter
import com.envyglit.chat.presentation.adapters.ChatItemTouchHelperCallback
import com.envyglit.chat.presentation.chat.ChatActivity
import com.envyglit.chat.presentation.main.MainActivity
import com.envyglit.chat.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

    private val viewModel: ArchiveViewModel by viewModels()

    private lateinit var binding: ActivityArchiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_archive)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home){
            finish()
            true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun initViews(){

        chatAdapter = ChatAdapter {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(MainActivity.CHAT_ID, it.id)
            startActivity(intent)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter) {
            val id = it.id
            viewModel.restoreFromArchive(it.id)
            val snackBar: Snackbar = Snackbar.make(
                binding.rvArchiveList,
                "${it.title} был восстановлен",
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction("Undo") {
                viewModel.addToArchive(id)
            }
            snackBar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(binding.rvArchiveList)

        with(binding.rvArchiveList) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        viewModel.getChatData().observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    chatAdapter.updateData(it.data.orEmpty())
                }
                Status.LOADING -> {
                    //TODO
                }
                Status.ERROR -> {
                    showToast("Something went wrong")
                }
            }
        })
    }

}
