package com.example.slideviewer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val intervalTime = 5000.toLong()
    lateinit var banner: ViewPager2
    private lateinit var handler: Handler
    private lateinit var mainAdapter: MainAdapter
    private lateinit var tabLayout: TabLayout

    val list = arrayListOf(
        "first",
        "second",
        "third",
        "fourth",
        "fifth"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        tabLayout = findViewById(R.id.tabLayout)

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )

        banner = findViewById<ViewPager2>(R.id.view_pager).apply {

            mainAdapter = MainAdapter()
            adapter = mainAdapter

            mainAdapter.addList(list)

            offscreenPageLimit = 1 // 다음 페이지 몇개까지 미리 로딩할지 정하는 함수
            currentItem = list.count() * 100
            // 항상 인접한 페이지가 배치되도록 오프스크린 페이지 제한을 1개 이상으로 설정한다
            setPageTransformer(pageTransformer)
            clipToPadding = false
            addItemDecoration(itemDecoration)
//            TabLayoutMediator(tabLayout, this) { tab, position ->
//
//            }.attach()

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop() //  뷰페이저가 움직이는 중일 때 자동 스크롤 중단 함수 호출
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime) // 뷰페이저 멈춰 있을때 자동 스크롤 시작 호출

                        ViewPager2.SCROLL_STATE_SETTLING -> { // 뷰페이저 끝 도달했을때 자동스크롤 중단
                            if (banner.currentItem != 0) {
                                autoScrollStop()
                            }
                        }
                    }
                }
            })
        }

        //배너 자동 스크롤 컨트롤
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 0) {
                    banner.setCurrentItem(banner.currentItem + 1, true) //다음 페이지로 이동
                    autoScrollStart(intervalTime) //스크롤 킵고잉
                }
            }
        }
    }

    private fun autoScrollStop() {
        handler.removeMessages(0)
    }

    //배너 자동 스크롤 시작하게 하는 함수
    private fun autoScrollStart(time: Long) {
        handler.removeMessages(0) //이거 안하면 핸들러가 여러개로 계속 늘어남
        handler.sendEmptyMessageDelayed(0, time) //intervalTime 만큼 반복해서 핸들러를 실행
    }


    override fun onPause() {
        super.onPause()
        handler.removeMessages(0)
    }

}