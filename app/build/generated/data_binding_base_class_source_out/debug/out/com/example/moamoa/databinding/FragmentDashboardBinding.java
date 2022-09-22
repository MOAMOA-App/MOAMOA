// Generated by view binder compiler. Do not edit!
package com.example.moamoa.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.moamoa.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDashboardBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText address;

  @NonNull
  public final EditText addressDetail;

  @NonNull
  public final Button buttonAddr;

  @NonNull
  public final Button buttonDashboard;

  @NonNull
  public final Button buttonImgs;

  @NonNull
  public final CheckBox checkBox;

  @NonNull
  public final EditText cost;

  @NonNull
  public final LinearLayout dashboard;

  @NonNull
  public final TextView dashboardbarname;

  @NonNull
  public final AppBarLayout dashboardtoolbar;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final EditText maxCount;

  @NonNull
  public final TextView numForm;

  @NonNull
  public final GridView photoDash;

  @NonNull
  public final RadioButton radioButton1;

  @NonNull
  public final RadioButton radioButton2;

  @NonNull
  public final RadioButton radioButton3;

  @NonNull
  public final RadioGroup radioGroup;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final Spinner spinner;

  @NonNull
  public final EditText subject;

  @NonNull
  public final EditText text;

  @NonNull
  public final TextView textDashboardend;

  @NonNull
  public final TextView textDashboardstart;

  @NonNull
  public final Toolbar toolbar;

  private FragmentDashboardBinding(@NonNull LinearLayout rootView, @NonNull EditText address,
      @NonNull EditText addressDetail, @NonNull Button buttonAddr, @NonNull Button buttonDashboard,
      @NonNull Button buttonImgs, @NonNull CheckBox checkBox, @NonNull EditText cost,
      @NonNull LinearLayout dashboard, @NonNull TextView dashboardbarname,
      @NonNull AppBarLayout dashboardtoolbar, @NonNull ImageView imageView,
      @NonNull EditText maxCount, @NonNull TextView numForm, @NonNull GridView photoDash,
      @NonNull RadioButton radioButton1, @NonNull RadioButton radioButton2,
      @NonNull RadioButton radioButton3, @NonNull RadioGroup radioGroup,
      @NonNull RecyclerView recyclerView, @NonNull Spinner spinner, @NonNull EditText subject,
      @NonNull EditText text, @NonNull TextView textDashboardend,
      @NonNull TextView textDashboardstart, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.address = address;
    this.addressDetail = addressDetail;
    this.buttonAddr = buttonAddr;
    this.buttonDashboard = buttonDashboard;
    this.buttonImgs = buttonImgs;
    this.checkBox = checkBox;
    this.cost = cost;
    this.dashboard = dashboard;
    this.dashboardbarname = dashboardbarname;
    this.dashboardtoolbar = dashboardtoolbar;
    this.imageView = imageView;
    this.maxCount = maxCount;
    this.numForm = numForm;
    this.photoDash = photoDash;
    this.radioButton1 = radioButton1;
    this.radioButton2 = radioButton2;
    this.radioButton3 = radioButton3;
    this.radioGroup = radioGroup;
    this.recyclerView = recyclerView;
    this.spinner = spinner;
    this.subject = subject;
    this.text = text;
    this.textDashboardend = textDashboardend;
    this.textDashboardstart = textDashboardstart;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.address;
      EditText address = ViewBindings.findChildViewById(rootView, id);
      if (address == null) {
        break missingId;
      }

      id = R.id.address_detail;
      EditText addressDetail = ViewBindings.findChildViewById(rootView, id);
      if (addressDetail == null) {
        break missingId;
      }

      id = R.id.button_addr;
      Button buttonAddr = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddr == null) {
        break missingId;
      }

      id = R.id.button_dashboard;
      Button buttonDashboard = ViewBindings.findChildViewById(rootView, id);
      if (buttonDashboard == null) {
        break missingId;
      }

      id = R.id.button_imgs;
      Button buttonImgs = ViewBindings.findChildViewById(rootView, id);
      if (buttonImgs == null) {
        break missingId;
      }

      id = R.id.checkBox;
      CheckBox checkBox = ViewBindings.findChildViewById(rootView, id);
      if (checkBox == null) {
        break missingId;
      }

      id = R.id.cost;
      EditText cost = ViewBindings.findChildViewById(rootView, id);
      if (cost == null) {
        break missingId;
      }

      LinearLayout dashboard = (LinearLayout) rootView;

      id = R.id.dashboardbarname;
      TextView dashboardbarname = ViewBindings.findChildViewById(rootView, id);
      if (dashboardbarname == null) {
        break missingId;
      }

      id = R.id.dashboardtoolbar;
      AppBarLayout dashboardtoolbar = ViewBindings.findChildViewById(rootView, id);
      if (dashboardtoolbar == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.max_count;
      EditText maxCount = ViewBindings.findChildViewById(rootView, id);
      if (maxCount == null) {
        break missingId;
      }

      id = R.id.num_form;
      TextView numForm = ViewBindings.findChildViewById(rootView, id);
      if (numForm == null) {
        break missingId;
      }

      id = R.id.photo_dash;
      GridView photoDash = ViewBindings.findChildViewById(rootView, id);
      if (photoDash == null) {
        break missingId;
      }

      id = R.id.radioButton1;
      RadioButton radioButton1 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton1 == null) {
        break missingId;
      }

      id = R.id.radioButton2;
      RadioButton radioButton2 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton2 == null) {
        break missingId;
      }

      id = R.id.radioButton3;
      RadioButton radioButton3 = ViewBindings.findChildViewById(rootView, id);
      if (radioButton3 == null) {
        break missingId;
      }

      id = R.id.radioGroup;
      RadioGroup radioGroup = ViewBindings.findChildViewById(rootView, id);
      if (radioGroup == null) {
        break missingId;
      }

      id = R.id.recyclerView;
      RecyclerView recyclerView = ViewBindings.findChildViewById(rootView, id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.spinner;
      Spinner spinner = ViewBindings.findChildViewById(rootView, id);
      if (spinner == null) {
        break missingId;
      }

      id = R.id.subject;
      EditText subject = ViewBindings.findChildViewById(rootView, id);
      if (subject == null) {
        break missingId;
      }

      id = R.id.text;
      EditText text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      id = R.id.text_dashboardend;
      TextView textDashboardend = ViewBindings.findChildViewById(rootView, id);
      if (textDashboardend == null) {
        break missingId;
      }

      id = R.id.text_dashboardstart;
      TextView textDashboardstart = ViewBindings.findChildViewById(rootView, id);
      if (textDashboardstart == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentDashboardBinding((LinearLayout) rootView, address, addressDetail,
          buttonAddr, buttonDashboard, buttonImgs, checkBox, cost, dashboard, dashboardbarname,
          dashboardtoolbar, imageView, maxCount, numForm, photoDash, radioButton1, radioButton2,
          radioButton3, radioGroup, recyclerView, spinner, subject, text, textDashboardend,
          textDashboardstart, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
