package com.example.moamoa.ui.search;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

    private static String search_std, search_input;    // std: 검색기준, input: EditText_search 텍스트화

    static ArrayList<Integer> my_category = new ArrayList<>();   // 카테고리 숫자 담을 리스트
    static ArrayList<Integer> my_state = new ArrayList<>();      // 게시글 상태 숫자 담을 리스트
    static int sort_std;
}
