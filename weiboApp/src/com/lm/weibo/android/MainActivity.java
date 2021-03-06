package com.lm.weibo.android;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lm.weibo.android.utils.Util;
import com.lm.weibo.android.views.FragmentHome;
import com.lm.weibo.android.views.WriteWeiboActivity;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private int select_item_number = -1;

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPixels();
		copyDBToSD();

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		onNavigationDrawerItemSelected(0);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		onSectionAttached(position);
		if (select_item_number != position) {
			select_item_number = position;
			FragmentManager fragmentManager = getFragmentManager();
			switch (select_item_number) {
			case 0:
				fragmentManager.beginTransaction()
						.replace(R.id.container, new FragmentHome()).commit();
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				break;
			}
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 0:
			mTitle = getString(R.string.title_section1);
			break;
		case 1:
			mTitle = getString(R.string.title_section2);
			break;
		case 2:
			mTitle = getString(R.string.title_section3);
			break;
		case 3:
			mTitle = getString(R.string.title_section4);
			break;
		case 4:
			mTitle = getString(R.string.title_section5);
			break;
		default:
			mTitle = getString(R.string.app_name);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_example:
			Toast.makeText(MainActivity.this, "Write weibo", Toast.LENGTH_SHORT)
					.show();
			// TODO 发送微博需要优化
			Intent intent = new Intent(MainActivity.this, WriteWeiboActivity.class);
			startActivity(intent);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getPixels() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		Util.widthPixels = mDisplayMetrics.widthPixels;
		Util.heightPixels = mDisplayMetrics.heightPixels;
	}

	// TODO 代码需要优化的地方,只在程序第一次启动的时候拷贝数据库
	private void copyDBToSD() {
		InputStream myInput;
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/"
							+ "sina_weibo.db");
			myInput = this.getAssets().open("sina_weibo.db");
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}

			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
