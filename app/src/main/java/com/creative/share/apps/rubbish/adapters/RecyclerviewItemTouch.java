package com.creative.share.apps.rubbish.adapters;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerviewItemTouch extends ItemTouchHelper.SimpleCallback {


    private SwipeListener listener;
    public RecyclerviewItemTouch(int dragDirs, int swipeDirs,SwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener!=null)
        {
            listener.onSwipe(viewHolder,direction,viewHolder.getAdapterPosition());


        }

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder!=null)
        {
            View view_foreground = ((EmployeeOrderAdapter.MyHolder) viewHolder).cons_foreground;
            getDefaultUIUtil().onSelected(view_foreground);

        }



    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View view_foreground = ((EmployeeOrderAdapter.MyHolder) viewHolder).cons_foreground;
        getDefaultUIUtil().clearView(view_foreground);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            EmployeeOrderAdapter.MyHolder myHolder = (EmployeeOrderAdapter.MyHolder) viewHolder;

            if (dX>0)
            {
                myHolder.ll_left.setVisibility(View.VISIBLE);
                myHolder.ll_right.setVisibility(View.INVISIBLE);
            }else
            {
                myHolder.ll_left.setVisibility(View.INVISIBLE);
                myHolder.ll_right.setVisibility(View.VISIBLE);

            }

        }
        View view_foreground = ((EmployeeOrderAdapter.MyHolder) viewHolder).cons_foreground;

        getDefaultUIUtil().onDraw(c,recyclerView,view_foreground,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view_foreground = ((EmployeeOrderAdapter.MyHolder) viewHolder).cons_foreground;

        getDefaultUIUtil().onDrawOver(c,recyclerView,view_foreground,dX,dY,actionState,isCurrentlyActive);


    }


    public interface SwipeListener
    {
        void onSwipe(RecyclerView.ViewHolder viewHolder,int direction,int position);
    }

}
