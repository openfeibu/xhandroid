package cn.flyexp.util;

import com.squareup.picasso.Picasso;

import cn.finalteam.galleryfinal.PauseOnScrollListener;

/**
 * Created by guo on 2016/7/27.
 */
public class PicassoPauseOnScrollListener extends PauseOnScrollListener {

    public PicassoPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        Picasso.with(getActivity()).resumeTag(getActivity());
    }

    @Override
    public void pause() {
        Picasso.with(getActivity()).pauseTag(getActivity());
    }
}
