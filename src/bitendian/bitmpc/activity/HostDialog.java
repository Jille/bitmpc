/*
 * bitMPC
 *
 * Copyright 2010 BITENDIAN S.L.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 * 
 * Author: Juanan Guerrero (jguerrero@bitendian.com)
 * 
 */
package bitendian.bitmpc.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import nl.vvs_nijmegen.music.bitmpc.R;
import bitendian.bitmpc.main.HostItem;

/**
 * 
 * Create/Edit Host Dialog
 * 
 * @author juanan
 *
 */
public class HostDialog extends Dialog {

	private HostItem host;
	private boolean edit;
	private BitMPCHandler handler;
	
	private android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		
		public void onClick(View _v) {
			switch (_v.getId()) {
			case R.id.connection_button_ok:
				readAddress();
				if (!edit) handler.addHost(host);
				else handler.updateHost(host);
			}
			dismiss();
		}

		private void readNewAddress() {
			host = new HostItem(((EditText) findViewById(R.id.connection_name)).getText().toString(), ((EditText) findViewById(R.id.connection_host)).getText().toString(), Integer.parseInt(((EditText) findViewById(R.id.connection_port)).getText().toString()), ((EditText) findViewById(R.id.connection_password)).getText().toString(), ((CheckBox) findViewById(R.id.connection_auth)).isChecked());			
		}
		
		private void updateAddress() {
			host.name = ((EditText) findViewById(R.id.connection_name)).getText().toString();
			host.host = ((EditText) findViewById(R.id.connection_host)).getText().toString();
			host.port = Integer.parseInt(((EditText) findViewById(R.id.connection_port)).getText().toString());			
			host.password = ((EditText) findViewById(R.id.connection_password)).getText().toString();
			host.auth = ((CheckBox) findViewById(R.id.connection_auth)).isChecked(); 
		}
		
		private void readAddress() {
			if (host == null) {
				readNewAddress();
			} else {
				updateAddress();
			}
		}
	};
	
	public HostDialog(BitMPC _context) {
		super(_context);
		handler = _context.handler;
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.connection);

		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		addListeners();
	}

	private void addListeners() {
		findViewById(R.id.connection_button_ok).setOnClickListener(listener);
		findViewById(R.id.connection_button_cancel).setOnClickListener(listener);
		((CheckBox) findViewById(R.id.connection_auth)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
				((EditText) findViewById(R.id.connection_password)).setEnabled(_isChecked);				
			}
		});
	}
	
	@Override
	public void show() {
		show(null);
	}
	
	public void show(HostItem _host) {
		edit = _host != null;
		host = _host;		
		super.show();
		((EditText) findViewById(R.id.connection_name)).setText(host == null ? "" : host.name);
		((EditText) findViewById(R.id.connection_host)).setText(host == null ? "" : host.host);
		((EditText) findViewById(R.id.connection_port)).setText(host == null ? "6600" : "" + host.port);
		((CheckBox) findViewById(R.id.connection_auth)).setChecked(host == null ? false : host.auth);
		EditText password = (EditText) findViewById(R.id.connection_password);
		password.setText(host == null ? "" : host.password);
		password.setEnabled(host == null ? false : host.auth);

		if(host != null && host.host.equals("music.vvs-nijmegen.nl")) {
			((EditText) findViewById(R.id.connection_name)).setEnabled(false);
			((EditText) findViewById(R.id.connection_host)).setEnabled(false);
			((EditText) findViewById(R.id.connection_port)).setEnabled(false);
			((CheckBox) findViewById(R.id.connection_auth)).setEnabled(false);
		}
	}
	
	public void setAddress(HostItem _host) { host = _host; }
}
