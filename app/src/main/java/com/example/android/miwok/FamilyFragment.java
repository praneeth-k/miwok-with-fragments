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

public class FamilyFragment extends Fragment {
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


    public FamilyFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.activity_family_members, container, false);
        // Inflate the layout for this fragment
        //setContentView(R.layout.activity_family);

//        Toolbar myChildToolbar =  (Toolbar) rootView.findViewById(R.id.toolbar);
//        setActionBar(myChildToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father","apa", R.raw.family_father, R.drawable.family_father));
        words.add(new Word("mother","ata", R.raw.family_mother, R.drawable.family_mother));
        words.add(new Word("son","angsi", R.raw.family_son, R.drawable.family_son));
        words.add(new Word("daughter","tune", R.raw.family_daughter, R.drawable.family_daughter));
        words.add(new Word("older brother","taachi", R.raw.family_older_brother, R.drawable.family_older_brother));
        words.add(new Word("younger brother","chalitti", R.raw.family_younger_brother, R.drawable.family_younger_brother));
        words.add(new Word("older sister","tete", R.raw.family_older_sister, R.drawable.family_older_sister));
        words.add(new Word("younger sister","kolliti", R.raw.family_younger_sister, R.drawable.family_younger_sister));
        words.add(new Word("grand mother","ama", R.raw.family_grandmother, R.drawable.family_grandmother));
        words.add(new Word("grand father","paapa", R.raw.family_grandfather, R.drawable.family_grandfather));
        ListView listView = (ListView) rootView.findViewById(R.id.familyRootView);
//        TextView textView;
//        for (String word: words)
//        {
//            textView = new TextView(this);
//            textView.setText(word);
//            assert rootView != null;
//            rootView.addView(textView);
//        }
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_family);
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
