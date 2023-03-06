package br.app.rml.dynamicgrid;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.app.rml.modelo.Comando;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:49 PM
 */

public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private Context mContext;

    private ArrayList<Object> mItems = new ArrayList<Object>();
    private int mColumnCount;

    protected BaseDynamicGridAdapter(Context context, int columnCount) {
        this.mContext = context;
        this.mColumnCount = columnCount;
    }

    public BaseDynamicGridAdapter(Context context, List<Comando> comandos, int columnCount) {
        mContext = context;
        mColumnCount = columnCount;
        init(comandos);
    }

    private void init(List< Comando > comandos) {
        addAllStableId(comandos);
        this.mItems.addAll(comandos);
    }


    public void set(List< Comando > comandos) {

        clear();
        init(comandos);
        notifyDataSetChanged();

    }

    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }

    public void add(Object item) {
        addStableId(item);
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void add(int position, Object item) {
        addStableId(item);
        mItems.add(position, item);
        notifyDataSetChanged();
    }

    public void add(List< Comando > comandos) {
        addAllStableId(comandos);
        this.mItems.addAll(comandos);
        notifyDataSetChanged();
    }


    public void remove(Object item) {
        mItems.remove(item);
        removeStableID(item);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
        notifyDataSetChanged();
    }

    @Override
    public void reorderItems(int originalPosition, int newPosition) {
        if (newPosition < getCount()) {
            DynamicGridUtils.reorder(mItems, originalPosition, newPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean canReorder(int position) {
        return true;
    }

    public List<Object> getItems() {
        return mItems;
    }

    protected Context getContext() {
        return mContext;
    }
}