// Generated by view binder compiler. Do not edit!
package com.example.moamoa.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.moamoa.R;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentChatsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button ButtonSend;

  @NonNull
  public final Button ButtonSendImg;

  @NonNull
  public final TextInputEditText EditTextChat;

  @NonNull
  public final RecyclerView chatsRecyclerview;

  @NonNull
  public final LinearLayout fragmentChatsView;

  private FragmentChatsBinding(@NonNull LinearLayout rootView, @NonNull Button ButtonSend,
      @NonNull Button ButtonSendImg, @NonNull TextInputEditText EditTextChat,
      @NonNull RecyclerView chatsRecyclerview, @NonNull LinearLayout fragmentChatsView) {
    this.rootView = rootView;
    this.ButtonSend = ButtonSend;
    this.ButtonSendImg = ButtonSendImg;
    this.EditTextChat = EditTextChat;
    this.chatsRecyclerview = chatsRecyclerview;
    this.fragmentChatsView = fragmentChatsView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentChatsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentChatsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_chats, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentChatsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Button_send;
      Button ButtonSend = ViewBindings.findChildViewById(rootView, id);
      if (ButtonSend == null) {
        break missingId;
      }

      id = R.id.Button_sendImg;
      Button ButtonSendImg = ViewBindings.findChildViewById(rootView, id);
      if (ButtonSendImg == null) {
        break missingId;
      }

      id = R.id.EditText_chat;
      TextInputEditText EditTextChat = ViewBindings.findChildViewById(rootView, id);
      if (EditTextChat == null) {
        break missingId;
      }

      id = R.id.chats_recyclerview;
      RecyclerView chatsRecyclerview = ViewBindings.findChildViewById(rootView, id);
      if (chatsRecyclerview == null) {
        break missingId;
      }

      LinearLayout fragmentChatsView = (LinearLayout) rootView;

      return new FragmentChatsBinding((LinearLayout) rootView, ButtonSend, ButtonSendImg,
          EditTextChat, chatsRecyclerview, fragmentChatsView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
