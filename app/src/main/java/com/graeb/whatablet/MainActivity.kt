package com.graeb.whatablet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var mContent: WebFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mContent = fragmentManager.findFragmentById(R.id.fragment) as WebFragment

        if (savedInstanceState != null) {
            mContent = fragmentManager.getFragment(savedInstanceState, TAG_RETAINED_FRAGMENT) as WebFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        fragmentManager.putFragment(outState, TAG_RETAINED_FRAGMENT, mContent)
    }

    companion object {
        private val TAG_RETAINED_FRAGMENT = "WebFragment"
    }

}
// TODO handle orientation changes
