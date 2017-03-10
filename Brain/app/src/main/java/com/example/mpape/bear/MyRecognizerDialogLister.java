package com.example.mpape.bear;
import android.content.Context;
import android.widget.Toast;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;
public class MyRecognizerDialogLister implements RecognizerDialogListener{
	private Context context;
	private String ans;
	VoiceToWord vv;
	public MyRecognizerDialogLister(Context context,VoiceToWord wtw)
	{
		this.context = context;
		vv=wtw;
	}
	public void onResult(RecognizerResult results, boolean isLast) {
		String text = JsonParser.parseIatResult(results.getResultString());
		System.out.println(text);
		ans=text;
		vv.dos(text);
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	public void onError(SpeechError error) {
		int errorCoder = error.getErrorCode();
		switch (errorCoder) {
			case 10118:
				System.out.println("user don't speak anything");
				break;
			case 10204:
				System.out.println("can't connect to internet");
				break;
			default:
				break;
		}
	}
}