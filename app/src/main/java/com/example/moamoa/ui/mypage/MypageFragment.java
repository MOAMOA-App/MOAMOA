package com.example.moamoa.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moamoa.R;
import com.example.moamoa.databinding.FragmentMyPageBinding;

public class MypageFragment extends Fragment implements AdapterView.OnItemClickListener{

    private FragmentMyPageBinding binding;
    static final String[] LIST_MENU = {"내 정보 수정", "만든 폼 관리", "참여한 폼 관리", "찜 목록", "앱 정보", "환경설정"} ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //MypageViewModel mypageViewModel =
        //        new ViewModelProvider(this).get(MypageViewModel.class);

        //binding = FragmentMyPageBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        //
        View view = inflater.inflate(R.layout.fragment_my_page, null) ;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) view.findViewById(R.id.listView) ;
        listview.setAdapter(adapter) ;

        return view ;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}