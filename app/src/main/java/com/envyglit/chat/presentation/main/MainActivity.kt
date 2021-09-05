package com.envyglit.chat.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.envyglit.chat.R
import com.envyglit.chat.databinding.ActivityMainBinding
import com.envyglit.chat.util.extensions.showToast
import com.envyglit.chat.domain.entities.data.ChatType
import com.envyglit.chat.presentation.adapters.ChatAdapter
import com.envyglit.chat.presentation.adapters.ChatItemTouchHelperCallback
import com.envyglit.chat.presentation.archive.ArchiveActivity
import com.envyglit.chat.presentation.chat.ChatActivity
import com.envyglit.chat.presentation.profile.ProfileActivity
import com.envyglit.chat.presentation.users.UsersActivity
import com.envyglit.chat.util.Status
import com.envyglit.chat.util.extensions.gone
import com.envyglit.chat.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val CHAT_ID = "chat_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        initToolbar()
        initViews()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_search, menu)
        menuInflater.inflate(R.menu.menu_profile, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
        searchView.queryHint ="Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initViews(){
        chatAdapter = ChatAdapter{
            if (it.chatType == ChatType.ARCHIVE) {
                val intent = Intent(this, ArchiveActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra(CHAT_ID, it.id)
                startActivity(intent)
            }

        }
        
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val id = it.id
            viewModel.addToArchive(it.id)
            val snackBar: Snackbar = Snackbar.make(
                binding.rvChatList,
                "Вы точно хотите добавить ${it.title} в архив?",
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction("Отмена") {
                viewModel.restoreFromArchive(id)
            }
            snackBar.show()
        }


        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(binding.rvChatList)

        with(binding.rvChatList) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupObserver() {
        viewModel.getChatData().observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.rvChatList.visible()
                    chatAdapter.updateData(it.data!!)
                }
                Status.LOADING -> {
                    binding.progressBar.visible()
                    binding.rvChatList.gone()
                }
                Status.ERROR -> {
                    binding.progressBar.gone()
                    showToast("Something went wrong")
                }
            }
        })

    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }

}