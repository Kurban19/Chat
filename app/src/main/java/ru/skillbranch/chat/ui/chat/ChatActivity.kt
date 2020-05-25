package ru.skillbranch.chat.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.utils.AppConstants
import ru.skillbranch.chat.viewmodels.ChatViewModel
import ru.skillbranch.chat.viewmodels.ProfileViewModel


class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()
        initViews()
        initViewModel()
    }



//    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
//        menuInflater.inflate(R.menu.menu_switch, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return if(item?.itemId == R.id.action_switch){
//            Toast.makeText(this, "Pressed", Toast.LENGTH_LONG).show()
//            viewModel.switchTheme()
//            true
//        }
//        else {
//            return super.onOptionsItemSelected(item)
//        }
//    }

    private fun initViews(){
        //val chat = ChatRepository.find(intent.getStringExtra(AppConstants.CHAT_ID)!!)
        //chat ?: return


    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.CHAT_NAME)
    }
}
