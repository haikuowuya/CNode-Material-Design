package org.cnodejs.android.md.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.adapter.NotificationAdapter;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.storage.LoginShared;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.notification_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.notification_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @Bind(R.id.notification_recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.notification_layout_no_data)
    protected ViewGroup layoutNoData;

    private NotificationAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);

        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, messageList);
        recyclerView.setAdapter(adapter);

        HandlerUtils.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }

        }, 100); // refreshLayout无法直接在onCreate中设置刷新状态
    }

    @Override
    public void onRefresh() {
        ApiClient.service.getMessages(LoginShared.getAccessToken(this), new Callback<Result<Notification>>() {

            @Override
            public void success(Result<Notification> result, Response response) {
                if (!isFinishing()) {
                    messageList.clear();
                    messageList.addAll(result.getData().getHasNotReadMessages());
                    messageList.addAll(result.getData().getHasReadMessages());
                    notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                        showAccessTokenErrorDialog();
                    } else {
                        Toast.makeText(NotificationActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
                    }
                    refreshLayout.setRefreshing(false);
                }
            }

        });
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        layoutNoData.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void showAccessTokenErrorDialog() {
        new MaterialDialog.Builder(this)
                .content(R.string.access_token_error_tip)
                .positiveText(R.string.confirm)
                .show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_all:
                markAllMessageReadAsyncTask();
                return true;
            default:
                return false;
        }
    }

    private void markAllMessageReadAsyncTask() {
        ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(this), new Callback<Void>() {

            @Override
            public void success(Void nothing, Response response) {
                if (!isFinishing()) {
                    for (Message message : messageList) {
                        message.setRead(true);
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                        showAccessTokenErrorDialog();
                    } else {
                        Toast.makeText(NotificationActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

}