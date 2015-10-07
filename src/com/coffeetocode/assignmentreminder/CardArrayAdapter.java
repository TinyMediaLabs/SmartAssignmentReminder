package com.coffeetocode.assignmentreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CardArrayAdapter extends RecyclerView.Adapter<CardArrayAdapter.CardViewHolder> {
    DBHandler dbHandler;
    private List<Card> cardList = new ArrayList<Card>();
    private Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView desc;
        TextView subject;
        TextView deadline_date;
        ImageButton deletebtn;


        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cards_feed);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
            subject = (TextView) itemView.findViewById(R.id.subject);
            deadline_date = (TextView) itemView.findViewById(R.id.deadline_date);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delete_button);
        }
    }

    public CardArrayAdapter(Context context, int textViewResourceId) {
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assignment_card, viewGroup, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);
        return cardViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {

        Calendar c = cardList.get(i).getDeadline();

        cardViewHolder.deadline_date.setText(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +
                ", " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                " " + c.get(Calendar.DATE) +
                ", " + c.get(Calendar.YEAR));
        cardViewHolder.title.setText(cardList.get(i).getTitle());
        cardViewHolder.desc.setText(cardList.get(i).getDescription());
        cardViewHolder.subject.setText(cardList.get(i).getSubject());

        cardViewHolder.deletebtn.setTag(i);
        cardViewHolder.deletebtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                remove(pos);
                CardArrayAdapter.this.notifyDataSetChanged();
            }
        });

        RelativeLayout cardItem = (RelativeLayout) cardViewHolder.itemView.findViewById(R.id.card);
        cardItem.setTag(i);
        cardItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(context, EditAssignment.class);
                i.putExtra("assignmentID", getItem(pos).getID());
                ((Activity) context).startActivityForResult(i, MainActivity.EDIT_ASSIGNMENT_REQUEST);
            }
        });
    }

    public void clear() {
        cardList.clear();
        notifyDataSetChanged();
    }

    public void add(Card object) {
        cardList.add(object);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        dbHandler.deleteAssignment(cardList.get(index).getID());
        cardList.remove(index);
        notifyDataSetChanged();
        Toast.makeText(context, "Assignment deleted", Toast.LENGTH_SHORT).show();
    }

    public void swapCards(int i, int j) {
        Collections.swap(this.cardList, i, j);
    }

    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    /*
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
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

        TextView deadline_time = (TextView) row.findViewById(R.id.deadline_time);
        TextView deadline_date = (TextView) row.findViewById(R.id.deadline_date);

        Calendar c = dbHandler.getAssignment(card.getID()).getDeadline();

        deadline_date.setText(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +
                ", " + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                " " + c.get(Calendar.DATE) +
                ", " + c.get(Calendar.YEAR));

        deadline_time.setText(String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));

        RelativeLayout cardItem = (RelativeLayout) row.findViewById(R.id.card);
        cardItem.setTag(position);
        cardItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(context, EditAssignment.class);
                i.putExtra("assignmentID", getItem(pos).getID());
                ((Activity) context).startActivityForResult(i, MainActivity.EDIT_ASSIGNMENT_REQUEST);
            }
        });

        return row;
    }*/

    public void sortBy(String parameter) {
        if (parameter.equals("Deadline")) {
            int a = 0;
            for (int i = 0; i < cardList.size() - 1; i++) {
                a = i;
                for (int j = i + 1; j < cardList.size(); j++) {
                    if (this.getItem(a).getDeadline().compareTo(this.getItem(j).getDeadline()) == 1) {
                        a = j;
                    }
                }
                if (a != i) {
                    this.swapCards(a, i);
                }
            }
        } else {
            if (parameter.equals("Difficulty")) {
                int a = 0;
                for (int i = 0; i < cardList.size() - 1; i++) {
                    a = i;
                    for (int j = i + 1; j < cardList.size(); j++) {
                        if (this.getItem(a).getDifficulty() < this.getItem(j).getDifficulty()) {
                            a = j;
                        }
                    }
                    if (a != i) {
                        this.swapCards(a, i);
                    }
                }
            }
        }
    }

}
