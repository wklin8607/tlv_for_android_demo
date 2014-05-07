package com.example.tlvdemo;

import java.util.List;

import com.soloicesky.tlvutils.HexStringUtil;
import com.soloicesky.tlvutils.TLV;
import com.soloicesky.tlvutils.TLVUtils;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start=(Button)findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String tlvConstructedData = "9F26087D7013C90F9B688B9F2701809F101307010103A0A802010A01000000361907167E7E9F37049F0E155D9F36020795950500800400009A031310259C01009F02060000000000095F2A02015682027C009F1A0201569F03060000000000009F3303E0E1C89F34030203009F3501229F1E083132333435363738";
				
				/*SaveTlvObj stlv = new SaveTlvObj();
				TLVUtils.parseTLVString(
						HexStringUtil.hexStringToBytes(tlvConstructedData),
						tlvConstructedData.length() / 2, true, stlv);
				*/
				
				List<TLV> tlvs = TLVUtils.parseTLVString(HexStringUtil.hexStringToBytes(tlvConstructedData),
						tlvConstructedData.length() / 2);
				for(TLV tlv:tlvs){
					Log.d("TLV","tag: "+String.format("%X", tlv.getTag()));
					Log.d("TLV","length:"+tlv.getLength());
					Log.d("TLV","value:"+HexStringUtil.byteArrayToHexstring(tlv.getValue(), 0, tlv.getLength()));
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
