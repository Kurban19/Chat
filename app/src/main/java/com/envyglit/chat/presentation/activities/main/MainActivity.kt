package com.envyglit.chat.presentation.activities.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.envyglit.chat.databinding.ActivityMainBinding
import com.envyglit.chat.presentation.ChatApp
import com.envyglit.chat.presentation.adapters.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter

//    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        WindowCompat.setDecorFitsSystemWindows(window, false)


//        initViews()
//        setupObserver()

        setContent {
            ChatApp()
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
//        menuInflater.inflate(R.menu.menu_search, menu)
//        menuInflater.inflate(R.menu.menu_profile, menu)
//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.isIconifiedByDefault = true
//        searchView.isFocusable = true
//        searchView.isIconified = false
//        searchView.requestFocusFromTouch()
//        searchView.queryHint ="Введите имя пользователя"
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                viewModel.handleSearchQuery(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                viewModel.handleSearchQuery(newText)
//                return true
//            }
//
//        })
//
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_profile -> {
//                val intent = Intent(this, ProfileActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> {
//                return super.onOptionsItemSelected(item)
//            }
//        }
//    }
//
//    private fun initViews(){
//        chatAdapter = ChatAdapter{
//            if (it.chatType == ChatType.ARCHIVE) {
//                val intent = Intent(this, ArchiveActivity::class.java)
//                startActivity(intent)
//            } else {
//                val intent = Intent(this, ChatActivity::class.java)
//                intent.putExtra(CHAT_ID, it.id)
//                startActivity(intent)
//            }
//
//        }
//
//        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
//            val id = it.id
//            viewModel.addToArchive(it.id)
//            val snackBar: Snackbar = Snackbar.make(
//                binding.rvChatList,
//                "Вы точно хотите добавить ${it.title} в архив?",
//                Snackbar.LENGTH_LONG
//            )
//            snackBar.setAction("Отмена") {
//                viewModel.restoreFromArchive(id)
//            }
//            snackBar.show()
//        }
//
//
//        val touchHelper = ItemTouchHelper(touchCallback)
//        touchHelper.attachToRecyclerView(binding.rvChatList)
//
//        with(binding.rvChatList) {
//            adapter = chatAdapter
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            addItemDecoration(divider)
//        }
//        binding.fab.setOnClickListener {
//            val intent = Intent(this, UsersActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    private fun setupObserver() {
//        viewModel.getChatData().observe(this) {
//            when (it.status) {
//                com.envyglit.core.ui.utils.Status.SUCCESS -> {
//                    it.data?.let { chats ->
//                        binding.progressBar.gone()
//                        binding.rvChatList.visible()
//                        chatAdapter.updateData(chats)
//                    }
//                }
//                com.envyglit.core.ui.utils.Status.LOADING -> {
//                    binding.progressBar.visible()
//                    binding.rvChatList.gone()
//                }
//                com.envyglit.core.ui.utils.Status.ERROR -> {
//                    binding.progressBar.gone()
//                    showToast("Something went wrong")
//                }
//            }
//        }
//
//    }
//
    companion object {
        const val CHAT_ID = "chat_id"
    }

}
