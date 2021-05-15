package com.sun.unsplash_01.ui.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.sun.unsplash_01.R
import com.sun.unsplash_01.databinding.FragmentHomePageBinding
import com.sun.unsplash_01.ui.collection.CollectionFragment
import com.sun.unsplash_01.ui.favorite.FavoriteFragment
import com.sun.unsplash_01.ui.home.HomeFragment
import com.sun.unsplash_01.utils.ItemBottomNav

class HomePageFragment : Fragment() {

    lateinit var binding: FragmentHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInitViewPager()
        onInitNav()
        onEvent()
    }

    private fun onEvent() {
        binding.imageViewSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun onInitNav() {
        binding.bottomNavHomePage.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemHomePage -> {
                    setTitle(R.string.home)
                    setViewPager(ItemBottomNav.HOME_ITEM.position)
                }
                R.id.itemCollectionPage -> {
                    setTitle(R.string.collection)
                    setViewPager(ItemBottomNav.COLLECTION_ITEM.position)
                }
                R.id.itemFavoritePage -> {
                    setTitle(R.string.favorite)
                    setViewPager(ItemBottomNav.FAVORITE_ITEM.position)
                }
                else -> false
            }
        }
    }

    private fun onInitViewPager() {
        val listFragment = listOf(
            HomeFragment.newInstance(),
            CollectionFragment.newInstance(),
            FavoriteFragment.newInstance()
        )
        childFragmentManager.let {
            binding.viewPagerHomePage.adapter = HomePageAdapter(it, listFragment)
        }
        binding.viewPagerHomePage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                binding.bottomNavHomePage.menu.getItem(position).isChecked = true
                when (position) {
                    ItemBottomNav.HOME_ITEM.ordinal -> setTitle(R.string.home)
                    ItemBottomNav.COLLECTION_ITEM.ordinal -> setTitle(R.string.collection)
                    ItemBottomNav.FAVORITE_ITEM.ordinal -> setTitle(R.string.favorite)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setTitle(stringTitle: Int) {
        binding.textTitle.text = getString(stringTitle)
    }

    private fun setViewPager(position: Int): Boolean {
        binding.viewPagerHomePage.currentItem = position
        return true
    }
}
