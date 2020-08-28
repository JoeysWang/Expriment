package com.joeys.expriment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.android.synthetic.main.activity_quran_share.*
import android.view.*
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ScreenUtils
import com.joeys.expriment.bottomDialog.DraggableBottomBehavior
import com.joeys.expriment.bottomDialog.DraggableBottomView
import com.joeys.expriment.utils.dp

class BehaviorActivity : AppCompatActivity() {
    private lateinit var mTop: View
    private lateinit var mBottom: DraggableBottomView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_share)

        mTop = findViewById(R.id.top)
        mBottom = findViewById(R.id.bottom)

        mBottom.layoutParams.height = ScreenUtils.getScreenHeight() - 100.dp


        val lp = mBottom.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = lp.behavior as DraggableBottomBehavior
        behavior.drawerHandler = button

        supportFragmentManager.beginTransaction()
                .add(R.id.container, DemoFragment())
                .commit()

        dismiss.setOnClickListener {
            mBottom.dismiss()
        }

    }


}


class DemoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_demo, container, false)
    }
}
