package com.example.mpape.bear;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
public class VoiceToWord extends Activity{
	private Context context;
	private Toast mToast;
	private RecognizerDialog iatDialog;
	private SpeechRecognizer iatRecognizer;
	private SharedPreferences mSharedPreferences;
	private RecognizerDialogListener recognizerDialogListener = null;
	MyApplication app;
	public VoiceToWord(Context context,String APP_ID,MyApplication ap) {
		app=ap;
		this.context = context;
		SpeechUser.getUser().login(context, null, null
				, "appid=" + APP_ID, listener);
		iatDialog =new RecognizerDialog(context);
		mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		iatDialog =new RecognizerDialog(context);
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
	}
	public VoiceToWord(Context context,String APP_ID,RecognizerDialogListener recognizerDialogListener)
	{
		this.context = context;
		SpeechUser.getUser().login(context, null, null
				, "appid=" + APP_ID, listener);
		iatDialog =new RecognizerDialog(context);
		mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		iatDialog =new RecognizerDialog(context);
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
		this.recognizerDialogListener = recognizerDialogListener;
	}
	public void GetWordFromVoice()
	{
		boolean isShowDialog = mSharedPreferences.getBoolean("iat_show",true);
		if (isShowDialog) {
			showIatDialog();
		} else {
			if(null == iatRecognizer) {
				iatRecognizer=SpeechRecognizer.createRecognizer(this);
			}
			if(iatRecognizer.isListening()) {
				iatRecognizer.stopListening();
			} else {
			}
		}
	}
	private void showTip(String str)
	{
		if(!TextUtils.isEmpty(str))
		{
			mToast.setText(str);
			mToast.show();
		}
	}
	public void showIatDialog()
	{
		if(null == iatDialog) {
			//初始化听写Dialog
			iatDialog =new RecognizerDialog(this);
		}
		String engine = mSharedPreferences.getString(
				"iat_engine",
				"iat");
		iatDialog.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
		iatDialog.setParameter(SpeechConstant.DOMAIN, engine);
		String rate = mSharedPreferences.getString(
				"sf",
				"sf");
		if(rate.equals("rate8k"))
		{
			iatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
		}
		else
		{
			iatDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		}
		if(recognizerDialogListener == null)
		{
			getRecognizerDialogListener();
		}
		iatDialog.setListener(recognizerDialogListener);
		iatDialog.show();
	}
	private void getRecognizerDialogListener()
	{
		recognizerDialogListener=new MyRecognizerDialogLister(context,this);
	}

	private SpeechListener listener = new SpeechListener()
	{
		public void onData(byte[] arg0) {
		}
		public void onCompleted(SpeechError error) {
			if(error != null) {
				System.out.println("user login success");
			}
		}
		public void onEvent(int arg0, Bundle arg1) {
		}
	};
	public void dos(String a){
		if(a.contains("左"))
			sendData("A");
		else if(a.contains("右"))
			sendData("D");
		else if(a.contains("前"))
			sendData("W");
		else if(a.contains("后"))
			sendData("S");
	}
	boolean sendData(String m){
		try{
			String msg = m;
			app.mmOutputStream.write(msg.getBytes());
			System.out.println("Data Sent");
			return true;
		}catch (Exception e){
			return false;
		}
	}
}
