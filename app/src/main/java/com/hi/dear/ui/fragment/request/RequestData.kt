package com.hi.dear.ui.fragment.request

import com.hi.dear.data.model.common.UserCore
import com.hi.dear.ui.Constant

data class RequestData(
    var status: String = Constant.requestNew,
) : UserCore()
