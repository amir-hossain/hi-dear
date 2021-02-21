package com.hi.dear.source


import com.hi.dear.ui.fragment.match.MatchData

interface IMatchDataSource {
    fun getData(): MutableList<MatchData>?
}
