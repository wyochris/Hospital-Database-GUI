package main;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class User {
	
		
	public abstract void initializeUserScreen();
	
	public void initalizeTable(ResultSet rs, JTable resultTable, JPanel resultPanel, JFrame frame) {
		try {
		DefaultTableModel tableModel = new DefaultTableModel();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		// Add column headers
		for (int i = 1; i <= columnsNumber; i++) {
			tableModel.addColumn(rsmd.getColumnName(i));
		}

		// Add data rows
		while (rs.next()) {
			Object[] rowData = new Object[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++) {
				rowData[i - 1] = rs.getString(i);
			}
			tableModel.addRow(rowData);
		}

		// Create JTable with the table model
		resultTable = new JTable(tableModel);

		// Add the table to the result panel
		resultPanel.removeAll();
		resultPanel.add(new JScrollPane(resultTable));

		
		// Add the result panel to the frame
		frame.add(resultPanel, BorderLayout.CENTER);
		frame.revalidate();
		} catch(SQLException ex){
			throw new RuntimeException(ex);
		}
		
	}
}

