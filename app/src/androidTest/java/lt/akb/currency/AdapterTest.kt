package lt.akb.currency

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.converter_fragment.*
import lt.akb.currency.converter.RateHolderEdit
import lt.akb.currency.converter.RateHolderView
import lt.akb.currency.converter.RatesAdapter
import lt.akb.currency.main.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdapterTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun clickMoveItemTest() {

        val position = 5
        val recyclerView = rule.activity.ratesRecyclerView
        val item = (recyclerView.adapter as RatesAdapter).currencyRates[position]

        onView(withId(R.id.ratesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
        )

        onView(withId(R.id.ratesRecyclerView)).check(matches(getItemByPosition(0, R.id.currencyTextView,
           withText(item.currency))))
    }

    private fun getItemByPosition(position: Int, viewId: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {

        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder =
                    view.findViewHolderForAdapterPosition(position)
                        ?:
                        return false
                return itemMatcher.matches(viewHolder.itemView.findViewById(viewId))
            }
        }
    }

    @Test
    fun holdersTest() {
        onView(withId(R.id.ratesRecyclerView)).check(matches(checkRateHolderEdit(0)))
        onView(withId(R.id.ratesRecyclerView)).check(matches(checkRateHolderView(1)))
    }

    private fun checkRateHolderView(position: Int): Matcher<View?>? {

        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                        return viewHolder is RateHolderView

            }
        }
    }

    private fun checkRateHolderEdit(position: Int): Matcher<View?>? {

        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                return viewHolder is RateHolderEdit

            }
        }
    }
}