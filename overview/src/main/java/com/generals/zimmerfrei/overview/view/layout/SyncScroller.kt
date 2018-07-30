package com.generals.zimmerfrei.overview.view.layout

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import java.lang.ref.WeakReference

class SyncScroller {

    private var recyclerView1: WeakReference<RecyclerView>? = null
    private var recyclerView2: WeakReference<RecyclerView>? = null

    private var isFirstScrolling = false
    private var isSecondScrolling = false

    fun bindFirst(recyclerView: RecyclerView): SyncScroller {
        recyclerView1 = WeakReference(recyclerView)
        return this
    }

    fun bindSecond(recyclerView: RecyclerView): SyncScroller {
        recyclerView2 = WeakReference(recyclerView)
        return this
    }

    fun sync() {
        recyclerView1?.get()?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isSecondScrolling) {
                    recyclerView2?.get()?.scrollBy(dx, dy)
                }
            }
        })

        recyclerView1?.get()?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                isFirstScrolling = true
                isSecondScrolling = false
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })


        recyclerView2?.get()?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isFirstScrolling) {
                    recyclerView1?.get()?.scrollBy(dx, dy)
                }
            }
        })

        recyclerView2?.get()?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                isSecondScrolling = true
                isFirstScrolling = false
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

}