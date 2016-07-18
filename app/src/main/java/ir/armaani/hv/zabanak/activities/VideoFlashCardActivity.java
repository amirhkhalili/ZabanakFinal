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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.exceptions.DoesNotStartedAlreadyException;
import ir.armaani.hv.zabanak.models.Package;
import ir.armaani.hv.zabanak.models.Word;

public class VideoFlashCardActivity extends AppCompatActivity implements ManifestFetcher.ManifestCallback<MediaPresentationDescription>,
        ExoPlayer.Listener,HlsSampleSource.EventListener, AudioManager.OnAudioFocusChangeListener, View.OnClickListener {

        private SurfaceView surface;
        private ImageView btn_play,btn_pause;
        private ExoPlayer player;
        private PlayerControl playerControl;
        private String video_url;
        private Handler mainHandler;
        private AudioManager am;
        private String userAgent;
        private ManifestFetcher<MediaPresentationDescription> playlistFetcher;
        private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
        private static final int MAIN_BUFFER_SEGMENTS = 254;
        public static final int TYPE_VIDEO = 0;
        TextView txt_playState,words_txt;
        private TrackRenderer videoRenderer;
        private MediaCodecAudioTrackRenderer audioRenderer;
        RelativeLayout ControllerLayout;
        RelativeLayout VideoLayout;
        WebView Nodata,Translate;
        ImageView nextTime,nextSub,previousTime,previousSub,OKword,NKword;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_videoflashcard);
                getSupportActionBar().hide();
                Intent intent = getIntent();
                Package packageItem = (Package) intent.getSerializableExtra("package");
                if (packageItem.getId()==null)
                        packageItem.setId(intent.getLongExtra("packageId" , 0));
                List<Word> wordsList = packageItem.getTodayWords();


                nextSub = (ImageView)findViewById(R.id.nextSub);
                nextTime = (ImageView)findViewById(R.id.nextTime);
                previousTime = (ImageView)findViewById(R.id.previousTime);
                previousSub = (ImageView)findViewById(R.id.previousSub);
                OKword = (ImageView)findViewById(R.id.okWord);
                NKword = (ImageView)findViewById(R.id.nkWord);
                final Button NextWord = (Button)findViewById(R.id.NextWord);
                Nodata = (WebView)findViewById(R.id.webViewNodata) ;
                Translate = (WebView)findViewById(R.id.TranslatewebView) ;
                ControllerLayout = (RelativeLayout)findViewById(R.id.ControllerLayout);
                ImageView img = (ImageView)findViewById(R.id.videoFlashcardBackground);
                surface = (SurfaceView) findViewById(R.id.surface_view); // we import surface
                txt_playState = (TextView) findViewById(R.id.txt_playstate);
                btn_play = (ImageView) findViewById(R.id.btn_play);
                btn_pause = (ImageView) findViewById(R.id.btn_pause);
                VideoLayout = (RelativeLayout) findViewById(R.id.VideoLayout);
                words_txt = (TextView)findViewById(R.id.words_txt);

            player = ExoPlayer.Factory.newInstance(2);
            playerControl = new PlayerControl(player);

                for(final Word word:wordsList) {
                        video_url= word.getMovieURL();
                        words_txt.setText(word.getWord());
                        playerControl.seekTo(word.getPlayTime()*1000);
                        img.setImageBitmap(word.getaPackage().getImage());


                NextWord.setVisibility(View.GONE);
                ControllerLayout.setVisibility(View.GONE);


                Nodata.loadUrl("file:///android_asset/html/nodata.html");
                Translate.loadUrl("file:///android_asset/html/translate.html");
                Translate.setVisibility(View.INVISIBLE);


                OKword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
                                playerControl.pause();
                                Translate.setVisibility(View.VISIBLE);
                                Translate.startAnimation(animation1);
                                NextWord.setVisibility(View.VISIBLE);
                                OKword.setVisibility(View.GONE);
                                NKword.setVisibility(View.GONE);
                                try {
                                        word.doSuccess();
                                } catch (DoesNotStartedAlreadyException e) {
                                        e.printStackTrace();
                                }

                        }
                });


                NKword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
                                playerControl.pause();
                                Translate.setVisibility(View.VISIBLE);
                                Translate.startAnimation(animation1);
                                NextWord.setVisibility(View.VISIBLE);
                                OKword.setVisibility(View.GONE);
                                NKword.setVisibility(View.GONE);
                                try {
                                        word.doFailure();
                                } catch (DoesNotStartedAlreadyException e) {
                                        e.printStackTrace();
                                }
                        }
                });


                NextWord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(VideoFlashCardActivity.this, VideoFlashCardActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            VideoFlashCardActivity.this.startActivity(myIntent);
                            finish();

                        }
                });



                ControllerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(ControllerLayout.getVisibility()==View.GONE){
                                        ControllerLayout.setVisibility(View.VISIBLE);
                                }else{
                                        ControllerLayout.setVisibility(View.GONE);
                                }
                        }
                });



                VideoLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    if(ControllerLayout.getVisibility()==View.GONE){
                            ControllerLayout.setVisibility(View.VISIBLE);
                            btn_play.setVisibility(View.VISIBLE);
                    }else{
                            ControllerLayout.setVisibility(View.GONE);
                    }
                        }
                });


                //INIT EXO


                btn_play.setOnClickListener(this);
                btn_pause.setOnClickListener(this);

                am = (AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                mainHandler = new Handler();
                userAgent = Util.getUserAgent(this, "MainActivity");
                MediaPresentationDescriptionParser parser = new MediaPresentationDescriptionParser();
                playlistFetcher = new ManifestFetcher<>(video_url, new DefaultUriDataSource(this, userAgent), parser);
                playlistFetcher.singleLoad(mainHandler.getLooper(), this);
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
                player.prepare(videoRenderer,audioRenderer);
                player.addListener(this);
                if (requestFocus())
                player.setPlayWhenReady(true);

                nextTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                playerControl.seekTo(playerControl.getCurrentPosition()+10000);

                        }
                });
                previousTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                playerControl.seekTo(playerControl.getCurrentPosition()-10000);

                        }
                });
        }

        public boolean requestFocus() {
                return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                        am.requestAudioFocus(VideoFlashCardActivity.this, AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN);
        }

        private void pushSurface(boolean blockForSurfacePush) {
                if (videoRenderer == null) {return;}
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

                if(playerControl.isPlaying()){
                        playerControl.pause();
                        btn_play.setVisibility(View.INVISIBLE);
                        btn_pause.setVisibility(View.VISIBLE);
                }else{
                        playerControl.start();
                        btn_play.setVisibility(View.VISIBLE);
                        btn_pause.setVisibility(View.INVISIBLE);
                        ControllerLayout.setVisibility(View.GONE);
                }

        }
}
