package com.applovin.apps.demoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public abstract class DemoMenuActivity
        extends AppCompatActivity
{
    protected ListView listView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list );

        listView = (ListView) findViewById( R.id.listView );

        setupListViewFooter();
        setupListViewContents( getListViewContents() );
    }

    protected void setupListViewFooter()
    {

    }

    protected abstract DemoMenuItem[] getListViewContents();

    private void setupListViewContents(final DemoMenuItem[] items)
    {
        ArrayAdapter<DemoMenuItem> listAdapter = new ArrayAdapter<DemoMenuItem>( this, android.R.layout.simple_list_item_2, items )
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = convertView;
                if ( row == null )
                {
                    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    row = inflater.inflate( android.R.layout.simple_list_item_2, parent, false );
                }

                DemoMenuItem item = items[position];

                TextView title = (TextView) row.findViewById( android.R.id.text1 );
                title.setText( item.getTitle() );
                TextView subtitle = (TextView) row.findViewById( android.R.id.text2 );
                subtitle.setText( item.getSubtitle() );

                return row;
            }
        };
        listView.setAdapter( listAdapter );

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = items[position].getIntent();
                if ( intent != null ) startActivity( intent );
            }
        };
        listView.setOnItemClickListener( itemClickListener );
    }
}
