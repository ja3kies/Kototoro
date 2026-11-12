package org.skepsun.kototoro.core.prefs

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class MainNavigationLimitTest {
    @Test
    fun `reader navigation migrates legacy tabs and keeps three required destinations`() {
        readerNavigationItems(listOf(NavItem.HOME, NavItem.FAVORITES, NavItem.EXPLORE, NavItem.FEED, NavItem.HOME)) shouldContainExactly
            listOf(NavItem.HOME, NavItem.EXPLORE, NavItem.HISTORY)
        defaultMainNavItems shouldContainExactly listOf(NavItem.HOME, NavItem.HISTORY, NavItem.EXPLORE)
    }


	@Test
	fun `main navigation keeps at most five distinct destinations`() {
		listOf(
			NavItem.HOME,
			NavItem.FAVORITES,
			NavItem.EXPLORE,
			NavItem.HISTORY,
			NavItem.FEED,
			NavItem.HOME,
		).limitMainNavigationItems() shouldContainExactly listOf(
			NavItem.HOME,
			NavItem.FAVORITES,
			NavItem.EXPLORE,
			NavItem.HISTORY,
			NavItem.FEED,
		)
	}
}
