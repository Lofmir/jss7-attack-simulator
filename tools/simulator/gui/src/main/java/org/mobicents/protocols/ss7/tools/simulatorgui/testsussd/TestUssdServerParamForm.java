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

package org.mobicents.protocols.ss7.tools.simulatorgui.testsussd;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.ProcessSsRequestAction;
import org.mobicents.protocols.ss7.tools.simulator.testsussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestUssdServerParamForm extends JDialog {

	private static final long serialVersionUID = 1589538832800021188L;

	private TestUssdServerManMBean ussdServer;
	private JTextField tbMsisdnAddress;
	private JTextField tbDataCodingScheme;
	private JTextField tbAlertingPattern;
	private JTextField tbAutoResponseString;
	private JTextField tbAutoUnstructured_SS_RequestString;
	private JComboBox cbAddressNature;
	private JComboBox cbNumberingPlan;
	private JComboBox cbProcessSsRequestAction;

	public TestUssdServerParamForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("USSD test server settings");
		setBounds(100, 100, 539, 380);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(10, 11, 511, 76);
		panel.add(panel_1);
		
		JLabel label = new JLabel("Msisdn value");
		label.setBounds(10, 0, 79, 14);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("String");
		label_1.setBounds(10, 22, 46, 14);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("AddressNature");
		label_2.setBounds(10, 47, 79, 14);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("NumberingPlan");
		label_3.setBounds(264, 47, 87, 14);
		panel_1.add(label_3);
		
		tbMsisdnAddress = new JTextField();
		tbMsisdnAddress.setColumns(10);
		tbMsisdnAddress.setBounds(99, 19, 402, 20);
		panel_1.add(tbMsisdnAddress);
		
		cbAddressNature = new JComboBox();
		cbAddressNature.setBounds(99, 44, 140, 20);
		panel_1.add(cbAddressNature);
		
		cbNumberingPlan = new JComboBox();
		cbNumberingPlan.setBounds(361, 44, 140, 20);
		panel_1.add(cbNumberingPlan);
		
		JLabel label_4 = new JLabel("Data coding scheme");
		label_4.setBounds(10, 98, 149, 14);
		panel.add(label_4);
		
		tbDataCodingScheme = new JTextField();
		tbDataCodingScheme.setColumns(10);
		tbDataCodingScheme.setBounds(423, 95, 86, 20);
		panel.add(tbDataCodingScheme);
		
		tbAlertingPattern = new JTextField();
		tbAlertingPattern.setColumns(10);
		tbAlertingPattern.setBounds(423, 126, 86, 20);
		panel.add(tbAlertingPattern);
		
		JLabel label_5 = new JLabel("Alerting pattern value (-1 means does not use AlertingPattern)");
		label_5.setBounds(10, 129, 339, 14);
		panel.add(label_5);
		
		JButton button = new JButton("Load default values for side A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataA();
			}
		});
		button.setBounds(10, 274, 200, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Load default values for side B");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataB();
			}
		});
		button_1.setBounds(224, 274, 200, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Cancel");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getJFrame().dispose();
			}
		});
		button_2.setBounds(307, 308, 117, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Save");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveData()) {
					getJFrame().dispose();
				}
			}
		});
		button_3.setBounds(180, 308, 117, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Reload");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		button_4.setBounds(10, 308, 144, 23);
		panel.add(button_4);
		
		JLabel lblProcessssrequestaction = new JLabel("<html>The action when processing<br>ProcessSsRequest</html>");
		lblProcessssrequestaction.setBounds(10, 155, 188, 34);
		panel.add(lblProcessssrequestaction);
		
		cbProcessSsRequestAction = new JComboBox();
		cbProcessSsRequestAction.setToolTipText("<html>\r\nAction which be performed when ProcessSsUnstructuredRequest has been received. When manual response user must manually send a response or SsUnstructuredRequest to the UssdClient. \r\n<br>\r\nOther actions are: auto sending \"AutoResponseString\" as a response, \r\n<br>\r\nauto sending \"AutoUnstructured_SS_RequestString\" as a SsUnstructuredRequest and then auto sending \"AutoResponseString\" as a response to SsUnstructured response\r\n</html>");
		cbProcessSsRequestAction.setBounds(226, 155, 283, 20);
		panel.add(cbProcessSsRequestAction);
		
		tbAutoResponseString = new JTextField();
		tbAutoResponseString.setColumns(10);
		tbAutoResponseString.setBounds(266, 200, 255, 20);
		panel.add(tbAutoResponseString);
		
		JLabel lblAutuString = new JLabel("String of auto processUnsructuresSsResponse");
		lblAutuString.setBounds(10, 203, 246, 14);
		panel.add(lblAutuString);
		
		JLabel lblStringOfAuto = new JLabel("String of auto unsructuresSsRequest");
		lblStringOfAuto.setBounds(10, 234, 246, 14);
		panel.add(lblStringOfAuto);
		
		tbAutoUnstructured_SS_RequestString = new JTextField();
		tbAutoUnstructured_SS_RequestString.setColumns(10);
		tbAutoUnstructured_SS_RequestString.setBounds(266, 231, 255, 20);
		panel.add(tbAutoUnstructured_SS_RequestString);
	}

	public void setData(TestUssdServerManMBean ussdServer) {
		this.ussdServer = ussdServer;
		
		this.reloadData();
	}

	private JDialog getJFrame() {
		return this;
	}

	private void reloadData() {
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.ussdServer.getMsisdnAddressNature());
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.ussdServer.getMsisdnNumberingPlan());
		M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, this.ussdServer.getProcessSsRequestAction());

		tbMsisdnAddress.setText(this.ussdServer.getMsisdnAddress());
		tbAutoResponseString.setText(this.ussdServer.getAutoResponseString());
		tbAutoUnstructured_SS_RequestString.setText(this.ussdServer.getAutoUnstructured_SS_RequestString());

		tbDataCodingScheme.setText(((Integer)this.ussdServer.getDataCodingScheme()).toString());
		tbAlertingPattern.setText(((Integer)this.ussdServer.getAlertingPattern()).toString());
	}

	private void loadDataA() {
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanType(NumberingPlan.ISDN.getIndicator()));
		M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, new ProcessSsRequestAction(ProcessSsRequestAction.VAL_MANUAL_RESPONSE));

		tbMsisdnAddress.setText("");
		tbAutoResponseString.setText("");
		tbAutoUnstructured_SS_RequestString.setText("");

		tbDataCodingScheme.setText("15");
		tbAlertingPattern.setText("-1");
	}

	private void loadDataB() {
		loadDataA();
	}

	private boolean saveData() {
		int dataCodingScheme = 0;
		int alertingPattern = 0;
		try {
			dataCodingScheme = Integer.parseInt(tbDataCodingScheme.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing DataCodingScheme value: " + e.toString());
			return false;
		}
		try {
			alertingPattern = Integer.parseInt(tbAlertingPattern.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Alerting Pattern value: " + e.toString());
			return false;
		}

		this.ussdServer.setMsisdnAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
		this.ussdServer.setMsisdnNumberingPlan((NumberingPlanType) cbNumberingPlan.getSelectedItem());
		this.ussdServer.setProcessSsRequestAction((ProcessSsRequestAction) cbProcessSsRequestAction.getSelectedItem());

		this.ussdServer.setMsisdnAddress(tbMsisdnAddress.getText());
		this.ussdServer.setAutoResponseString(tbAutoResponseString.getText());
		this.ussdServer.setAutoUnstructured_SS_RequestString(tbAutoUnstructured_SS_RequestString.getText());

		this.ussdServer.setDataCodingScheme(dataCodingScheme);
		this.ussdServer.setAlertingPattern(alertingPattern);

		return true;
	}
}
