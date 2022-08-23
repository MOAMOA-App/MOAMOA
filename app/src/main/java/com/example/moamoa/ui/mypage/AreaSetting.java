package com.example.moamoa.ui.mypage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moamoa.R;


public class AreaSetting extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinnerCity, spinnerSigungu, spinnerDong;
    private ArrayAdapter<String> arrayAdapter;
    public static final String EXTRA_ADDRESS = "address";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.areasetting);

        spinnerCity = (Spinner)findViewById(R.id.spin_city);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(R.array.spinner_region));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(arrayAdapter);

        spinnerSigungu = (Spinner)findViewById(R.id.spin_sigungu);
//        spinnerDong = (Spinner)findViewById(R.id.spin_dong);

        initAddressSpinner();

        Button okBtn = findViewById(R.id.spinbtn_ok);
        okBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.spinbtn_ok) {
            if (spinnerCity.getSelectedItemPosition() == 0) {
//                MyToast.l(getApplicationContext(), "행정구역 주소를 입력해주세요. ^^");
            } else {
                Intent data = new Intent();

                if (spinnerDong.getSelectedItem() != null) {
                    String address = spinnerCity.getSelectedItem().toString() + " " + spinnerSigungu.getSelectedItem().toString() + " " + spinnerDong.getSelectedItem().toString();
                    data.putExtra(EXTRA_ADDRESS, address);
                } else {
                    String address = spinnerCity.getSelectedItem().toString() + " " + spinnerSigungu.getSelectedItem().toString();
                    data.putExtra(EXTRA_ADDRESS, address);
                }

                setResult(RESULT_OK, data);
                finish();
//            }

//        } else if(view.getId() == R.id.xbtn) {
//            finish();
            }
        }
    }

    private void initAddressSpinner() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 시군구, 동의 스피너를 초기화한다.
                switch (position) {
                    case 0:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_seoul);
                        break;
                    case 1:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_busan);
                        break;
                    case 2:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daegu);
                        break;
                    case 3:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_incheon);
                        break;
                    case 4:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gwangju);
                        break;
                    case 5:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_daejeon);
                        break;
                    case 6:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_ulsan);
                        break;
                    case 7:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_sejong);
                        break;
                    case 8:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeonggi);
                        break;
                    case 9:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gangwon);
                        break;
                    case 10:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_buk);
                        break;
                    case 11:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_nam);
                        break;
                    case 12:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_buk);
                        break;
                    case 13:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_nam);
                        break;
                    case 14:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_buk);
                        break;
                    case 15:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_nam);
                        break;
                    case 16:
                        setSigunguSpinnerAdapterItem(R.array.spinner_region_jeju);
                        break;
                    default:
                        spinnerSigungu.setAdapter(null);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        spinnerSigungu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // 서울특별시 선택시
//                if(spinnerCity.getSelectedItemPosition() == 1 && spinnerSigungu.getSelectedItemPosition() > -1) {
//                    switch(position) {
//                        //25
//                        case 0:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangnam);
//                            break;
//                        case 1:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangdong);
//                            break;
//                        case 2:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangbuk);
//                            break;
//                        case 3:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangseo);
//                            break;
//                        case 4:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwanak);
//                            break;
//                        case 5:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwangjin);
//                            break;
//                        case 6:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_guro);
//                            break;
//                        case 7:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_geumcheon);
//                            break;
//                        case 8:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_nowon);
//                            break;
//                        case 9:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dobong);
//                            break;
//                        case 10:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongdaemun);
//                            break;
//                        case 11:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongjag);
//                            break;
//                        case 12:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_mapo);
//                            break;
//                        case 13:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seodaemun);
//                            break;
//                        case 14:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seocho);
//                            break;
//                        case 15:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongdong);
//                            break;
//                        case 16:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongbuk);
//                            break;
//                        case 17:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_songpa);
//                            break;
//                        case 18:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yangcheon);
//                            break;
//                        case 19:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yeongdeungpo);
//                            break;
//                        case 20:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yongsan);
//                            break;
//                        case 21:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_eunpyeong);
//                            break;
//                        case 22:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jongno);
//                            break;
//                        case 23:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jung);
//                            break;
//                        case 24:
//                            setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jungnanggu);
//                            break;
//                    }
//                } else {
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void setSigunguSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerSigungu.setAdapter(null);
            arrayAdapter = null;
        }

        if (spinnerCity.getSelectedItemPosition() > 1) {
            spinnerDong.setAdapter(null);
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSigungu.setAdapter(arrayAdapter);
    }

    private void setDongSpinnerAdapterItem(int array_resource) {
        if (arrayAdapter != null) {
            spinnerDong.setAdapter(null);
            arrayAdapter = null;
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[])getResources().getStringArray(array_resource));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDong.setAdapter(arrayAdapter);
    }

}