package com.shkiper.chat.ui.main

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
import com.shkiper.chat.R
import com.shkiper.chat.extensions.gone
import com.shkiper.chat.extensions.showToast
import com.shkiper.chat.extensions.visible
import com.shkiper.chat.model.data.ChatType
import com.shkiper.chat.ui.adapters.ChatAdapter
import com.shkiper.chat.ui.adapters.ChatItemTouchHelperCallback
import com.shkiper.chat.ui.archive.ArchiveActivity
import com.shkiper.chat.ui.chat.ChatActivity
import com.shkiper.chat.ui.profile.ProfileActivity
import com.shkiper.chat.ui.users.UsersActivity
import com.shkiper.chat.util.Status
import com.shkiper.chat.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var chatAdapter: ChatAdapter
    private val viewModel: MainViewModel by viewModels()

    companion object{
        const val CHAT_ID = "chat_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            val snackBar: Snackbar = Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
            snackBar.setAction("Отмена"){
                viewModel.restoreFromArchive(id)
            }
            snackBar.show()
        }


        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }
        fab.setOnClickListener{
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupObserver() {
        viewModel.getChatData().observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    progress_bar.gone()
                    rv_chat_list.visible()
                    chatAdapter.updateData(it.data!!)
                }
                Status.LOADING -> {
                    progress_bar.visible()
                    rv_chat_list.gone()
                    showToast("Loading")
                }
                Status.ERROR -> {
                    progress_bar.gone()
                    showToast("Something went wrong")
                }
            }
        })

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main)
    }


}
