package ru.skillbranch.chat.ui.group

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.firebase.FireBase
import ru.skillbranch.chat.models.data.UserItem
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.repositories.UsersRepository
import ru.skillbranch.chat.ui.adapters.UserAdapter
import ru.skillbranch.chat.viewmodels.GroupViewModel

class GroupActivity : AppCompatActivity() {

    companion object{
        const val TAG = "GroupActivity"
    }

    private lateinit var usersAdapter: UserAdapter
    private lateinit var viewModel:GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        initToolbar()
        initViews()
        initViewModel() }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.isIconifiedByDefault = true;
        searchView.isFocusable = true;
        searchView.isIconified = false;
        searchView.requestFocusFromTouch();
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
        return if(item?.itemId == android.R.id.home){
            finish()
            true
            }
        else {
            return super.onOptionsItemSelected(item)
        }
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        usersAdapter = UserAdapter {
//            val user = UsersRepository.findUser(it.id)
//            //viewModel.handleSelectedItem(it.id)
//            FireBase.getOrCreateChat(user)
//            finish()
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        with(rv_user_list){
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener{
            viewModel.handleCreatedGroup()
            finish()
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        viewModel.getUsersData().observe(this, Observer { usersAdapter.updateData(it) })
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size>1)
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if(isShow) fab.show()
        else fab.hide()

    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            text = user.fullName
            chipIcon = resources.getDrawable(R.drawable.avatar_default, theme)
            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(Color.WHITE)
            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.color_primary_light))
            setTextColor(Color.WHITE)
        }
        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>){
        chip_group.visibility = if(listUsers.isEmpty()) View.GONE else View.VISIBLE
        val users = listUsers
                .associateBy { user -> user.id }
                .toMutableMap()

        val views = chip_group.children.associateBy { view -> view.tag }

        for((k,v) in views){
            if(users.containsKey(k)) chip_group.removeView(v)
            else users.remove(k)
        }

        users.forEach{(_,v) ->addChipToGroup(v)}
    }
}
