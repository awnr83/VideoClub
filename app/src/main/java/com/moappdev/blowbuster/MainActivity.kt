package com.moappdev.blowbuster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.moappdev.blowbuster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BlowBuster)
        super.onCreate(savedInstanceState)

        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        mDrawerLayout= mBinding.drawerLayout

        val navCont= this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navCont, mDrawerLayout)
        mAppBarConfiguration= AppBarConfiguration(navCont.graph, mDrawerLayout)
        NavigationUI.setupWithNavController(mBinding.navView,navCont)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCont= this.findNavController(R.id.myNavHostFragment)
        return navCont.navigateUp()
    }
}