package ir.armaani.hv.zabanak.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.ChunkSource;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatEvaluator;
import com.google.android.exoplayer.dash.DashChunkSource;
import com.google.android.exoplayer.dash.DefaultDashTrackSelector;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescriptionParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.PlayerControl;
import com.google.android.exoplayer.util.Util;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.models.Package;
import ir.armaani.hv.zabanak.models.Word;

public class VideoFlashCardActivity extends AppCompatActivity implements ManifestFetcher.ManifestCallback<MediaPresentationDescription>,
        ExoPlayer.Listener, HlsSampleSource.EventListener, AudioManager.OnAudioFocusChangeListener, View.OnClickListener {

    private SurfaceView surface;
    private ImageView btn_play, btn_pause;
    private ExoPlayer player;
    private PlayerControl playerControl;
    private Handler mainHandler;
    private AudioManager am;
    private String userAgent;
    private ManifestFetcher<MediaPresentationDescription> playlistFetcher;
    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int MAIN_BUFFER_SEGMENTS = 254;
    private static final int TYPE_VIDEO = 0;
    private TextView txt_playState, words_txt;
    private TrackRenderer videoRenderer;
    private MediaCodecAudioTrackRenderer audioRenderer;
    private RelativeLayout ControllerLayout;
    private RelativeLayout VideoLayout;
    private WebView Nodata;
    private ImageView nextTime, nextSub, previousTime, previousSub, img;
    private SeekBar seekBar;
    private List<Word> wordsList;
    private Integer currentWordId;
    private Word currentWord;
    private Button showTranslate_btn;

    public void loadWordToActivity(Word word) {

        words_txt.setText(word.getWord());
        img.setImageBitmap(word.getaPackage().getSeries().getImage());
        seekBar.setProgress(50);

        MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
        playlistFetcher = new ManifestFetcher<>(word.getMovieURL(), new DefaultUriDataSource(this, userAgent), parser);
        playlistFetcher.singleLoad(mainHandler.getLooper(), this);

        playerControl.seekTo(500000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoflashcard);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        Package packageItem = (Package) intent.getSerializableExtra("package");
        if (packageItem.getId() == null)
            packageItem.setId(intent.getLongExtra("packageId", 0));
        wordsList = packageItem.getTodayWords();
        if (wordsList == null) finish();
        if (wordsList.size() < 1) finish();

        nextSub = (ImageView) findViewById(R.id.nextSub);
        nextTime = (ImageView) findViewById(R.id.nextTime);
        previousTime = (ImageView) findViewById(R.id.previousTime);
        previousSub = (ImageView) findViewById(R.id.previousSub);

        showTranslate_btn = (Button) findViewById(R.id.showTranslate);
        Nodata = (WebView) findViewById(R.id.webViewNodata);
        ControllerLayout = (RelativeLayout) findViewById(R.id.ControllerLayout);
        img = (ImageView) findViewById(R.id.videoFlashcardBackground);
        surface = (SurfaceView) findViewById(R.id.surface_view); // we import surface
        txt_playState = (TextView) findViewById(R.id.txt_playstate);
        btn_play = (ImageView) findViewById(R.id.btn_play);
        btn_pause = (ImageView) findViewById(R.id.btn_pause);
        VideoLayout = (RelativeLayout) findViewById(R.id.VideoLayout);
        words_txt = (TextView) findViewById(R.id.words_txt);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        player = ExoPlayer.Factory.newInstance(2);
        playerControl = new PlayerControl(player);
        ControllerLayout.setVisibility(View.GONE);
        Nodata.loadUrl("file:///android_asset/html/nodata.html");

        showTranslate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerControl.pause();
                Intent myIntent = new Intent(VideoFlashCardActivity.this, TranslateActivity.class);
                myIntent.putExtra("word", currentWord); //Optional parameters
                myIntent.putExtra("wordId", currentWord.getId());
                VideoFlashCardActivity.this.startActivityForResult(myIntent, 0);


            }
        });



        ControllerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ControllerLayout.getVisibility() == View.GONE) {
                    ControllerLayout.setVisibility(View.VISIBLE);
                } else {
                    ControllerLayout.setVisibility(View.GONE);
                }
            }
        });

        VideoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ControllerLayout.getVisibility() == View.GONE) {
                    ControllerLayout.setVisibility(View.VISIBLE);
                    btn_play.setVisibility(View.VISIBLE);
                } else {
                    ControllerLayout.setVisibility(View.GONE);
                }
            }
        });

        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);

        am = (AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mainHandler = new Handler();
        userAgent = Util.getUserAgent(this, VideoFlashCardActivity.class.toString());

        currentWordId = 0;
        currentWord = wordsList.get(currentWordId);
        loadWordToActivity(currentWord);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            switch (resultCode) {
                case RESULT_OK:
                    currentWordId++;
                    if (currentWordId == wordsList.size()) {
                        Intent myIntent = new Intent(VideoFlashCardActivity.this, PakageActivity.class);
                        myIntent.putExtra("series", wordsList.get(0).getaPackage().getSeries()); //Optional parameters
                        myIntent.putExtra("seriesId", wordsList.get(0).getaPackage().getSeries().getId());
                        VideoFlashCardActivity.this.startActivity(myIntent);
                        finish();
                    } else {
                        currentWord = wordsList.get(currentWordId);
                        loadWordToActivity(currentWord);
                    }
                    break;
            }
        }
    }

    @Override
    public void onSingleManifest(MediaPresentationDescription manifest) {
        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        PtsTimestampAdjusterProvider timestampAdjusterProvider = new PtsTimestampAdjusterProvider();
        DataSource videoDataSource = new DefaultUriDataSource(this, bandwidthMeter, userAgent);
        ChunkSource videoChunkSource = new DashChunkSource(playlistFetcher, DefaultDashTrackSelector.newVideoInstance(this, true, false), videoDataSource, new FormatEvaluator.AdaptiveEvaluator(bandwidthMeter), MAIN_BUFFER_SEGMENTS, 10, null, null, TYPE_VIDEO);
        ChunkSampleSource videoSampleSource = new ChunkSampleSource(videoChunkSource, loadControl, MAIN_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this, videoSampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        DataSource audioDataSource = new DefaultUriDataSource(this, bandwidthMeter, userAgent);
        ChunkSource audioChunkSource = new DashChunkSource(playlistFetcher, DefaultDashTrackSelector.newAudioInstance(), audioDataSource, null, MAIN_BUFFER_SEGMENTS, 10, null, null, TYPE_VIDEO);
        ChunkSampleSource audioSampleSource = new ChunkSampleSource(audioChunkSource, loadControl, MAIN_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(audioSampleSource, MediaCodecSelector.DEFAULT);
        this.videoRenderer = videoRenderer;
        this.audioRenderer = audioRenderer;
        pushSurface(false);
        player.prepare(videoRenderer, audioRenderer);
        player.addListener(this);
        if (requestFocus())
            player.setPlayWhenReady(true);



        nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerControl.seekTo(playerControl.getCurrentPosition() + 10000);

            }
        });
        previousTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerControl.seekTo(playerControl.getCurrentPosition() - 10000);

            }
        });
    }

    public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                am.requestAudioFocus(VideoFlashCardActivity.this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
    }

    private void pushSurface(boolean blockForSurfacePush) {
        if (videoRenderer == null) {
            return;
        }
        if (blockForSurfacePush) {
            player.blockingSendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface.getHolder().getSurface());
        } else {
            player.sendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface.getHolder().getSurface());
        }
    }


    @Override
    public void onSingleManifestError(IOException e) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String text = "";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                text += "در حال دریافت دیتا ;)";
                ControllerLayout.setVisibility(View.GONE);
                break;
            case ExoPlayer.STATE_ENDED:
                text += "تمام شد :)";
                break;
            case ExoPlayer.STATE_IDLE:
                ControllerLayout.setVisibility(View.GONE);
                break;
            case ExoPlayer.STATE_PREPARING:
                ControllerLayout.setVisibility(View.GONE);
                break;
            case ExoPlayer.STATE_READY:
                Nodata.setVisibility(View.GONE);
                //ControllerLayout.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                btn_play.setVisibility(View.GONE);
                break;
            default:
                ControllerLayout.setVisibility(View.GONE);
                text += "اتصال دیتا خود را چک کنید :(";
                break;
        }
        txt_playState.setText(text);

    }

    @Override
    public void onPlayWhenReadyCommitted() {
        playerControl.seekTo(currentWord.getPlayTime()*1000);
/*        Timer t =new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        } , 0 , 1000);
        t.purge();
        t.cancel();*/
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {

    }

    @Override
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {

    }

    @Override
    public void onLoadCanceled(int sourceId, long bytesLoaded) {

    }

    @Override
    public void onLoadError(int sourceId, IOException e) {

    }

    @Override
    public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {

    }

    @Override
    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onClick(View v) {

        if (playerControl.isPlaying()) {
            playerControl.pause();
            btn_play.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
        } else {
            playerControl.start();
            btn_play.setVisibility(View.VISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);
            ControllerLayout.setVisibility(View.GONE);
        }

    }
}
