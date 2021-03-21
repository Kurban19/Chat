package ru.skillbranch.chat.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.chat.App
import ru.skillbranch.chat.R
import ru.skillbranch.chat.firebase.FireBaseUsers
import ru.skillbranch.chat.ui.adapters.ChatAdapter
import ru.skillbranch.chat.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.chat.ui.archive.ArchiveActivity
import ru.skillbranch.chat.ui.chat.ChatActivity
import ru.skillbranch.chat.ui.users.UsersActivity
import ru.skillbranch.chat.ui.login.LoginActivity
import ru.skillbranch.chat.viewmodels.MainViewModel
import ru.skillbranch.chat.viewmodels.UsersViewModel
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var chatAdapter: ChatAdapter
    @Inject
    lateinit var viewModel: MainViewModel

    companion object{
        const val CHAT_ID = "chat_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as App).appComponent.inject(this)
        FireBaseUsers.getUsers()
        initToolbar()
        initViews()
        initViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_search, menu)
        menuInflater.inflate(R.menu.menu_archive, menu)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_archive -> {
                val intent = Intent(this, ArchiveActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_profile -> {

                AuthUI.getInstance()
                        .signOut(this@MainActivity)
                        .addOnCompleteListener {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                FireBaseUsers.updateCurrentUser(Date(), false)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews(){
        chatAdapter = ChatAdapter{
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(CHAT_ID, it.id)
            startActivity(intent)
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

    private fun initViewModel()  {
        viewModel.getChatData().observe(this, Observer {
//            if(it.isEmpty()){
//                toggleProgressBar(true)
//            }
//            else{
//                toggleProgressBar(false)
//            }
            chatAdapter.updateData(it)
        })
    }

    private fun toggleProgressBar(visible: Boolean){
        if (visible){
            rv_chat_list.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
        }
        else{
            rv_chat_list.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }

    }

}
