package lnbti.gtp01.droidai.ui.airesponse

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter for managing fragments in ViewPager2.
 *
 * This adapter provides fragments for two tabs: FragmentPending and FragmentResponded.
 *
 * @property fragmentManager The fragment manager to interact with the fragments.
 * @property lifecycle The lifecycle of the fragments.
 */
class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * Gets the total number of fragments managed by the adapter.
     *
     * @return The total number of fragments.
     */
    override fun getItemCount(): Int {
        return 2
    }

    /**
     * Creates a fragment for the specified position.
     *
     * @param position The position of the fragment.
     * @return The fragment corresponding to the position.
     */
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) PendingResponseFragment()  else FragmentResponded()
    }
}