package com.envyglit.chat.presentation.users

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
import com.envyglit.chat.R
import com.envyglit.chat.databinding.ActivityUsersBinding
import com.envyglit.chat.util.extensions.showToast
import com.envyglit.chat.domain.entities.data.UserItem
import com.envyglit.chat.presentation.adapters.UserAdapter
import com.envyglit.chat.presentation.dialogs.GetTitleOfGroupDialog
import com.envyglit.chat.util.Status
import com.envyglit.chat.util.extensions.gone
import com.envyglit.chat.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : AppCompatActivity(), GetTitleOfGroupDialog.GetTitleDialogListener {

    private lateinit var usersAdapter: UserAdapter

    private val viewModel: UsersViewModel by viewModels()

    private lateinit var binding: ActivityUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        usersAdapter = UserAdapter {
            viewModel.handleSelectedItem(it.id)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        with(binding.rvUserList) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@UsersActivity)
            addItemDecoration(divider)
        }

        binding.fab.setOnClickListener {
            if (viewModel.getSizeOfSelectedItems() > 1) {
                openDialog()
            } else {
                viewModel.handleCreatedChat()
                finish()
            }

        }

    }


    private fun setupObserver(){
        viewModel.getUsers().observe(this, {
            when(it.status){
                Status.SUCCESS -> {
                    binding.progressBar.gone()
                    binding.rvUserList.visible()
                    usersAdapter.updateData(it.data!!)
                }
                Status.LOADING -> {
                    binding.progressBar.visible()
                    binding.rvUserList.gone()
                }
                Status.ERROR -> {
                    binding.progressBar.gone()
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
        if (isShow) binding.fab.show()
        else binding.fab.hide()
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
        binding.chipGroup.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>){
        binding.chipGroup.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE
        val users = listUsers.associateBy { user -> user.id }
            .toMutableMap()

        val views = binding.chipGroup.children.associateBy { view -> view.tag }

        for((k, v) in views){
            if (users.containsKey(k)) binding.chipGroup.removeView(v)
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
