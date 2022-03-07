package com.techtown.mymovie2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_page.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var imageId:String? = null
    var title:String? = null
    var details1:String? = null
    var details2:String? = null

    var index: Int = 0

    var callback: FragmentCallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentCallBack){
            callback = context
        } else {
            Log.d(TAG, "Activity is not FragmentCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        if(callback != null){
            callback = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null){
            imageId = bundle.getString("imageId")
            title = bundle.getString("title")
            details1 = bundle.getString("details1")
            details2 = bundle.getString("details2")
            index = bundle.getInt("index", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_page, container, false)

        if(imageId != null && imageId!!.isNotEmpty()){
            val url = "http://image.tmdb.org/t/p/w200${imageId}"

            Glide.with(this).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).dontAnimate().into(rootView.posterImageView)
        }
        rootView.titleTextView.text = title
        rootView.details1TextView.text = details1
        rootView.details2TextView.text = details2

        rootView.detailsButton.setOnClickListener {
            if(callback != null){
                val bundle = Bundle()
                bundle.putInt("index", index)

                callback!!.onFragmentSelected(FragmentCallBack.FragmentItem.ITEM2, bundle)
            }
        }

        return rootView
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        private const val TAG = "PageFragment"

        fun newInstance(imageId : String?, title : String?, details1 : String?, details2 : String?, index: Int) : PageFragment {
            val fragment = PageFragment()

            val bundle = Bundle()
            bundle.putString("imageId", imageId)
            bundle.putString("title", title)
            bundle.putString("details1", details1)
            bundle.putString("details2", details2)
            bundle.putInt("index", index)
            fragment.arguments = bundle

            return fragment
        }
    }
}