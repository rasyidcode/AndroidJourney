package com.rcd.bambang.daggerdota2stat

import android.net.Uri
import android.provider.BaseColumns

class DotaContract {

    class DotaSubscriber {

        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACC).build()
            val TABLE_NAME: String = "favorites"
            val _ID: String = BaseColumns._ID
            val _COUNT: String = BaseColumns._COUNT
            val COLUMN_ACC_ID: String = "acc_id"
            val COLUMN_PLAYER_NAME: String = "name"
            val COLUMN_AVATAR_URL: String = "avatar_url"
        }
    }

    companion object {
        val CONTENT_AUTHORITY: String = "com.rcd.bambang.daggerdota2stat"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")
        val PATH_ACC: String = "account"
    }

}