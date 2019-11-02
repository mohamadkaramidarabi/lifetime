package com.example.lifetime.ui.message

import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView
import java.util.ArrayList

object MessageInteractor {
    interface MessagesMVPView : MVPView {

//        fun updateList(data: ArrayList<UserMessagesResponse.Message>)
        fun seenMessage(position: Int)

        fun showEmptyState()
        fun hideEmptyState()
    }


    interface MessagesMVPPresenter<V : MessagesMVPView> : MVPPresenter<V> {
        fun getMessages()
//        fun setSeenMessage(message: UserMessagesResponse.Message, position: Int)
    }
}