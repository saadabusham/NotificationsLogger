package com.sedo.notificationlogger.ui.logger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sedo.notificationlogger.R;
import com.sedo.notificationlogger.data.models.LoggerModel;

public class TransactionOverviewFragment extends Fragment implements TransactionFragment {

    TextView title;
    TextView subtitle;
    TextView key;
    TextView status;
    TextView data;
    TextView requestTime;
    TextView responseTime;
    TextView duration;

    private LoggerModel transaction;

    public TransactionOverviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_logger_fragment_transaction_overview, container, false);
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
        key = (TextView) view.findViewById(R.id.key);
        status = (TextView) view.findViewById(R.id.status);
        data = (TextView) view.findViewById(R.id.data);
        requestTime = (TextView) view.findViewById(R.id.request_time);
        responseTime = (TextView) view.findViewById(R.id.response_time);
        duration = (TextView) view.findViewById(R.id.duration);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void transactionUpdated(LoggerModel transaction) {
        this.transaction = transaction;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && transaction != null) {
            title.setText(transaction.getTitle());
            subtitle.setText(transaction.getSubtitle());
            key.setText(transaction.getKey());
            status.setText(LoggerModel.Status.fromInt(Integer.parseInt(transaction.getStatus())).name());
            data.setText(transaction.getData());
            requestTime.setText(transaction.getRequestStartTimeString());
            responseTime.setText(transaction.getRequestEndTimeString());
            duration.setText(transaction.getDurationString());
        }
    }
}