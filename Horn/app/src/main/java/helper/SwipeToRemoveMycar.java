package helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.horn.workshop.R;

import adapters.MyCarAdapter;

/**
 * Created by Sariga on 2/15/2016.
 */
public class SwipeToRemoveMycar extends ItemTouchHelper.SimpleCallback {
    private MyCarAdapter mMyCarAdapter;
    private RecyclerView mrecyclerView;
    Context context;
    public SwipeToRemoveMycar(MyCarAdapter mMyCarAdapter, RecyclerView recyclerView){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
        this.mMyCarAdapter = mMyCarAdapter;
        mrecyclerView = recyclerView;
        context = recyclerView.getContext();
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return false;
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

        new AlertDialog.Builder(context)
                .setMessage("Are you sure?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mMyCarAdapter.remove(viewHolder.getAdapterPosition());
                    }
                })
                .setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        mMyCarAdapter.notifyDataSetChanged();
                      // mMyCarAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }

}
