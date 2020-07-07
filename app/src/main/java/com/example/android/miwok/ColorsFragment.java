package com.example.android.miwok;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //release media playback resources
                releaseMediaPlayer();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                // reset media playback
                if(mediaPlayer!=null) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Pause playback
                // reset media playback
                if(mediaPlayer!=null) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again
                // resume playback
                if(mediaPlayer!=null)
                    mediaPlayer.start();
            }
        }
    };
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.activity_colors, container, false);
        // Inflate the layout for this fragment
        //setContentView(R.layout.activity_colors);

//        Toolbar myChildToolbar =  (Toolbar) rootView.findViewById(R.id.toolbar);
//        setActionBar(myChildToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red","wetetti", R.raw.color_red, R.drawable.color_red));
        words.add(new Word("mustard yellow","chiwiita", R.raw.color_mustard_yellow, R.drawable.color_mustard_yellow));
        words.add(new Word("dusty yellow","topiisa", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow));
        words.add(new Word("green","chokokki", R.raw.color_green, R.drawable.color_green));
        words.add(new Word("brown","takaakki", R.raw.color_brown, R.drawable.color_brown));
        words.add(new Word("gray","topoppi", R.raw.color_gray, R.drawable.color_gray));
        words.add(new Word("black","kululli", R.raw.color_black, R.drawable.color_black));
        words.add(new Word("white","kelelli", R.raw.color_white, R.drawable.color_white));
        ListView listView = (ListView) rootView.findViewById(R.id.colorsRootView);
//        TextView textView;
//        for (String word: words)
//        {
//            textView = new TextView(this);
//            textView.setText(word);
//            assert rootView != null;
//            rootView.addView(textView);
//        }
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_colors);
        assert listView != null;
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = words.get(i);
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request temp focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getAudioSrc());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer()
    {
        if(mediaPlayer != null)
        {
            //if media player is not null release the audio player resources;
            mediaPlayer.release();;
            mediaPlayer = null;
            //unregister the onAudioFocusChange listener when playback is done
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }
}
