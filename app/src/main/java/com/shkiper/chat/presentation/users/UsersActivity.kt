package com.shkiper.chat.presentation.users

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.shkiper.chat.R
import com.shkiper.chat.extensions.gone
import com.shkiper.chat.extensions.showToast
import com.shkiper.chat.extensions.visible
import com.shkiper.chat.domain.entities.data.UserItem
import com.shkiper.chat.presentation.adapters.UserAdapter
import com.shkiper.chat.presentation.dialogs.GetTitleOfGroupDialog
import com.shkiper.chat.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.fab
import kotlinx.android.synthetic.main.activity_users.toolbar_main

@AndroidEntryPoint
class UsersActivity : AppCompatActivity(), GetTitleOfGroupDialog.GetTitleDialogListener {

    private lateinit var usersAdapter: UserAdapter
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        initToolbar()
        initViews()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
        searchView.queryHint ="Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        return if(item.itemId == android.R.id.home){
            finish()
            true
            }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        usersAdapter = UserAdapter {
            viewModel.handleSelectedItem(it.id)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        with(rv_user_list){
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@UsersActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener{
            if(viewModel.getSizeOfSelectedItems() > 1){
                openDialog()
            }
            else{
                viewModel.handleCreatedChat()
                finish()
            }

        }

    }


    private fun setupObserver(){
        viewModel.getUsers().observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    progress_bar_users.gone()
                    rv_user_list.visible()
                    usersAdapter.updateData(it.data!!)
                }
                Status.LOADING -> {
                    progress_bar_users.visible()
                    rv_user_list.gone()
                }
                Status.ERROR -> {
                    progress_bar.gone()
                    showToast("Something went wrong")
                }
            }
        })


        viewModel.getSelectedData().observe(this, {
            if (it != null) {
                updateChips(it)
            }
            if (it != null) {
                toggleFab(it.isNotEmpty())
            }
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if(isShow) fab.show()
        else fab.hide()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
        val users = listUsers.associateBy { user -> user.id }
            .toMutableMap()

        val views = chip_group.children.associateBy { view -> view.tag }

        for((k, v) in views){
            if(users.containsKey(k)) chip_group.removeView(v)
            else users.remove(k)
        }
        users.forEach{ (_, v) ->addChipToGroup(v)}
    }

    private fun openDialog() {
        val dialog = GetTitleOfGroupDialog()
        dialog.show(supportFragmentManager, "example dialog")
    }


    override fun getTitleOfChat(titleOfChat: String) {
        viewModel.handleCreatedGroupChat(titleOfChat)
        finish()
    }


}
