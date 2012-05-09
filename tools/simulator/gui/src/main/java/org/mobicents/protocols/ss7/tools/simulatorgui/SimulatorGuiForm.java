/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tools.simulatorgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L1;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L2;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L3;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_TestTask;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHostMBean;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.TestUssdClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.testsussd.TestUssdClientForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.testsussd.TestUssdClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.testsussd.TestUssdServerForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.testsussd.TestUssdServerParamForm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SimulatorGuiForm extends JFrame implements NotificationListener {

	private static final long serialVersionUID = 3154289048277602010L;

	private TesterHost hostImpl;
	private TesterHostMBean host;
	private M3uaManMBean m3ua;
	private SccpManMBean sccp;
	private MapManMBean map;
	
	private TestUssdClientManMBean ussdClient;
	private TestUssdServerManMBean ussdServer;
	
	private TestingForm testingForm;
	
	private JPanel contentPane;
	private javax.swing.Timer tm;
	private JComboBox cbL1;
	private JLabel lblLayer_1;
	private JComboBox cbL2;
	private JComboBox cbL3;
	private JLabel lblLayer_2;
	private JComboBox cbTestTask;
	private JLabel lblTestingTask;
	private JButton btReload;
	private JButton btSave;
	private JButton btStart;
	private JButton btL1;
	private JButton btL2;
	private JButton btL3;
	private JButton btTestTask;
	
	public SimulatorGuiForm() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (hostImpl != null) {
					hostImpl.quit();
				}
			}
		});

		setTitle("SS7 Simulator: ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 532, 277);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblLayer = new JLabel("Layer 1");
		lblLayer.setBounds(10, 11, 46, 14);
		panel.add(lblLayer);
		
		cbL1 = new JComboBox();
		cbL1.setBounds(114, 11, 230, 20);
		panel.add(cbL1);
		
		lblLayer_1 = new JLabel("Layer 2");
		lblLayer_1.setBounds(10, 42, 46, 14);
		panel.add(lblLayer_1);
		
		cbL2 = new JComboBox();
		cbL2.setBounds(114, 42, 230, 20);
		panel.add(cbL2);
		
		cbL3 = new JComboBox();
		cbL3.setBounds(114, 73, 230, 20);
		panel.add(cbL3);
		
		lblLayer_2 = new JLabel("Layer 3");
		lblLayer_2.setBounds(10, 73, 46, 14);
		panel.add(lblLayer_2);
		
		cbTestTask = new JComboBox();
		cbTestTask.setBounds(114, 105, 230, 20);
		panel.add(cbTestTask);
		
		lblTestingTask = new JLabel("Testing task");
		lblTestingTask.setBounds(10, 108, 82, 14);
		panel.add(lblTestingTask);
		
		btReload = new JButton("Reload");
		btReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		btReload.setBounds(10, 158, 144, 23);
		panel.add(btReload);
		
		btSave = new JButton("Save");
		btSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveData();
			}
		});
		btSave.setBounds(164, 158, 144, 23);
		panel.add(btSave);
		
		btStart = new JButton("Start");
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Starting tests
				saveData();

				TestingForm dlg = null;
				switch (((Instance_L3) cbTestTask.getSelectedItem()).intValue()) {
				case Instance_TestTask.VAL_USSD_TEST_CLIENT: {
					dlg = new TestUssdClientForm(getJFrame());
				}
					break;
				case Instance_TestTask.VAL_USSD_TEST_SERVER: {
					dlg = new TestUssdServerForm(getJFrame());
				}
					break;
					
					// TODO: other tests form options editing
				}
				if (dlg == null)
					return;

				testingForm = dlg;
				dlg.setData(host);
				dlg.setVisible(true);
				testingForm = null;
			}
		});
		btStart.setBounds(318, 158, 144, 23);
		panel.add(btStart);
		
		btL1 = new JButton(". . .");
		btL1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (((Instance_L1) cbL1.getSelectedItem()).intValue()) {
				case Instance_L1.VAL_M3UA: {
					M3uaForm frame = new M3uaForm(getJFrame());
					frame.setData(m3ua);
					frame.setVisible(true);
				}
					break;
				case Instance_L1.VAL_DIALOGIC: {
					// TODO: L1 data edit - DIALOGIC
				}
					break;
				}
			}
		});
		btL1.setBounds(381, 10, 56, 23);
		panel.add(btL1);
		
		btL2 = new JButton(". . .");
		btL2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (((Instance_L2) cbL2.getSelectedItem()).intValue()) {
				case Instance_L2.VAL_SCCP: {
					SccpForm frame = new SccpForm(getJFrame());
					frame.setData(sccp);
					frame.setVisible(true);
				}
					break;
				case Instance_L2.VAL_ISUP: {
					// TODO: L2 data edit - ISUP
				}
					break;
				}
			}
		});
		btL2.setBounds(381, 41, 56, 23);
		panel.add(btL2);
		
		btL3 = new JButton(". . .");
		btL3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (((Instance_L3) cbL3.getSelectedItem()).intValue()) {
				case Instance_L3.VAL_MAP: {
					MapForm frame = new MapForm(getJFrame());
					frame.setData(map);
					frame.setVisible(true);
				}
					break;
				case Instance_L3.VAL_CAP: {
					// TODO: implement it ......
				}
					break;
				case Instance_L3.VAL_INAP: {
					// TODO: implement it ......
				}
					break;
				}
			}
		});
		btL3.setBounds(381, 72, 56, 23);
		panel.add(btL3);

		btTestTask = new JButton(". . .");
		btTestTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (((Instance_L3) cbTestTask.getSelectedItem()).intValue()) {
				case Instance_TestTask.VAL_USSD_TEST_CLIENT: {
					TestUssdClientParamForm frame = new TestUssdClientParamForm(getJFrame());
					frame.setData(ussdClient);
					frame.setVisible(true);
				}
					break;
				case Instance_TestTask.VAL_USSD_TEST_SERVER: {
					TestUssdServerParamForm frame = new TestUssdServerParamForm(getJFrame());
					frame.setData(ussdServer);
					frame.setVisible(true);
				}
					break;
					
					// TODO: other tests form options editing
				}
			}
		});
		btTestTask.setBounds(381, 104, 56, 23);
		panel.add(btTestTask);
	}

	protected void startHost(String appName, final TesterHost hostImpl, TesterHostMBean host, M3uaManMBean m3ua, SccpManMBean sccp, MapManMBean map,
			TestUssdClientManMBean ussdClient, TestUssdServerManMBean ussdServer) {
		setTitle(getTitle() + appName);

		this.hostImpl = hostImpl;
		this.host = host;
		this.m3ua = m3ua;
		this.sccp = sccp;
		this.map = map;
		this.ussdClient = ussdClient;
		this.ussdServer = ussdServer;

		this.tm = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hostImpl != null) {
					hostImpl.checkStore();
					hostImpl.execute();

					// TODO: extra action for updating GUI from host notifications if a host is local
				}
			}
		});
		this.tm.start();

		this.reloadData();
	}

	private JFrame getJFrame() {
		return this;
	}

	private void reloadData() {
		this.setEnumeratedBaseComboBox(cbL1, this.host.getInstance_L1());
		this.setEnumeratedBaseComboBox(cbL2, this.host.getInstance_L2());
		this.setEnumeratedBaseComboBox(cbL3, this.host.getInstance_L3());
		this.setEnumeratedBaseComboBox(cbTestTask, this.host.getInstance_TestTask());
	}

	private void saveData() {
		this.host.setInstance_L1((Instance_L1) cbL1.getSelectedItem());
		this.host.setInstance_L2((Instance_L2) cbL2.getSelectedItem());
		this.host.setInstance_L3((Instance_L3) cbL3.getSelectedItem());
		this.host.setInstance_TestTask((Instance_TestTask) cbTestTask.getSelectedItem());
	}

	private void setEnumeratedBaseComboBox(JComboBox cb, EnumeratedBase defaultValue) {
		cb.removeAllItems();
		EnumeratedBase[] ebb = defaultValue.getList();
		EnumeratedBase dv = null;
		for (EnumeratedBase eb : ebb) {
			cb.addItem(eb);
			if (eb.intValue() == defaultValue.intValue())
				dv = eb;
		}
		if (dv != null)
			cb.setSelectedItem(dv);
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {

		TestingForm fm = testingForm;
		if (fm != null) {
			fm.sendNotif(notification);
		}
	}
}

