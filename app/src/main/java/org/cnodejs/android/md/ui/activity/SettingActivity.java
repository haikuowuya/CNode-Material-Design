package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.storage.SettingShared;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.setting_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.setting_switch_notification)
    protected SwitchCompat switchNotification;

    @Bind(R.id.setting_switch_theme_dark)
    protected SwitchCompat switchThemeDark;

    @Bind(R.id.setting_switch_new_topic_draft)
    protected SwitchCompat switchNewTopicDraft;

    @Bind(R.id.setting_switch_topic_sign)
    protected SwitchCompat switchTopicSign;

    @Bind(R.id.setting_btn_modify_topic_sign)
    protected TextView btnModifyTopicSign;

    @Bind(R.id.setting_switch_third_party_image_upload_api)
    protected SwitchCompat switchThirdPartyImageUploadApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        switchNotification.setChecked(SettingShared.isEnableNotification(this));
        switchThemeDark.setChecked(SettingShared.isEnableThemeDark(this));
        switchNewTopicDraft.setChecked(SettingShared.isEnableNewTopicDraft(this));
        switchTopicSign.setChecked(SettingShared.isEnableTopicSign(this));
        btnModifyTopicSign.setEnabled(SettingShared.isEnableTopicSign(this));
        switchThirdPartyImageUploadApi.setChecked(SettingShared.isEnableThirdPartyImageUploadApi(this));
    }

    @OnClick(R.id.setting_btn_notification)
    protected void onBtnNotificationClick() {
        switchNotification.toggle();
        SettingShared.setEnableNotification(this, switchNotification.isChecked());
    }

    @OnClick(R.id.setting_btn_theme_dark)
    protected void onBtnThemeDarkClick() {
        switchThemeDark.toggle();
        SettingShared.setEnableThemeDark(this, switchThemeDark.isChecked());

        // TODO 

    }

    @OnClick(R.id.setting_btn_new_topic_draft)
    protected void onBtnNewTopicDraftClick() {
        switchNewTopicDraft.toggle();
        SettingShared.setEnableNewTopicDraft(this, switchNewTopicDraft.isChecked());
    }

    @OnClick(R.id.setting_btn_topic_sign)
    protected void onBtnTopicSignClick() {
        switchTopicSign.toggle();
        SettingShared.setEnableTopicSign(this, switchTopicSign.isChecked());
        btnModifyTopicSign.setEnabled(switchTopicSign.isChecked());
    }

    @OnClick(R.id.setting_btn_modify_topic_sign)
    protected void onBtnModifyTopicSignClick() {
        startActivity(new Intent(this, ModifyTopicSignActivity.class));
    }

    @OnClick(R.id.setting_btn_third_party_image_upload_api)
    protected void onBtnThirdPartyImageUploadApiClick() {
        switchThirdPartyImageUploadApi.toggle();
        SettingShared.setEnableThirdPartyImageUploadApi(this, switchThirdPartyImageUploadApi.isChecked());

        // TODO
        Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
        // TODO
    }

}
