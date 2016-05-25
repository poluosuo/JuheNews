package com.aishang5wpj.juhenews.main.movie;

import android.content.Context;

import com.aishang5wpj.juhenews.bean.MovieBean;
import com.aishang5wpj.juhenews.bean.MovieChannelBean;
import com.aishang5wpj.juhenews.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wpj on 16/5/24下午5:17.
 */
public class MovieModelImpl implements IMovieModel {

    private Gson mGson;
    private OkHttpClient mOkHttpClient;

    public MovieModelImpl() {
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public void loadChannel(Context context, OnLoadChannelListener listener) {
        String result = FileUtils.readAssertsFile(context, "movieChannel.json");
        List<MovieChannelBean> channelList = mGson.fromJson(result, new TypeToken<List<MovieChannelBean>>() {
        }.getType());
        if (null != listener) {
            listener.onLoadComplted(channelList);
        }
    }

    @Override
    public void loadMovies(MovieChannelBean channel, int pageIndex, final OnLoadMoviesListener loadMoviesListener) {

        String url = channel.getUrl(pageIndex);
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MovieBean movieBean = mGson.fromJson(result, MovieBean.class);
                if (null != loadMoviesListener) {
                    loadMoviesListener.onLoadComplted(movieBean);
                }
            }
        });
    }
}