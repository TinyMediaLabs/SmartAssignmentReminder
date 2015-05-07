package com.coffeetocode.assignmentreminder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CardArrayAdapter extends ArrayAdapter<Card> {
    private List<Card> cardList = new ArrayList<Card>();
    DBHandler dbHandler = new DBHandler(getContext());

    static class CardViewHolder {
        TextView title;
        TextView desc;
    }

    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void clear() {
        cardList.clear();
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    public void remove(int index) {
        super.remove(cardList.get(index));
        dbHandler.deleteAssignment(cardList.get(index).getID());
        cardList.remove(index);
        Toast.makeText(getContext(), "Assignment deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.assignment_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.title = (TextView) row.findViewById(R.id.title);
            viewHolder.desc = (TextView) row.findViewById(R.id.description);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Card card = getItem(position);
        viewHolder.title.setText(card.getTitle());
        viewHolder.desc.setText(card.getDescription());

        ImageButton del = (ImageButton) row.findViewById(R.id.delete_button);
        del.setTag(position);
        del.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                remove(pos);
                // TODO: DELETE FROM DATABASE HERE
                CardArrayAdapter.this.notifyDataSetChanged();
            }
        });
        return row;
    }


}
