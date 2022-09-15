package com.example.moamoa.ui.dashboard;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.moamoa.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 지도*/
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static NaverMap naverMap;
    private LatLng myLatLng = new LatLng( 37.3399, 126.733);
    Marker marker = new Marker();
    private Geocoder geocoder;
    Button btn_map,btn_submit;
    EditText adr;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("xjdzzwh9wk"));
        mapView = (MapView)findViewById(R.id.mv);
        btn_map = (Button)findViewById(R.id.button_map);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        adr = (EditText)findViewById(R.id.map_text);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        geocoder = new Geocoder(this);

        btn_map.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=adr.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[splitStr.length-6].substring(splitStr[splitStr.length-6].indexOf("=") + 1); // 위도
                String longitude = splitStr[splitStr.length-4].substring(splitStr[splitStr.length-4].indexOf("=") + 1); // 경도

                Log.d("MainActivity", "vv: " + latitude);
                Log.d("MainActivity", "vv: " + longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                // 마커 생성
                marker.setPosition(point);
                marker.setMap(naverMap);
                // 해당 좌표로 화면 줌
                CameraPosition cameraPosition = new CameraPosition(point, 16);
                naverMap.setCameraPosition(cameraPosition);
            }
        });

        btn_submit.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                Bundle bundle = new Bundle(); // 번들을 통해 값 전달
                bundle.putString("address",address);//번들에 넘길 값 저장
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                DashboardFragment fragment2 = new DashboardFragment();//프래그먼트2 선언
                fragment2.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.address, fragment2);
                transaction.commit();


            }

        });



    }

}
