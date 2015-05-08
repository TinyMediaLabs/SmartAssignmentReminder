package com.coffeetocode.assignmentreminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardArrayAdapter extends ArrayAdapter<Card> {
    DBHandler dbHandler = new DBHandler(getContext());
    private List<Card> cardList = new ArrayList<Card>();
    private Context context;

    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
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
            viewHolder.subject = (TextView) row.findViewById(R.id.subject);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Card card = getItem(position);
        viewHolder.title.setText(card.getTitle());
        viewHolder.desc.setText(card.getDescription());
        viewHolder.subject.setText(card.getSubject());

        ImageButton del = (ImageButton) row.findViewById(R.id.delete_button);
        del.setTag(position);
        del.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                remove(pos);
                CardArrayAdapter.this.notifyDataSetChanged();
            }
        });

        RelativeLayout cardItem = (RelativeLayout) row.findViewById(R.id.card);
        cardItem.setTag(position);
        cardItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(context, EditAssignment.class);
                i.putExtra("assignmentID", getItem(pos).getID());
                context.startActivity(i);
            }
        });

        return row;
    }

    static class CardViewHolder {
        TextView title;
        TextView desc;
        TextView subject;
    }


}
