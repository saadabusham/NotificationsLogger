package com.sedo.notificationlogger.ui.logger;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sedo.notificationlogger.R;
import com.sedo.notificationlogger.data.models.LoggerModel;
import com.sedo.notificationlogger.data.models.LocalCupboard;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final Context context;
    private final TransactionListFragment.OnListFragmentInteractionListener listener;
    private final CursorAdapter cursorAdapter;

    private final int colorDefault;
    private final int colorRequested;
    private final int colorError;
    private final int color500;
    private final int color400;
    private final int color300;

    TransactionAdapter(Context context, TransactionListFragment.OnListFragmentInteractionListener listener) {
        this.listener = listener;
        this.context = context;
        colorDefault = ContextCompat.getColor(context, R.color.status_default);
        colorRequested = ContextCompat.getColor(context, R.color.status_requested);
        colorError = ContextCompat.getColor(context, R.color.status_error);
        color500 = ContextCompat.getColor(context, R.color.status_500);
        color400 = ContextCompat.getColor(context, R.color.status_400);
        color300 = ContextCompat.getColor(context, R.color.status_300);

        cursorAdapter = new CursorAdapter(TransactionAdapter.this.context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_logger_list_item_transaction, parent, false);
                ViewHolder holder = new ViewHolder(itemView);
                itemView.setTag(holder);
                return itemView;
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                final LoggerModel transaction = LocalCupboard.getInstance().withCursor(cursor).get(LoggerModel.class);
                final ViewHolder holder = (ViewHolder) view.getTag();
                holder.title.setText(transaction.getTitle());
                holder.start.setText(transaction.getRequestStartTimeString());
                if (Integer.parseInt(transaction.getStatus()) == LoggerModel.Status.Complete.getMode()) {
                    holder.key.setText(transaction.getKey());
                } else {
                    holder.key.setText(null);
                }
                if (Integer.parseInt(transaction.getStatus()) == LoggerModel.Status.Failed.getMode()) {
                    holder.key.setText("!!!");
                }
                setStatusColor(holder, transaction);
                holder.transaction = transaction;
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != TransactionAdapter.this.listener) {
                            TransactionAdapter.this.listener.onListFragmentInteraction(holder.transaction);
                        }
                    }
                });
            }

            private void setStatusColor(ViewHolder holder, LoggerModel transaction) {
                int color;
//                if (transaction.getStatus() == LoggerModel.Status.Failed) {
//                    color = colorError;
//                } else if (transaction.getStatus() == LoggerModel.Status.Requested) {
//                    color = colorRequested;
//                } else if (transaction.getResponseCode() >= 500) {
//                    color = color500;
//                } else if (transaction.getResponseCode() >= 400) {
//                    color = color400;
//                } else if (transaction.getResponseCode() >= 300) {
//                    color = color300;
//                } else {
//                    color = colorDefault;
//                }
//                holder.key.setTextColor(color);
//                holder.title.setTextColor(color);
            }
        };
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    void swapCursor(Cursor newCursor) {
        cursorAdapter.swapCursor(newCursor);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView key;
        public final TextView title;
        public final TextView start;
        LoggerModel transaction;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            key = (TextView) view.findViewById(R.id.key);
            title = (TextView) view.findViewById(R.id.title);
            start = (TextView) view.findViewById(R.id.start);
        }
    }
}