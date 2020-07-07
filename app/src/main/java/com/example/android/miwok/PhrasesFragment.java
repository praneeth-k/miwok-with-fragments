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

public class PhrasesFragment extends Fragment {
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


    public PhrasesFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.activity_phrases, container, false);
        // Inflate the layout for this fragment
        //setContentView(R.layout.activity_phrases);

//        Toolbar myChildToolbar =  (Toolbar) rootView.findViewById(R.id.toolbar);
//        setActionBar(myChildToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?","minto wksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinna oyaase'na", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michaksas", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good","kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","aanas'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I'm coming","haa' aanam", R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming","aanam", R.raw.phrase_im_coming));
        words.add(new Word("Let's go","yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here","anni'nem", R.raw.phrase_come_here));
        ListView listView = (ListView) rootView.findViewById(R.id.phrasesRootView);
//        TextView textView;
//        for (String word: words)
//        {
//            textView = new TextView(this);
//            textView.setText(word);
//            assert rootView != null;
//            rootView.addView(textView);
//        }
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);
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
