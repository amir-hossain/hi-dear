package com.hi.dear.ui.fragment.top

import com.hi.dear.data.model.common.UserCore
import com.hi.dear.ui.Constant

data class TopProfileData(
    var status: String = Constant.requestNew,
) : UserCore()
