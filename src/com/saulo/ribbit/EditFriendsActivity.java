package com.saulo.ribbit;

import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends ListActivity {
	public static final String TAG = EditFriendsActivity.class.getSimpleName();

	protected List<ParseUser> mUsers;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setProgressBarIndeterminateVisibility(true);
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.orderByAscending(ParseConstants.KEY_USERNAME);
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				if (e == null) {
					mUsers = users;
					String[] usernames = new String[mUsers.size()];
					int i = 0;
					
					for (ParseUser user : mUsers) {
						usernames[i] = user.getUsername();
						i++;
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							EditFriendsActivity.this, 
							android.R.layout.simple_list_item_checked, 
							usernames);
					setListAdapter(adapter);
					
					addFriendsCheckmarks();
				} else {
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
					builder.setTitle(R.string.error_title)
						.setMessage(e.getMessage())
						.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		setProgressBarIndeterminateVisibility(true);
		
		
		if (getListView().isItemChecked(position)) {
			mFriendsRelation.add(mUsers.get(position));
			Toast.makeText(EditFriendsActivity.this, R.string.user_added_toast, Toast.LENGTH_SHORT).show();
		} else {
			mFriendsRelation.remove(mUsers.get(position));
			Toast.makeText(EditFriendsActivity.this, R.string.user_deleted_toast, Toast.LENGTH_SHORT).show();
		}

		mCurrentUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e != null) {
					Log.e(TAG, e.getMessage());
				}
			}
		});			
	}
	
	private void addFriendsCheckmarks() {
		mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					for (int i = 0; i < mUsers.size(); i++) {
						ParseUser user = mUsers.get(i);
						for ( ParseUser friend : friends ) {
							if (friend.getObjectId().equals(user.getObjectId())) {
								getListView().setItemChecked(i, true);
							}
						}
					}
				} else {
					Log.e(TAG, e.getMessage());
				}
			}
		});
	}
}
