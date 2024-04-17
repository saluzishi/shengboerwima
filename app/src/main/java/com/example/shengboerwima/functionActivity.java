package com.example.shengboerwima;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.libra.sinvoice.SinVoice;
import com.libra.sinvoice.VoiceEncoder;

import java.util.Hashtable;

public class functionActivity extends AppCompatActivity implements SinVoice.Listener {//implements SinVoice.Listener {


    private final static String TAG = "SinVoice_MainActivity";

    private final static int CLICK_INTERVAL = 400;
    private final static int CLICK_COUNT = 6;

    private SinVoice mSinVoice;
    private boolean mIsReadFromFile;
    private EditText mEvSend;
    private TextView mTvRecognised;
    private TextView mTvRegState;
    private TextView mTvAbout;
    private Button mBtnSend;
    private Button mBtnListen;
    private Spinner mSpEffectParam;
    private int mEffect;
    private long mTVClickLastTime;
    private int mClickCount = 0;
    private boolean mEnableWritePcmToFile;
    private int mSystemVolume;
    private int mSetVolume;
    private String mStateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        mStateString = getString(R.string.state);

        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(functionActivity.this).setTitle(R.string.permission_lack_record)
                    .setMessage(R.string.permission_lack_record_info)
                    .setPositiveButton(R.string.command_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            functionActivity.this.finish();
                        }
                    }).setCancelable(false).show();

            return;
        } else {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mSystemVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSetVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2 + 1;
            am.setStreamVolume(AudioManager.STREAM_MUSIC, mSetVolume, 0);
        }

        mSinVoice = new SinVoice(this, this);

        mEnableWritePcmToFile = false;
        mIsReadFromFile = false;
        mTVClickLastTime = 0;

        initEffectSpinner();

        mEvSend = (EditText) findViewById(R.id.playtext);
        mEvSend.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvRecognised = (TextView) findViewById(R.id.regtext);
        mTvRegState = (TextView) findViewById(R.id.reg_status);
        mTvRegState.setTextColor(Color.RED);

        mBtnSend = (Button) findViewById(R.id.start_send);
        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSinVoice.isSending()) {
                    mSinVoice.stopSend();
                } else {
                    mSinVoice.send(mEvSend.getText().toString());
                }
            }
        });

        mBtnListen = (Button) findViewById(R.id.start_listen);
        mBtnListen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSinVoice.isListening()) {
                    mSinVoice.stopListen();
                } else {
                    mSinVoice.startListen(mIsReadFromFile);
                }
            }
        });

        View root = this.findViewById(R.id.root);
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                if (time - mTVClickLastTime < CLICK_INTERVAL) {
                    ++mClickCount;
                    if (mClickCount >= CLICK_COUNT) {
                        mClickCount = 0;
                        openOrCloseRecordFromFile();
                    }
                } else {
                    mClickCount = 1;
                }
                mTVClickLastTime = time;
            }
        });

        mTvAbout = (TextView) this.findViewById(R.id.about_info);
        mTvAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                if (time - mTVClickLastTime < CLICK_INTERVAL) {
                    ++mClickCount;
                    if (mClickCount >= CLICK_COUNT) {
                        mClickCount = 0;
                        openOrCloseWritePcmToFile();
                    }
                } else {
                    mClickCount = 1;
                }
                mTVClickLastTime = time;
            }
        });
    }

    private void openOrCloseRecordFromFile() {
        mIsReadFromFile = !mIsReadFromFile;
        if (mIsReadFromFile) {
            this.setTitle(getString(R.string.app_name) + getString(R.string.record_from_file));
        } else {
            this.setTitle(R.string.app_name);
        }
    }

    private void openOrCloseWritePcmToFile() {
        mEnableWritePcmToFile = !mEnableWritePcmToFile;
        mSinVoice.enableWritePcmToFile(mEnableWritePcmToFile);
        if (mEnableWritePcmToFile) {
            mTvAbout.setText(mTvAbout.getText() + getString(R.string.write_pcm_to_file));
        } else {
            mTvAbout.setText(R.string.info);
        }
    }

    private void initEffectSpinner() {
        Spinner spinner = (Spinner)this.findViewById(R.id.effect_spinner);
        mSpEffectParam = (Spinner)findViewById(R.id.effect_param_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mEffect = position + 1;
                mSinVoice.setEffect(mEffect);
                if (VoiceEncoder.ENCODER_EFFECT_CUSTOM == mEffect) {
                    mSpEffectParam.setVisibility(View.VISIBLE);
                    mSpEffectParam.setSelection(0);
                } else {
                    mSpEffectParam.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpEffectParam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (VoiceEncoder.ENCODER_EFFECT_CUSTOM == mEffect) {
                    mSinVoice.setEffectParam(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setVolume();
        if (null != mSinVoice) {
            mSinVoice.startListen(mIsReadFromFile);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != mSinVoice) {
            mSinVoice.stop();
        }

        restoreVolume();
    }

    private void setVolume() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int systemVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mSetVolume != systemVolume) {
            if (systemVolume != mSystemVolume) {
                mSystemVolume = systemVolume;
                mSetVolume = systemVolume;
            }
            am.setStreamVolume(AudioManager.STREAM_MUSIC, mSetVolume, 0);
        }
    }

    private void restoreVolume() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, mSystemVolume, 0);
    }

    public Bitmap createQRcodeImage(String url , int w , int h)
    {
        Bitmap bitmap = null;

        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * w + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mSinVoice) {
            mSinVoice.destroy();
        }
    }

    @Override
    public void onSinVoiceSendStart() {
        mBtnSend.setText(R.string.stop_send);
    }

    @Override
    public void onSinVoiceSendFinish() {
        mBtnSend.setText(R.string.send_data);
    }

    private void setState(String str1) {
        setState(str1, "");
    }

    private void setState(String str1, String str2) {
        mTvRegState.setText(String.format(mStateString, str1, str2));
    }

    @Override
    public void onSinVoiceStartListen() {
        setState(getString(R.string.state_listening_sonic));
        mBtnListen.setText(R.string.stop_listen);
    }

    @Override
    public void onSinVoiceStopListen() {
        setState(getString(R.string.state_stop_listening_sonic));
        mBtnListen.setText(R.string.start_listen);
    }

    @Override
    public void onSinVoiceReceiveStart() {
        setState(getString(R.string.state_receiving_data));
    }

    @Override
    public void onSinVoiceReceiveSuccess(String text) {
        setState(getString(R.string.state_listening_sonic), getString(R.string.state_receiving_successful));
        mTvRecognised.setText(text);
//        ImageView qrCode = findViewById(R.id.qrCode);
//
//        Bitmap bitmap = createQRcodeImage(text,280,280);
//
//        qrCode.setImageBitmap(bitmap);
    }

    @Override
    public void onSinVoiceReceiveFailed() {
        setState(getString(R.string.state_listening_sonic), getString(R.string.state_receiving_failed));
        mTvRecognised.setText("");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
